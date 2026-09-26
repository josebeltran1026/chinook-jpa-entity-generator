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
@Table(name = "media_type")
public class MediaType {

    @Id
    @NotNull
    @Column(name = "media_type_id")
    private Integer mediaTypeId;

    @Size(max = 120)
    @Column(name = "name")
    private String name;

}
