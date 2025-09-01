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
@Table(name = "character")
public class Character {

  @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long characterId;

  @Column
  private String code;

  @Column
  private String name;

  @Column
  private String description;

  @Builder
  private Character(Long characterId, String code, String name, String description) {
    this.characterId = characterId;
    this.code = code;
    this.name = name;
    this.description = description;
  }

  public static Character of(String code, String name, String description) {
    return Character.builder()
        .characterId(null)
        .code(code)
        .name(name)
        .description(description)
        .build();
  }

}
