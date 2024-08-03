package com.nrdChat.app.service;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.model.UserChat;

public interface IMessageManagementService {

    public MessageDTO saveMessage(MessageDTO messageDTO);

    public MessageDTO updateMessage(MessageDTO messageDTO);

    public MessageDTO deleteMessage(Long messageId);

    public MessageDTO getReceiverMessageBySender(UserChat receiver, UserChat sender);

}
