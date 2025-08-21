import java.util.ArrayList;
import java.util.Scanner;

public class Gigachad {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello! I'm Gigachad");
        System.out.println("What can I do for you?");
        System.out.println("Bye. Hope to see you again soon!");

        // init scanner
        Scanner scanner = new Scanner(System.in);

        // init arraylist to store tasks
        ArrayList<Task> listOfTasks = new ArrayList<>();

        // ask for user input
        System.out.println("Enter a command: ");
        String command = "";

        while (!command.equals("bye")) {
            command = scanner.nextLine();

            if (command.equals("list")) {
                for (int i = 0; i < listOfTasks.size(); i++) {
                    System.out.println((i + 1) + ". " + listOfTasks.get(i));
                }
            } else if (command.startsWith("mark")) {
                String[] parts = command.split(" ");
                if (parts.length == 2) {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber > listOfTasks.size()) {
                        System.out.println("Invalid task!");
                    } else {
                        listOfTasks.get(taskNumber).markAsDone();
                    }
                }
            } else if (command.startsWith("unmark")) {
                String[] parts = command.split(" ");
                if (parts.length == 2) {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber > listOfTasks.size()) {
                        System.out.println("Invalid task!");
                    } else {
                        listOfTasks.get(taskNumber).unmark();
                    }
                }
            } else {
                Task newTask = new Task(command);
                listOfTasks.add(newTask);
                System.out.println("added: " + newTask.getDescription());
            }
        }

        System.out.println("Bye. Hope to see you again soon!");
        scanner.close();
    }
}
