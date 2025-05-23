package com.aibasedcrosswordcreator.crosswordservice.repository;

import com.aibasedcrosswordcreator.crosswordservice.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Theme c WHERE c.name = :categoryName")
    boolean existsByThemeName(@Param("categoryName") String categoryName);

    @Query("SELECT c FROM Theme c WHERE c.name = :categoryName")
    Optional<Theme> findByThemeName(@Param("categoryName") String categoryName);

    Optional<Theme> findByName(String name);
}
