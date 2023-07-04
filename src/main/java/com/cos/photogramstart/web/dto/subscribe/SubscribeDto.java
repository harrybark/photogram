package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {

    private Long id;         // 구독대상 유저
    private String username;     // 구독한 사람명
    private String profileImageUrl;
    private Long subscribeState; // 구독상태
    private Long equalUserState; // 세션과 동일한 유저정보인지

    public SubscribeDto(BigInteger id, String username, String profileImageUrl, Integer subscribeState, Integer equalUserState) {
        this.id = id == null ? null : id.longValue();
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.subscribeState = subscribeState.longValue();
        this.equalUserState = equalUserState.longValue();
    }
}
