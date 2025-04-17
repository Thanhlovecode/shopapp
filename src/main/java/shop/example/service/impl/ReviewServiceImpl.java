package shop.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.converter.DataConverter;
import shop.example.domain.Review;
import shop.example.dto.request.review.ReviewRequest;
import shop.example.dto.request.review.ReviewUpdateRequest;
import shop.example.dto.response.common.PageResponse;
import shop.example.dto.response.review.ReviewResponse;
import shop.example.dto.response.user.UserReviewResponse;
import shop.example.enums.ErrorCode;
import shop.example.exceptions.ResourceNotFoundException;
import shop.example.repository.ProductRepository;
import shop.example.repository.ReviewRepository;
import shop.example.repository.UserRepository;
import shop.example.service.ReviewService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DataConverter dataConverter;

    @Override
    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequest updateRequest) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Find not found reviewId with " + reviewId));
        review.setComment(updateRequest.getComment());
        review.setRating(updateRequest.getRating());
        reviewRepository.save(review);
        log.info("update review with id {} successfully", review.getId());
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
        log.info("deleted successfully with reviewId:{}", reviewId);
    }

    @Override
    public PageResponse<List<ReviewResponse>> getAllReviewsByProductId(Long productId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByProductId(productId, pageable);
        return PageResponse.<List<ReviewResponse>>builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElement(reviews.getTotalElements())
                .totalPage(reviews.getTotalPages())
                .items(reviews.getContent().stream().map(dataConverter::convertReviewResponse).toList())
                .build();
    }

    @Override
    public PageResponse<List<UserReviewResponse>> getAllReviewsByUserId(Long userId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByUserId(userId, pageable);
        return PageResponse.<List<UserReviewResponse>>builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(reviews.getTotalPages())
                .totalElement(reviews.getTotalElements())
                .items(toListUserReviewResponse(reviews.getContent()))
                .build();
    }

    private List<UserReviewResponse> toListUserReviewResponse(List<Review> reviewList) {
        return reviewList.stream().map(
                review -> {
                    return UserReviewResponse.builder()
                            .productId(review.getProduct().getId())
                            .productName(review.getProduct().getName())
                            .comment(review.getComment())
                            .rating(review.getRating())
                            .build();
                }).toList();
    }
    @Override
    @Transactional
    public ReviewResponse createReview(Long productId, ReviewRequest reviewRequest) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
        Review review = Review.builder()
                .user(userRepository.getReferenceById(userId))
                .product(productRepository.getReferenceById(productId))
                .comment(reviewRequest.getComment())
                .rating(reviewRequest.getRating())
                .build();

        reviewRepository.save(review);
        log.info("Create review successfully with productId: {}", productId);
        return dataConverter.convertReviewResponse(review);
    }
}
