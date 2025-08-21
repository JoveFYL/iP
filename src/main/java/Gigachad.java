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
            String[] parts = command.split(" ");
            String firstWord = parts[0].toLowerCase();

            switch (firstWord) {
                case "list":
                    for (int i = 0; i < listOfTasks.size(); i++) {
                        System.out.println((i + 1) + ". " + listOfTasks.get(i));
                    }
                    break;
                case "mark":
                    if (parts.length == 2) {
                        int taskNumber = Integer.parseInt(parts[1]) - 1;
                        if (taskNumber > listOfTasks.size()) {
                            System.out.println("Invalid task!");
                        } else {
                            listOfTasks.get(taskNumber).markAsDone();
                        }
                    }
                    break;
                case "unmark":
                    if (parts.length == 2) {
                        int taskNumber = Integer.parseInt(parts[1]) - 1;
                        if (taskNumber > listOfTasks.size()) {
                            System.out.println("Invalid task!");
                        } else {
                            listOfTasks.get(taskNumber).unmark();
                        }
                    }
                    break;
                case "todo":
                    String todoDescription = command.substring(5); // skip "todo "
                    ToDo todo = new ToDo(todoDescription);
                    listOfTasks.add(todo);

                    System.out.println("Got it. I've added this task:");
                    System.out.println(todo);
                    System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
                    break;
                case "deadline":
                    int firstDeadlineSpace = command.indexOf(' ');
                    int byIndexDeadline = command.indexOf('/');

                    String deadlineDescription = command.substring(firstDeadlineSpace + 1, byIndexDeadline).trim();
                    String deadlineDueDate = command.substring(byIndexDeadline + 4).trim();

                    Deadline deadline = new Deadline(deadlineDescription, deadlineDueDate);
                    listOfTasks.add(deadline);

                    System.out.println("Got it. I've added this task:");
                    System.out.println(deadline);
                    System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
                    break;
                case "event":
                    int firstSpace = command.indexOf(' ');
                    int fromIndexEvent = command.indexOf(" /from");
                    int toIndexEvent = command.indexOf(" /to");

                    String eventDescription = command.substring(firstSpace + 1, fromIndexEvent).trim();
                    String from = command.substring(fromIndexEvent + 7, toIndexEvent).trim();
                    String to = command.substring(toIndexEvent + 5).trim();

                    Event event = new Event(eventDescription, from, to);
                    listOfTasks.add(event);

                    System.out.println("Got it. I've added this task:");
                    System.out.println(event);
                    System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
                    break;
                case "bye":
                    System.out.println("Bye. Hope to see you again soon!");
                    scanner.close();
                    break;
            }
        }
    }
}
