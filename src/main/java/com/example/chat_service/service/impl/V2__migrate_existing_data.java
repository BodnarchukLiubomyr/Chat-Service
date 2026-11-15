package com.example.chat_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class V2__migrate_existing_data extends BaseJavaMigration {
    private final JdbcTemplate mainJdbcTemplate;

    @Override
    public void migrate(Context context) throws SQLException {
        Connection connection = context.getConnection();
        migrateChats(connection);
        migrateMessages(connection);
        migrateChatUnread(connection);
        migrateGroupMessages(connection);
        migrateGroupMessageUnread(connection);
    }

    public void migrateChats(Connection connection) throws SQLException {
        List<OldChat> chats = mainJdbcTemplate.query(
                """
                        SELECT
                            c.id AS chat_id,
                            cu.user_id
                        FROM chats c
                        JOIN chat_user cu ON cu.chat_id = c.id
                        ORDER BY c.id, cu.user_id
                        """,
                (rs, rowNum) -> new OldChat(
                        rs.getLong("chat_id"),
                        rs.getLong("user_id")
                )
        );

        Map<Long, List<Long>> usersByChat = chats.stream()
                .collect(Collectors.groupingBy(
                        OldChat::chatId, LinkedHashMap::new,
                        Collectors.mapping(OldChat::userId, Collectors.toList())
                ));

        try (PreparedStatement ps = connection.prepareStatement(
                """
                        INSERT INTO chats (id, user_id1, user_id2)
                        VALUES (?, ?, ?)
                        """
        )) {
            for (Map.Entry<Long, List<Long>> entry : usersByChat.entrySet()) {

                List<Long> users = entry.getValue();

                if (users.size() != 2) {
                    throw new IllegalStateException(
                            "Chat " + entry.getKey()
                                    + " has " + users.size() + " users"
                    );
                }

                ps.setLong(1, entry.getKey());
                ps.setLong(2, users.get(0));
                ps.setLong(3, users.get(1));
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private record OldChat(
            Long chatId,
            Long userId
    ) {
    }

    public void migrateMessages(Connection connection) throws SQLException {
        List<OldMessage> messages = mainJdbcTemplate.query(
                """
                        SELECT
                            id,
                            content,
                            user_id,
                            chat_id,
                            timestamp
                        FROM messages
                        ORDER BY id
                        """,
                (rs, rowNum) -> new OldMessage(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getLong("user_id"),
                        rs.getLong("chat_id"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                )
        );

        try (PreparedStatement ps = connection.prepareStatement(
                """
                        INSERT INTO messages (
                            id,
                            content,
                            sender_id,
                            chat_id,
                            timestamp
                        )
                        VALUES (?, ?, ?, ?, ?)
                        """
        )) {
            for (OldMessage message : messages) {

                ps.setLong(1, message.id());
                ps.setString(2, message.content());
                ps.setLong(3, message.senderId());
                ps.setLong(4, message.chatId());
                ps.setTimestamp(5, Timestamp.valueOf(message.timestamp()));
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private record OldMessage(
            Long id,
            String content,
            Long senderId,
            Long chatId,
            LocalDateTime timestamp
    ) {
    }

    public void migrateChatUnread(Connection connection) throws SQLException {
        List<OldChatUnread> oldChatUnreads = mainJdbcTemplate.query(
                """
                        SELECT
                            id,
                            chat_id,
                            user_id
                        FROM chat_unread
                        ORDER BY id
                        """,
                (rs, rowNum) -> new OldChatUnread(
                        rs.getLong("id"),
                        rs.getLong("chat_id"),
                        rs.getLong("user_id")
                )
        );

        try (PreparedStatement ps = connection.prepareStatement(
                """
                        INSERT INTO chat_unread (
                            id,
                            chat_id,
                            user_id
                        )
                        VALUES (?, ?, ?)
                        """
        )) {
            for (OldChatUnread oldChatUnread : oldChatUnreads) {

                ps.setLong(1, oldChatUnread.id());
                ps.setLong(2, oldChatUnread.chatId());
                ps.setLong(3, oldChatUnread.userId());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private record OldChatUnread(
            Long id,
            Long chatId,
            Long userId
    ) {
    }

    public void migrateGroupMessages(Connection connection) throws SQLException {
        List<OldGroupMessage> oldGroupMessages = mainJdbcTemplate.query(
                """
                        SELECT
                            id,
                            content,
                            user_id,
                            group_id,
                            timestamp
                        FROM group_messages
                        ORDER BY id
                        """,
                (rs, rowNum) -> new OldGroupMessage(
                        rs.getLong("id"),
                        rs.getString("content"),
                        rs.getLong("user_id"),
                        rs.getLong("group_id"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                )
        );

        try (PreparedStatement ps = connection.prepareStatement(
                """
                        INSERT INTO group_messages (
                            id,
                            content,
                            sender_id,
                            group_id,
                            timestamp
                        )
                        VALUES (?, ?, ?, ?, ?)
                        """
        )) {
            for (OldGroupMessage oldGroupMessage : oldGroupMessages) {
                ps.setLong(1, oldGroupMessage.id());
                ps.setString(2, oldGroupMessage.content());
                ps.setLong(3, oldGroupMessage.senderId());
                ps.setLong(4, oldGroupMessage.groupId());
                ps.setTimestamp(5, Timestamp.valueOf(oldGroupMessage.timestamp()));
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private record OldGroupMessage(
            Long id,
            String content,
            Long senderId,
            Long groupId,
            LocalDateTime timestamp
    ) {
    }

    public void migrateGroupMessageUnread(Connection connection) throws SQLException {
        List<OldGroupMessageUnread> oldGroupMessageUnreads = mainJdbcTemplate.query(
                """
                        SELECT
                            id,
                            group_id,
                            user_id
                        FROM group_unread
                        ORDER BY id
                        """,
                (rs, rowNum) -> new OldGroupMessageUnread(
                        rs.getLong("id"),
                        rs.getLong("group_id"),
                        rs.getLong("user_id")
                )
        );

        try (PreparedStatement ps = connection.prepareStatement(
                """
                        INSERT INTO group_message_unread (
                            id,
                            group_id,
                            user_id
                        )
                        VALUES (?, ?, ?)
                        """
        )) {
            for (OldGroupMessageUnread oldGroupMessageUnread : oldGroupMessageUnreads) {

                ps.setLong(1, oldGroupMessageUnread.id());
                ps.setLong(2, oldGroupMessageUnread.groupId());
                ps.setLong(3, oldGroupMessageUnread.userId());
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    private record OldGroupMessageUnread(
            Long id,
            Long groupId,
            Long userId
    ) {
    }
}

