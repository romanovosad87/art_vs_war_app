package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.collection.CollectionInPaintingResponseDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Collection;
import com.example.artvswar.model.Medium;
import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.Subject;
import com.example.artvswar.model.Support;
import com.example.artvswar.model.enumModel.PaymentStatus;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.StyleService;
import com.example.artvswar.service.SubjectService;
import com.example.artvswar.service.SupportService;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.roomView.RoomViewManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaintingMapper {
    private final PaintingImageMapper paintingImageMapper;
    private final RoomViewManager roomViewManager;
    private final StyleService styleService;
    private final MediumService mediumService;
    private final SupportService supportService;
    private final SubjectService subjectService;
    private final CloudinaryClient cloudinaryClient;

    public PaintingResponseDto toPaintingResponseDto(Painting painting) {
        PaintingResponseDto dto = new PaintingResponseDto();
        dto.setId(painting.getId());
        dto.setPrettyId(painting.getPrettyId());
        dto.setTitle(painting.getTitle());
        dto.setDescription(painting.getDescription());
        dto.setYearOfCreation(painting.getYearOfCreation());
        dto.setPrice(painting.getPrice());
        dto.setWeight(painting.getWeight());
        dto.setWidth(painting.getWidth());
        dto.setHeight(painting.getHeight());
        dto.setDepth(painting.getDepth());
        dto.setPaymentStatus(painting.getPaymentStatus());
        dto.setAddedToDatabase(painting.getEntityCreatedAt());

        CollectionInPaintingResponseDto collectionDto = new CollectionInPaintingResponseDto();
        Collection collection = painting.getCollection().orElseGet(Collection::new);
        collectionDto.setId(collection.getId());
        collectionDto.setPrettyId(collection.getPrettyId());
        collectionDto.setTitle(collection.getTitle());

        dto.setCollection(collectionDto);

        AuthorForPaintingResponseDto authorDto = new AuthorForPaintingResponseDto();
        authorDto.setId(painting.getAuthor().getCognitoSubject());
        authorDto.setPrettyId(painting.getAuthor().getPrettyId());
        authorDto.setFullName(painting.getAuthor().getFullName());
        authorDto.setCountry(painting.getAuthor().getCountry());
        dto.setAuthor(authorDto);

        List<String> styles = painting.getStyles().stream()
                .map(Style::getName)
                .collect(Collectors.toList());
        dto.setStyles(styles);

        List<String> mediums = painting.getMediums().stream()
                .map(Medium::getName)
                .collect(Collectors.toList());
        dto.setMediums(mediums);

        List<String> supports = painting.getSupports().stream()
                .map(Support::getName)
                .collect(Collectors.toList());
        dto.setSupports(supports);

        List<String> subjects = painting.getSubjects().stream()
                .map(Subject::getName)
                .collect(Collectors.toList());
        dto.setSubjects(subjects);

        PaintingImage paintingImage = painting.getPaintingImage();
        List<AdditionalImage> additionalImages = painting.getAdditionalImages();
        PaintingImageResponseDto paintingImageResponseDto
                = paintingImageMapper.toImageResponseDto(paintingImage, additionalImages);
        dto.setPaintingImageResponseDto(paintingImageResponseDto);

        return dto;
    }

    @Transactional
    public Painting toPaintingModel(PaintingCreateRequestDto dto) {
        Painting painting = new Painting();
        painting.setTitle(dto.getTitle().trim());
        painting.setPrice(dto.getPrice());
        painting.setYearOfCreation(dto.getYearOfCreation());
        painting.setWeight(dto.getWeight());
        painting.setWidth(dto.getWidth());
        painting.setHeight(dto.getHeight());
        painting.setDepth(dto.getDepth());
        painting.setDescription(dto.getDescription().trim());
        painting.setPaymentStatus(PaymentStatus.AVAILABLE);
        PaintingImage paintingImage = paintingImageMapper.toImageModel(dto.getImage());
        long start = System.currentTimeMillis();
        List<RoomView> viewRooms = roomViewManager.getViewRooms(paintingImage.getImage().getPublicId(),
                dto.getWidth(), dto.getHeight());
        long end = System.currentTimeMillis();
        long duration = end - start;   // measure parallel stream
        System.out.println(duration + " µ");
        paintingImage.getRoomViews().addAll(viewRooms);
        painting.addPaintingImage(paintingImage);

        dto.getStyleIds().forEach(
                styleId -> painting.addStyle(styleService.getReferenceById(styleId)));
        dto.getMediumIds().forEach(
                mediumId -> painting.addMedium(mediumService.getReferenceById(mediumId)));
        dto.getSupportIds().forEach(
                supportId -> painting.addSupport(supportService.getReferenceById(supportId)));
        dto.getSubjectIds().forEach(
                subjectId -> painting.addSubject(subjectService.getReferenceById(subjectId))
        );

        return painting;
    }

    @Transactional
    public Painting updatePaintingModel(PaintingUpdateRequestDto dto, Painting paintingFromDB) {
        paintingFromDB.setTitle(dto.getTitle());
        paintingFromDB.setPrice(dto.getPrice());
        paintingFromDB.setYearOfCreation(dto.getYearOfCreation());
        paintingFromDB.setWeight(dto.getWeight());
        paintingFromDB.setWidth(dto.getWidth());
        paintingFromDB.setHeight(dto.getHeight());
        paintingFromDB.setDepth(dto.getDepth());
        paintingFromDB.setDescription(dto.getDescription());
        dto.getStyleIds().forEach(
                styleId -> paintingFromDB.addStyle(styleService.getReferenceById(styleId)));
        dto.getMediumIds().forEach(
                mediumId -> paintingFromDB.addMedium(mediumService.getReferenceById(mediumId)));
        dto.getSupportIds().forEach(
                supportId -> paintingFromDB.addSupport(supportService.getReferenceById(supportId)));
        dto.getSubjectIds().forEach(
                subjectId -> paintingFromDB.addSubject(subjectService.getReferenceById(subjectId))
        );

        if (!dto.getImage().getPublicId().equals(paintingFromDB.getPaintingImage().getImage().getPublicId())) {
            PaintingImage paintingImageFromDB = paintingFromDB.getPaintingImage();
            PaintingImage paintingImage = paintingImageMapper.toImageModel(dto.getImage());
            List<RoomView> viewRooms = roomViewManager.getViewRooms(paintingImage.getImage().getPublicId(),
                    dto.getWidth(), dto.getHeight());
            paintingImage.getRoomViews().addAll(viewRooms);

            paintingFromDB.setPaintingImage(paintingImage);

            cloudinaryClient.delete(paintingImageFromDB.getImage().getPublicId());
        }
        return paintingFromDB;
    }
}
