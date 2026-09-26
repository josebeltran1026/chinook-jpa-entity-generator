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
@Table(name = "album")
public class Album {

    @Id
    @NotNull
    @Column(name = "album_id")
    private Integer albumId;

    @NotNull
    @Size(max = 160)
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "artist_id")
    private Integer artistId;

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "artist_id", insertable = false, updatable = false)
    private Artist artist;

}
