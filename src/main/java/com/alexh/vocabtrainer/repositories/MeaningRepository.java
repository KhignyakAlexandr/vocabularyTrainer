package com.alexh.vocabtrainer.repositories;

import com.alexh.vocabtrainer.entities.Meaning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeaningRepository extends JpaRepository<Meaning, Long> {

    Optional<Meaning> findByCardWordAndPartOfSpeech(String cardWord, String partOfSpeech);
}
