package com.purchase.product.repository;

import com.purchase.product.entity.PurchaseProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseProductRepo extends JpaRepository<PurchaseProductEntity,Integer> {
}
