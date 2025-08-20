package com.nrdChat.test.messageTest;

import com.nrdChat.app.controller.ChatController;
import com.nrdChat.app.dtos.MessageDTO;
import com.nrdChat.app.enums.MessageState;
import com.nrdChat.app.model.MessageEntity;
import com.nrdChat.app.model.UserChat;
import com.nrdChat.app.repository.MessageRepository;
import com.nrdChat.app.repository.UserRepository;
import com.nrdChat.app.service.MessageManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageManagementService messageService;


    @Test
    void testSaveMessage() {
        // Simulated Data
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

        MessageEntity message = MessageEntity.builder()
                .messageId(1L)
                .content("Hola!")
                .sendDate(LocalDateTime.now())
                .urlImg(null)
                .messageState(MessageState.SENT)
                .sender(sender)
                .receiver(receiver)
                .build();

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setMessageList(List.of(message));

        // Repository Behavior Simulation
        when(messageRepository.save(any(MessageEntity.class))).thenReturn( message );

        // Call the test method
        MessageEntity result = messageService.saveMessage( messageDTO ).getMessageEntity();

        // We verified that it was saved correctly
        assertNotNull(result);
        assertEquals("Hola!", result.getContent());
        assertEquals("maria@email.com", result.getReceiver().getEmail());
    }

    @Test
    void testListMessages() {
        // Simulated Data
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

        // Call the test method
        MessageDTO result = messageService.getChatMessages(sender.getEmail(), receiver.getEmail());

        // We verified that it was saved correctly
        assertNotNull(result);
    }



}
