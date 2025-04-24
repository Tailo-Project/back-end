package com.growith.tailo.notification.repository;

import com.growith.tailo.member.entity.Member;
import com.growith.tailo.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiver(Member member);
}
