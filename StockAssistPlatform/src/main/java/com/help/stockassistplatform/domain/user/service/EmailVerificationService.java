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
public class EmailVerificationService {
	private final JavaMailSender mailSender;
	private final EmailContentProvider emailContentProvider;

	public void sendVerificationEmail(final String token, final String email) {
		// TODO: 인증 링크 프론트엔드 주소로 변경
		final String verificationLink = "http://localhost:5173/verify?token=" + token;
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

	public void sendPasswordResetEmail(final String token, final String email) {
		// TODO: 프론트엔드 비밀번호 재설정 주소로 변경
		final String resetLink = "http://localhost:5173/reset-password?token=" + token;

		try {
			final MimeMessage message = mailSender.createMimeMessage();
			final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(email);
			helper.setSubject("🔒 투자인 비밀번호 재설정 안내");
			helper.setText(
				"""
					<p>안녕하세요.</p>
					<p>비밀번호 재설정을 원하신다면 아래 버튼을 클릭해주세요.</p>
					<a href='%s'>비밀번호 재설정</a>
					""".formatted(resetLink), true
			);

			mailSender.send(message);
		} catch (final MessagingException e) {
			log.error("비밀번호 재설정 이메일 전송 실패: ", e);
			throw new RuntimeException("이메일 전송 실패", e);
		}
	}
}
