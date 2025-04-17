package shop.example.service;

import org.springframework.data.domain.Pageable;
import shop.example.dto.request.product.ProductRequest;
import shop.example.dto.request.product.ProductSearchRequest;
import shop.example.dto.request.product.ProductUpdateRequest;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.product.ProductResponse;

import java.util.List;

public interface ProductService {
    PageResponse<List<ProductResponse>> getAllProducts(Pageable pageable);
    PageResponse<List<ProductResponse>> getAllProductsByCategoryId(Long id,Pageable pageable);
    void createProduct(ProductRequest productRequest);
    void updateProduct(Long id,ProductUpdateRequest updateRequest);
    void deleteProduct(Long id);
    ProductResponse findByProductId(Long id);
    PageResponse<List<ProductResponse>> searchProducts(ProductSearchRequest request,Pageable pageable);

}
