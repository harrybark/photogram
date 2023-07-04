package com.cos.photogramstart.service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${file.path}")
    private String uploadFolder;

    @Transactional
    public void ImageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {

        UUID uuid = UUID.randomUUID();
        String imageFileName = String.valueOf(uuid).concat(imageUploadDto.getFile().getOriginalFilename());

        Path imageFilePath = Paths.get(uploadFolder.concat(imageFileName));

        try {
            Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image image = imageUploadDto.toEntity(imageUploadDto, imageFileName, principalDetails.getUser());
        imageRepository.save(image);

    }

    public Page<Image> mStory(Long principalId, Pageable pageable) {
        Page<Image> images = imageRepository.mStory(principalId, pageable);

        images.forEach((image)-> {
            image.setLikeCount(Long.valueOf(image.getLikesList().size()));
            image.getLikesList().forEach((like) -> {
                if(like.getUser().getId() == principalId) {
                    image.setLikesState(true);
                }
            });
        });

        return images;
    }

    public List<Image> mPopularImage() {
        List<Image> result = imageRepository.mPopularImage();
        return result;
    }
}
