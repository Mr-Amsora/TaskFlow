package com.ammar.taskflow;

import com.ammar.taskflow.config.ConnectionManager;
import com.ammar.taskflow.domain.*;
import com.ammar.taskflow.event.*;
import com.ammar.taskflow.repository.ReminderRepository;
import com.ammar.taskflow.repository.TaskRepository;
import com.ammar.taskflow.repository.UserRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        EntityManager entityManager = ConnectionManager.getInstance().getEntityManager();

        UserRepository userRepository = new UserRepository(entityManager);
        TaskRepository taskRepository = new TaskRepository(entityManager);
        ReminderRepository reminderRepository = new ReminderRepository(entityManager);

        User user = userRepository.save(new User("Ammar", "ammar@example.com"));
        System.out.println("Saved user id: " + user.getId());

        Task task = taskRepository.save(
                new Task(user, "Finish TaskFlow", LocalDateTime.now().plusDays(1), Priority.HIGH)
        );
        System.out.println("Saved task id: " + task.getId());

        reminderRepository.save(
                new Reminder(task, LocalDateTime.now().plusHours(1), DeliveryChannel.EMAIL)
        );
        System.out.println("Saved reminder for task id: " + task.getId());

        TaskEventPublisher publisher = new TaskEventPublisher();
        TaskStatsObserver stats = new TaskStatsObserver();

        publisher.subscribe(new LoggingObserver());
        publisher.subscribe(stats);
        publisher.subscribe(new ReminderTriggerObserver(reminderRepository));

        publisher.publish(new TaskCreatedEvent(task));
        publisher.publish(new TaskCompletedEvent(task));

        System.out.println("Completed count: " + stats.getTotalCompletedTasks());

        entityManager.close();
        ConnectionManager.getInstance().close();
    }
}