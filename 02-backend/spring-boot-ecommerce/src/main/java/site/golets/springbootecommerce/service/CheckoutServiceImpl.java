package site.golets.springbootecommerce.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import site.golets.springbootecommerce.dao.CustomerRepository;
import site.golets.springbootecommerce.dto.Purchase;
import site.golets.springbootecommerce.dto.PurchaseResponse;
import site.golets.springbootecommerce.entity.Customer;
import site.golets.springbootecommerce.entity.Order;
import site.golets.springbootecommerce.entity.OrderItem;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with order items
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(order::add);

        // populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();
        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
