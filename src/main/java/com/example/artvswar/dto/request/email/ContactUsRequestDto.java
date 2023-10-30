package com.example.artvswar.dto.request.email;

import com.example.artvswar.lib.FirstOrder;
import com.example.artvswar.lib.SecondOrder;
import com.example.artvswar.lib.ThirdOrder;
import lombok.Data;
import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@GroupSequence({ContactUsRequestDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class ContactUsRequestDto {
    @NotBlank(message = "email field is required", groups = FirstOrder.class)
    @Email(message = "incorrectly formed email address",
            regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,15}$",
            groups = SecondOrder.class)
    String email;

    @NotBlank(message = "message field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 10000,
            message = "message must be between 1 and 10 000 characters", groups = SecondOrder.class)
    String message;
}
