package com.cos.photogramstart.config.oauth;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> userInfo = oAuth2User.getAttributes();
        String name = "facebook_".concat((String)userInfo.get("id"));
        String encPassword = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String username = (String)userInfo.get("name");
        String email = (String)userInfo.get("email");

        User findUserInfo = userRepository.findByUsername(name);

        if(findUserInfo == null) {
            User user = User.builder()
                    .username(name)
                    .password(encPassword)
                    .email(email)
                    .name(username)
                    .role("ROLE_USER")
                    .build();

            User userEntity = userRepository.save(user);
            return new PrincipalDetails(userEntity, userInfo);
        } else {
            return new PrincipalDetails(findUserInfo, userInfo);
        }
    }
}
