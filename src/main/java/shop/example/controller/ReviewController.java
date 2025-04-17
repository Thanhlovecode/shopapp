package shop.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.example.dto.request.review.ReviewRequest;
import shop.example.dto.request.review.ReviewUpdateRequest;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.common.ResponseData;
import shop.example.dto.response.review.ReviewResponse;
import shop.example.dto.response.user.UserReviewResponse;
import shop.example.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("${api.prefix}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create/{productId}")
    @Operation(summary = "create review for productId", description = "API for create review for productId")
    public ResponseEntity<ResponseData<ReviewResponse>> createReview(@PathVariable("productId") Long productId,
                                                                     @Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseData.<ReviewResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("create review successfully")
                        .data(reviewService.createReview(productId, reviewRequest))
                        .build());
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "update review by reviewId", description = "API for update review by reviewId")
    public ResponseEntity<ResponseData<?>> updateReview(@PathVariable("reviewId") Long reviewId,
                                                        @Valid @RequestBody ReviewUpdateRequest updateRequest) {
        reviewService.updateReview(reviewId, updateRequest);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("update successfully")
                .build());
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "delete review by reviewId", description = "API for update review by reviewId")
    public ResponseEntity<ResponseData<?>> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("delete successfully")
                .build());
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get all reviews by productId", description = "API for get all reviews by productId")
    public ResponseEntity<ResponseData<PageResponse<List<ReviewResponse>>>> getAllReviewsByProductId(
            @PathVariable("productId") Long productId, Pageable pageable) {
        return ResponseEntity.ok(ResponseData.<PageResponse<List<ReviewResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(reviewService.getAllReviewsByProductId(productId, pageable))
                .build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "get all reviews by userId", description = "API for get all reviews by userId")
    public ResponseEntity<ResponseData<PageResponse<List<UserReviewResponse>>>> getAllReviewsByUserId(
            @PathVariable("userId") Long userId, Pageable pageable) {
        return ResponseEntity.ok(ResponseData.<PageResponse<List<UserReviewResponse>>>builder()
                .status(HttpStatus.OK.value())
                .message("success")
                .data(reviewService.getAllReviewsByUserId(userId, pageable))
                .build());
    }
}

