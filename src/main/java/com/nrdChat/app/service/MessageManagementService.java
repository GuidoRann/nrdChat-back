package com.nrdChat.app.service;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageManagementService implements IMessageManagementService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageDTO saveMessage( MessageDTO messageDTO ) {
        MessageDTO resp = new MessageDTO();

        try{
            MessageEntity message = MessageEntity.builder()
                    .content( messageDTO.getContent() )
                    .sendDate( messageDTO.getSendDate() )
//                    .messageType( messageDTO.getMessageType() )
                    .messageState( MessageState.DELIVERED )
                    .sender( messageDTO.getSender() )
                    .receiver( messageDTO.getReceiver() )
                    .build();

            MessageEntity messageRepo = messageRepository.save( message );

            if ( messageRepo.getMessageId() > 0 ){
                resp.setMessageEntity(messageRepo);
                resp.setStatusCode( 200 );
                resp.setMessage("Message saved successfully");
            } else {
                resp.setStatusCode( 500 );
                resp.setMessage("Message not saved");
            }

        } catch ( Exception e ) {
            resp.setStatusCode( 500 );
            resp.setError( e.getMessage() );
        }

        return resp;
    }

    @Override
    public MessageDTO deleteMessage( Long messageId ) {
        MessageDTO resp = new MessageDTO();

        try{
            messageRepository.findById( messageId ).ifPresent( messageRepository::delete );
            resp.setStatusCode( 200 );
            resp.setMessage( "Message deleted successfully" );

        } catch ( Exception e ) {
            resp.setStatusCode( 500 );
            resp.setError( e.getMessage() );
        }

        return resp;
    }

    @Override
    public MessageDTO getChatMessages(String senderEmail, String receiverEmail) {
        MessageDTO resp = new MessageDTO();

        try {
            List<MessageEntity> messages = messageRepository.findChatMessages(senderEmail, receiverEmail);

            if (!messages.isEmpty()) {
                resp.setMessageList(messages);
                resp.setMessage("Messages found successfully");
                resp.setStatusCode(200);
            } else {
                resp.setStatusCode(404);
                resp.setMessage("No messages found");
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
            resp.setMessage("Error occurred: " + e.getMessage());
        }

        return resp;
    }

}
