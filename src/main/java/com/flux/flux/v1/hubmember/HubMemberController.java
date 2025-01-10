package com.flux.flux.v1.hubmember;

import com.flux.flux.v1._shared.model.dto.PageableDTO;
import com.flux.flux.v1.hubmember.dto.HubMemberDTO;
import com.flux.flux.v1.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hubs/{hubId}/members")
@RequiredArgsConstructor
public class HubMemberController {
    private final HubMemberService hubMemberService;

    @GetMapping
    public ResponseEntity<Page<HubMemberDTO>> getMembers(@PathVariable Long hubId, @ModelAttribute PageableDTO pageableDTO) {
        return ResponseEntity.ok(hubMemberService.getAllMembers(hubId, pageableDTO));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long hubId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal User user
    ) {
        hubMemberService.deleteMember(hubId, memberId, user);
        return ResponseEntity.noContent().build();
    }
}
