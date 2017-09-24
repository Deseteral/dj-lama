package xyz.deseteral.djlama.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.deseteral.djlama.song.SongService;

@RestController
@RequestMapping("/status")
public class StatusController {
    private final SongService songService;

    @Autowired
    public StatusController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public Status getStatus() {
        return Status.builder()
            .withSongCount(songService.getCount())
            .isOnAir(false)
            .build();
    }
}
