package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.web.api.UserApiController;
import com.cos.photogramstart.web.dto.user.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    @Value("${file.path}")
    private String uploadFolder;

    private Logger logger = LoggerFactory.getLogger(UserApiController.class);

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserProfileDto findUserProfile(Long pageUserId, Long loginUserId) {

        UserProfileDto profileDto = new UserProfileDto();

        User findUserInfo = userRepository.findById(pageUserId)
                .orElseThrow(() -> {
                    throw new CustomException("Not Found User");
                });

        int subscribeCount = subscribeRepository.countSubscribeByFromUser_Id(pageUserId);
        int subscribeState = subscribeRepository.countSubscribeByFromUser_IdAndToUser_Id(loginUserId, pageUserId);

        profileDto.setUser(findUserInfo);
        profileDto.setImageCount(findUserInfo.getImageList().size());
        profileDto.setPageOwnerState(pageUserId == loginUserId);
        profileDto.setSubscribeState(subscribeState==1);
        profileDto.setSubscribeCount(subscribeCount);
        findUserInfo.getImageList().forEach(image -> {
            image.setLikeCount(Long.valueOf(image.getLikesList().size()));
        });
        return profileDto;
    }
    @Transactional
    public User updateUser(Long id, User user) {

        // 1. 영속화
        User userEntity = userRepository.findById(id).orElseThrow(() -> new CustomValidationApiException("ID Not Found"));

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        // 2. 영속화된 오브젝트를 수정 - 더티체킹(업데이트 완료)
        userEntity.setName(user.getName());
        userEntity.setPassword(encPassword);
        userEntity.setBio(user.getBio());
        userEntity.setWebsite(user.getWebsite());
        userEntity.setPhone(user.getPhone());
        userEntity.setGender(user.getGender());

        return userEntity;
    }

    @Transactional
    public User updateUserProfile(Long id, Long principalId, MultipartFile multipartFile) {
        if (id != principalId) {
            throw new CustomApiException("Id does not match.");
        }

        UUID uuid = UUID.randomUUID();
        String imageFileName = String.valueOf(uuid).concat(multipartFile.getOriginalFilename());

        Path imageFilePath = Paths.get(uploadFolder.concat(imageFileName));

        try {
            Files.write(imageFilePath, multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        User userEntity = userRepository.findById(id).orElseThrow(() -> {
            throw new CustomApiException("User Not Found");
        });

        userEntity.setProfileImageUrl(imageFileName);
        return userEntity;
    }
}
