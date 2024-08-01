package org.example.bikers.domain.talk.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.MyTalkRoomGetResponseDto;
import org.example.bikers.domain.talk.dto.TalkRoomCreateRequestDto;
import org.example.bikers.domain.talk.service.TalkRoomService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/talk-rooms")
public class TalkRoomController {

    private final TalkRoomService talkRoomService;

    @PostMapping
    public ResponseEntity<Void> createRoom(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody TalkRoomCreateRequestDto requestDto) {
        talkRoomService.createRoom(userDetails.getMember().getId(), requestDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/mine")
    public ResponseEntity<CommonResponseDto<List<MyTalkRoomGetResponseDto>>> getMyRooms(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<MyTalkRoomGetResponseDto> responseDtoList = talkRoomService.getMyRooms(
            userDetails.getMember().getId());
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

}
