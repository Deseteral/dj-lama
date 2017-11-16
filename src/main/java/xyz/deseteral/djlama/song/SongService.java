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

    public Song getById(String id) {
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
                .withPlayCount(0)
                .build()
        );
    }

    Song addLegacy(Song song) {
        return repository.save(
            Song.builder(song)
                .withPlayCount(song.getPlayCount() == null ? 0 : song.getPlayCount())
                .build()
        );
    }

    Song update(Song song) {
        final Song foundSong = repository.findOne(song.getId());

        if (foundSong == null) {
            throw new ResourceNotFoundException(
                String.format("Song with ID %s does not exist", song.getId())
            );
        }

        return repository.save(
            Song.builder(foundSong)
                .withTitle(song.getTitle())
                .withArtist(song.getArtist())
                .withYoutubeId(song.getYoutubeId())
                .withPlayCount(song.getPlayCount())
                .build()
        );
    }

    void remove(String id) {
        if (repository.findOne(id) == null) {
            throw new ResourceNotFoundException(
                String.format("Song with ID %s does not exist", id)
            );
        }

        repository.delete(id);
    }

    public long getCount() {
        return repository.count();
    }

    public void markAsPlayed(String id) {
        final Song foundSong = repository.findOne(id);

        if (foundSong == null) {
            throw new ResourceNotFoundException(
                String.format("Song with ID %s does not exist", id)
            );
        }

        final Song updatedSong = Song
            .builder(foundSong)
            .withPlayCount(foundSong.getPlayCount() + 1)
            .build();

        this.update(updatedSong);
    }
}
