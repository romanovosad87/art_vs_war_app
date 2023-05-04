package com.example.artvswar.dto.response;

import com.example.artvswar.dto.response.page.PageResponse;
import lombok.Data;
import java.util.List;

@Data
public class EntitiesPageResponse<T> {
    private List<T> entities;
    private PageResponse page;
}
