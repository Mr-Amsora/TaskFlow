package com.ammar.taskflow.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    public Task(){}

    public Task(User owner, String title, LocalDateTime dueDate, Priority priority){
        this.owner = owner;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = Status.TODO;
    }

    public UUID getId() { return id; }
    public User getOwner() { return owner; }
    public String getTitle() { return title; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }


}