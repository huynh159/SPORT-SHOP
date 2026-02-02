package com.example.demo.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private final JavaMailSender mailSender;

	// Constructor Injection
	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendVerificationEmail(String toEmail, String token) {
		// Đường dẫn này sẽ dẫn người dùng về API xác nhận mà chúng ta sẽ viết ở bước
		// sau
		String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("S-Mart <your-email@gmail.com>");
		message.setTo(toEmail);
		message.setSubject("[S-Mart] Xác nhận tài khoản của bạn");
		message.setText("Chào mừng bạn đến với shop đồ thể thao S-Mart!\n\n"
				+ "Vui lòng nhấn vào đường dẫn dưới đây để kích hoạt tài khoản của bạn:\n" + verificationUrl + "\n\n"
				+ "Lưu ý: Mã này sẽ hết hạn sau 24 giờ.");

		mailSender.send(message);
	}
}