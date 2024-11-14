package com.halfgallon.withcon.domain.member.dto.response;

import com.halfgallon.withcon.domain.member.constant.LoginType;
import com.halfgallon.withcon.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberMyInfoResponse(
    Long memberId,
    String username,
    String nickname,
    String phoneNumber,
    LoginType loginType
) {

  public static MemberMyInfoResponse fromEntity(Member member) {
    return MemberMyInfoResponse.builder()
        .memberId(member.getId())
        .username(member.getUsername())
        .nickname(member.getNickname())
        .phoneNumber(member.getPhoneNumber())
        .loginType(member.getLoginType())
        .build();
  }
}
