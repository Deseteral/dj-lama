package xyz.deseteral.djlama.queue;

import org.springframework.stereotype.Component;
import xyz.deseteral.djlama.song.Song;

import java.util.ArrayList;
import java.util.List;

@Component
public class Queue {
    private List<Song> songs;

    public Queue() {
        this.songs = new ArrayList<>();
    }

    void push(Song s) {
        songs.add(s);
    }

    public Song pop() {
        if (songs.size() == 0) {
            return null;
        }

        Song value = songs.get(0);
        songs.remove(0);

        return value;
    }

    public int getLength() {
        return songs.size();
    }
}
