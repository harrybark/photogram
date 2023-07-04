package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Modifying
    @Query(value = "INSERT INTO Subscribe(fromUserId, toUserId, createDate) VALUES (:fromUserId, :toUserId, now())", nativeQuery = true)
    int subscribe(Long fromUserId, Long toUserId);

    @Modifying
    @Query(value = "DELETE FROM Subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
    int unSubscribe(Long fromUserId, Long toUserId);

    int countSubscribeByFromUser_Id(long pageUserId);

    int countSubscribeByFromUser_IdAndToUser_Id(long loginUserId, long pageUserId);

}
