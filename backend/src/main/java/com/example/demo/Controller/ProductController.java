package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// Dùng .* để tránh thiếu import
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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

	@PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Product> create(@RequestPart("product") Product product,
			@RequestPart("image") MultipartFile image) {
		// 1. Lưu file ảnh trước để lấy tên file
		String fileName = fileStorageService.storeFile(image);

		// 2. Gán tên file vào đối tượng Product
		product.setImageUrl(fileName);

		// 3. Lưu Product vào Database
		Product savedProduct = productService.saveProduct(product);

		return ResponseEntity.ok(savedProduct);
	}

	@PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })

	public ResponseEntity<Product> update(@PathVariable Long id, @RequestPart("product") Product productDetails,
			@RequestPart(value = "image", required = false) MultipartFile image // required = false vì không phải lúc
																				// nào cũng đổi ảnh
	) {
		System.out.println("Ten moi nhan duoc: " + productDetails.getName());
		Product product = productService.getProductById(id);

		// 1. Cập nhật các thông tin cơ bản
		product.setName(productDetails.getName());
		product.setBrand(productDetails.getBrand());
		product.setCategory(productDetails.getCategory());
		product.setDescription(productDetails.getDescription());

		// 2. Nếu người dùng có chọn ảnh mới thì mới xử lý lưu ảnh
		if (image != null && !image.isEmpty()) {
			String fileName = fileStorageService.storeFile(image);
			product.setImageUrl(fileName);
		}

		// 3. Lưu vào database
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