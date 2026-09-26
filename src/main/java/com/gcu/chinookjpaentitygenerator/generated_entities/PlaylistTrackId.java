package com.gcu.chinookjpaentitygenerator.generated_entities;

import java.io.Serializable;
import java.util.Objects;

public class PlaylistTrackId implements Serializable {

    private Integer playlistId;
    private Integer trackId;

    public PlaylistTrackId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof PlaylistTrackId that)) {
            return false;
        }

        return Objects.equals(playlistId, that.playlistId)
                && Objects.equals(trackId, that.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlistId, trackId);
    }
}
