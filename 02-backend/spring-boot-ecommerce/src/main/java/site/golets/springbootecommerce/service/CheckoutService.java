package site.golets.springbootecommerce.service;

import site.golets.springbootecommerce.dto.Purchase;
import site.golets.springbootecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
