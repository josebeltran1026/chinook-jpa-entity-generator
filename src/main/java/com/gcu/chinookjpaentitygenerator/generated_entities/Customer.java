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
@Table(name = "customer")
public class Customer {

    @Id
    @NotNull
    @Column(name = "customer_id")
    private Integer customerId;

    @NotNull
    @Size(max = 40)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(max = 20)
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 80)
    @Column(name = "company")
    private String company;

    @Size(max = 70)
    @Column(name = "address")
    private String address;

    @Size(max = 40)
    @Column(name = "city")
    private String city;

    @Size(max = 40)
    @Column(name = "state")
    private String state;

    @Size(max = 40)
    @Column(name = "country")
    private String country;

    @Size(max = 10)
    @Column(name = "postal_code")
    private String postalCode;

    @Size(max = 24)
    @Column(name = "phone")
    private String phone;

    @Size(max = 24)
    @Column(name = "fax")
    private String fax;

    @NotNull
    @Size(max = 60)
    @Column(name = "email")
    private String email;

    @Column(name = "support_rep_id")
    private Integer supportRepId;

    @ManyToOne
    @JoinColumn(name = "support_rep_id", referencedColumnName = "employee_id", insertable = false, updatable = false)
    private Employee employee;

}
