package com.ammar.taskflow;

import com.ammar.taskflow.cli.TaskFlowCLI;
import com.ammar.taskflow.config.ConnectionManager;
import com.ammar.taskflow.event.*;
import com.ammar.taskflow.repository.ReminderRepository;
import com.ammar.taskflow.repository.TaskRepository;
import com.ammar.taskflow.repository.UserRepository;
import com.ammar.taskflow.service.*;
import jakarta.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {

        EntityManager em = ConnectionManager.getInstance().getEntityManager();

        UserRepository userRepository = new UserRepository(em);
        TaskRepository taskRepository = new TaskRepository(em);
        ReminderRepository reminderRepository = new ReminderRepository(em);

        TaskEventPublisher publisher = new TaskEventPublisher();
        publisher.subscribe(new LoggingObserver());
        publisher.subscribe(new ReminderTriggerObserver(reminderRepository));
        publisher.subscribe(new TaskStatsObserver());

        UserService userService = new UserService(userRepository);
        TaskService taskService = new TaskService(taskRepository, publisher);
        ReminderService reminderService = new ReminderService(reminderRepository);
        TaskReportService taskReportService = new TaskReportService(taskRepository);
        TaskCacheService taskCacheService = new TaskCacheService(taskRepository);

        OverdueTaskScannerService scanner = new OverdueTaskScannerService(
                ConnectionManager.getInstance().getEntityManagerFactory(), publisher);
        scanner.start();

        TaskFlowCLI cli = new TaskFlowCLI(userService, taskService, reminderService, taskReportService, taskCacheService);
        cli.start();

        scanner.stop();
        em.close();
        ConnectionManager.getInstance().close();
    }
}