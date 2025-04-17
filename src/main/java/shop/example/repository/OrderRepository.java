package shop.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.example.domain.Order;
import shop.example.enums.PaymentStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByUserId(Long id,Pageable pageable);
    Optional<Order> findByRequestId(String requestId);

    @EntityGraph(attributePaths = {"orderDetails","orderDetails.product"})
    List<Order> findByPaymentStatusAndOrderDateBefore(PaymentStatus status, Date date);
}
