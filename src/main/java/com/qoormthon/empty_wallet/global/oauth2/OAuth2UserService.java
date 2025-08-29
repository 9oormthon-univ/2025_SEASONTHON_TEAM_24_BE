package com.qoormthon.empty_wallet.global.oauth2;

import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.repositroy.UserRepository;
import com.qoormthon.empty_wallet.domain.user.service.SocialProvider;
import com.qoormthon.empty_wallet.global.security.core.CustomUserDetails;
import com.qoormthon.empty_wallet.global.security.jwt.JwtTokenProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

   private final UserRepository userRepository;

   // jwt 토큰 발급을 위한 유틸 객체
   private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.accessTokenExpirationMs}")
    private Long jwtAccessTokenExpirationTime;
    @Value("${jwt.refreshTokenExpirationMs}")
    private Long jwtRefreshTokenExpirationTime;




    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String socialEmail, nickname;

        Map<String, Object> customAttributes = new HashMap<>(attributes);
        AtomicBoolean isNewUser = new AtomicBoolean(false); // 신규 사용자 여부를 나타내는 플래그



        if ("kakao".equals(provider)) {

          // 고유 id + "@kakao" 형태로 고유한 소셜 이메일을 생성합니다
            socialEmail = attributes.get("id").toString()+"@kakao";

            log.info("socialId : " + attributes.get("id"));


            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            nickname = (String) profile.get("nickname");

        } else {
            // 기타 provider 처리 안함
            socialEmail = null;
            nickname = null;
            throw new OAuth2AuthenticationException("지원되지 않는 소셜 서비스 입니다. : " + provider);
        }

        log.info(provider+" 로그인 확인 socialEmail = " + socialEmail);
        log.info(provider+" 로그인 확인 nickname = " + nickname);




        // 회원 정보가 DB에 존재하는지 확인
      User userExam = User.createStandardUser(socialEmail, SocialProvider.fromString(provider));
      log.info("userExam = " + userExam.getProvider());


        User user = userRepository.findBySocialEmail(socialEmail)
                .orElseGet(() -> {
                    // 회원이 없다면 자동 회원가입 처리
                    isNewUser.set(true);
                    return userRepository.save(User.createStandardUser(
                        socialEmail
                        ,SocialProvider.fromString(provider)
                    ));
                });

        customAttributes.put("isNewUser", isNewUser.get()); // 신규 사용자 여부를 속성에 추가

        // 시큐리티에서 사용할 인증 객체 생성
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));


        // JWT 액세스 & 리프레시 토큰 발급
        String accessToken = jwtTokenProvider.createToken(authentication,jwtAccessTokenExpirationTime);
        String refreshToken = jwtTokenProvider.createToken(authentication,jwtRefreshTokenExpirationTime);

        customAttributes.put("accessToken", accessToken);
        customAttributes.put("refreshToken", refreshToken);
        customAttributes.put("id", user.getId());

        // 최종적으로 Spring Security에 전달할 OAuth2User 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                customAttributes,
                "id"
        );
    }
}