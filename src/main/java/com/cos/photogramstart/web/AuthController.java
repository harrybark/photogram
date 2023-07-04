package com.cos.photogramstart.web;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller //  IoC, 파일을 리턴하는 Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService ;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/auth/signin")
    public String signinForm() {
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm() {
        return "auth/signup";
    }

    /**
     * 1.회원가입 버튼 - /auth/signup
     * 2.로그인 창으로 이동 - /auth/signin
     * @return
     */
    @PostMapping("/auth/signup")
    public String signup(@Valid SignupDto signupDto
            , BindingResult bindingResult
    ) { // key=value(x-www-form-urlencoded)

        User user = signupDto.toEntity();
        User userEntity = authService.signup(user);
        return "redirect:/auth/signin";

    }
}
