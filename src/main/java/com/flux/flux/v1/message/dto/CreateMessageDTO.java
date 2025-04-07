package com.flux.flux.v1.message.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record CreateMessageDTO(
        @NotBlank
        String content,
        MultipartFile[] attachments
) {
}
