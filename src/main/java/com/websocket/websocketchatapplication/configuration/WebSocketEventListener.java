package com.websocket.websocketchatapplication.configuration;

import com.websocket.websocketchatapplication.controller.ChatMessage;
import com.websocket.websocketchatapplication.controller.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ){
        StompHeaderAccessor headerAccessor =StompHeaderAccessor.wrap(event.getMessage());
        String username =(String) headerAccessor.getSessionAttributes().get("username");
        if(username!=null){
            log.info("user disconnected: {}",username);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .build();
            messageTemplate.convertAndSend("/topic/public",chatMessage);

        }

    }
}
