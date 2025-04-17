package shop.example.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import shop.example.domain.Product;
import shop.example.dto.request.product.ProductSearchRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ProductSpecification {
    public static Specification<Product> filter(ProductSearchRequest request, List<Long> ids){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates= Stream.of(
                    Optional.ofNullable(ids)
                            .filter(listId->!listId.isEmpty())
                            .map(listId-> root.get("id").in(listId)),

                    Optional.ofNullable(request.getMinPrice())
                            .map(min->criteriaBuilder.greaterThanOrEqualTo(root.get("price"),min))

            )
                    .flatMap(Optional::stream)
                    .toList();
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
