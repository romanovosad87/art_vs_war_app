package com.example.artvswar.dto.response.error;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class AllErrorFieldResponse {
    private Date timestamp;
    private int status;
    private String error;
    private int fieldErrorCount;
    private List<ErrorFieldResponse> errors;

}
