package com.vce.vce.category;

import com.vce.vce.category.dto.CategoryDTO;
import com.vce.vce.category.dto.CreateCategoryDTO;
import com.vce.vce.category.dto.UpdateCategoryDTO;
import com.vce.vce.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hubs/{hubId}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(@PathVariable Long hubId) {
        return ResponseEntity.ok(categoryService.getAllCategories(hubId));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @PathVariable Long hubId,
            @Valid @RequestBody CreateCategoryDTO createCategoryDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.create(hubId, createCategoryDTO, user));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long hubId,
            @PathVariable Long categoryId,
            @RequestBody UpdateCategoryDTO updateCategoryDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.update(hubId, categoryId, updateCategoryDTO, user));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long hubId,
            @PathVariable Long categoryId,
            @AuthenticationPrincipal User user
    ) {
        categoryService.delete(hubId, categoryId, user);
        return ResponseEntity.noContent().build();
    }
}
