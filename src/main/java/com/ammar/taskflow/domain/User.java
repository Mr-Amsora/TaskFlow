package com.ammar.taskflow.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true , nullable = false)
    private String email;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

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
    public List<Task> getTasks() {return tasks;}

    public void updateEmail(String email) { this.email = email; }
    public void updateUserName(String name) { this.name = name; }
}