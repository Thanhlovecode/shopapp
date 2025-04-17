package shop.example.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AbstractEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToOne(mappedBy = "product",cascade =CascadeType.ALL,orphanRemoval = true)
    private Inventory inventory;

}
