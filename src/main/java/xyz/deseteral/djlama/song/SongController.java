package xyz.deseteral.djlama.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/song")
public class SongController {
    private final SongService service;

    @Autowired
    public SongController(SongService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Song> listAll() {
        return service.getAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Song findOne(@PathVariable(value = "id") String id) {
        return service.getById(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody Song song) {
        Song createdSong = service.add(song);
        return created(
            URI.create("/song/" + createdSong.getId())
        ).body(createdSong);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable(value = "id") String id,
                                 @Valid @RequestBody Song category) {
        Song updatedCategory = service.update(
            Song.builder(category).withId(id).build()
        );

        return accepted().body(updatedCategory);
    }
}
