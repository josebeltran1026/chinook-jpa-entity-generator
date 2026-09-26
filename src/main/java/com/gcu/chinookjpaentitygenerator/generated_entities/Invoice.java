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
@Table(name = "invoice")
public class Invoice {

    @Id
    @NotNull
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @NotNull
    @Column(name = "customer_id")
    private Integer customerId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @NotNull
    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;

    @Size(max = 70)
    @Column(name = "billing_address")
    private String billingAddress;

    @Size(max = 40)
    @Column(name = "billing_city")
    private String billingCity;

    @Size(max = 40)
    @Column(name = "billing_state")
    private String billingState;

    @Size(max = 40)
    @Column(name = "billing_country")
    private String billingCountry;

    @Size(max = 10)
    @Column(name = "billing_postal_code")
    private String billingPostalCode;

    @NotNull
    @Column(name = "total")
    private BigDecimal total;

}
