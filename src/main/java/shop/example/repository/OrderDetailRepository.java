package shop.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.example.domain.OrderDetail;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
