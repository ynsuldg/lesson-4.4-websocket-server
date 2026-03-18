package se.iths.yunus.websocketserver;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ChatEndpoint {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        sessions.add(session);
        broadcast("En användare anslöt");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        broadcast("Meddelande: " + message);
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessions.remove(session);
        broadcast("En användare kopplade ner");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("WebSocket error: " + throwable.getMessage());
    }

    private void broadcast(String message) throws IOException {
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(message);
            }
        }
    }
}