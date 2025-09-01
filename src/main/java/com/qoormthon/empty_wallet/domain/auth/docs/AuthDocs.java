package com.qoormthon.empty_wallet.domain.auth.docs;

import com.qoormthon.empty_wallet.global.common.dto.response.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthDocs {

  ResponseDTO<String> issueAccessToken(HttpServletRequest request);

}
