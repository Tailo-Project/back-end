package com.growith.tailo.notification.repository;

import com.growith.tailo.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NorificationRepository extends JpaRepository<Notification, Long> {
}
