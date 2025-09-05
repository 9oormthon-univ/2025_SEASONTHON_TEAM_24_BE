package com.qoormthon.empty_wallet.domain.character.repository;

import com.qoormthon.empty_wallet.domain.character.entity.CharCode;
import com.qoormthon.empty_wallet.domain.character.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByCode(String code); // "CAF", "TAX" ...
;
}