package com.cos.photogramstart.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT * FROM Image WHERE userId IN " +
            "(SELECT toUserId FROM Subscribe WHERE fromUserId = :principalId)"
            , nativeQuery = true)
    Page<Image> mStory(Long principalId, Pageable pageable);

    @Query(value = "SELECT i.* FROM Image i INNER JOIN " +
            "(SELECT imageId, count(imageId) likeCount FROM Likes GROUP BY imageId) i2 ON i.id = i2.imageId ORDER BY i2.likeCount DESC",
            nativeQuery = true)
    List<Image> mPopularImage();
}
