package xyz.deseteral.djlama.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.deseteral.djlama.discord.DiscordBot;
import xyz.deseteral.djlama.song.Song;
import xyz.deseteral.djlama.song.SongService;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin
@RestController
@RequestMapping("/queue")
public class QueueController {
    private Queue queue;
    private SongService songService;
    private DiscordBot discordBot;

    @Autowired
    public QueueController(Queue queue, SongService songService, DiscordBot discordBot) {
        this.queue = queue;
        this.songService = songService;
        this.discordBot = discordBot;
    }

    @GetMapping
    public Iterable<Song> findAll() {
        return queue.getSongs();
    }

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/{id}"
    )
    public ResponseEntity push(@PathVariable(value = "id") String id) {
        Song song = songService.getById(id);
        queue.push(song);
        discordBot.play();

        return ok().build();
    }
}
