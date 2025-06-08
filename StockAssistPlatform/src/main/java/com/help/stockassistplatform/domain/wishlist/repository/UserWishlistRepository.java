package com.help.stockassistplatform.domain.wishlist.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.help.stockassistplatform.domain.wishlist.entity.UserWishlist;
import com.help.stockassistplatform.domain.wishlist.entity.UserWishlistId;

public interface UserWishlistRepository extends JpaRepository<UserWishlist, UserWishlistId> {

	List<UserWishlist> findByIdUserIdOrderByIdTickerAsc(UUID userId);

	boolean existsById(UserWishlistId id);

	void deleteById(UserWishlistId id);

	@Query("select w.id.ticker from UserWishlist w where w.id.userId = :userId")
	List<String> findTickersByUserId(@Param("userId") UUID userId);
}
