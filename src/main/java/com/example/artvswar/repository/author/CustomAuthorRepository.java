package com.example.artvswar.repository.author;

import com.example.artvswar.dto.response.author.AuthorResponseDto;
import com.example.artvswar.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CustomAuthorRepository {
    Map<String, Set<String>> authorsAllStyleMap(List<String> authorsCognitoSubjects);


    Page<AuthorResponseDto> findAllBySpecification(Specification<Author> specification,
                                                   Pageable pageable);

}
