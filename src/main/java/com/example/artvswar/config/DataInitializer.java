package com.example.artvswar.config;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.Medium;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.Support;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.service.StyleService;
import com.example.artvswar.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final AuthorService authorService;
    private final PaintingService paintingService;
    private final StyleService styleService;
    private final MediumService mediumService;
    private final SupportService supportService;

    @PostConstruct
    public void inject() {
        List.of("abstraction", "cubism", "expressionism", "impressionism", "photorealism",
                        "minimalism", "modern", "pop art", "primitivism/naive art", "realism",
                        "surrealism")
                .forEach(style -> styleService.save(new Style(style)));

        List.of("oil", "acrylic", "watercolor", "gouache", "collage", "pencil", "levkas",
                        "pastel", "mixed media", "lonocut", "monotype", "enamel")
                .forEach(medium -> mediumService.save(new Medium(medium)));

        List.of("canvas", "paper", "hardboard", "cardboard", "silk", "glass", "wooden board", "plywood")
                .forEach(support -> supportService.save(new Support(support)));


        Author bilokyr = new Author();
        bilokyr.setName("Kateryna Bilokyr");
        bilokyr.setCountry("Germany");
        bilokyr.setCity("Cologne");
        bilokyr.setShortStory(" Ukrainian folk artist born in the Poltava Governorate");
        bilokyr.setEmail("bilokyr@ukr.net");
        Author savedBilokyr = authorService.save(bilokyr);

        Author marchuk = new Author();
        marchuk.setName("Ivan Marchuk");
        marchuk.setCountry("France");
        marchuk.setCity("Reims");
        marchuk.setShortStory("France");
        marchuk.setShortStory("A contemporary Ukrainian painter, Honored Artist of Ukraine, "
                + "laureate of the Shevchenko National Prize, "
                + "Honorary Citizen of Ternopil and Kyiv");
        marchuk.setEmail("marchyk@gmail.com");
        Author savedMarchuk = authorService.save(marchuk);

        paintingService.save(new Painting("Flowers_1", BigDecimal.valueOf(500), "Amazing!",
                50, 60, 1986, savedBilokyr, styleService.get(1L), mediumService.get(1L),
                supportService.get(1L), "Flowers.jpg"));

        paintingService.save(new Painting("Flowers_2", BigDecimal.valueOf(350), "Amazing!",
                20, 20, 2004, savedBilokyr, styleService.get(3L), mediumService.get(4L),
                supportService.get(6L), "Flowers2.jpg"));

        paintingService.save(new Painting("Flowers_3", BigDecimal.valueOf(1500), "Amazing!",
                90, 150, 2020, savedBilokyr, styleService.get(5L), mediumService.get(2L),
                supportService.get(3L), "Flowers3.jpg"));

        paintingService.save(new Painting("Flowers_4", BigDecimal.valueOf(100), "Amazing!",
                30, 25, 2008, savedBilokyr, styleService.get(2L), mediumService.get(3L),
                supportService.get(2L), "Flowers4.jpg"));

        paintingService.save(new Painting("Sunrise over the Dnipro_1", BigDecimal.valueOf(800), "Cute!",
                70, 70, 1985, savedMarchuk, styleService.get(2L), mediumService.get(2L),
                supportService.get(2L), "Sunrise.jpg"));

        paintingService.save(new Painting("Sunrise over the Dnipro_2", BigDecimal.valueOf(1100), "Cute!",
                90, 90, 1999, savedMarchuk, styleService.get(8L), mediumService.get(4L),
                supportService.get(4L), "Sunrise2.jpg"));

        paintingService.save(new Painting("Sunrise over the Dnipro_3", BigDecimal.valueOf(500), "Cute!",
                60, 10, 2006, savedMarchuk, styleService.get(7L), mediumService.get(11L),
                supportService.get(5L), "Sunrise3.jpg"));

        paintingService.save(new Painting("Sunrise over the Dnipro_4", BigDecimal.valueOf(388), "Cute!",
                75, 75, 2021, savedMarchuk, styleService.get(4L), mediumService.get(10L),
                supportService.get(8L), "Sunrise4.jpg"));
    }
}
