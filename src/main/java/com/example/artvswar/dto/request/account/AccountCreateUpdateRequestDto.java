package com.example.artvswar.dto.request.account;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import lombok.Data;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@GroupSequence({AccountCreateUpdateRequestDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class AccountCreateUpdateRequestDto {
    @NotBlank(message = "first name field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 30,
            message = "first name must be between 1 and 30 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s'-]+", message = "first name except only Latin, space, ' and -",
            groups = ThirdOrder.class)
    private String firstName;

    @NotBlank(message = "last name field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 30,
            message = "last name must be between 1 and 30 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s'-]+", message = "last name except only Latin, space, ' and -",
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
