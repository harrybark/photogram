package com.cos.photogramstart.domain.user;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String website;
    private String bio;

    @Column(nullable = false)
    private String email;
    private String phone;
    private String gender;

    private String profileImageUrl;

    private String role; // 권한

    /*
    @OneToMany(mappedBy = "toUser", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})
    private List<Subscribe> subscribeList = new ArrayList<>();
    */

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"}) // 사용하지 않으면 무한참조가 발생한다.
    private List<Image> imageList = new ArrayList<>();

    private LocalDateTime createDate;

    @PrePersist // DB에 Persist() 되기 이전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
