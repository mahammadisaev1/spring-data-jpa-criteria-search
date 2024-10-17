package com.demo.springdatajpacriteriasearch.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Address {

    @Id
    @GeneratedValue
    private Integer id;

    private String streetName;

    private String houseNumber;

    private String zipCode;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
