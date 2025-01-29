package com.nrdChat.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.enums.MessageType;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO {

    private int statusCode;
    private String error;
    private String message;
    private String content;
    private LocalDateTime sendDate;
    private String urlImg;
    private MessageType messageType;
    private MessageState messageState;
    private UserChat sender;
    private UserChat receiver;
    private List<MessageEntity> messageList;
    private MessageEntity messageEntity;

}
