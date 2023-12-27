package com.example.artvswar.util;

import com.example.artvswar.dto.response.painting.PaintingMainPageResponseDto;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class MainPageDefaultImage {
    private static final String STRING = "https://res.cloudinary.com/dq415fvzp/image/upload";
    private static final String PRETTY_ID = "";
    private static final String AUTHOR_PRETTY_ID = "ArtvsWar";
    private static final PaintingMainPageResponseDto IMG_075_1
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703548877/art-app/service/main_page/0.75-1.jpg");
    private static final PaintingMainPageResponseDto IMG_075_2
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703548922/art-app/service/main_page/0.75-2.jpg");

    private static final PaintingMainPageResponseDto IMG_100_1
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703548954/art-app/service/main_page/1-1.jpg");

    private static final PaintingMainPageResponseDto IMG_100_2
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703548971/art-app/service/main_page/1-2.jpg");

    private static final PaintingMainPageResponseDto IMG_100_3
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703548985/art-app/service/main_page/1-3.jpg");

    private static final PaintingMainPageResponseDto IMG_100_4
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549000/art-app/service/main_page/1-4.jpg");

    private static final PaintingMainPageResponseDto IMG_125_1
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549048/art-app/service/main_page/1.25-1.jpg");

    private static final PaintingMainPageResponseDto IMG_125_2
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549063/art-app/service/main_page/1.25-2.jpg");

    private static final PaintingMainPageResponseDto IMG_125_3
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549083/art-app/service/main_page/1.25-3.jpg");

    private static final PaintingMainPageResponseDto IMG_150_1
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549118/art-app/service/main_page/1.5-1.jpg");

    private static final PaintingMainPageResponseDto IMG_150_2
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549133/art-app/service/main_page/1.5-2.jpg");

    private static final PaintingMainPageResponseDto IMG_175_1
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549166/art-app/service/main_page/1.75-1.jpg");

    private static final PaintingMainPageResponseDto IMG_200_1
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549385/art-app/service/main_page/2-1.jpg");

    private static final PaintingMainPageResponseDto IMG_200_2
            = new PaintingMainPageResponseDto(PRETTY_ID, AUTHOR_PRETTY_ID,
            STRING + "/v1703549399/art-app/service/main_page/2-2.jpg");

    public static Map<Double, List<PaintingMainPageResponseDto>> getDefaultImageMap() {
        Map<Double, List<PaintingMainPageResponseDto>> map = new HashMap<>();
        map.put(0.75, new ArrayList<>(List.of(IMG_075_1, IMG_075_2)));
        map.put(1.0, new ArrayList<>(List.of(IMG_100_1, IMG_100_2, IMG_100_3, IMG_100_4)));
        map.put(1.25, new ArrayList<>(List.of(IMG_125_1, IMG_125_2, IMG_125_3)));
        map.put(1.5, new ArrayList<>(List.of(IMG_150_1, IMG_150_2)));
        map.put(1.75, new ArrayList<>(List.of(IMG_175_1)));
        map.put(2.0, new ArrayList<>(List.of(IMG_200_1, IMG_200_2)));
        return map;
    }
}
