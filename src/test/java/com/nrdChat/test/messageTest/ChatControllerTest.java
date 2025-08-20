package com.nrdChat.test.messageTest;

import com.nrdChat.app.controller.ChatController;
import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.service.MessageManagementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatControllerTest {

    @Autowired
    private ChatController chatController;

    @MockBean
    private MessageManagementService messageService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @Test
    void testSendPrivateMessage() {
        // Crear usuarios simulados
        UserChat sender = UserChat.builder()
                .userId(1L)
                .email("juan@email.com")
                .name("Juan")
                .role("USER")
                .password("12345")
                .build();

        UserChat receiver = UserChat.builder()
                .userId(2L)
                .email("maria@email.com")
                .name("Maria")
                .role("USER")
                .password("12345")
                .build();

        // Crear mensaje simulado
        MessageEntity entity = MessageEntity.builder()
                .messageId(1L)
                .content("¡Hola! ¿Cómo estás?")
                .sendDate(LocalDateTime.now())
                .messageState(MessageState.SENT)
                .sender(sender)
                .receiver(receiver)
                .build();

        // DTO con el mensaje
        MessageDTO dto = new MessageDTO();
        dto.setMessageList(List.of(entity));
        dto.setSender(sender);
        dto.setReceiver(receiver);

        // Configuramos el comportamiento del mock del servicio
        when(messageService.saveMessage(any(MessageDTO.class))).thenReturn(dto);

        // Ejecutamos el método
        chatController.sendPrivateMessage(dto);

        // Verificamos que se haya llamado al servicio
        verify(messageService).saveMessage(any(MessageDTO.class));

        // Verificamos que se haya enviado el mensaje al usuario correcto
        Mockito.<SimpMessagingTemplate>verify(simpMessagingTemplate).convertAndSendToUser(
                eq("maria@email.com"),
                eq("/queue/messages"),
                eq(dto)
        );
    }
}
