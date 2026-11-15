package com.example.chat_service.service;

import com.example.chat_service.exceptionHandler.ServiceException;
import com.example.chat_service.model.GroupMessage;
import com.example.chat_service.model.GroupMessageUnread;

import java.util.List;
import java.util.Set;

public interface GroupMessageService {
    Set<GroupMessage> getChatHistory(Long groupId) throws ServiceException;

    GroupMessage sendMessage(Long groupId, Long userId, String content) throws ServiceException;

    List<GroupMessageUnread> getGroupsUnread(Long userId) throws ServiceException;

    void clearUnread(Long groupId, Long userId);

    GroupMessage updateMessage(Long messageId, String content) throws ServiceException;

    void deleteGroupMessage(Long messageId, String content) throws ServiceException;
}
