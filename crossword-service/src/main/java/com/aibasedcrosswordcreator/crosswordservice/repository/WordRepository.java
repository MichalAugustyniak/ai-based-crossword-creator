package com.aibasedcrosswordcreator.crosswordservice.repository;

import com.aibasedcrosswordcreator.crosswordservice.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query("SELECT w FROM Word w WHERE w.text IN :texts")
    List<Word> findAllByTextIsIn(Collection<String> texts);
}
