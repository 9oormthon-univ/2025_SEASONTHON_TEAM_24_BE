package com.qoormthon.empty_wallet.domain.survey.service;

import com.qoormthon.empty_wallet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CharacterResolverService {
    private static final Set<String> ALLOWED = Set.of("CAF","TAX","FASH","SUB","IMP","YOLO");
    private final UserRepository userRepository;

    public String resolve(String paramOrHeader, Long userIdOrNull) {
        // 1) 쿼리파라미터/헤더 우선
        String c = norm(paramOrHeader);
        if (c != null) return c;
        // 2) 로그인 사용자면 DB 조회
        if (userIdOrNull != null) {
            return userRepository.findCharacterCodeById(userIdOrNull)
                    .map(this::norm).orElse(null);
        }
        // 3) 없으면 null (Q3 제외)
        return null;
    }

    private String norm(String raw) {
        if (!StringUtils.hasText(raw)) return null;
        String u = raw.trim().toUpperCase();
        return ALLOWED.contains(u) ? u : null;
    }
}
