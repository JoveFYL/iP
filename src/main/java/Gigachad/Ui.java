package Gigachad;

import Gigachad.task.Task;

import java.util.Scanner;

/**
 * Handles all user interface interactions for the Gigachad application.
 * This class is responsible for displaying messages to the user and reading input commands.
 * It provides methods for various UI operations like welcoming the user, displaying tasks,
 * and showing operation results.
 */
public class Ui {
    protected Scanner scanner;

    /**
     * Constructs a new Ui instance and initialises the scanner for reading user input using keyboard.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a command from the user via standard input.
     *
     * @return the command string entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a visual divider to format output.
     */
    public void showLine() {
        System.out.println("---------------------------------");
    }

    /**
     * Displays welcome message to user when chatbot starts up.
     */
    public void welcomeUser() {
        showLine();
        System.out.println("Hello! I'm Gigachad");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Displays goodbye message to user when user exits application.
     * Closes scanner to free up resources.
     */
    public void goodbyeUser() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
        scanner.close();
    }

    /**
     * Displays message indicating that task has been marked complete.
     *
     * @param task the task that was marked complete
     */
    public void markTask(Task task) {
        showLine();
        if (!task.getIsDone()) {
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(task);
        } else {
            System.out.println("I already marked this task as done!");
        }
        showLine();
    }

    /**
     * Displays message indicating that task has been marked incomplete.
     * @param task the task that was marked incomplete
     */
    public void unmarkTask(Task task) {
        showLine();
        if (task.getIsDone()) {
            System.out.println("Nice! I've marked this task as undone:");
            System.out.println(task);
        } else {
            System.out.println("I already marked this task as undone!");
        }
        showLine();
    }

    /**
     * Displays a message confirming that a task has been added to the task list.
     * @param task the task that was added
     * @param listOfTasks the current task list containing all the tasks
     */
    public void addTask(Task task, TaskList listOfTasks) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a message confirming that a task has been removed from the task list.
     * @param task the task that was removed
     * @param listOfTasks the current task list containing all the tasks
     */
    public void deleteTask(Task task, TaskList listOfTasks) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
        showLine();
    }

    /**
     * Displays all tasks in the task list with their corresponding numbers.
     * @param listOfTasks the current task list containing all the tasks
     */
    public void listTasks(TaskList listOfTasks) {
        for (int i = 0; i < listOfTasks.size(); i++) {
            System.out.println((i + 1) + ". " + listOfTasks.getTask(i));
        }
    }

    /**
     * Displays an error message when an invalid command is entered by user.
     * Provides the user with guidance on the correct command format.
     */
    public void invalidCommand() {
        System.out.println("Invalid command! To add tasks, use todo, deadline or event. " +
                "To mark or unmark tasks as done or undone, use mark <taskNumber> or unmark <taskNumber>" +
                "To exit, use bye.");
    }
}
