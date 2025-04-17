package shop.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.example.domain.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product>{
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    Optional<Product> findByNameIgnoreCase(String name);

    @Query(value = "select id from products where match(name) against(:keyword)",nativeQuery = true)
    List<Long> findProductIds(@Param("keyword") String keyword);
}
