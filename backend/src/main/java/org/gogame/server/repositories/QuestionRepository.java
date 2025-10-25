package org.gogame.server.repositories;

import org.gogame.server.domain.entities.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

    @Query("SELECT q FROM QuestionEntity q")
    List<QuestionEntity> findAllQuestions();
}
