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
@GroupSequence({AccountShippingRequestDto.class, FirstOrder.class, SecondOrder.class, ThirdOrder.class})
public class AccountShippingRequestDto {
    @NotBlank(message = "first name field is required", groups = FirstOrder.class)
    private String firstName;
    @NotBlank(message = "last name field is required", groups = FirstOrder.class)
    private String lastName;
    @NotBlank(message = "phone field is required", groups = FirstOrder.class)
    private String phone;

    @NotBlank(message = "address line1 field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 40,
            message = "address line1 must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s'-/]+", message = "address line1 except only Latin, space, ', /, and -",
            groups = ThirdOrder.class)
    private String addressLine1;

    @Size(min = 1, max = 40,
            message = "address line2 must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s'-/]+", message = "address line2 except only Latin, space, ', /, and -",
            groups = ThirdOrder.class)
    private String addressLine2;

    @NotBlank(message = "city field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 40,
            message = "city must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s'-/]+", message = "city except only Latin, space, ', /, and -",
            groups = ThirdOrder.class)
    private String city;

    @Size(min = 1, max = 50,
            message = "state must be between 1 and 50 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s'-/]+", message = "state except only Latin, space, ', /, and -",
            groups = ThirdOrder.class)
    private String state;
    @NotBlank(message = "country field is required", groups = FirstOrder.class)
    @Size(min = 1, max = 56,
            message = "country must be between 1 and 56 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\s'-/]+", message = "country except only Latin, space, ', /, and -",
            groups = ThirdOrder.class)
    private String country;
    @NotBlank(message = "postalcode field is required", groups = FirstOrder.class)
    @Size(min = 4, max = 10,
            message = "postalcode must be between 1 and 40 characters", groups = SecondOrder.class)
    @Pattern(regexp = "[\\p{IsLatin}\\d\\s-]+", message = "postalcode except only Latin, space, and -",
            groups = ThirdOrder.class)
    private String postalCode;
}
