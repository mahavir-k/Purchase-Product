package com.purchase.product.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purchase.product.dto.*;
import com.purchase.product.entity.AccountEntity;
import com.purchase.product.entity.PurchaseProductEntity;
import com.purchase.product.repository.PurchaseProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PurchaseProductService {

    @Autowired
    private PurchaseProductRepo repository;

    @Autowired
    private RestTemplate restTemplate;


    public PurchaseProductResponse purchaseProduct(PurchaseProductRequest purchaseRequest) {
        PurchaseProductResponse response = new PurchaseProductResponse();
        try {
            PurchaseProductEntity purchaseEntity = new PurchaseProductEntity();

            // Call product-service to search product
            ProductResponse product = restTemplate.getForObject("http://PRODUCT-SERVICE/product/detail/byName/" + purchaseRequest.getProductName(), ProductResponse.class);
            if (product == null) {
                response.setStatus("Product not available, Please try after some time.....!");
            } else {
                // Call customer-reg-service to check if customer already register or not?
                Boolean isCustomer = restTemplate.getForObject("http://CUSTOMER-SERVICE/customer/detail/" + purchaseRequest.getCustomerId(), Boolean.class);
                if (isCustomer) {

                    // Calculate total purchase amount of customer
                    double purchasePrice = product.getProductPrice() * purchaseRequest.getQuantity();

                    // Call account-service to check balance to customer, if sufficient balance available then proceed further
                    AccountResponse accountResponse = restTemplate.getForObject("http://ACCOUNT-SERVICE/account/detail/" + purchaseRequest.getCustomerId(), AccountResponse.class);

                    if (purchasePrice <= accountResponse.getCustBalance()) {
                        // update remaining balance into customer account
                        double remain = accountResponse.getCustBalance() - purchasePrice;
                        // @PutMapping("update/{customerId}/{custBalance}")
                        restTemplate.put("http://ACCOUNT-SERVICE/account/update/" + purchaseRequest.getCustomerId() + "/" + remain, AccountEntity.class);


                        // Setting final orders detail in response
                        response.setStatus("Product ordered successfully.....!");
                        response.setProductName(purchaseRequest.getProductName());
                        response.setOrderId(UUID.randomUUID().toString());
                        response.setOrderDate(new Date().toString());
                        response.setCustomerName(accountResponse.getCustomerName());
                        response.setQuantity(purchaseRequest.getQuantity());


                    } else {
                        response.setStatus("Insufficient balance.....!");
                    }
                } else {
                    response.setStatus("Please register yourself to purchase this product.....!");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public List<ProductResponse> getProducts() {
        /*ProductResponse[] products = restTemplate.getForObject("http://PRODUCT-SERVICE/product/all", ProductResponse[].class);
        return Arrays.asList(products);*/


        List<ProductResponse> response = restTemplate.exchange(
                "http://PRODUCT-SERVICE/product/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>() {}
        ).getBody();

       return response;
    }
}

