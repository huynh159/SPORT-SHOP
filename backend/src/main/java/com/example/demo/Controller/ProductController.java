package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// Dùng .* để tránh thiếu import
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Entity.Product;
import com.example.demo.Service.FileStorageService;
import com.example.demo.Service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<Product> getAll() {
		return productService.getAllProducts();
	}

	@PostMapping
	public Product create(@RequestBody Product product) {
		return productService.saveProduct(product);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product productDetails) {
		Product product = productService.getProductById(id);

		product.setName(productDetails.getName());
		product.setBrand(productDetails.getBrand());
		product.setCategory(productDetails.getCategory());
		product.setDescription(productDetails.getDescription());

		return ResponseEntity.ok(productService.saveProduct(product));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok("Đã xóa sản phẩm thành công!");
	}

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("/{id}/upload-image")
	public ResponseEntity<Product> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
		Product product = productService.getProductById(id);
		String fileName = fileStorageService.storeFile(file);

		product.setImageUrl(fileName); // lưu tên vào file vào db
		productService.saveProduct(product);
		return ResponseEntity.ok(product);
	}
}