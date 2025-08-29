package com.qoormthon.empty_wallet.global.security.core;

import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.exception.UserNotFoundException;
import com.qoormthon.empty_wallet.domain.user.repositroy.UserRepository;
import com.qoormthon.empty_wallet.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	/**
	 * 사용자 인증 객체를 생성하는 메서드입니다.
	 *
	 * @param SocialEmail 사용자 식별자인 소셜 이메일 값이 들어갑니다.
	 * @return CustomUserDetails 객체
	 * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
	 */
	@Override
	public UserDetails loadUserByUsername(String SocialEmail) throws UserNotFoundException {
		User user = userRepository.findBySocialEmail(SocialEmail).orElseThrow(
			() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

		return new CustomUserDetails(user);
	}

	/**
	 * 사용자 ID로 사용자 인증 객체를 생성하는 메서드입니다.
	 *
	 * @param id 사용자 ID
	 * @return CustomUserDetails 객체
	 * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
	 */
	public UserDetails loadUserById(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id).orElseThrow(
			() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
		return new CustomUserDetails(user);
	}
}