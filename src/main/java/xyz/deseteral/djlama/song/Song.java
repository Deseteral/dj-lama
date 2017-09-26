package xyz.deseteral.djlama.song;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class Song {
    @Id
    @JsonProperty("id")
    private String id;
    private String title;
    private String artist;
    private String youtubeId;
    private Integer playCount;

    @JsonCreator
    Song(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("artist") String artist,
        @JsonProperty("youtubeId") String youtubeId,
        @JsonProperty("playCount") Integer playCount
    ) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.youtubeId = youtubeId;
        this.playCount = playCount;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    static Builder builder(Song song) {
        return new Builder(song);
    }

    static final class Builder {
        private String id;
        private String title;
        private String artist;
        private String youtubeId;
        private Integer playCount;

        private Builder(Song song) {
            this.id = song.id;
            this.title = song.title;
            this.artist = song.artist;
            this.youtubeId = song.youtubeId;
            this.playCount = song.playCount;
        }

        Builder withId(String id) {
            this.id = id;
            return this;
        }

        Builder withPlayCount(int playCount) {
            this.playCount = playCount;
            return this;
        }

        Song build() {
            return new Song(id, title, artist, youtubeId, playCount);
        }
    }
}
