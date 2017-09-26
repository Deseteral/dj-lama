package xyz.deseteral.djlama.play;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.deseteral.djlama.discord.DiscordBot;

@Component
public class PlayService {
    private DiscordBot discordBot;

    @Autowired
    public PlayService(DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    void play(String id) {
        discordBot.play(id);
    }
}
