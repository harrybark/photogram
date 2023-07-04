package com.cos.photogramstart.web.dto.image;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class ImageUploadDto {

    @NotBlank
    private MultipartFile file;
    private String caption;

    public Image toEntity(ImageUploadDto imageUploadDto, String filePath, User user) {
        return Image.builder()
                .caption(imageUploadDto.getCaption())
                .postImageUrl(filePath)
                .user(user)
                .build();
    }
}
