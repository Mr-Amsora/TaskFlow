package com.ammar.taskflow.algorithm;

import com.ammar.taskflow.domain.Task;
import com.ammar.taskflow.exception.InvalidTaskPriorityException;

import java.util.List;

public class InsertionTaskSorter {


    //we sort the list using the insertion sort algorithm
    //the space complexity of the insertion sort algorithm is O(1) because it sorts the list
    //in place and does not require any additional data structures that grow with the input size.
    //the time complexity of the insertion sort algorithm is O(n^2) in the worst case,
    //This happens when the list is sorted in reverse order,
    //In the average and best cases, the time complexity is O(n^2) and O(n).
    //honestly I picked it because it is simple to implement and understand,
    //and it works well for small lists or nearly sorted lists.
    //and it does not use advance topic like divide and conquer or recursion.
    public static void sort(List<Task> tasks) {
        for (int i = 1; i < tasks.size(); i++) {
            Task currentTask = tasks.get(i);
            int j = i - 1;

            while (j >= 0 && isBefore(currentTask, tasks.get(j))) {
                tasks.set(j + 1, tasks.get(j));
                j--;
            }

            tasks.set(j + 1, currentTask);
        }
    }

    private static boolean isBefore(Task task1, Task task2) {
        if (task1.getDueDate().isBefore(task2.getDueDate())){
            return true;
        }else if (task1.getDueDate().isAfter(task2.getDueDate())) {
            return false;
        } else {
            return priorityValue(task1) < priorityValue(task2);
        }
    }

    private static int priorityValue(Task task) {
        return switch (task.getPriority()) {
            case HIGH ->  1;
            case MEDIUM -> 2;
            case LOW -> 3;
            default -> throw new InvalidTaskPriorityException("Invalid priority");
        };
    }
}
