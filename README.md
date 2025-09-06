# 24팀 api 서버 입니다.

---

# [1] Git 규칙

## (1) 커밋 규칙
* **Fix**: 버그 수정
* **Docs**: 문서 수정
* **Chore**: 빌드, 패키지 관리, 파일 삭제 등 기타 작업 (간단하고 하찮은? 일들)
* **Refactor**: 코드 리팩토링
* **Feat**: 새로운 기능 추가
* **Merge**: 병합 작업

ex) 
Feat: 한글

## (2) 브랜치 관리 전략
**Git Flow**
* main (배포환경)
* develop (개발환경)
* Feat/#이슈번호 (새로운 기능 추가시)


---

# [2] 이슈 작성 규칙

## (1) 이슈 제목 작성 규칙
* **[Feature]** 새로운 기능 추가
* **[Fix]** 버그 수정 
* **[Documentation]** 문서 관련
* **[Refactor]** 코드 리팩토링
* **[Chore]** 기타 작업
* **[Hotfix]** 긴급 수정
* **[Security]** 보안 관련

ex) 
[Feature] 회원관리

# [3] 프로젝트 구조
```
empty-wallet/
├── README.md
├── build.gradle
├── src/
│   └── main/
│       ├── java/com/qoormthon/empty_wallet/
│       │   ├── EmptyWalletApplication.java
│       │   ├── domain/
│       │   │   ├── auth/
│       │   │   │   ├── controller/AuthController.java
│       │   │   │   ├── service/AuthService.java
│       │   │   │   └── exception/TokenExtractionException.java
│       │   │   ├── character/
│       │   │   │   ├── controller/CharacterController.java
│       │   │   │   ├── service/CharacterScoreService.java
│       │   │   │   ├── repository/CharacterRepository.java
│       │   │   │   └── entity/
│       │   │   │       ├── Character.java
│       │   │   │       ├── CharCode.java
│       │   │   │       └── Score.java
│       │   │   ├── strategy/
│       │   │   │   ├── controller/StrategyController.java
│       │   │   │   ├── service/StrategyService.java
│       │   │   │   ├── repository/StrategyActiveRepository.java
│       │   │   │   ├── dto/StrategyDataDTO.java
│       │   │   │   └── entity/
│       │   │   │       ├── StrategyActive.java
│       │   │   │       ├── StrategyStatus.java
│       │   │   │       └── StrategyType.java
│       │   │   ├── survey/
│       │   │   │   ├── controller/SurveyController.java
│       │   │   │   ├── service/
│       │   │   │   │   ├── SurveyService.java
│       │   │   │   │   ├── SurveyCommandService.java
│       │   │   │   │   └── CharacterResolverService.java
│       │   │   │   ├── repository/
│       │   │   │   │   ├── SurveyRepository.java
│       │   │   │   │   └── SurveyOptionRepository.java
│       │   │   │   ├── dto/
│       │   │   │   │   ├── request/SubmitSurveyRequest.java
│       │   │   │   │   └── response/
│       │   │   │   │       ├── SurveyBundleResponse.java
│       │   │   │   │       ├── QuestionResponse.java
│       │   │   │   │       └── OptionResponse.java
│       │   │   │   └── entity/
│       │   │   │       ├── Survey.java
│       │   │   │       ├── SurveyOption.java
│       │   │   │       └── SurveyType.java
│       │   │   └── user/
│       │   │       ├── controller/UserController.java
│       │   │       ├── service/UserService.java
│       │   │       ├── repository/UserRepository.java
│       │   │       ├── dto/
│       │   │       │   ├── RequiredDaysRequest.java
│       │   │       │   └── RequiredDaysResponse.java
│       │   │       ├── entity/
│       │   │       │   ├── User.java
│       │   │       │   ├── Gender.java
│       │   │       │   └── SocialProvider.java
│       │   │       └── exception/
│       │   │           ├── UserNotFoundException.java
│       │   │           └── UserDuplicationException.java
│       │   ├── global/
│       │   │   ├── config/
│       │   │   │   ├── SecurityConfig.java
│       │   │   │   ├── SwaggerConfig.java
│       │   │   │   └── ClaudeAiConfig.java
│       │   │   ├── security/jwt/
│       │   │   │   ├── JwtTokenProvider.java
│       │   │   │   └── JwtTokenFilter.java
│       │   │   ├── oauth2/
│       │   │   │   ├── OAuth2UserService.java
│       │   │   │   └── OAuth2LoginSuccessHandler.java
│       │   │   ├── exception/
│       │   │   │   ├── GlobalExceptionHandler.java
│       │   │   │   └── ErrorCode.java
│       │   │   └── common/dto/ResponseDTO.java
│       │   └── infra/
│       └── resources/
│           ├── application.yml
│           └── static/data/strategyData.json
└── build/
```


