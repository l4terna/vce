package com.flux.flux.v1.media.service;

import com.flux.flux.v1.media.Media;
import com.flux.flux.v1.media.dto.MediaRetrieveDTO;
import com.flux.flux.v1.media.enumeration.MediaVisibility;
import org.springframework.web.multipart.MultipartFile;

public interface MediaStorageService {
    Media store(MultipartFile file, MediaVisibility mediaVisibility, Long createdById);

    MediaRetrieveDTO retrieve(String storageKey);

    void delete(String storageKey);
}
