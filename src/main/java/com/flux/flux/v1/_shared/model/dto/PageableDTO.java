package com.flux.flux.v1._shared.model.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
@ParameterObject
public class PageableDTO {
    @Parameter(description = "Page number", example = "0")
    private Integer page = 0;

    @Parameter(description = "Page size", example = "10")
    private Integer size = 10;

    @Parameter(description = "Sort field", example = "id")
    private String sortBy = "id";

    @Parameter(description = "Sort direction", example = "ASC")
    private String sortDirection = "ASC";

    public Pageable toPageable() {
        return PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortBy);
    }
}