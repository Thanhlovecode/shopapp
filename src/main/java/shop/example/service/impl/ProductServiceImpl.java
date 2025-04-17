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
import shop.example.domain.Inventory;
import shop.example.domain.Product;
import shop.example.dto.request.product.ProductRequest;
import shop.example.dto.request.product.ProductSearchRequest;
import shop.example.dto.request.product.ProductUpdateRequest;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.product.ProductResponse;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.ResourceExistedException;
import shop.example.exceptions.ResourceNotFoundException;
import shop.example.repository.CategoryRepository;
import shop.example.repository.ProductRepository;
import shop.example.repository.specification.ProductSpecification;
import shop.example.service.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final DataConverter productConverter;
    private final ModelMapper modelMapper;

    @Override
    @Cacheable(value = "products")
    public PageResponse<List<ProductResponse>> getAllProducts(Pageable pageable) {
        return buildPageResponse(productRepository.findAll(pageable));
    }

    @Override
    @Cacheable(value = "products")
    public PageResponse<List<ProductResponse>> getAllProductsByCategoryId(Long categoryId, Pageable pageable) {
        return buildPageResponse(productRepository.findByCategoryId(categoryId, pageable));
    }


    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void createProduct(ProductRequest productRequest) {
        checkInvalidProduct(productRequest);
        var categoryProxy = categoryRepository.getReferenceById(productRequest.getCategoryId());
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .category(categoryProxy)
                .build();
        Inventory inventory = Inventory.builder()
                .product(product)
                .stockQuantity(productRequest.getQuantity())
                .build();
        product.setInventory(inventory);
        productRepository.save(product);
    }


    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void updateProduct(Long id, ProductUpdateRequest updateRequest) {
        Product product = findProductById(id);
        modelMapper.map(updateRequest, product);
        product.getInventory().setStockQuantity(updateRequest.getQuantity());
    }

    @Override
    public ProductResponse findByProductId(Long id) {
        return productConverter.convertProductResponse(findProductById(id));
    }

    @Override
    public PageResponse<List<ProductResponse>> searchProducts(ProductSearchRequest request,
                                                              Pageable pageable) {
        List<Long> listProductId= productRepository.findProductIds(request.getName());
        return buildPageResponse(productRepository.findAll(ProductSpecification.
                filter(request,listProductId), pageable));
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void checkInvalidProduct(ProductRequest productRequest) {
        if (!categoryRepository.existsById(productRequest.getCategoryId())) {
            throw new ResourceNotFoundException(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
        }
        if (productRepository.findByNameIgnoreCase(productRequest.getName()).isPresent()) {
            throw new ResourceExistedException(ErrorCode.PRODUCT_EXISTED.getMessage());
        }
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage()));
    }

    private PageResponse<List<ProductResponse>> buildPageResponse(Page<Product> products) {
        return PageResponse.<List<ProductResponse>>builder()
                .pageNo(products.getNumber())
                .pageSize(products.getSize())
                .totalPage(products.getTotalPages())
                .totalElement(products.getTotalElements())
                .items(products.getContent().stream().map(productConverter::convertProductResponse).toList())
                .build();
    }

}
