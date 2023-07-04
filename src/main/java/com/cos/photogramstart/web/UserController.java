package com.cos.photogramstart.web;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/user/profile/{pageUserId}")
    public String profile(@PathVariable("pageUserId") Long pageUserId,
                          Model model,
                          @AuthenticationPrincipal PrincipalDetails principalDetails){
        model.addAttribute("dto", userService.findUserProfile(pageUserId, principalDetails.getUser().getId()));
        return "user/profile";
    }

    @GetMapping("/user/{id}/update")
    public String updateProfile(@PathVariable("id") Long id,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // session 1
        User user = principalDetails.getUser();

        // session 2
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails mPrincipalDetails = (PrincipalDetails) authentication.getPrincipal();
        User sessionUser = mPrincipalDetails.getUser();

        return "user/update";
    }
}
