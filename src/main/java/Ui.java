import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    protected Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("---------------------------------");
    }

    public void welcomeUser() {
        showLine();
        System.out.println("Hello! I'm Gigachad");
        System.out.println("What can I do for you?");
        showLine();
    }

    public void goodbyeUser() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
        scanner.close();
    }

    public void markTask(Task task) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
        showLine();
    }

    public void unmarkTask(Task task) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
        showLine();
    }

    public void addTask(Task task, ArrayList<Task> listOfTasks) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
        showLine();
    }

    public void deleteTask(Task task, ArrayList<Task> listOfTasks) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
        showLine();
    }

    public void listTasks(ArrayList<Task> listOfTasks) {
        for (int i = 0; i < listOfTasks.size(); i++) {
            System.out.println((i + 1) + ". " + listOfTasks.get(i));
        }
    }

    public void invalidCommand() {
        System.out.println("Invalid command! To add tasks, use todo, deadline or event. " +
                "To mark or unmark tasks as done or undone, use mark <taskNumber> or unmark <taskNumber>" +
                "To exit, use bye.");
    }
}
