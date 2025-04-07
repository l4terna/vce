package com.flux.flux.v1.media.dto;

import java.io.InputStream;

public record MediaRetrieveDTO (
        InputStream inputStream,
        String contentType
) { }
