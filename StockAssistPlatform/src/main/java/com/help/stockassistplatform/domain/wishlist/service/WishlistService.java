package com.help.stockassistplatform.domain.wishlist.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.financial.repository.StockPriceViewRepository;
import com.help.stockassistplatform.domain.stock.service.StockQueryService;
import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.domain.wishlist.dto.WishlistItemDto;
import com.help.stockassistplatform.domain.wishlist.entity.UserWishlist;
import com.help.stockassistplatform.domain.wishlist.entity.UserWishlistId;
import com.help.stockassistplatform.domain.wishlist.repository.UserWishlistRepository;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;
import com.help.stockassistplatform.global.jwt.CustomUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

	private final UserWishlistRepository wishlistRepository;
	private final StockPriceViewRepository stockPriceViewRepository;
	private final StockQueryService stockQueryService;

	@Transactional
	public void add(final CustomUser currentUser, final String ticker) {
		final User user = currentUser.getDomainUser();
		final UserWishlistId id = new UserWishlistId(user.getUserId(), ticker);
		if (wishlistRepository.existsById(id))
			return;
		if (!stockPriceViewRepository.existsById(ticker)) {
			throw new CustomException(ErrorCode.HEART_TICKER_NOT_FOUND);
		}
		wishlistRepository.save(UserWishlist.of(user, ticker));
	}

	@Transactional
	public void remove(final CustomUser currentUser, final String ticker) {
		final User user = currentUser.getDomainUser();
		wishlistRepository.deleteById(new UserWishlistId(user.getUserId(), ticker));
	}

	public List<WishlistItemDto> list(final CustomUser currentUser) {
		final User user = currentUser.getDomainUser();
		return wishlistRepository.findByIdUserIdOrderByIdTickerAsc(user.getUserId())
			.stream()
			.map(this::toDto)
			.flatMap(Optional::stream)
			.toList();
	}

	private Optional<WishlistItemDto> toDto(final UserWishlist entity) {
		return stockQueryService.getSummary(entity.getId().getTicker())
			.map(info -> new WishlistItemDto(
				info.ticker(),
				info.name(),
				info.price().toPlainString(),
				true
			));
	}
}
