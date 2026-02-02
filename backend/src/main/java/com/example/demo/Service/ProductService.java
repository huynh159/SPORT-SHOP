package com.example.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Product;
import com.example.demo.Repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// Lấy tất cả sản phẩm
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// Thêm hoặc cập nhật sản phẩm
	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	// Lấy chi tiết 1 sản phẩm
	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm id: " + id));
	}

	// Xóa sản phẩm
	public void deleteProduct(Long id) {
		productRepository.deleteById(id);
	}
}