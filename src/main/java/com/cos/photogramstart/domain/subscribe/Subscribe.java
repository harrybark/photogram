package com.cos.photogramstart.domain.subscribe;

import com.cos.photogramstart.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
        uniqueConstraints = {
        @UniqueConstraint(
                name="subscribe_uk",
                columnNames = {"fromUserId", "toUserId"}
        )
    }
)
@ToString(exclude = {"toUser"})
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "fromUserId")
    @ManyToOne
    private User fromUser;

    @JoinColumn(name = "toUserId")
    @ManyToOne
    private User toUser;

    private LocalDateTime createDate;

    @PrePersist // DB에 Persist() 되기 이전에 실행
    public void createDate() {
        this.createDate = LocalDateTime.now();
    }
}
