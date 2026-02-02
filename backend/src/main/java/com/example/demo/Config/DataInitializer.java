package com.example.demo.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Product;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;

	public DataInitializer(CategoryRepository categoryRepository, ProductRepository productRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		if (categoryRepository.count() == 0) {
			// 1. Tạo Category mẫu
			Category giay = new Category();
			giay.setName("Giày Thể Thao");
			giay.setDescription("Các loại giày chạy bộ, đá bóng");
			categoryRepository.save(giay);

			Category ao = new Category();
			ao.setName("Quần Áo");
			ao.setDescription("Đồ tập Gym, áo đá banh");
			categoryRepository.save(ao);

			// 2. Tạo Sản phẩm mẫu
			Product p1 = new Product();
			p1.setName("Nike Air Max 2026");
			p1.setBrand("Nike");
			p1.setCategory(giay);
			productRepository.save(p1);

			System.out.println(">> Đã khởi tạo dữ liệu mẫu thành công!");
		}
	}
}