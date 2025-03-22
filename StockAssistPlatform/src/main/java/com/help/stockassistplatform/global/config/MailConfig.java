package com.help.stockassistplatform.global.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	private static final String MAIL_DEBUG = "mail.debug";
	private static final String MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";

	@Bean
	public JavaMailSender javaMailSender(
		@Value("${spring.mail.host}") final String host,
		@Value("${spring.mail.port}") final int port,
		@Value("${spring.mail.username}") final String username,
		@Value("${spring.mail.password}") final String password
	) {
		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		final Properties props = mailSender.getJavaMailProperties();
		props.setProperty(MAIL_TRANSPORT_PROTOCOL, "smtp");
		props.setProperty(MAIL_SMTP_AUTH, "true");
		props.setProperty(MAIL_SMTP_STARTTLS_ENABLE, "true");
		props.setProperty(MAIL_SMTP_STARTTLS_REQUIRED, "true");
		props.setProperty(MAIL_SMTP_TIMEOUT, "5000");
		props.setProperty(MAIL_DEBUG, "true");
		return mailSender;
	}
}
