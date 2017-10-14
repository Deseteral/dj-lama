package xyz.deseteral.djlama.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import xyz.deseteral.djlama.song.Song;

class Status {
    @JsonProperty("songCount")
    private Long songCount;

    @JsonProperty("currentlyPlaying")
    private Song currentlyPlaying;

    Status(
        Long songCount,
        Song currentlyPlaying
    ) {
        this.songCount = songCount;
        this.currentlyPlaying = currentlyPlaying;
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private Long songCount;
        private Song currentlyPlaying;

        private Builder() {
            this.songCount = 0L;
            this.currentlyPlaying = null;
        }

        Builder withSongCount(long songCount) {
            this.songCount = songCount;
            return this;
        }

        Builder withCurrentlyPlaying(Song song) {
            this.currentlyPlaying = song;
            return this;
        }

        Status build() {
            return new Status(songCount, currentlyPlaying);
        }
    }
}
