package com.nrdChat.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDTO {

    private Integer statusCode;
    private String error;
    private String message;

    private Long id;
    private String content;
    private LocalDateTime sendDate;
    private String urlImg;
    private MessageType messageType;
    private MessageState messageState;

    private UserDTO sender;
    private UserDTO receiver;

    private List<MessageDTO> messages;

}
