package xyz.deseteral.djlama.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.deseteral.djlama.web.ResourceNotFoundException;

@Component
public class SongService {
    private final SongRepository repository;

    @Autowired
    public SongService(SongRepository repository) {
        this.repository = repository;
    }

    Iterable<Song> getAll() {
        return repository.findAll();
    }

    Song getById(String id) {
        Song song = repository.findOne(id);

        if (song == null) {
            throw new ResourceNotFoundException(
                String.format("Song with ID %s does not exist", id)
            );
        }

        return song;
    }

    Song add(Song song) {
        return repository.save(
            Song.builder(song)
                .withPlayCount(song.getPlayCount() == null ? 0 : song.getPlayCount())
                .build()
        );
    }

    Song update(Song song) {
        if (repository.findOne(song.getId()) == null) {
            throw new ResourceNotFoundException(
                String.format("Song with ID %s does not exist", song.getId())
            );
        }

        return repository.save(
            Song.builder(song).build()
        );
    }
}
