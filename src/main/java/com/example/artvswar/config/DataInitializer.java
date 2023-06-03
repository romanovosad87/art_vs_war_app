package com.example.artvswar.config;

import com.example.artvswar.model.Medium;
import com.example.artvswar.model.Style;
import com.example.artvswar.model.Subject;
import com.example.artvswar.model.Support;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.MediumService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.service.StyleService;
import com.example.artvswar.service.SubjectService;
import com.example.artvswar.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
    private final SubjectService subjectService;

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

        List.of("Abstract", "Landscape", "Architecture", "People", "Portrait", "Fantasy", "Political", "Still Life",
                        "Animal", "Flora", "Symbolism")
                .forEach(subject -> subjectService.save(new Subject(subject)));

    }
}
