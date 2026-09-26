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
@Table(name = "employee")
public class Employee {

    @Id
    @NotNull
    @Column(name = "employee_id")
    private Integer employeeId;

    @NotNull
    @Size(max = 20)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(max = 20)
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 30)
    @Column(name = "title")
    private String title;

    @Column(name = "reports_to")
    private Integer reportsTo;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

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

    @Size(max = 60)
    @Column(name = "email")
    private String email;

}
