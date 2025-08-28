import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Gigachad {
    public static void main(String[] args) {
        // init scanner
        Scanner scanner = new Scanner(System.in);

        // universal path
        Path filePath = Paths.get("data/tasks.txt");

        // init Storage, Ui
        Storage storage = new Storage(filePath);
        Ui ui = new Ui();

        ui.welcomeUser();

        // init arraylist to store tasks
        ArrayList<Task> listOfTasks = storage.initStorage();

        // ask for user input
        String command = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        while (!command.equals("bye")) {
            command = ui.readCommand();
            String[] parts = command.split(" ");
            String firstWord = parts[0].toLowerCase();

            switch (firstWord) {
            case "list":
                try {
                    if (listOfTasks.isEmpty()) {
                        throw new GigachadException("Empty list!");
                    }

                    ui.listTasks(listOfTasks);
                } catch (GigachadException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "delete":
                try {
                    if (listOfTasks.isEmpty()) {
                        throw new GigachadException("Empty list! Nothing can be deleted.");
                    }

                    if (parts.length == 2) {
                        int taskNumber = Integer.parseInt(parts[1]) - 1;
                        if (taskNumber >= listOfTasks.size()) {
                            throw new GigachadException("Invalid task number! You only have " + listOfTasks.size()
                                    + " tasks.");
                        } else {
                            Task removedTask = listOfTasks.remove(taskNumber);
                            storage.saveToStorage(listOfTasks);
                            ui.deleteTask(removedTask, listOfTasks);
                        }
                    }
                } catch (GigachadException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "mark":
                try {
                    if (parts.length == 2) {
                        int taskNumber = Integer.parseInt(parts[1]) - 1;
                        if (taskNumber >= listOfTasks.size()) {
                            throw new GigachadException("Invalid task number!  You only have " + listOfTasks.size()
                                    + " tasks.");
                        } else {
                            Task task = listOfTasks.get(taskNumber);
                            task.markAsDone();
                            storage.saveToStorage(listOfTasks);
                            ui.markTask(task);
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
                        if (taskNumber >= listOfTasks.size()) {
                            throw new GigachadException("Invalid task number!  You only have " + listOfTasks.size()
                                    + " tasks.");
                        } else {
                            Task task = listOfTasks.get(taskNumber);
                            listOfTasks.get(taskNumber).unmark();
                            storage.saveToStorage(listOfTasks);
                            ui.unmarkTask(task);
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
                    if (parts.length >= 2 && command.length() > 4) {
                        String todoDescription = command.substring(5); // skip "todo "
                        ToDo todo = new ToDo(todoDescription);
                        listOfTasks.add(todo);
                        storage.saveToStorage(listOfTasks);

                        ui.addTask(todo, listOfTasks);
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
                        throw new GigachadException("Invalid usage! Usage: deadline <task> /by <due date/time>. " +
                                "Format for the date is: yyyy-MM-dd HHmm");
                    }

                    String deadlineDescription = command.substring(firstSpaceDeadline + 1, byIndexDeadline).trim();
                    String deadlineDueDate = command.substring(byIndexDeadline + 4).trim();

                    if (deadlineDescription.isEmpty() || deadlineDueDate.isEmpty()) {
                        throw new GigachadException("Invalid usage! Task description or datetime missing.");
                    }

                    Deadline deadline = new Deadline(deadlineDescription,
                            LocalDateTime.parse(deadlineDueDate, formatter));
                    listOfTasks.add(deadline);
                    storage.saveToStorage(listOfTasks);

                    ui.addTask(deadline, listOfTasks);
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
                                "/to <end date>. Format for the date is: yyyy-MM-dd HHmm");
                    }

                    String eventDescription = command.substring(firstSpace + 1, fromIndexEvent).trim();
                    String from = command.substring(fromIndexEvent + 7, toIndexEvent).trim();
                    String to = command.substring(toIndexEvent + 5).trim();

                    if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
                        throw new GigachadException("Invalid usage! Task description or date missing.");
                    }

                    Event event = new Event(eventDescription,
                            LocalDateTime.parse(from, formatter),
                            LocalDateTime.parse(to, formatter));
                    listOfTasks.add(event);
                    storage.saveToStorage(listOfTasks);

                    ui.addTask(event, listOfTasks);
                } catch (GigachadException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "bye":
                ui.goodbyeUser();
                break;
            default:
                ui.invalidCommand();
            }
        }
    }
}
