package com.example.artvswar.dto.request.account;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import lombok.Data;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@GroupSequence({AccountCreateUpdateRequestDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class AccountCreateUpdateRequestDto {
    @NotBlank(message = "first name field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 30,
            message = "first name must be between 1 and 30 characters", groups = SecondOrder.class)
    @Pattern(regexp = "\\b[A-Z][\\p{IsLatin}\\w\\s\\p{P}\\p{S}]*", message = "first name must start "
            + "with capital letter and except only Latin",
            groups = ThirdOrder.class)
    private String firstName;

    @NotBlank(message = "last name field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 30,
            message = "last name must be between 1 and 30 characters", groups = SecondOrder.class)
    @Pattern(regexp = "\\b[A-Z][\\p{IsLatin}\\w\\s\\p{P}\\p{S}]*", message = "last name must start "
            + "with capital letter except only Latin",
            groups = ThirdOrder.class)
    private String lastName;

    @NotBlank(message = "email field is required", groups = FirstOrder.class)
    private String email;

    @NotBlank(message = "phone field is required", groups = FirstOrder.class)
    @Size(min = 7, max = 30,
            message = "phone field must be between 7 and 30 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\d\\s-()+]*", message = "phone field except only digits, space '(', ')', '+' and '-'",
            groups = ThirdOrder.class)
    private String phone;

}
