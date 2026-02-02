package com.example.demo.Config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Lấy đường dẫn tuyệt đối của thư mục uploads
		Path uploadPath = Paths.get(uploadDir);
		String fullPath = uploadPath.toFile().getAbsolutePath();

		// Cấu hình: Nếu URL bắt đầu bằng /uploads/ thì sẽ tìm file trong thư mục vật lý
		// tương ứng
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + fullPath + "/");
	}
}