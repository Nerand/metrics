package org.example.performancedemo;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ResultSocket extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                session.sendMessage(new TextMessage("done"));
            } catch (Exception ignored) {}
        }).start();
    }
}