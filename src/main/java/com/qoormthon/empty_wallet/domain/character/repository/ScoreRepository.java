package com.qoormthon.empty_wallet.domain.character.repository;


import com.qoormthon.empty_wallet.domain.survey.entity.Score;
import com.qoormthon.empty_wallet.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByUser(User user);
    boolean existsByUser(User user);
}