package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.Security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth

				// 1. Các API công khai: Đăng kí, đăng nhập, xem sản phẩm, xem ảnh
				.requestMatchers("/api/auth/**", "/api/products/**", "/uploads/**").permitAll()
				// 2. Các API chỉ dành cho Admin: Thêm, sửa, Xóa sản phẩm, upload ảnh
				.requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

				// 3. Các API dành cho User đã đăng nhập: Giỏ hàng, Đặt hàng(sẽ làm sau)
				.anyRequest().authenticated()

		);

		// Thêm bộ lọc JWT vào trước bộ lọc mặc định của Spring
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}