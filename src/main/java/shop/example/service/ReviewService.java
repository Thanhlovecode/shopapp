package shop.example.service;

import org.springframework.data.domain.Pageable;
import shop.example.dto.request.review.ReviewRequest;
import shop.example.dto.request.review.ReviewUpdateRequest;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.review.ReviewResponse;
import shop.example.dto.response.user.UserReviewResponse;

import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(Long productId, ReviewRequest reviewRequest);
    void updateReview(Long reviewId, ReviewUpdateRequest updateRequest);
    void deleteReview(Long reviewId);
    PageResponse<List<ReviewResponse>> getAllReviewsByProductId(Long productId, Pageable pageable);
    PageResponse<List<UserReviewResponse>> getAllReviewsByUserId(Long userId, Pageable pageable);

}
