package site.golets.springbootecommerce.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.golets.springbootecommerce.dto.Purchase;
import site.golets.springbootecommerce.dto.PurchaseResponse;
import site.golets.springbootecommerce.service.CheckoutService;


@RestController
@RequestMapping("/api/checkout")
@AllArgsConstructor
public class CheckoutController {

    private CheckoutService checkoutService;

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase) {

        return checkoutService.placeOrder(purchase);
    }

}
