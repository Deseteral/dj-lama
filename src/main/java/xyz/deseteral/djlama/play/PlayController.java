package xyz.deseteral.djlama.play;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/play")
public class PlayController {
    private PlayService service;

    @Autowired
    public PlayController(PlayService service) {
        this.service = service;
    }

    @RequestMapping(
        method = RequestMethod.POST,
        value = "/{id}"
    )
    public ResponseEntity play(@PathVariable(value = "id") String id) {
        service.play(id);
        return ok().build();
    }
}
