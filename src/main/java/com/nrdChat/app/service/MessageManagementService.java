package com.nrdChat.app.service;

import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.mapper.DtoMapper;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageManagementService implements IMessageManagementService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public MessageDTO saveMessage( MessageDTO messageDTO ) {
        MessageDTO resp;

        try{
            MessageEntity message = MessageEntity.builder()
                    .content( messageDTO.getContent() )
                    .sendDate( messageDTO.getSendDate() )
                    .messageState( MessageState.DELIVERED )
                    .receiver( DtoMapper.toUserEntity( messageDTO.getReceiver() ) )
                    .sender( DtoMapper.toUserEntity( messageDTO.getSender() ) )
                    .build();

            MessageEntity messageRepo = messageRepository.save( message );

            resp = MessageDTO.builder()
                    .statusCode( messageRepo.getMessageId() > 0 ? 200 : 500 )
                    .message( messageRepo.getMessageId() > 0
                            ? "Message saved successfully"
                            : "Message not saved" )
                    .build();

        } catch ( Exception e ) {
            resp = MessageDTO.builder()
                    .statusCode( 500 )
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }

    @Override
    public MessageDTO deleteMessage( Long messageId ) {
        MessageDTO resp;

        try{
            messageRepository.findById( messageId )
                    .ifPresent( messageRepository::delete );

            resp = MessageDTO.builder()
                    .statusCode( 200 )
                    .message( "Message deleted successfully" )
                    .build();

        } catch ( Exception e ) {
            resp = MessageDTO.builder()
                    .statusCode( 500)
                    .error( e.getMessage() )
                    .build();
        }

        return resp;
    }


}
