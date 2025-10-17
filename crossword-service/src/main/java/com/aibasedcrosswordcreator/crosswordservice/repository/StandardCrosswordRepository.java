package com.aibasedcrosswordcreator.crosswordservice.repository;

import com.aibasedcrosswordcreator.crosswordservice.model.StandardCrossword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StandardCrosswordRepository extends JpaRepository<StandardCrossword, Long>, JpaSpecificationExecutor<StandardCrossword> {
    @Query("SELECT c " +
            "FROM StandardCrossword c " +
            "JOIN FETCH c.theme " +
            "JOIN FETCH c.coordinates co " +
            "JOIN FETCH co.clue cl " +
            "JOIN FETCH cl.word " +
            "JOIN FETCH c.language " +
            "WHERE c.uuid = :uuid ")
    Optional<StandardCrossword> findByUuidFetchedThemeAndCoordinatesAndClueAndWordAndLanguage(@Param("uuid") UUID uuid);
}
