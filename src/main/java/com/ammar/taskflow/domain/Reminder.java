package com.ammar.taskflow.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminder")
public class Reminder {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_task_id", nullable = false)
    private Task linkedTask;

    @Column(nullable = false)
    private LocalDateTime triggerTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryChannel deliveryChannel;

    public Reminder() {}

    public Reminder(Task linkedTask, LocalDateTime triggerTime, DeliveryChannel deliveryChannel) {
        this.linkedTask = linkedTask;
        this.triggerTime = triggerTime;
        this.deliveryChannel = deliveryChannel;
    }

    public Task getLinkedTask() { return linkedTask; }
    public LocalDateTime getTriggerTime() { return triggerTime; }
    public DeliveryChannel getDeliveryChannel() { return deliveryChannel; }

    public void updateDeliveryChannel(DeliveryChannel deliveryChannel) {this.deliveryChannel = deliveryChannel;}
    public void updateTriggerTime(LocalDateTime triggerTime) {this.triggerTime = triggerTime;}
}