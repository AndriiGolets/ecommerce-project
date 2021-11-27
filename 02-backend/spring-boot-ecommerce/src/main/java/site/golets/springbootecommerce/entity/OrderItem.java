package site.golets.springbootecommerce.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String imageUrl;

    private BigDecimal unitPrice;

    private int quantity;

    private Long productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity && Objects.equals(id, orderItem.id) && Objects.equals(imageUrl, orderItem.imageUrl) && Objects.equals(unitPrice, orderItem.unitPrice) && Objects.equals(productId, orderItem.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageUrl, unitPrice, quantity, productId);
    }
}
