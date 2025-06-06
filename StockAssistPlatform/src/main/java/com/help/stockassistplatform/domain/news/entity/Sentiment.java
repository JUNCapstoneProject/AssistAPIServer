package com.help.stockassistplatform.domain.news.entity;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

public enum Sentiment {
	NEGATIVE(0, "negative", "neg", "부정"),
	NEUTRAL(1, "neutral", "neu", "중립"),
	POSITIVE(2, "positive", "pos", "긍정"),
	;

	@Getter
	private final int score;
	private final Set<String> aliases;

	Sentiment(int score, String... aliases) {
		this.score = score;
		this.aliases = Arrays.stream(aliases)
			.map(String::toLowerCase)
			.collect(Collectors.toUnmodifiableSet());
	}

	public static Optional<Sentiment> of(final String raw) {
		if (raw == null || raw.isBlank())
			return Optional.empty();
		final String v = raw.trim().toLowerCase();
		return Arrays.stream(values())
			.filter(s -> String.valueOf(s.score).equals(v)
				|| s.name().equalsIgnoreCase(v)
				|| s.aliases.contains(v))
			.findFirst();
	}
}

