package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    private final EntityManager em;

    @Transactional
    public long subscribe(Long fromUserId, Long toUserId) {
        int result = 0;
        try {
            result = subscribeRepository.subscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomApiException("Already Subscribe.");
        }
        return result;
    }

    @Transactional
    public long unSubscribe(Long fromUserId, Long toUserId) {
        int result = 0;
        try {
            result = subscribeRepository.unSubscribe(fromUserId, toUserId);
        } catch (Exception e) {
            throw new CustomApiException("unsubscribe failed");
        }
        return result;
    }

    public List<SubscribeDto> subscribeList(Long principalUserId, Long pageUserId) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT u.id AS id, u.username AS username, u.profileImageUrl AS profileImageUrl, \n");
        query.append("       IF((SELECT 1 FROM Subscribe s1 WHERE s1.fromUserId = :loginUserId AND s1.toUserId = u.id), 1, 0) subscribeState, \n");
        query.append("       IF((u.id = :loginUserId), 1, 0) AS equalUserState \n");
        query.append("  FROM User u \n");
        query.append(" INNER JOIN Subscribe s \n");
        query.append("    ON u.id = s.toUserId \n");
        query.append(" WHERE s.fromUserId = :pageUserId \n");

        Query subscribeQuery = em.createNativeQuery(query.toString())
                .setParameter("loginUserId", principalUserId)
                .setParameter("pageUserId", pageUserId);

        // QLRM : ResultSet을 Java Class에 Mapping 해주는 것
        JpaResultMapper result = new JpaResultMapper();
        List<SubscribeDto> list = result.list(subscribeQuery, SubscribeDto.class);

        return list;
    }

}
