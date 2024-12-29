package com.vce.vce._shared.model.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableDTO {
    protected int page = 0;
    protected int size = 10;
    protected String sortBy = "id";
    protected String sortDirection = "ASC";

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);
    }
}
