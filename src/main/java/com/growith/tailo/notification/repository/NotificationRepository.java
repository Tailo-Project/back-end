package com.growith.tailo.notification.repository;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByReceiver(Member receiver, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n Set n.isRead = true WHERE n.id = :notificationId")
    int markNotification(@Param("notificationId") Long notificationId);
}
