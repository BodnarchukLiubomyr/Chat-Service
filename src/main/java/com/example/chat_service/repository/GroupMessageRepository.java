package com.example.chat_service.repository;

import com.example.chat_service.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {
    Set<GroupMessage> findByGroupId(Long groupId);

    GroupMessage findGroupMessageById(Long id);

    void deleteByGroupId(Long groupId);
}
