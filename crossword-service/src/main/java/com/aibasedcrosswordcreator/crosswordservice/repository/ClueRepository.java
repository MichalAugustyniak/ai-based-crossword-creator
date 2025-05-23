package com.aibasedcrosswordcreator.crosswordservice.repository;

import com.aibasedcrosswordcreator.crosswordservice.model.Clue;
import com.aibasedcrosswordcreator.crosswordservice.model.Language;
import com.aibasedcrosswordcreator.crosswordservice.model.ProviderModel;
import com.aibasedcrosswordcreator.crosswordservice.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClueRepository extends JpaRepository<Clue, Long> {
    List<Clue> findAllByThemeAndLanguage(Theme theme, Language language);

    @Query("SELECT c " +
            "FROM Clue c " +
            "JOIN FETCH c.word cw " +
            "WHERE c.theme = :theme AND c.language = :language AND c.providerModel = :providerModel AND LENGTH(c.word.text) <= :maxLength")
    List<Clue> findByThemeAndLanguageAndProviderModelFetchedWord(
            @Param("theme") Theme theme,
            @Param("language") Language language,
            @Param("providerModel") ProviderModel providerModel,
            @Param("maxLength") int maxLength
    );

    @Query("SELECT c " +
            "FROM Clue c " +
            "JOIN FETCH c.word cw " +
            "WHERE c.theme = :theme AND c.language = :language AND c.providerModel IS null AND LENGTH(c.word.text) <= :maxLength")
    List<Clue> findByThemeAndLanguageFetchedWord(
            @Param("theme") Theme theme,
            @Param("language") Language language,
            @Param("maxLength") int maxLength
    );

    @Query("SELECT c " +
            "FROM Clue c " +
            "JOIN FETCH c.word cw " +
            "WHERE cw.text IN :texts AND c.language = :language AND c.theme = :theme")
    List<Clue> findAllByTextCollectionAndThemeAndLanguageFetchedWord(
            @Param("texts") Collection<String> texts,
            @Param("theme") Theme theme,
            @Param("language") Language language
    );

    @Query("SELECT c " +
            "FROM Clue c " +
            "JOIN FETCH c.word cw " +
            "WHERE c.theme = :theme AND c.language = :language AND c.providerModel = :providerModel")
    List<Clue> findByThemeAndLanguageAndProviderModelFetchedWord(
            @Param("theme") Theme theme,
            @Param("language") Language language,
            @Param("providerModel") ProviderModel providerModel);

    @Query("SELECT c " +
            "FROM Clue c " +
            "JOIN FETCH c.word cw " +
            "WHERE cw.text IN :texts AND c.language = :language AND c.theme = :theme AND c.providerModel = :providerModel")
    List<Clue> findAllByTextCollectionAndThemeAndLanguageAndProviderModelFetchedWord(
            @Param("texts") Collection<String> texts,
            @Param("theme") Theme theme,
            @Param("language") Language language,
            @Param("providerModel") ProviderModel providerModel
    );

    @Query("SELECT c " +
            "FROM Clue c " +
            "JOIN FETCH c.word cw " +
            "WHERE cw.text IN :texts AND c.language = :language AND c.theme = :theme AND c.providerModel IS null ")
    List<Clue> findAllByTextCollectionAndThemeAndLanguageProviderModelIsNullFetchedWord(
            @Param("texts") Collection<String> texts,
            @Param("theme") Theme theme,
            @Param("language") Language language
    );
}
