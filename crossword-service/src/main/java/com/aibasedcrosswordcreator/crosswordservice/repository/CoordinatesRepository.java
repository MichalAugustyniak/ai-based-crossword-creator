package com.aibasedcrosswordcreator.crosswordservice.repository;

import com.aibasedcrosswordcreator.crosswordservice.model.Coordinates;
import com.aibasedcrosswordcreator.crosswordservice.model.StandardCrossword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
    List<Coordinates> findAllByCrossword(StandardCrossword crossword);

    @Query("SELECT c " +
            "FROM Coordinates c " +
            "JOIN FETCH c.clue AS cc " +
            "JOIN FETCH cc.word " +
            "WHERE c.crossword = :crossword")
    List<Coordinates> findAllByCrosswordFetchedClueAndWord(@Param("crossword") StandardCrossword crossword);
}
