package com.example.artvswar.dto.response.painting;

import com.example.artvswar.util.DateTimePatternUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PaintingViewDto {
    Long getId();

    String getTitle();

    String getDescription();

    BigDecimal getPrice();

    int getHeight();

    int getWidth();

    int getYearOfCreation();

    AuthorView getAuthor();

    interface AuthorView {
        String getCognitoUsername();
        String getFullName();
    }

    List<StyleView> getStyles();

    interface StyleView {
        Long getId();
        String getName();
    }

    List<MediumView> getMediums();

    interface MediumView {
        Long getId();
        String getName();
    }

    List<SupportView> getSupports();

    interface SupportView {
        Long getId();
        String getName();
    }

    List<SubjectView> getSubjects();

    interface SubjectView {
        Long getId();
        String getName();
    }

    String getImageFileName();
    @JsonFormat(pattern = DateTimePatternUtil.DATE_TIME_PATTERN)
    LocalDateTime getEntityCreatedAt();
}
