package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.image.FullImageUpdateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.collection.CollectionInPaintingResponseDto;
import com.example.artvswar.dto.response.image.PaintingImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingProfileResponseDto;
import com.example.artvswar.dto.response.painting.PaintingProfileResponseDto.IdValuePair;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.model.AdditionalImage;
import com.example.artvswar.model.Author;
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
import org.jetbrains.annotations.NotNull;
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

        AuthorForPaintingResponseDto authorDto = new AuthorForPaintingResponseDto();

        Author author = painting.getAuthor();
        if (author != null) {
            authorDto.setId(author.getCognitoSubject());
            authorDto.setPrettyId(author.getPrettyId());
            authorDto.setFullName(author.getFullName());
            authorDto.setCountry(author.getCountry());
        }
        dto.setAuthor(authorDto);

        CollectionInPaintingResponseDto collectionDto = new CollectionInPaintingResponseDto();
        Collection collection = painting.getCollection().orElseGet(Collection::new);
        collectionDto.setId(collection.getId());
        collectionDto.setPrettyId(collection.getPrettyId());
        collectionDto.setTitle(collection.getTitle());

        dto.setCollection(collectionDto);


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

        AuthorForPaintingResponseDto authorDto = getAuthorForPaintingResponseDto(painting);
        dto.setAuthor(authorDto);

        CollectionInPaintingResponseDto collectionDto = new CollectionInPaintingResponseDto();
        Collection collection = painting.getCollection().orElseGet(Collection::new);
        collectionDto.setId(collection.getId());
        collectionDto.setPrettyId(collection.getPrettyId());
        collectionDto.setTitle(collection.getTitle());

        dto.setCollection(collectionDto);

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
        Painting painting = populatePaintingFromDto(dto);

        PaintingImage paintingImage = paintingImageMapper.toImageModel(dto.getImage());

    // Check if paintingImage is null before using it
    if (paintingImage != null && paintingImage.getImage() != null) {
        long start = System.currentTimeMillis();
        List<RoomView> viewRooms = roomViewManager.getViewRooms(paintingImage.getImage().getPublicId(),
                dto.getWidth(), dto.getHeight());
        long end = System.currentTimeMillis();
        long duration = end - start;  // measure time taken to get room views

        // Log the duration of room views creation
        log.info("Room views creation lasted " + duration + " ms");

        paintingImage.getRoomViews().addAll(viewRooms);
        painting.addPaintingImage(paintingImage);
    } else {
        log.warn("Painting image or image data is null, cannot create room views.");
    }

        // Add styles, mediums, supports, and subjects
        if (dto.getStyleIds() != null) {
            dto.getStyleIds().forEach(styleId -> painting.addStyle(styleService.getReferenceById(styleId)));
        }
        if (dto.getMediumIds() != null) {
            dto.getMediumIds().forEach(mediumId -> painting.addMedium(mediumService.getReferenceById(mediumId)));
        }
        if (dto.getSupportIds() != null) {
            dto.getSupportIds().forEach(supportId -> painting.addSupport(supportService.getReferenceById(supportId)));
        }
        if (dto.getSubjectIds() != null) {
            dto.getSubjectIds().forEach(subjectId -> painting.addSubject(subjectService.getReferenceById(subjectId)));
        }

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

        FullImageUpdateRequestDto imageDto = dto.getImage();
        boolean isNewImageNotDownloaded = imageDto != null && imageDto.getPublicId() != null &&
                imageDto.getPublicId().equals(paintingFromDB.getPaintingImage().getImage().getPublicId());

        boolean isWidthChanged = (widthDto != null && !widthDto.equals(dbWidth));
        boolean isHeightChanged = (heightDto != null && !heightDto.equals(dbHeight));

        if ((isWidthChanged || isHeightChanged) && isNewImageNotDownloaded) {
            createMockRoomsSameImage(dto, paintingFromDB);
        }

        paintingFromDB.setWeight(dto.getWeight());
        paintingFromDB.setWidth(widthDto != null ? widthDto : dbWidth);
        paintingFromDB.setHeight(heightDto != null ? heightDto : dbHeight);
        paintingFromDB.setDepth(dto.getDepth());
        paintingFromDB.setDescription(dto.getDescription());

        // Clear existing associations and safely add new ones
        paintingFromDB.getStyles().clear();
        if (dto.getStyleIds() != null) {
            dto.getStyleIds().forEach(styleId -> paintingFromDB.addStyle(styleService.getReferenceById(styleId)));
        }

        paintingFromDB.getMediums().clear();
        if (dto.getMediumIds() != null) {
            dto.getMediumIds().forEach(mediumId -> paintingFromDB.addMedium(mediumService.getReferenceById(mediumId)));
        }

        paintingFromDB.getSupports().clear();
        if (dto.getSupportIds() != null) {
            dto.getSupportIds().forEach(supportId -> paintingFromDB.addSupport(supportService.getReferenceById(supportId)));
        }

        paintingFromDB.getSubjects().clear();
        if (dto.getSubjectIds() != null) {
            dto.getSubjectIds().forEach(subjectId -> paintingFromDB.addSubject(subjectService.getReferenceById(subjectId)));
        }

        if (imageDto != null && !isNewImageNotDownloaded) {
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

    private static @NotNull AuthorForPaintingResponseDto getAuthorForPaintingResponseDto(Painting painting) {
        AuthorForPaintingResponseDto authorDto = new AuthorForPaintingResponseDto();
        Author author = painting.getAuthor();
        if (author != null) {
            authorDto.setId(author.getCognitoSubject());
            authorDto.setPrettyId(author.getPrettyId());
            authorDto.setFullName(author.getFullName());
            authorDto.setCountry(author.getCountry());
        } else {
            // Optionally set default values or leave as null
            authorDto.setId(null);
            authorDto.setPrettyId(null);
            authorDto.setFullName("Unknown Artist");
            authorDto.setCountry("Unknown");
        }
        return authorDto;
    }

    private static @NotNull Painting populatePaintingFromDto(PaintingCreateRequestDto dto) {
        Painting painting = new Painting();
        painting.setTitle(dto.getTitle() != null ? dto.getTitle().trim() : "");
        painting.setPrice(dto.getPrice());
        painting.setYearOfCreation(dto.getYearOfCreation());
        painting.setWeight(dto.getWeight());
        painting.setWidth(dto.getWidth());
        painting.setHeight(dto.getHeight());
        painting.setDepth(dto.getDepth());

        // Safely handle potentially null description
        String description = dto.getDescription();
        painting.setDescription(description != null ? description.trim() : "");

        painting.setPaymentStatus(PaymentStatus.AVAILABLE);
        return painting;
    }
}
