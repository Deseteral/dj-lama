package xyz.deseteral.djlama.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.deseteral.djlama.discord.DiscordBot;
import xyz.deseteral.djlama.song.SongService;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/status")
public class StatusController {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;

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
        final Instant startTime = discordBot.getStartTime();

        return Status.builder()
            .withSongCount(songService.getCount())
            .withQueueLength(discordBot.getQueue().getLength())
            .withCurrentlyPlaying(discordBot.getCurrentlyPlaying())
            .withOnAirSince(startTime == null ? null : dateTimeFormatter.format(startTime))
            .build();
    }
}
