package com.qoormthon.empty_wallet.domain.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "characters")
public class Character {

  @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long characterId;

  @Column
  private String code;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false, length = 255)
  private String trait;

  @Builder
  private Character(Long characterId, String code, String name, String description, String trait) {
    this.characterId = characterId;
    this.code = code;
    this.name = name;
    this.description = description;
    this.trait = trait;
  }

  public static Character of(String code, String name, String description, String trait) {
    return Character.builder()
        .characterId(null)
        .code(code)
        .name(name)
        .description(description)
        .trait(trait)
        .build();
  }

}
