package com.purchase.product.controller;

import com.purchase.product.dto.ProductResponse;
import com.purchase.product.dto.PurchaseProductRequest;
import com.purchase.product.dto.PurchaseProductResponse;
import com.purchase.product.service.PurchaseProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diwali-offer-product")
public class PurchaseProductController {

    @Value("${server.port}")
    private int portNumber;

    @Autowired
    private PurchaseProductService service;

    @PostMapping("/purchase")
    public PurchaseProductResponse purchaseProduct(@RequestBody PurchaseProductRequest request) {
        return service.purchaseProduct(request);
    }

    @GetMapping("search")
    public List<ProductResponse> getProducts(){
        System.out.println("Server port: " + portNumber);
        return service.getProducts();
    }
}
