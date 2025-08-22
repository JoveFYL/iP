import java.util.ArrayList;
import java.util.Scanner;

public class Gigachad {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from");
        System.out.println(logo);
        System.out.println("Hello! I'm Gigachad");
        System.out.println("What can I do for you?");

        // init scanner
        Scanner scanner = new Scanner(System.in);

        // init arraylist to store tasks
        ArrayList<Task> listOfTasks = new ArrayList<>();

        // ask for user input
        String command = "";

        while (!command.equals("bye")) {
            command = scanner.nextLine();
            String[] parts = command.split(" ");
            String firstWord = parts[0].toLowerCase();

            switch (firstWord) {
                case "list":
                    try {
                        if (listOfTasks.isEmpty()) {
                            throw new GigachadException("Empty list!");
                        }

                        for (int i = 0; i < listOfTasks.size(); i++) {
                            System.out.println((i + 1) + ". " + listOfTasks.get(i));
                        }
                    } catch (GigachadException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "mark":
                    try {
                        if (parts.length == 2) {
                            int taskNumber = Integer.parseInt(parts[1]) - 1;
                            if (taskNumber > listOfTasks.size()) {
                                System.out.println("Invalid task!");
                            } else {
                                listOfTasks.get(taskNumber).markAsDone();
                            }
                        } else {
                            throw new GigachadException("Invalid usage! Usage: mark <int>");
                        }
                    } catch (GigachadException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "unmark":
                    try {
                        if (parts.length == 2) {
                            int taskNumber = Integer.parseInt(parts[1]) - 1;
                            if (taskNumber > listOfTasks.size()) {
                                System.out.println("Invalid task!");
                            } else {
                                listOfTasks.get(taskNumber).unmark();
                            }
                        } else {
                            throw new GigachadException("Invalid usage! Usage: mark <int>");
                        }
                    } catch (GigachadException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "todo":
                    try {
                        if (parts.length < 2 && command.length() > 4) {
                            String todoDescription = command.substring(5); // skip "todo "
                            ToDo todo = new ToDo(todoDescription);
                            listOfTasks.add(todo);

                            System.out.println("Got it. I've added this task:");
                            System.out.println("  " + todo);
                            System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
                        } else {
                            throw new GigachadException("Invalid usage! Usage: todo <task>");
                        }
                    } catch (GigachadException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "deadline":
                    try {
                        int firstSpaceDeadline = command.indexOf(' ');
                        int byIndexDeadline = command.indexOf('/');
                        if (firstSpaceDeadline == -1 || byIndexDeadline == -1
                                || byIndexDeadline <= firstSpaceDeadline) {
                            throw new GigachadException("Invalid usage! Usage: deadline <task> /by <due date/time>");
                        }

                        String deadlineDescription = command.substring(firstSpaceDeadline + 1, byIndexDeadline).trim();
                        String deadlineDueDate = command.substring(byIndexDeadline + 4).trim();

                        if (deadlineDescription.isEmpty() || deadlineDueDate.isEmpty()) {
                            throw new GigachadException("Invalid usage! Task description or date missing.");
                        }

                        Deadline deadline = new Deadline(deadlineDescription, deadlineDueDate);
                        listOfTasks.add(deadline);

                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + deadline);
                        System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
                    } catch (GigachadException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "event":
                    try {
                        int firstSpace = command.indexOf(' ');
                        int fromIndexEvent = command.indexOf(" /from");
                        int toIndexEvent = command.indexOf(" /to");

                        if (firstSpace == -1 || fromIndexEvent == -1 || toIndexEvent == -1) {
                            throw new GigachadException("Invalid usage! Usage: event <task> /from <beginning date> " +
                                    "/to <end date");
                        }

                        String eventDescription = command.substring(firstSpace + 1, fromIndexEvent).trim();
                        String from = command.substring(fromIndexEvent + 7, toIndexEvent).trim();
                        String to = command.substring(toIndexEvent + 5).trim();

                        if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
                            throw new GigachadException("Invalid usage! Task description or date missing.");
                        }

                        Event event = new Event(eventDescription, from, to);
                        listOfTasks.add(event);

                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + event);
                        System.out.println("Now you have " + listOfTasks.size() + " tasks in the list.");
                    } catch (GigachadException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "bye":
                    System.out.println("Bye. Hope to see you again soon!");
                    scanner.close();
                    break;
                default:
                    System.out.println("Invalid command! To add tasks, use todo, deadline or event. " +
                            "To mark or unmark tasks as done or undone, use mark <taskNumber> or unmark <taskNumber>" +
                            "To exit, use bye.");
            }
        }
    }
}
