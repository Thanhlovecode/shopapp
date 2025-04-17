package shop.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.example.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findByProductId(Long productId, Pageable pageable);

    @EntityGraph(attributePaths = {"product"})
    Page<Review> findByUserId(Long userId,Pageable pageable);
}
