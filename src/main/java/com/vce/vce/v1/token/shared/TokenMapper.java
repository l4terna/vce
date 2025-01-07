package com.vce.vce.v1.token.shared;

import com.vce.vce.v1.token.shared.dto.TokenDTO;
import org.springframework.stereotype.Component;

@Component
public interface TokenMapper<T extends Token> {
    TokenDTO toDTO(T token);
}
