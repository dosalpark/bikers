package org.example.bikers.domain.talk.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bikers.domain.talk.dto.MyTalkRoomGetResponseDto;
import org.example.bikers.domain.talk.dto.TalkRoomJoinRequestDto;
import org.example.bikers.domain.talk.service.TalkRoomMemberService;
import org.example.bikers.global.dto.CommonResponseDto;
import org.example.bikers.global.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/talk-rooms")
public class TalkRoomMemberController {

    private final TalkRoomMemberService talkRoomMemberService;


    @GetMapping("/mine")
    public ResponseEntity<CommonResponseDto<List<MyTalkRoomGetResponseDto>>> getMyRooms(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<MyTalkRoomGetResponseDto> responseDtoList = talkRoomMemberService.getMyRooms(
            userDetails.getMember().getId());
        return ResponseEntity.status(HttpStatus.OK)
            .body(CommonResponseDto.success(responseDtoList));
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinRoom(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody TalkRoomJoinRequestDto requestDto) {
        talkRoomMemberService.joinRoom(
            userDetails.getMember().getId(),
            userDetails.getMember().getEmail(),
            requestDto.getRoomId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/leave/{roomId}")
    public ResponseEntity<Void> leaveRoom(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long roomId) {
        talkRoomMemberService.leaveRoom(userDetails.getMember().getId(), roomId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
