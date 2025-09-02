package com.qoormthon.empty_wallet.domain.character.repository;

import com.qoormthon.empty_wallet.domain.character.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {


}
