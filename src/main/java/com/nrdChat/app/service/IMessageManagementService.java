package com.nrdChat.app.service;

import com.nrdChat.app.dtos.MessageDTO;


public interface IMessageManagementService {

    public MessageDTO saveMessage(MessageDTO messageDTO);

    public MessageDTO deleteMessage(Long messageId);

    public MessageDTO getChatMessages(String senderEmail, String receiverEmail);

//    public MessageDTO getAllMessages(UserChat receiver, UserChat sender);

}
