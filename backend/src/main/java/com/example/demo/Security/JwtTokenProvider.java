package com.example.demo.Security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	// Lưu ý trong thực tế hãy để secret key này vào file properties
	final String JWT_SECRET = "SmartSportsShopSecretKeyMustBeVeryLongAndSecure123456";
	private final long JWT_EXPIRATION = 604800000L; // 7 ngày

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
	}

	// Tạo token từ username
	public String generateToken(String username) {
		Date now = new Date();
		Date exporyDate = new Date(now.getTime() + JWT_EXPIRATION);

		return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(exporyDate)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	// Lấy username từ token
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	// kiểm tra token hợp lệ
	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
			return false;
		}
	}
}
