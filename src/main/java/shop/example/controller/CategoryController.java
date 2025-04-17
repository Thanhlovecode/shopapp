package shop.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.example.dto.request.category.CategoryRequest;
import shop.example.dto.response.category.CategoryResponse;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.common.ResponseData;
import shop.example.service.CategoryService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
@Tag(name = "Category Controller")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "create new category", description = "API for create new category")
    public ResponseEntity<ResponseData<?>> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        categoryService.createCategory(categoryRequest);
        log.info("create '{}' category successfully", categoryRequest.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.builder()
                .status(HttpStatus.CREATED.value())
                .message("create new category successfully")
                .build());
    }

    @GetMapping
    @Operation(summary = "get all categories", description = "API for get all categories")
    public ResponseEntity<ResponseData<PageResponse<List<CategoryResponse>>>> getAllCategories(Pageable pageable) {
        return ResponseEntity.ok(ResponseData.<PageResponse<List<CategoryResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(categoryService.getAllCategories(pageable))
                .build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete category by id", description = "API for delete category by id")
    public ResponseEntity<ResponseData<?>> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        log.info("category with '{}' is deleted successfully", id);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("delete category successfully")
                .build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "update category by id ", description = "API for update category by id")
    public ResponseEntity<ResponseData<?>> updateCategory(@PathVariable("id") Long id,
                                                          @RequestBody @Valid CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        log.info("Category with '{}' updated successfully", id);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("update category successfully")
                .build());
    }
}
