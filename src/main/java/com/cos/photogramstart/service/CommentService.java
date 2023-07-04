package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment commentSave(String content, Long imageId, Long userId) {

        // return 시에 image 객체와 user 객체는 id 만 넘어감
        Image image = new Image();
        image.setId(imageId);

        User userEntity = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomApiException("Not Found User");
        });

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setImage(image);
        comment.setUser(userEntity);

        Comment commentEntity = commentRepository.save(comment);
        return commentEntity;
    }

    @Transactional
    public void commentDelete(Long commentId){
        try {
            commentRepository.deleteById(commentId);
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        } finally {

        }
    }
}
