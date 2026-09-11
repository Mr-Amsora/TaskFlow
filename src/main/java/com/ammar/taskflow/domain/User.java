package com.ammar.taskflow.domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(unique = false , nullable = false)
    String name;

    @Column(unique = true , nullable = false)
    String email;


}