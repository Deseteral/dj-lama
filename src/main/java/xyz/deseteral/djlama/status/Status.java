package xyz.deseteral.djlama.status;

import com.fasterxml.jackson.annotation.JsonProperty;

class Status {
    @JsonProperty("songCount")
    private Long songCount;

    @JsonProperty("onAir")
    private Boolean onAir;

    Status(
        Long songCount,
        Boolean onAir
    ) {
        this.songCount = songCount;
        this.onAir = onAir;
    }

    static Builder builder() {
        return new Builder();
    }

    static final class Builder {
        private Long songCount;
        private Boolean onAir;

        private Builder() {
            this.songCount = 0L;
            this.onAir = false;
        }

        Builder withSongCount(long songCount) {
            this.songCount = songCount;
            return this;
        }

        Builder isOnAir(boolean onAir) {
            this.onAir = onAir;
            return this;
        }

        Status build() {
            return new Status(songCount, onAir);
        }
    }
}
