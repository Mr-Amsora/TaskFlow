# TaskFlow
the taskflow project is a simple CLI project that allows you to make the
basic CRUD operations for a users, tasks and reminders and more.
I kept the default name "TaskFlow" for the project as the name you have mentioned in the task 
instructions.

# java & maven
I used java 26 as the default java version i used also maven to install
the JPA and Hibernate dependencies for the project.

# Setup & Run
Requirements: Java 26, Maven
No database setup needed. H2 creates the database file automatically in the /data folder on first run.
the project is a CLI that work on while(true) loop you will be first met with the main menu
from it u can select to open Users, tasks, reminders, reports CLI, or exit by entering the option number
then each of the sub CLI will have its own menu and you can perform the CRUD operations and more
by typing the number of the operation you want to perform and then follow the input instructions.

# Entities and Relationships
The project has three main entities: User, Task, and Reminder. 
The relationships between these entities are as follows:
     A User can have multiple Tasks (One to Many with cascade the User is the owner).
     A Task can have only one Reminder (One to One with cascade the task is the owner).

the columns for each entity are as follows:
- User: id, name, email
- Task: id, owner, title, dueDate, priority (LOW / MEDIUM / HIGH),
  created at, completed at, status (TODO / IN_PROGRESS / DONE / OVERDUE)
- Reminder: linkedTask, triggerTime, deliveryChannel(EMAIL / SMS / WHATSAPP / NOTIFICATION)

# JPA 
I used JPA and Hibernate instead of JDBC for this project.
I chose JPA because it provides a simple and easy to use API for working with relational databases.
It also allows me to use annotations to define the entity mappings, which makes the code more readable and maintainable.

# Validation & Exceptions
I created a small custom exception hierarchy for the project.
the super exception is TaskFlowException and the sub exceptions are:
- InvalidTaskStateException
- DuplicateUserException
- InvalidUserEmailException
- InvalidTaskPriorityException
- InvalidDeliveryChannelException

I also validated the email format using this regex: 
    ^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$

# Collections & Generics
I created a generic repository abstraction, Repository<T, ID>, which is used to perform CRUD operations
on the entities. I also created an in-memory cache of all tasks using a ArrayList.

# Hand-Rolled Algorithm
I implemented an insertion sort algorithm that work on O(n^2) for avg and worst time complexity
and O(n) for best time complexity to produce a "due soon" report, ordered by dueDate then Priority.
I picked it because it is simple to implement and understand,
and it works well for small lists or nearly sorted lists.
and it does not use hard to explain topics like divide and conquer or recursion.

# Streams & Lambdas
I used streams and lambdas in the project but i focused to use then in
TaskReportService class to generate reports for the tasks:
- userTaskCompletedThisWeek
- overdueTaskCountByPriority
- averageTimeToCompletion

# Design Patterns
I used the following design patterns in the project:
- Factory: I created a ReminderFactory that produces the 
    right delivery mechanism (Email/SMS/WHATSAPP/NOTIFICATION).
- Observer: I created an event system where task lifecycle events 
    (TaskCreated, TaskCompleted, TaskOverdue) are published and multiple independent observers react.
- Singelton: I created a ConnectionManager class that manages the database connection
    and ensures that only one instance of the connection is created.

# Extra Design Pattern — Template Method (Bonus)
I used the Template Method pattern in the Repository<T, ID> abstract class.
The base class defines the structure for all data access operations: save, findById,
update, findAll and deleteById. The concrete subclasses UserRepository, TaskRepository
and ReminderRepository inherit this structure and only add their own domain-specific
queries on top.

# Structure
i made 9 clear packages for the project:
- domain: contains the entities and their relationships.
- repository: contains the generic repository abstraction and the implementations for the entities.
- service: contains the services for the entities and the reports.
- reminder: contains the reminder factory and the delivery mechanisms.
- event: contains the event system and the observers.
- cli: contains the CLI for the project.
- config: contains the Connection Manager class.
- algorithm: contains the insertion sort algorithm.
- exception: contains the custom exceptions for the project.

# Multi-threading:
I created an OverdueTaskScannerService class that runs a background thread every minute
using ScheduledExecutorService. It scans for tasks where dueDate is before now and the status
is not DONE or OVERDUE, marks them as overdue, update the changes to the database,
and call a TaskOverdueEvent to all observers.
To protect against race conditions I did the following:
- OverdueTaskScannerService creates a fresh EntityManager per scan using the EntityManagerFactory,
  it never shares the main thread EntityManager since JPA EntityManager is not thread safe.
- The scan() method is synchronized to prevent two overlapping scans if one runs slow.
- TaskEventPublisher subscribe() and publish() are synchronized so the main thread and scanner
  thread do not conflict when publishing events.
- TaskStatsObserver onTaskEvent() and getTotalCompletedTasks() are synchronized to prevent
  a race on the completed tasks counter.
- TaskCacheService all methods are synchronized to prevent the scanner from reading
  while the main thread is refreshing the cache.
- ConnectionManager getInstance() is synchronized to prevent two instances being created.

# Judgment Call
I decide to implement the following: if the due date of a task is pushed back 
and the task has a reminder:
i make that when we update the due date for a task to check if it has a reminder
if it has one i will check if the reminder trigger time is after the new due date
if it is i will delete the reminder else i will leave the trigger time as it is
because i make it that u cant create a reminder with the reminder trigger time after the due date 
...what's the point.


