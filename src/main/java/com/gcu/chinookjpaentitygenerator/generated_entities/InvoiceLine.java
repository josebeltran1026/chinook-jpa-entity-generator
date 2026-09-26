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
@Table(name = "invoice_line")
public class InvoiceLine {

    @Id
    @NotNull
    @Column(name = "invoice_line_id")
    private Integer invoiceLineId;

    @NotNull
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @NotNull
    @Column(name = "track_id")
    private Integer trackId;

    @NotNull
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

}
