package com.cos.photogramstart.config;

import com.cos.photogramstart.config.oauth.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity  // 해당 파일로 Security 를 활성화
@Configuration      // IoC에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* 기존 시큐리티가 가지고 있는 기능이 모두 비활성화
        super.configure(http);
         */

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/","/user/**","/image/**","/subscribe/**","/comment/**", "/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/signin") // GET
                .loginProcessingUrl("/auth/signin")  // POST -> 스프링 시큐리티가 로그인 프로세스 진행
                .defaultSuccessUrl("/")
                // form login 뿐만 아니라 OAuth2 login도 가능하다는 설정
                .and()
                .oauth2Login()
                .userInfoEndpoint() // oauth2 로그인을 하면 최종응답을 회원정보로 바로 받을 수 있다.
                .userService(oAuth2DetailsService)
        ;
    }
}
