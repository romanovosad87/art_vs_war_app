package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.collection.CollectionInPaintingResponseDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingProfileResponseDto;
import com.example.artvswar.dto.response.painting.PaintingProfileResponseDto.IdValuePair;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Collection;
import com.example.artvswar.model.Medium;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.PaintingImage;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.Subject;
import com.example.artvswar.model.Support;
import com.example.artvswar.model.enummodel.PaymentStatus;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.StyleService;
import com.example.artvswar.service.SubjectService;
import com.example.artvswar.service.SupportService;
import com.example.artvswar.util.image.CloudinaryClient;
import com.example.artvswar.util.image.roomView.RoomViewManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
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

    public PaintingProfileResponseDto toPaintingProfileResponseDto(Painting painting) {
        PaintingProfileResponseDto dto = new PaintingProfileResponseDto();
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

        List<IdValuePair> styles = painting.getStyles().stream()
                .map(ent -> new IdValuePair(ent.getId(), ent.getName()))
                .collect(Collectors.toList());
        dto.setStyles(styles);

        List<IdValuePair> mediums = painting.getMediums().stream()
                .map(ent -> new IdValuePair(ent.getId(), ent.getName()))
                .collect(Collectors.toList());
        dto.setMediums(mediums);

        List<IdValuePair> supports = painting.getSupports().stream()
                .map(ent -> new IdValuePair(ent.getId(), ent.getName()))
                .collect(Collectors.toList());
        dto.setSupports(supports);

        List<IdValuePair> subjects = painting.getSubjects().stream()
                .map(ent -> new IdValuePair(ent.getId(), ent.getName()))
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
        log.info("Mock rooms creation lasted " + duration + " µ");
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

        Double widthDto = dto.getWidth();
        Double heightDto = dto.getHeight();

        Double dbWidth = paintingFromDB.getWidth();
        Double dbHeight = paintingFromDB.getHeight();

        boolean isNewImageNotDownloaded = dto.getImage().getPublicId()
                .equals(paintingFromDB.getPaintingImage().getImage().getPublicId());

        if ((!widthDto.equals(dbWidth) || !heightDto.equals(dbHeight))
        && isNewImageNotDownloaded) {
            createMockRoomsSameImage(dto, paintingFromDB);
        }

        paintingFromDB.setWeight(dto.getWeight());
        paintingFromDB.setWidth(widthDto);
        paintingFromDB.setHeight(heightDto);
        paintingFromDB.setDepth(dto.getDepth());
        paintingFromDB.setDescription(dto.getDescription());

        paintingFromDB.getStyles().clear();
        paintingFromDB.getMediums().clear();
        paintingFromDB.getSupports().clear();
        paintingFromDB.getSubjects().clear();

        dto.getStyleIds().forEach(
                styleId -> paintingFromDB.addStyle(styleService.getReferenceById(styleId)));
        dto.getMediumIds().forEach(
                mediumId -> paintingFromDB.addMedium(mediumService.getReferenceById(mediumId)));
        dto.getSupportIds().forEach(
                supportId -> paintingFromDB.addSupport(supportService.getReferenceById(supportId)));
        dto.getSubjectIds().forEach(
                subjectId -> paintingFromDB.addSubject(subjectService.getReferenceById(subjectId))
        );


        if (!isNewImageNotDownloaded) {
            PaintingImage paintingImageFromDB = paintingFromDB.getPaintingImage();
            String publicId = paintingImageFromDB.getImage().getPublicId();

            createMockRooms(dto, paintingFromDB);

            cloudinaryClient.delete(publicId);
        }
        return paintingFromDB;
    }

    private void createMockRooms(PaintingUpdateRequestDto dto, Painting paintingFromDB) {
        PaintingImage paintingImageFromDB = paintingFromDB.getPaintingImage();
        PaintingImage paintingImage = paintingImageMapper.toImageModel(dto.getImage(), paintingImageFromDB);
        List<RoomView> viewRooms = roomViewManager.getViewRooms(paintingImage.getImage().getPublicId(),
                dto.getWidth(), dto.getHeight());
        List<RoomView> roomViews = paintingImage.getRoomViews();
        roomViews.clear();
        roomViews.addAll(viewRooms);

        paintingFromDB.addPaintingImage(paintingImage);
    }

    private void createMockRoomsSameImage(PaintingUpdateRequestDto dto, Painting paintingFromDB) {
        PaintingImage paintingImageFromDB = paintingFromDB.getPaintingImage();
        List<RoomView> viewRooms = roomViewManager.getViewRooms(paintingImageFromDB.getImage().getPublicId(),
                dto.getWidth(), dto.getHeight());
        List<RoomView> roomViews = paintingImageFromDB.getRoomViews();
        roomViews.clear();
        roomViews.addAll(viewRooms);

        paintingFromDB.addPaintingImage(paintingImageFromDB);
    }
}
