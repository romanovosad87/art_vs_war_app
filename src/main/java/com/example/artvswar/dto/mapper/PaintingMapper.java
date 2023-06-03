package com.example.artvswar.dto.mapper;

import com.example.artvswar.dto.request.painting.PaintingCreateRequestDto;
import com.example.artvswar.dto.request.painting.PaintingUpdateRequestDto;
import com.example.artvswar.dto.response.MediumResponseDto;
import com.example.artvswar.dto.response.StyleResponseDto;
import com.example.artvswar.dto.response.SubjectResponseDto;
import com.example.artvswar.dto.response.SupportResponseDto;
import com.example.artvswar.dto.response.author.AuthorForPaintingResponseDto;
import com.example.artvswar.dto.response.image.ImageResponseDto;
import com.example.artvswar.dto.response.painting.PaintingResponseDto;
import com.example.artvswar.model.Image;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.RoomView;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.StyleService;
import com.example.artvswar.service.SubjectService;
import com.example.artvswar.service.SupportService;
import com.example.artvswar.util.image.roomView.RoomViewManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class PaintingMapper {
    private final ImageMapper imageMapper;
    private final RoomViewManager roomViewManager;
    private final StyleService styleService;
    private final MediumService mediumService;
    private final SupportService supportService;
    private final SubjectService subjectService;

    public PaintingResponseDto toPaintingResponseDto(Painting painting) {
        PaintingResponseDto dto = new PaintingResponseDto();
        dto.setId(painting.getId());
        dto.setTitle(painting.getTitle());
        dto.setDescription(painting.getDescription());
        dto.setYearOfCreation(painting.getYearOfCreation());
        dto.setPrice(painting.getPrice());
        dto.setWidth(painting.getWidth());
        dto.setHeight(painting.getHeight());
        dto.setAddedToDatabase(painting.getEntityCreatedAt());

        AuthorForPaintingResponseDto authorDto = new AuthorForPaintingResponseDto();
        authorDto.setId(painting.getAuthor().getCognitoUsername());
        authorDto.setFullName(painting.getAuthor().getFullName());
        dto.setAuthor(authorDto);

        List<StyleResponseDto> styles = painting.getStyles().stream()
                .map(style -> new StyleResponseDto(style.getId(), style.getName()))
                .collect(Collectors.toList());
        dto.setStyles(styles);

        List<MediumResponseDto> mediums = painting.getMediums().stream()
                .map(medium -> new MediumResponseDto(medium.getId(), medium.getName()))
                .collect(Collectors.toList());
        dto.setMediums(mediums);

        List<SupportResponseDto> supports = painting.getSupports().stream()
                .map(support -> new SupportResponseDto(support.getId(), support.getName()))
                .collect(Collectors.toList());
        dto.setSupports(supports);

        List<SubjectResponseDto> subjects = painting.getSubjects().stream()
                .map(support -> new SubjectResponseDto(support.getId(), support.getName()))
                .collect(Collectors.toList());
        dto.setSubjects(subjects);

        Image image = painting.getImage();
        ImageResponseDto imageResponseDto = imageMapper.toImageResponseDto(image);
        dto.setImageResponseDto(imageResponseDto);

        return dto;
    }

    public Painting toPaintingModel(PaintingCreateRequestDto dto) {
        Painting painting = new Painting();
        painting.setTitle(dto.getTitle());
        painting.setPrice(dto.getPrice());
        painting.setYearOfCreation(dto.getYearOfCreation());
        painting.setHeight(dto.getHeight());
        painting.setWidth(dto.getWidth());
        painting.setDescription(dto.getDescription());
        Image image = imageMapper.toImageModel(dto.getImage());
        List<RoomView> viewRooms = roomViewManager.getViewRooms(image.getId(),
                dto.getWidth(), dto.getHeight());
        image.getRoomViews().addAll(viewRooms);
        painting.setImage(image);

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

    public Painting toPaintingModel(PaintingUpdateRequestDto dto) {
        Painting painting = new Painting();
        painting.setTitle(dto.getTitle());
        painting.setPrice(dto.getPrice());
        painting.setYearOfCreation(dto.getYearOfCreation());
        painting.setHeight(dto.getHeight());
        painting.setWidth(dto.getWidth());
        painting.setDescription(dto.getDescription());
        Image image = imageMapper.toImageModel(dto.getImage());
        List<RoomView> viewRooms = roomViewManager.getViewRooms(image.getId(),
                dto.getWidth(), dto.getHeight());
        image.getRoomViews().addAll(viewRooms);
        painting.setImage(image);

        dto.getStyleIds().forEach(
                styleId -> painting.getStyles().add(styleService.getReferenceById(styleId)));
        dto.getMediumIds().forEach(
                mediumId -> painting.getMediums().add(mediumService.getReferenceById(mediumId))
        );
        dto.getSupportIds().forEach(
                supportId -> painting.getSupports().add(supportService.getReferenceById(supportId))
        );

        return painting;
    }
}
