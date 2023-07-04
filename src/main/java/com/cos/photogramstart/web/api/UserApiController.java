package com.cos.photogramstart.web.api;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private Logger logger = LoggerFactory.getLogger(UserApiController.class);
    private final UserService userService;
    private final SubscribeService subscribeService;

    @GetMapping("/api/user/{pageUserId}/subscribe")
    public ResponseEntity<CMRespDto<?>> subscribeList(@PathVariable("pageUserId") Long pageUserId,
                                                      @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<SubscribeDto> subscribeDtoList = subscribeService.subscribeList(principalDetails.getUser().getId(), pageUserId);
        return new ResponseEntity<>(new CMRespDto<>(1, "success", subscribeDtoList), HttpStatus.OK);
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity<CMRespDto<?>> update(@PathVariable("id") Long id,
                                              @Valid UserUpdateDto userUpdateDto,
                                              BindingResult bindingResult,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        User userEntity = userService.updateUser(id, userUpdateDto.toEntity());
        principalDetails.setUser(userEntity);

        // 응답시 userEntity 의 모든 getter 함수가 호출되고 JSON 으로 파싱하여 응답한다.
        return new ResponseEntity<>(new CMRespDto<>(1, "success", userEntity), HttpStatus.OK);

    }

    @PutMapping("/api/user/{principalId}/profileImageUrl")
    public ResponseEntity<CMRespDto<?>> profileImageUrl(@PathVariable("principalId") Long id,
                                                        MultipartFile profileImageFile,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails
                                                        ) {

        User userEntity = userService.updateUserProfile(id, principalDetails.getUser().getId(), profileImageFile);
        principalDetails.setUser(userEntity);
        return new ResponseEntity<>(new CMRespDto<>(1, "success", null), HttpStatus.OK);

    }
}
