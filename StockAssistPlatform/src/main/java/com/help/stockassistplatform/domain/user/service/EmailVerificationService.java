package com.help.stockassistplatform.domain.user.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.global.common.VerificationLinkBuilder;

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
	private final VerificationLinkBuilder linkBuilder;

	public void sendVerificationEmail(final String token, final String email, final String baseUrl) {
		final String verificationLink = linkBuilder.build(baseUrl, token);
		
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
		final String resetLink = "https://www.tuzain.com/reset-password?token=" + token;

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
