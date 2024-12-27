package com.vce.vce.token.shared;

import com.vce.vce.token.shared.dto.TokenDTO;
import org.springframework.stereotype.Component;

@Component
public interface TokenMapper<T extends TokenEntity> {
    TokenDTO toDTO(T token);
}
