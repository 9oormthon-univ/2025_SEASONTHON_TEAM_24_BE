package com.qoormthon.empty_wallet.domain.user.entity;

import com.qoormthon.empty_wallet.domain.character.entity.Character;
import com.qoormthon.empty_wallet.domain.user.dto.RequiredDaysRequest;
import com.qoormthon.empty_wallet.global.security.core.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  @Column(name = "user_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "character_id")
  private Character character;

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
  private User(Long id, String socialEmail, Role role, SocialProvider authType,
      Character character, String nickname, String name,
      Integer age, Gender gender, String birthDate,
      Long monthlyPay, Long monthlyCost, Long targetPrice) {
    this.id = id;
    this.authType = authType;
    this.socialEmail = socialEmail;
    this.role = role;

    this.character = character;
    this.nickname = nickname;
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.birthDate = birthDate;
    this.monthlyPay = monthlyPay;
    this.monthlyCost = monthlyCost;
    this.targetPrice = targetPrice;
  }

  /**
   * 일반 사용자를 생성하는 메서드 입니다
   *
   * @param socialEmail 소셜로그인 후 반환되는 사용자 이메일 입니다.
   * @param authType 소셜로그인 종류 입니다. (KAKAO, GOOGLE)
   * @return 생성된 User 객체
   */
  public static User createStandardUser(String socialEmail, String name, SocialProvider authType) {
    return User.builder()
        .id(null)
        .socialEmail(socialEmail)
        .authType(authType)
        .name(name)
        .role(Role.ROLE_USER)

        .character(null)
        .nickname(null)
        .age(null)
        .gender(null)
        .birthDate(null)
        .monthlyPay(null)
        .monthlyCost(null)
        .targetPrice(null)

        .build();
  }


  /**
   * 목표 금액까지 걸리는 예상 일수를 계산합니다.
   * @return 목표 금액까지 걸리는 예상 일수 (소수점 첫째 자리까지 표시)
   */
  public double getDaysToGoal() {
    double savingMoney = (this.monthlyPay-this.monthlyCost)/10.0; // 하루 저축 금액
    double days = ((double)this.targetPrice/(savingMoney))*30;
    return (double) Math.round(days);
  }

  public void setDaysToGoal(RequiredDaysRequest requiredDaysRequest) {
    this.monthlyCost = null;
    this.monthlyPay = requiredDaysRequest.getMonthlyPay()*10000;
    this.targetPrice = requiredDaysRequest.getTargetPrice()*10000;
  }

  public void setCharacter(Character character) {   // 서비스에서 호출할 메서드
    this.character = character;
  }
}
