package com.example.artvswar.config;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.Medium;
import com.example.artvswar.model.Painting;
import com.example.artvswar.model.Style;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.service.StyleService;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final AuthorService authorService;
    private final PaintingService paintingService;
    private final StyleService styleService;
    private final MediumService mediumService;

    public DataInitializer(AuthorService authorService, PaintingService paintingService,
                           StyleService styleService, MediumService mediumService) {
        this.authorService = authorService;
        this.paintingService = paintingService;
        this.styleService = styleService;
        this.mediumService = mediumService;
    }

    @PostConstruct
    public void inject() {
        Author bilokyr = new Author();
        bilokyr.setName("Kateryna Bilokyr");
        bilokyr.setCountry("Germany");
        bilokyr.setCity("Cologne");
        bilokyr.setShortStory(" Ukrainian folk artist born in the Poltava Governorate");
        Author savedBilokyr = authorService.save(bilokyr);

        Style styleExpr = new Style();
        styleExpr.setName("Expressionism");
        styleService.save(styleExpr);

        Medium mediumOil = new Medium();
        mediumOil.setName("Oil");
        mediumService.save(mediumOil);

        Painting flowers = new Painting();
        flowers.setTitle("Flowers");
        flowers.setDescription("Amazing!");
        flowers.setPrice(BigDecimal.valueOf(500));
        flowers.setHeight(50);
        flowers.setWidth(60);
        flowers.setAuthor(savedBilokyr);
        flowers.setImageFileName("Flowers.jpg");
        flowers.setStyle(styleExpr);
        flowers.setMedium(mediumOil);
        paintingService.save(flowers);

        Author marchuk = new Author();
        marchuk.setName("Ivan Marchuk");
        marchuk.setCountry("France");
        marchuk.setCity("Reims");
        marchuk.setShortStory("France");
        marchuk.setShortStory("A contemporary Ukrainian painter, Honored Artist of Ukraine, "
                + "laureate of the Shevchenko National Prize, "
                + "Honorary Citizen of Ternopil and Kyiv");
        Author savedMarchuk = authorService.save(marchuk);

        Painting sunrise = new Painting();
        sunrise.setTitle("Sunrise over the Dnipro");
        sunrise.setDescription("Cute!");
        sunrise.setPrice(BigDecimal.valueOf(400));
        sunrise.setHeight(70);
        sunrise.setWidth(70);
        sunrise.setAuthor(savedMarchuk);
        sunrise.setImageFileName("Sunrise.jpg");
        sunrise.setStyle(styleExpr);
        sunrise.setMedium(mediumOil);
        paintingService.save(sunrise);
    }
}
