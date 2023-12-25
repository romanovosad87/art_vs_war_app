package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.PaintingMapper;
import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.FolderResponseDto;
import com.example.artvswar.dto.response.image.AdditionalImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingMainPageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingParametersForSearchResponseDto;
import com.example.artvswar.dto.response.painting.PaintingProfileResponseDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.dto.response.painting.PaintingShortResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.exception.PaintingNotAvailableException;
import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.enumModel.ModerationStatus;
import com.example.artvswar.model.enumModel.PaymentStatus;
import com.example.artvswar.repository.painting.PaintingRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.CloudinaryFolderCreator;
import com.example.artvswar.util.ModerationMockImage;
import com.example.artvswar.util.PrettyIdCreator;
import com.example.artvswar.util.UrlParser;
import com.example.artvswar.util.image.CloudinaryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class PaintingServiceImpl implements PaintingService {
    public static final String PAINTING = "Can't find painting by prettyId: %s";
    private final PaintingRepository paintingRepository;
    private final PaintingMapper paintingMapper;
    private final UrlParser urlParser;
    private final AuthorService authorService;
    private final PrettyIdCreator prettyIdCreator;
    private final CloudinaryClient cloudinaryClient;
    private final CloudinaryFolderCreator cloudinaryFolderCreator;

    @Override
    @Transactional
    public PaintingResponseDto save(PaintingCreateRequestDto dto, String cognitoSubject) {
        Painting painting = paintingMapper.toPaintingModel(dto);
        Author author = authorService.getAuthorByCognitoSubject(cognitoSubject);
        painting.setAuthor(author);
        painting.setPrettyId(createPrettyId(dto.getTitle()));
        Painting savedPainting = paintingRepository.save(painting);
        return paintingMapper.toPaintingResponseDto(savedPainting);
    }

    @Override
    @Transactional
    public PaintingResponseDto update(String prettyId, PaintingUpdateRequestDto dto, String cognitoSubject) {
        Painting paintingFromDB = paintingRepository.findByPrettyId(prettyId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format(PAINTING, prettyId)));

        if (paintingFromDB.getPaymentStatus() != PaymentStatus.AVAILABLE) {
            throw new PaintingNotAvailableException("Can't update painting as it is sold "
                    + "or is in process of payment");
        }

        if (paintingFromDB.getAuthor().getCognitoSubject().equals(cognitoSubject)) {
            if(!dto.getTitle().equals(paintingFromDB.getTitle())) {
                paintingFromDB.setPrettyId(createPrettyId(dto.getTitle()));
            }
            Painting painting = paintingMapper.updatePaintingModel(dto, paintingFromDB);
            return paintingMapper.toPaintingResponseDto(painting);
        } else {
            throw new RuntimeException(
                    String.format("Author with cognitoSubject: %s is not allowed "
                            + "to update painting with prettyId: %s", cognitoSubject, prettyId));
        }
    }

    @Override
    public Painting get(Long id) {
        return paintingRepository.findById(id).orElseThrow(() -> new AppEntityNotFoundException(
                String.format("Can't find painting by id: %s", id)));
    }

    @Override
    public Painting getReference(Long id) {
        return paintingRepository.getReferenceById(id);
    }

    @Override
    public Page<PaintingShortResponseDto> getAll(Map<String, String> params, Pageable pageable) {
        Specification<Painting> specification = urlParser.getPaintingSpecification(params);
        return paintingRepository.getAllShortDtosBySpecification(specification, pageable);
    }

    @Override
    public PaintingResponseDto getDto(Long id) {
        Painting painting = paintingRepository.findById(id)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format("Can't find painting by id: %s", id)));
        return paintingMapper.toPaintingResponseDto(painting);
    }

    @Override
    public PaintingResponseDto getByPrettyId(String prettyId) {
        Painting painting = paintingRepository.findByPrettyId(prettyId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                String.format(PAINTING, prettyId)));
        return paintingMapper.toPaintingResponseDto(painting);
    }

    @Override
    public PaintingProfileResponseDto getForProfileByPrettyId(String prettyId) {
        Painting painting = paintingRepository.findByPrettyId(prettyId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format(PAINTING, prettyId)));
        return paintingMapper.toPaintingProfileResponseDto(painting);
    }

    @Override
    @Transactional
    public void deleteByPrettyId(String prettyId) {
        Painting painting = paintingRepository.findByPrettyId(prettyId)
                .orElseThrow(() -> new AppEntityNotFoundException(
                        String.format(PAINTING, prettyId)));

        if (painting.getPaymentStatus() != PaymentStatus.AVAILABLE) {
            throw new PaintingNotAvailableException("Can't delete painting because it is sold "
                    + "or is in process of purchasing");
        }

        paintingRepository.delete(painting);
        cloudinaryClient.delete(painting.getPaintingImage().getImage().getPublicId());
        painting.getAdditionalImages()
                .forEach(additionalImage -> cloudinaryClient
                        .delete(additionalImage.getImage().getPublicId()));
    }


    @Override
    public long getNumberOfAllPaintings() {
        return paintingRepository.count();
    }

    @Override
    public Page<PaintingShortResponseDto> findAllByAuthorPrettyId(String prettyId,
                                                                  Pageable pageable) {
        return paintingRepository.findAllByAuthorPrettyId(prettyId, pageable);
    }

    @Override
    public Page<PaintingShortResponseDto> findAllByAuthorCognitoSubject(String cognitoSubject,
                                                                        Pageable pageable) {
        return paintingRepository.findAllByAuthorCognitoSubject(PaintingShortResponseDto.class,
                cognitoSubject, pageable);
    }

    @Override
    public List<PaintingShortResponseDto> getAdditionalPaintings(String paintingPrettyId,
                                                                                      int size) {
        String authorPrettyId = paintingRepository.getAuthorPrettyId(paintingPrettyId);
        return paintingRepository
                .getAdditionalPaintings(paintingPrettyId, authorPrettyId, size);
    }

    @Override
    public List<PaintingShortResponseDto> getRecommendedPaintings(Set<String> paintingPrettyIds,
                                                                  int limit) {
        return paintingRepository.getRecommendedPaintings(paintingPrettyIds, limit);
    }

    @Override
    public Page<PaintingShortResponseDto> findAllByCollectionPrettyId(String collectionPrettyId,
                                                                      Pageable pageable) {
        return paintingRepository
                .findAllByCollectionPrettyId(collectionPrettyId, pageable);
    }

    @Override
    public Page<PaintingShortResponseDto> getAllByParamsReturnDto(Map<String, String> params,
                                                     Pageable pageable) {
        Specification<Painting> specification = urlParser.getPaintingSpecification(params);
        return paintingRepository.getAllShortDtosBySpecification(specification, pageable);
    }

    @Override
    public FolderResponseDto createCloudinaryFolder(String cognitoSubject, String title) {
        String prettyId = createPrettyId(title);
        return cloudinaryFolderCreator.createForPaintingImage(cognitoSubject, prettyId);
    }

    public String createPrettyId(String title) {
        String proposedPrettyId = prettyIdCreator.create(title);
        int repentance = paintingRepository.checkIfExistPrettyId(proposedPrettyId);
        if (repentance == 0) {
            return proposedPrettyId;
        } else {
            return proposedPrettyId + "-" + new Random().nextInt(100000);
        }
    }

    @Override
    public List<AdditionalImageResponseDto> getAdditionalImagesByPrettyId(String prettyId) {
        List<AdditionalImageResponseDto> images = paintingRepository.getAdditionalImagesByPrettyId(prettyId);
        images.forEach(image -> {
            ModerationStatus moderationStatus = image.getImageModerationStatus();
            if (moderationStatus == ModerationStatus.PENDING) {
                image.setImageUrl(ModerationMockImage.PENDING_URL);
            } else if (moderationStatus == ModerationStatus.REJECTED) {
                image.setImageUrl(ModerationMockImage.REJECTED_URL);
            }
        });
        return images;
    }

    @Override
    public PaintingParametersForSearchResponseDto getDistinctParams() {
        return paintingRepository.getDistinctParameters();
    }

    @Override
    public Map<Double, List<PaintingMainPageResponseDto>> getPaintingsForMainPage() {
        return parseObject(paintingRepository.findPaintingsForMainPage(
                0.75, 2,
                1, 4,
                1.25, 3,
                1.5, 2,
                1.75, 1,
                2, 2));
    }

    @Override
    @Transactional
    public void changePaymentStatus(Painting painting, PaymentStatus paymentStatus) {
        painting.setPaymentStatus(paymentStatus);
        log.info(String.format("For painting with id: '%s', title: '%s', payment status changed to %s",
                painting.getId(), painting.getTitle(), paymentStatus));
    }

    @Override
    public Page<PaintingShortResponseDto> findRecentSelling(String authorSubject,
                                                            Pageable pageable) {
        return paintingRepository.findResentSelling(authorSubject, pageable);
    }

    private Map<Double, List<PaintingMainPageResponseDto>> parseObject(List<Object[]> response) {
        return response.stream()
                .collect(Collectors.groupingBy(obj -> (Double) obj[1],
                        Collectors.mapping(obj -> new PaintingMainPageResponseDto((String) obj[0],
                                        (String) obj[2], (String) obj[3]),
                                Collectors.toList())));
    }
}
