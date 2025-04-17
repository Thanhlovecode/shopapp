package shop.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.example.domain.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
}
