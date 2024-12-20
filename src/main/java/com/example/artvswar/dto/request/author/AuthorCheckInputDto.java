package com.example.artvswar.dto.request.author;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import lombok.Data;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@GroupSequence({AuthorCheckInputDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class AuthorCheckInputDto {
    @NotBlank(message = "full name field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 40,
            message = "full name must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\w\\s\\p{P}\\p{S}]+", message = "full name except only Latin",
            groups = ThirdOrder.class)
    private String fullName;

    @NotBlank(message = "country field is required", groups = FirstOrder.class)
    @Size(min = 2, max = 50,
            message = "country must be between 2 and 50 characters", groups = SecondOrder.class)
    @Pattern(regexp = "\\b[A-Z][\\p{IsLatin}\\w\\s\\p{P}\\p{S}]+", message = "country must start with capital letter,"
            + " except only Latin", groups = ThirdOrder.class)
    private String country;

    @NotBlank(message = "city field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 40,
            message = "city must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "\\b[A-Z][\\p{IsLatin}\\w\\s\\p{P}\\p{S}]*", message = "city must start with capital letter,"
            + " except only Latin", groups = ThirdOrder.class)
    private String city;

    @NotBlank(message = "About Me field is required", groups = FirstOrder.class)
    @Size(min = 3, max = 1000,
            message = "About Me must be between 3 and 1000 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\w\\s\\p{P}\\p{S}]+", message = "About Me except only Latin",
            groups = ThirdOrder.class)
    private String aboutMe;

    @NotBlank(message = "email field is required", groups = FirstOrder.class)
    private String email;
}
