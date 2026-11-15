package com.example.chat_service.repository;

import com.example.chat_service.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findChatById(Long id);

    Optional<Chat> findByUserId1AndUserId2(Long userId1, Long userId2);

    @Query("""
                SELECT c
                FROM Chat c
                WHERE c.userId1 = :userId
                   OR c.userId2 = :userId
            """)
    List<Chat> findAllByUserId(@Param("userId") Long userId);

    @Query("""
                SELECT c
                FROM Chat c
                WHERE
                    (c.userId1 = :userId1 AND c.userId2 = :userId2)
                    OR
                    (c.userId1 = :userId2 AND c.userId2 = :userId1)
            """)
    Optional<Chat> findChatBetweenUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2
    );
}
