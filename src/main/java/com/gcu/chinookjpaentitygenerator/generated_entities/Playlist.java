package com.gcu.chinookjpaentitygenerator.generated_entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist")
public class Playlist {

    @Id
    @NotNull
    @Column(name = "playlist_id")
    private Integer playlistId;

    @Size(max = 120)
    @Column(name = "name")
    private String name;

}
