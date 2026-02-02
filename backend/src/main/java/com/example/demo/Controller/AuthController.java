package com.example.demo.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Thêm import này
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository; // Import chuẩn
import com.example.demo.Security.JwtTokenProvider; // Import class JWT bạn vừa tạo
import com.example.demo.Service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final AuthService authService;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	// Inject tất cả các bean cần thiết vào Constructor
	public AuthController(AuthService authService, UserRepository userRepository, PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider) {
		this.authService = authService;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		try {
			authService.register(user);
			return ResponseEntity.ok("Đăng ký thành công! Vui lòng kiểm tra email để kích hoạt tài khoản.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Lỗi đăng ký: " + e.getMessage());
		}
	}

	@GetMapping("/verify")
	public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
		boolean isVerified = authService.verifyToken(token);
		if (isVerified) {
			return ResponseEntity.ok("Tài khoản đã được kích hoạt thành công! Bạn có thể đăng nhập ngay bây giờ.");
		} else {
			return ResponseEntity.badRequest().body("Mã xác nhận không hợp lệ hoặc hết hạn.");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User loginRequest) {
		return userRepository.findByUsername(loginRequest.getUsername()).map(user -> {
			// Kiểm tra mật khẩu đã mã hóa
			if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
				if (!user.isEnabled()) {
					return ResponseEntity.badRequest().body("Tài khoản chưa được kích hoạt qua email!");
				}
				// Tạo Token JWT
				String token = jwtTokenProvider.generateToken(user.getUsername());
				// Trả về Token cho khách hàng
				return ResponseEntity.ok(token);
			}
			return ResponseEntity.badRequest().body("Sai mật khẩu!");
		}).orElse(ResponseEntity.badRequest().body("Không tìm thấy người dùng!"));
	}
}