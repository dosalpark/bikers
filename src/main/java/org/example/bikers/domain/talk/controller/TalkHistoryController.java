package org.example.bikers.domain.talk.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.TalkHistoryGetResponseDto;
import org.example.bikers.domain.talk.service.TalkHistoryService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/talk-rooms")
public class TalkHistoryController {

    private final TalkHistoryService talkHistoryService;

    @GetMapping("/{roomId}")
    public ResponseEntity<CommonResponseDto<List<TalkHistoryGetResponseDto>>> getTalkHistoryByRoomId(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long roomId) {
        List<TalkHistoryGetResponseDto> responseDtoList =
            talkHistoryService.getTalkHistoryByRoomId(userDetails.getMember().getId(), roomId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

}
