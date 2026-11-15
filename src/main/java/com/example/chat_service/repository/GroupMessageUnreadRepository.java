package com.example.chat_service.repository;

import com.example.chat_service.model.GroupMessageUnread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GroupMessageUnreadRepository extends JpaRepository<GroupMessageUnread,Long> {
    List<GroupMessageUnread> findAllByUserId(Long userId);
    void deleteByGroupIdAndUserId(Long groupId, Long userId);
}
