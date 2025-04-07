package com.flux.flux.v1.hubavatar;

import com.flux.flux.v1.hub.Hub;
import com.flux.flux.v1.media.LocalMediaStorageService;
import com.flux.flux.v1.media.Media;
import com.flux.flux.v1.media.enumeration.MediaVisibility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class HubAvatarService {
    private final LocalMediaStorageService localMediaStorageService;
    private final HubAvatarRepository hubAvatarRepository;

    @Transactional
    public Media save(MultipartFile file, Long hubId, Long userId) {
        Media media = localMediaStorageService.store(file, MediaVisibility.PUBLIC, userId);

        HubAvatar hubAvatar = HubAvatar.builder()
                .mediaId(media.getId())
                .hubId(hubId)
                .build();

        hubAvatarRepository.save(hubAvatar);
        return media;
    }

    @Transactional
    public Media update(MultipartFile file, Hub hub, Long userId) {
        if (hub.getAvatar() != null) {
            hubAvatarRepository.deleteByHubId(hub.getId());
            localMediaStorageService.delete(hub.getAvatar().getStorageKey());
        }

        return save(file, hub.getId(), userId);
    }
}
