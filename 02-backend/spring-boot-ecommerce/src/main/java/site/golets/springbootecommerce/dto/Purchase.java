package site.golets.springbootecommerce.dto;

import lombok.Data;
import site.golets.springbootecommerce.entity.Address;
import site.golets.springbootecommerce.entity.Customer;
import site.golets.springbootecommerce.entity.Order;
import site.golets.springbootecommerce.entity.OrderItem;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
