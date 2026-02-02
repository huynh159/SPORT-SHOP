package com.example.demo.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
	@Value("${file.upload-dir}")
	private String uploadDir;

	public String storeFile(MultipartFile file) {
		try {
			// tạo thư mục nếu chưa tồn tại
			Path uploadPath = Paths.get(uploadDir);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			// Đổi tên file thành UUID để tránh trùng lặp ( Ví dụ nike.jpg -> a1-b2-c3.jpg)
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = uploadPath.resolve(fileName);

			// Lưu file
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			return fileName; // trả về tên file để lưu vao db

		} catch (IOException e) {
			throw new RuntimeException("Không thể lưu file: " + e.getMessage());
		}

	}
}
