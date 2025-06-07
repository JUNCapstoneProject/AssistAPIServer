package com.help.stockassistplatform.domain.wishlist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.help.stockassistplatform.domain.wishlist.entity.UserWishlist;
import com.help.stockassistplatform.domain.wishlist.entity.UserWishlistId;

public interface UserWishlistRepository extends JpaRepository<UserWishlist, UserWishlistId> {

	List<UserWishlist> findByIdUserIdOrderByIdTickerAsc(UUID userId);

	boolean existsById(UserWishlistId id);

	void deleteById(UserWishlistId id);
}
