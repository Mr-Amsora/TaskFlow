package com.ammar.taskflow.repository;

import com.ammar.taskflow.domain.Reminder;
import com.ammar.taskflow.domain.Task;
import jakarta.persistence.EntityManager;

public class ReminderRepository extends Repository<Reminder,Long>{
    public ReminderRepository(EntityManager entityManager) {
        super(entityManager, Reminder.class);
    }

    public Reminder findByTask(Task task) {
        return findById(task.getId());
    }
}
