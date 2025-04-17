package shop.example.domain;

import jakarta.persistence.*;
import lombok.*;
import org.apache.kafka.common.record.UnalignedMemoryRecords;

@Entity
@Table(name = "review")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends AbstractEntity {

    @Column(name = "rating",nullable = false)
    private Integer rating;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

}
