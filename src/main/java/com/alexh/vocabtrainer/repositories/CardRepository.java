package com.alexh.vocabtrainer.repositories;

import com.alexh.vocabtrainer.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    void deleteByWord(String word);
}
