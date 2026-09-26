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
@Table(name = "track")
public class Track {

    @Id
    @NotNull
    @Column(name = "track_id")
    private Integer trackId;

    @NotNull
    @Size(max = 200)
    @Column(name = "name")
    private String name;

    @Column(name = "album_id")
    private Integer albumId;

    @NotNull
    @Column(name = "media_type_id")
    private Integer mediaTypeId;

    @Column(name = "genre_id")
    private Integer genreId;

    @Size(max = 220)
    @Column(name = "composer")
    private String composer;

    @NotNull
    @Column(name = "milliseconds")
    private Integer milliseconds;

    @Column(name = "bytes")
    private Integer bytes;

    @NotNull
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

}
