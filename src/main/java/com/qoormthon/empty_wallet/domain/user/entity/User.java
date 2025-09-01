package com.qoormthon.empty_wallet.domain.user.entity;

import com.qoormthon.empty_wallet.domain.user.dto.Gender;
import com.qoormthon.empty_wallet.domain.user.dto.SocialProvider;
import com.qoormthon.empty_wallet.global.security.core.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  //TODO: character_id 연관관계 매핑이 필요합니다.

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SocialProvider authType;

  @Column(unique = true, nullable = false)
  private String socialEmail;

  @Column
  private String nickname;

  @Column
  private String name;

  @Column
  private Integer age;

  @Column
  private Gender gender; // MALE, FEMALE

  @Column
  private String birthDate; // ex) 1995-03-15

  @Column
  private Long monthlyPay;

  @Column
  private Long monthlyCost;

  @Column
  private Long targetPrice;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Builder
  private User(Long id, String socialEmail, Role role, SocialProvider authType) {
    this.id = id;
    this.authType = authType;
    this.socialEmail = socialEmail;
    this.role = role;
  }

  /**
   * 일반 사용자를 생성하는 메서드 입니다
   *
   * @param socialEmail 소셜로그인 후 반환되는 사용자 이메일 입니다.
   * @return 생성된 User 객체
   */
  public static User createStandardUser(String socialEmail, SocialProvider authType) {
    return User.builder()
        .id(null)
        .socialEmail(socialEmail)
        .authType(authType)
        .role(Role.ROLE_USER)
        .build();
  }

}
