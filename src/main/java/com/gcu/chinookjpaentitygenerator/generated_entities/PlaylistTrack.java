package com.gcu.chinookjpaentitygenerator.generated_entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_track")
@IdClass(PlaylistTrackId.class)
public class PlaylistTrack {

    @Id
    @NotNull
    @Column(name = "playlist_id")
    private Integer playlistId;

    @ManyToOne
    @JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id", insertable = false, updatable = false)
    private Playlist playlist;

    @Id
    @NotNull
    @Column(name = "track_id")
    private Integer trackId;

    @ManyToOne
    @JoinColumn(name = "track_id", referencedColumnName = "track_id", insertable = false, updatable = false)
    private Track track;

}
