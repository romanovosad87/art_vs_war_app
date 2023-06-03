package com.example.artvswar.util.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageTransformation {
    private final Cloudinary cloudinary;

    public String generateUrl(String id, double transformedRatio) {
        return cloudinary.url().transformation(
                            new Transformation<>().height(200)
                                    .aspectRatio(transformedRatio)
                                    .crop("fill")
                                    .gravity("auto")
                                    .quality("auto")
                                    .fetchFormat("auto"))
                    .generate(id);
    }
}
