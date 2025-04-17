package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.converter.DataConverter;
import shop.example.domain.Category;
import shop.example.dto.request.category.CategoryRequest;
import shop.example.dto.response.category.CategoryResponse;
import shop.example.dto.response.common.PageResponse;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.ResourceExistedException;
import shop.example.exceptions.ResourceNotFoundException;
import shop.example.repository.CategoryRepository;
import shop.example.service.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final DataConverter categoryConverter;
    private final ModelMapper modelMapper;

    @Override
    @Cacheable(value = "categories")
    public PageResponse<List<CategoryResponse>> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return PageResponse.<List<CategoryResponse>>builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(categories.getTotalPages())
                .totalElement(categories.getTotalElements())
                .items(categories.getContent().stream().map(categoryConverter::convertCategoryResponse).toList())
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories",allEntries = true)
    public void createCategory(CategoryRequest categoryRequest) {
        if(categoryRepository.findByNameIgnoreCase(categoryRequest.getName()).isPresent()){
            throw new ResourceExistedException(ErrorCode.CATEGORY_EXISTED.getMessage());
        }
        Category category =categoryConverter.convertCategoryEntity(categoryRequest);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories",allEntries = true)
    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage()));
        modelMapper.map(categoryRequest,category);
    }
}
