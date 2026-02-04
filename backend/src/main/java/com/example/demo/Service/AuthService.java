package com.example.demo.Service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.User;
import com.example.demo.Entity.VerificationToken;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.VerificationTokenRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final VerificationTokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;

	public AuthService(UserRepository userRepository, RoleRepository roleRepository,
			VerificationTokenRepository tokenRepository, PasswordEncoder passwordEncoder, MailService mailService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.tokenRepository = tokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
	}

	@Transactional
	public void register(User user) {
		// Kiểm tra trùng lặp trước khi xử lý
		if (userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Tên đăng nhập đã tồn tại!");
		}
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email đã được sử dụng!");
		}

		// 1. Mã hóa mật khẩu
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(false);

		// 2. Gán quyền ROLE_USER mặc định
		roleRepository.findByName("ROLE_USER").ifPresent(role -> {
			user.getRoles().add(role);
		});

		// 3. Lưu User vào DB
		User savedUser = userRepository.save(user);

		// 4. Tạo mã Token xác nhận
		String token = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken(token, savedUser);
		tokenRepository.save(verificationToken);

		// 5. Gửi Email
		mailService.sendVerificationEmail(savedUser.getEmail(), token);
	}

	// Thêm vào trong AuthService.java
	@Transactional
	public boolean verifyToken(String token) {
		return tokenRepository.findByToken(token).map(verificationToken -> {
			// Kiểm tra xem token đã hết hạn chưa
			if (verificationToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
				return false;
			}

			// Kích hoạt User và xóa token sau khi dùng xong
			User user = verificationToken.getUser();
			user.setEnabled(true);
			userRepository.save(user);
			tokenRepository.delete(verificationToken);
			return true;
		}).orElse(false);
	}
}