package Gigachad;

import Gigachad.task.Task;

import java.util.ArrayList;

public class TaskList {
    protected ArrayList<Task> listOfTasks;

    public TaskList() {
        this.listOfTasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> listOfTasks) {
        this.listOfTasks = listOfTasks;
    }

    public void addTask(Task task) {
        listOfTasks.add(task);
    }

    public Task getTask(int index) {
        return listOfTasks.get(index);
    }

    public Task deleteTask(int index) {
        return listOfTasks.remove(index);
    }

    public boolean isEmpty() {
        return listOfTasks.isEmpty();
    }

    public ArrayList<Task> allTasks() {
        return this.listOfTasks;
    }

    public int size() {
        return listOfTasks.size();
    }
}
