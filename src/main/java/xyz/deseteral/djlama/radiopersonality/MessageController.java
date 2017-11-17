package xyz.deseteral.djlama.radiopersonality;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import xyz.deseteral.djlama.discord.DiscordBot;

@Controller
public class MessageController {
    private final DiscordBot discordBot;

    private int connectionNumber;
    private int clientsFinished;

    @Autowired
    public MessageController(DiscordBot discordBot) {
        this.discordBot = discordBot;
        this.connectionNumber = 0;
        this.clientsFinished = 0;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public Message recivedMessage(Message message) {
        discordBot.fadeOut();
        clientsFinished = 0;
        return message;
    }

    @MessageMapping("/messageEnd")
    public void finishedMessage() {
        clientsFinished++;

        if (clientsFinished == connectionNumber) {
            discordBot.fadeIn();
            clientsFinished = 0;
        }
    }

    @EventListener
    private void onSessionConnectedEvent(SessionConnectedEvent event) {
        connectionNumber++;
    }

    @EventListener
    private void onSessionDisconnectEvent(SessionDisconnectEvent event) {
        connectionNumber--;
    }
}
