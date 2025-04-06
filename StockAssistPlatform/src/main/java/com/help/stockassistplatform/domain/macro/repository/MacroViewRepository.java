package com.help.stockassistplatform.domain.macro.repository;

import com.help.stockassistplatform.domain.macro.entity.MacroView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MacroViewRepository extends JpaRepository<MacroView, String> {
    List<MacroView> findTop2ByIndexNameOrderByPostedAtDesc(String indexName);
}