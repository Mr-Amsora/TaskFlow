package com.ammar.taskflow.service;

import com.ammar.taskflow.domain.Status;
import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.event.TaskEventPublisher;
import com.ammar.taskflow.event.TaskOverdueEvent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OverdueTaskScannerService {

    private final EntityManagerFactory entityManagerFactory;
    private final TaskEventPublisher publisher;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public OverdueTaskScannerService(EntityManagerFactory emf, TaskEventPublisher publisher) {
        this.entityManagerFactory = emf;
        this.publisher = publisher;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::scan, 0, 1, TimeUnit.MINUTES);
    }

    private synchronized void scan() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String query = "SELECT t FROM Task t WHERE t.dueDate < :now AND t.status <> :done AND t.status <> :overdue";
            List<Task> overdueTasks = entityManager.createQuery(query, Task.class)
                    .setParameter("now", LocalDateTime.now())
                    .setParameter("done", Status.DONE)
                    .setParameter("overdue", Status.OVERDUE)
                    .getResultList();

            for (Task task : overdueTasks) {
                entityManager.getTransaction().begin();
                task.setAsOverdue();
                entityManager.merge(task);
                entityManager.getTransaction().commit();
                publisher.publish(new TaskOverdueEvent(task));
                System.out.println("[Scanner] Task marked overdue: " + task.getTitle());
            }
        } catch (Exception e) {
            System.out.println("[Scanner] Error during overdue scan: " + e.getMessage());
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            entityManager.close();
        }
    }

    public void stop() {
        scheduler.shutdown();
    }
}