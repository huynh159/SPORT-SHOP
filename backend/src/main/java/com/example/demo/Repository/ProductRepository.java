package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	// Tìm sản phẩm theo thương hiệu (Ví dụ: Nike, Adidas)
	List<Product> findByBrand(String brand);
}