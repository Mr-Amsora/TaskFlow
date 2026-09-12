package com.ammar.taskflow.domain;

import jakarta.persistence.*;


@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true , nullable = false)
    private String email;

    public User(){}

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public Long getId() { return id; }
    public String getEmail() {
        return email;
    }

}