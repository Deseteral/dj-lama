package xyz.deseteral.djlama.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import xyz.deseteral.djlama.song.Song;

class Status {
    @JsonProperty("songCount")
    private Long songCount;

    @JsonProperty("queueLength")
    private Integer queueLength;

    @JsonProperty("currentlyPlaying")
    private Song currentlyPlaying;

    @JsonProperty("onAirSince")
    private String onAirSince;

    Status(
        Long songCount,
        Integer queueLength,
        Song currentlyPlaying,
        String onAirSince
    ) {
        this.songCount = songCount;
        this.queueLength = queueLength;
        this.currentlyPlaying = currentlyPlaying;
        this.onAirSince = onAirSince;
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private Long songCount;
        private Integer queueLength;
        private Song currentlyPlaying;
        private String onAirSince;

        private Builder() {
            this.songCount = 0L;
            this.queueLength = 0;
            this.currentlyPlaying = null;
            this.onAirSince = null;
        }

        Builder withSongCount(long songCount) {
            this.songCount = songCount;
            return this;
        }

        Builder withQueueLength(int queueLength) {
            this.queueLength = queueLength;
            return this;
        }

        Builder withCurrentlyPlaying(Song song) {
            this.currentlyPlaying = song;
            return this;
        }

        Builder withOnAirSince(String onAirSince) {
            this.onAirSince = onAirSince;
            return this;
        }

        Status build() {
            return new Status(songCount, queueLength, currentlyPlaying, onAirSince);
        }
    }
}
