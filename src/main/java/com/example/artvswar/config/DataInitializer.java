package com.example.artvswar.config;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.Painting;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PaintingService;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final AuthorService authorService;
    private final PaintingService paintingService;

    public DataInitializer(AuthorService authorService, PaintingService paintingService) {
        this.authorService = authorService;
        this.paintingService = paintingService;
    }

    @PostConstruct
    public void inject() {
        Author bilokyr = new Author();
        bilokyr.setName("Bilokyr");
        Author savedBilokyr = authorService.save(bilokyr);

        Painting flowers = new Painting();
        flowers.setTitle("Flowers");
        flowers.setAuthor(savedBilokyr);
        flowers.setImageFileName("Flowers.jpg");
        paintingService.save(flowers);

        Author marchuk = new Author();
        marchuk.setName("Marchuk");
        Author savedMarchuk = authorService.save(marchuk);

        Painting sunrise = new Painting();
        sunrise.setTitle("Sunrise over the Dnipro");
        sunrise.setAuthor(savedMarchuk);
        sunrise.setImageFileName("Sunrise.jpg");
        paintingService.save(sunrise);
    }
}
