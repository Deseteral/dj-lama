package xyz.deseteral.djlama.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.deseteral.djlama.discord.DiscordBot;
import xyz.deseteral.djlama.song.SongService;

@RestController
@RequestMapping("/status")
public class StatusController {
    private final SongService songService;
    private final DiscordBot discordBot;

    @Autowired
    public StatusController(SongService songService, DiscordBot discordBot) {
        this.songService = songService;
        this.discordBot = discordBot;
    }

    @CrossOrigin
    @GetMapping
    public Status getStatus() {
        return Status.builder()
            .withSongCount(songService.getCount())
            .withCurrentlyPlaying(discordBot.getCurrentlyPlaying())
            .build();
    }
}
