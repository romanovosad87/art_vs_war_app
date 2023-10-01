package com.example.artvswar.util.image;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    public String generateUrl(String id) {
        return cloudinary.url().transformation(
                        new Transformation<>()
                                .width(1920)
                                .height(1080)
                                .crop("fit")
                                .quality("auto"))
                .generate(id);
    }

    public String generateBlurUrl(String id) {
        return cloudinary.url().transformation(
                        new Transformation<>()
                                .width(20)
                                .crop("scale")
                                .quality("auto"))
                .generate(id);
    }

    public String generateAuthorPhotoUrl(String id) {
        return cloudinary.url().transformation(
                        new Transformation<>()
                                .width(500)
                                .height(700)
                                .crop("fit")
                                .quality("auto"))
                .generate(id);
    }

    public String photoImageEagerTransformation(String publicId) {
        try {
            Map<String, Object> eagerUpload = cloudinary.uploader().explicit(publicId, ObjectUtils.asMap(
                    "type", "upload",
                    "eager", Collections.singletonList(new Transformation<>()
                            .width(500).height(700).crop("fit")
                            .quality("auto").fetchFormat("auto")
                    )));
            List<Map<String, Object>> eager = (List<Map<String, Object>>) eagerUpload.get("eager");
            return (String) eager.get(0).get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Can't eager upload photo image transformation "
                                    + "for asset with public id = %s",
                            publicId), e);
        }
    }

    public String paintingImageEagerTransformation(String publicId) {
        try {
            Map<String, Object> eagerUpload = cloudinary.uploader().explicit(publicId, ObjectUtils.asMap(
                    "type", "upload",
                    "eager", Collections.singletonList(new Transformation<>()
                            .width(1920).height(1080).crop("fit")
                            .quality("auto").fetchFormat("auto")
                    )));
            List<Map<String, Object>> eager = (List<Map<String, Object>>) eagerUpload.get("eager");
            return (String) eager.get(0).get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Can't eager upload additional painting image transformation "
                                    + "for asset with public id = %s",
                            publicId), e);
        }
    }
}
