package gigachad;

import java.util.Scanner;

import gigachad.task.Task;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

/**
 * Handles all user interface interactions for the gigachad application.
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
    public String welcomeUser() {
        return "Hello! I'm gigachad!\n What can I do for you?";
    }

    /**
     * Displays goodbye message to user when user exits application.
     * Closes scanner to free up resources.
     */
    public String goodbyeUser() {
        scanner.close();
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Displays message indicating that task has been marked complete.
     *
     * @param task the task that was marked complete
     */
    public String markTask(Task task) {
        if (!task.getIsDone()) {
            return "Nice! I've marked this task as done:\n" + task;
        } else {
            return "I already marked this task as done!";
        }
    }

    /**
     * Displays message indicating that task has been marked incomplete.
     * @param task the task that was marked incomplete
     */
    public String unmarkTask(Task task) {
        if (task.getIsDone()) {
            return "Nice! I've marked this task as undone:\n" + task;
        } else {
            return "I already marked this task as undone!";
        }
    }

    /**
     * Displays a message confirming that a task has been added to the task list.
     * @param task the task that was added
     * @param listOfTasks the current task list containing all the tasks
     */
    public String addTask(Task task, TaskList listOfTasks) {
        return "Got it. I've added this task:\n" + "  " + task + "\n"
            + "Now you have " + listOfTasks.size() + " tasks in the list.";
    }

    /**
     * Displays a message confirming that a task has been removed from the task list.
     * @param task the task that was removed
     * @param listOfTasks the current task list containing all the tasks
     */
    public String deleteTask(Task task, TaskList listOfTasks) {
        return "Noted. I've removed this task:\n" + "  " + task + "\n"
            + "Now you have " + listOfTasks.size() + " tasks in the list.";
    }

    /**
     * Displays all tasks in the task list with their corresponding numbers.
     * @param listOfTasks the current task list containing all the tasks
     */
    public String listTasks(TaskList listOfTasks) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < listOfTasks.size(); i++) {
            stringBuilder.append((i + 1)).append(". ").append(listOfTasks.getTask(i)).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Displays all tasks in the task list matching user query.
     * @param listOfTasks the current task list containing all the tasks
     */
    public String findTasks(TaskList listOfTasks) {
        return "Here are the matching tasks in your list:\n" + this.listTasks(listOfTasks);
    }

    /**
     * Displays an error message when an invalid command is entered by user.
     * Provides the user with guidance on the correct command format.
     */
    public String invalidCommand() {
        return "Invalid command! To add tasks, use todo, deadline or event. "
                + "To mark or unmark tasks as done or undone, use mark <taskNumber> or unmark <taskNumber>"
                + "To exit, use bye.";
    }
}
