package shop.example.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.example.domain.Inventory;
import shop.example.domain.Order;
import shop.example.enums.PaymentStatus;
import shop.example.repository.InventoryRepository;
import shop.example.repository.OrderRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {

    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;

    //1day
    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void cleanupPendingOrders() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        Date thresholdDate = Date.from(threshold.atZone(ZoneId.systemDefault()).toInstant());

        List<Order> pendingOrders = orderRepository.findByPaymentStatusAndOrderDateBefore(PaymentStatus.PENDING
                , thresholdDate);
        if (pendingOrders.isEmpty()) {
            log.info("No pending orders to clean up");
            return;
        }
        Map<Long, Inventory> inventoryUpdates = pendingOrders.stream()
                .flatMap(order -> order.getOrderDetails().stream()
                        .map(orderDetail -> {
                            Inventory inventory = orderDetail.getProduct().getInventory();
                            inventory.setStockQuantity(inventory.getStockQuantity() + orderDetail.getQuantity());
                            return inventory;
                        }))
                .distinct()
                .collect(Collectors.toMap(Inventory::getId, inventory -> inventory, (i1, i2) -> i1));
        pendingOrders.forEach(
                order -> {
                    order.setPaymentStatus(PaymentStatus.FAILED);
                    log.info("Cancelled pending order {}", order.getRequestId());
                }
        );
        inventoryRepository.saveAll(inventoryUpdates.values());
        orderRepository.saveAll(pendingOrders);
    }
}
