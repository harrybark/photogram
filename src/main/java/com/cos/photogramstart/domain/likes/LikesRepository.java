package com.cos.photogramstart.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Modifying
    @Query(value = "INSERT INTO Likes (imageId, userId, createDate) VALUES (:imageId, :principalId, now())", nativeQuery = true)
    int mLikes(Long imageId, Long principalId);

    @Modifying
    @Query(value = "DELETE FROM Likes WHERE imageId = :imageId and userId = :principalId", nativeQuery = true)
    int mUnLikes(Long imageId, Long principalId);
}
