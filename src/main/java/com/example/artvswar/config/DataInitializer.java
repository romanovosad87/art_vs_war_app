package com.example.artvswar.config;

import com.example.artvswar.model.Author;
import com.example.artvswar.model.Picture;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.PictureService;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class DataInitializer {
    private final AuthorService authorService;
    private final PictureService pictureService;

    public DataInitializer(AuthorService authorService, PictureService pictureService) {
        this.authorService = authorService;
        this.pictureService = pictureService;
    }

    @PostConstruct
    public void inject() {
        Author author = new Author();
        author.setName("Tumchenko");
        Author savedAuthor = authorService.save(author);

        Picture picture = new Picture();
        picture.setTitle("Winter");
        picture.setAuthor(savedAuthor);
        pictureService.save(picture);
    }
}
