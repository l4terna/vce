package com.vce.vce.member;

import com.vce.vce._shared.model.dto.PageableDTO;
import com.vce.vce.member.dto.MemberDTO;
import com.vce.vce.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubs/{hubId}/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<Page<MemberDTO>> getMembers(@PathVariable Long hubId, @ModelAttribute PageableDTO pageableDTO) {
        return ResponseEntity.ok(memberService.getAllMembers(hubId, pageableDTO));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long hubId,
            @PathVariable Long memberId,
            @AuthenticationPrincipal User user
    ) {
        memberService.deleteMember(hubId, memberId, user);
        return ResponseEntity.noContent().build();
    }
}
