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
        Author bilokin = new Author();
        bilokin.setName("Bilokin");
        Author savedBilokin = authorService.save(bilokin);

        Painting flowers = new Painting();
        flowers.setTitle("Flowers");
        flowers.setAuthor(savedBilokin);
        flowers.setImageFileName("Flowers.jpg");
        paintingService.save(flowers);

//        Author marchuk = new Author();
//        marchuk.setName("Marchuk");
//        Author savedMarchuk = authorService.save(marchuk);
//
//        Picture sunrise = new Picture();
//        sunrise.setTitle("Sunrise over the Dnipro");
//        sunrise.setAuthor(savedMarchuk);
//        sunrise.setPictureUrl("https://drive.google.com/file/d/1lHjMpynHEujXRh4kj3mDP9JeNu_p_MVY/view?usp=share_link");
//        pictureService.save(sunrise);
    }
}
