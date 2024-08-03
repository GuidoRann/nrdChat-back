package com.nrdChat.app.service;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.dtos.UserDTO;
import com.nrdChat.app.enums.UserState;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageManagementService implements IMessageManagementService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageDTO saveMessage(MessageDTO messageDTO) {
        MessageDTO resp = new MessageDTO();

        try{
            MessageEntity message = MessageEntity.builder()
                    .content(messageDTO.getContent())
                    .sendDate(messageDTO.getSendDate())
                    .messageType(messageDTO.getMessageType())
                    .messageState(messageDTO.getMessageState())
                    .sender(messageDTO.getSender())
                    .receiver(messageDTO.getReceiver())
                    .build();

            MessageEntity messageRepo = messageRepository.save(message);

            if(messageRepo.getMessageId() > 0){
                resp.setMessageEntity(messageRepo);
                resp.setStatusCode(200);
                resp.setMessage("Message saved successfully");
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }

    @Override
    public MessageDTO updateMessage(MessageDTO messageDTO) {
        return null;
    }

    @Override
    public MessageDTO deleteMessage(Long messageId) {
        return null;
    }

    @Override
    public MessageDTO getReceiverMessageBySender(UserChat receiver, UserChat sender) {
        return null;
    }
}
