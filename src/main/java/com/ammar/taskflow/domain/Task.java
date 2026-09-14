package com.ammar.taskflow.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime completedAt;

    @OneToOne(mappedBy = "linkedTask", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Reminder reminder;

    public Task(){}

    public Task(User owner, String title, LocalDateTime dueDate, Priority priority){
        this.owner = owner;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = Status.TODO;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public User getOwner() { return owner; }
    public String getTitle() { return title; }
    public LocalDateTime getDueDate() { return dueDate; }
    public Priority getPriority() { return priority; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public Reminder getReminder() {return reminder;}

    public void updateTitle(String title) {this.title = title;}
    public void updateDueDate(LocalDateTime dueDate) {this.dueDate = dueDate;}
    public void setAsDone() {
        this.status = Status.DONE;
        this.completedAt = LocalDateTime.now();
    }
    public void setAsInProgress() {this.status = Status.IN_PROGRESS;}
    public void setAsOverdue() {this.status = Status.OVERDUE;}
    public void updatePriority(Priority priority) {this.priority = priority;}
}