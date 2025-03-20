package com.help.stockassistplatform.domain.user.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {
	private final JavaMailSender mailSender;
	private final EmailContentProvider emailContentProvider;

	public void sendVerificationEmail(final String token, final String email) {
		// TODO: 인증 링크 프론트엔드 주소로 변경
		final String verificationLink = "http://localhost:8080/api/auth/verify?token=" + token;
		try {
			final MimeMessage message = mailSender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject(emailContentProvider.buildEmailSubject());
			helper.setText(emailContentProvider.buildEmailContent(verificationLink), true);

			mailSender.send(message);
		} catch (final MessagingException e) {
			log.error("이메일 전송 실패: ", e);
			throw new RuntimeException("이메일 전송 실패", e);
		}
	}

}
