package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@ToString(exclude = {"user"})
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사진 부연설명
    private String caption;
    // 사진을 전송 받아서 그 사진을 서버 특정 폴더에 저장 : DB에 그 저장된 경로를 저장
    private String postImageUrl;

    @JsonIgnoreProperties({"imageList"})
    @JoinColumn(name = "userId")
    @ManyToOne
    private User user;

    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image")
    private List<Likes> likesList = new ArrayList<>();

    @OrderBy("id DESC")
    @JsonIgnoreProperties({"image"})
    @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    @Transient // DB에 컬럼이 만들어지지 않음
    private boolean likesState;

    @Transient
    private Long likeCount;

    // 좋아요
    // 댓글
    private LocalDateTime createDate;

    @PrePersist // DB에 Persist() 되기 이전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }

}
