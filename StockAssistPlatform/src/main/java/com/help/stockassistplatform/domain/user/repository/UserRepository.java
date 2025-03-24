package com.help.stockassistplatform.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Transactional(readOnly = true)
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
