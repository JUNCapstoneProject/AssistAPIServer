package com.help.stockassistplatform.global.common;

import java.net.URI;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VerificationBaseUrlResolver {
	public String resolve(final HttpServletRequest request) {
		return Stream.of(
				request.getHeader("Origin"),
				extractBaseFromReferer(request.getHeader("Referer")),
				deriveFromRequestHost(request)
			)
			.filter(StringUtils::hasText)
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("유효한 Origin을 찾을 수 없습니다."));
	}

	private String extractBaseFromReferer(String referer) {
		if (!StringUtils.hasText(referer))
			return null;
		final URI uri = URI.create(referer);
		return uri.getScheme() + "://" + uri.getHost()
			+ (uri.getPort() > 0 ? ":" + uri.getPort() : "");
	}

	private String deriveFromRequestHost(HttpServletRequest req) {
		return ServletUriComponentsBuilder.fromRequestUri(req)
			.replacePath(null)
			.build()
			.toUriString();
	}
}
