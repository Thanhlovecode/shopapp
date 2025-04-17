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
import shop.example.dto.request.product.ProductRequest;
import shop.example.dto.request.product.ProductSearchRequest;
import shop.example.dto.request.product.ProductUpdateRequest;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.product.ProductResponse;
import shop.example.dto.response.common.ResponseData;
import shop.example.service.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product Controller")
@Slf4j
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "get all products", description = "API for get all products")
    public ResponseEntity<ResponseData<PageResponse<List<ProductResponse>>>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(ResponseData.<PageResponse<List<ProductResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(productService.getAllProducts(pageable))
                .build());
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "get all products by categoryId", description = "API for get all products by categoryId")
    public ResponseEntity<ResponseData<PageResponse<List<ProductResponse>>>> getAllProductsByCategoryId(
            @PathVariable("categoryId") Long categoryId, Pageable pageable) {
        return ResponseEntity.ok(ResponseData.<PageResponse<List<ProductResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(productService.getAllProductsByCategoryId(categoryId, pageable))
                .build());
    }

    @PostMapping
    @Operation(summary = "create new product", description = "API for create new product")
    public ResponseEntity<ResponseData<?>> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        productService.createProduct(productRequest);
        log.info("Product '{}' created successfully", productRequest.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseData.builder()
                .status(HttpStatus.CREATED.value())
                .message("Create product successfully")
                .build());
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "delete product by productId", description = "API for delete product by productId")
    public ResponseEntity<ResponseData<?>> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        log.info("Product with '{}' deleted successfully", id);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("deleted product successfully")
                .build());
    }

    @PutMapping("/{productId}")
    @Operation(summary = "update product by productId", description = "API for update product by productId")
    public ResponseEntity<ResponseData<?>> updateProduct(
            @PathVariable("productId") Long productId, @RequestBody @Valid ProductUpdateRequest request) {
        productService.updateProduct(productId, request);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("update product successfully")
                .build());
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get product by Id", description = "API for get product by id")
    public ResponseEntity<ResponseData<ProductResponse>> getProductById(
            @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(ResponseData.<ProductResponse>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(productService.findByProductId(productId))
                .build());
    }

    @GetMapping("/search")
    @Operation(summary = "search product", description = "API for search product")
    public ResponseEntity<ResponseData<PageResponse<List<ProductResponse>>>> searchProduct(
            @ModelAttribute ProductSearchRequest request, Pageable pageable){
        return ResponseEntity.ok(ResponseData.<PageResponse<List<ProductResponse>>>builder()
                        .status(HttpStatus.OK.value())
                        .message("success")
                        .data(productService.searchProducts(request,pageable))
                .build());
    }

//    @PostMapping("/generateFakeProducts")
//    private ResponseEntity<String> generateFakeProducts() {
//        Faker faker = new Faker();
//
//        for (int i = 0; i < 10_000; i++) {
//            String productName = faker.commerce().productName();
//            if(productRepository.findByNameIgnoreCase(productName).isPresent()){
//                continue;
//            }
//            ProductRequest productRequest = ProductRequest.builder()
//                    .name(productName)
//                    .price((double)faker.number().numberBetween(10, 90_000_000))
//                    .categoryId((long)faker.number().numberBetween(1,3))
//                    .quantity(faker.number().numberBetween(5,20))
//                    .build();
//            productService.createProduct(productRequest);
//        }
//        return ResponseEntity.ok("Fake Products created successfully");
//    }


}
