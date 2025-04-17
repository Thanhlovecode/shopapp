package shop.example.service;

import org.springframework.data.domain.Pageable;
import shop.example.dto.request.category.CategoryRequest;
import shop.example.dto.response.category.CategoryResponse;
import shop.example.dto.response.common.PageResponse;

import java.util.List;

public interface CategoryService {
    PageResponse<List<CategoryResponse>> getAllCategories(Pageable pageable);
    void createCategory(CategoryRequest categoryRequest);
    void deleteCategory(Long id);
    void updateCategory(Long id,CategoryRequest categoryRequest);
}
