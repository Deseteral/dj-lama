package xyz.deseteral.djlama.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    private final Integer status;
    private final String key;

    @JsonCreator
    Error(
        @JsonProperty("status") Integer status,
        @JsonProperty("key") String key
    ) {
        this.status = status;
        this.key = key;
    }

    public Integer getStatus() {
        return status;
    }

    public String getKey() {
        return key;
    }

    public static Builder builder(Error error) {
        return new Builder(error);
    }

    public static final class Builder {
        private Integer status;
        private String key;

        public Builder() {
            this.status = 200;
            this.key = "";
        }

        public Builder(Error error) {
            this.status = error.getStatus();
            this.key = error.getKey();
        }

        public Builder withStatus(Integer status) {
            this.status = status;
            return this;
        }

        public Builder withKey(String key) {
            this.key= key;
            return this;
        }

        public Error build() {
            return new Error(status, key);
        }
    }
}
