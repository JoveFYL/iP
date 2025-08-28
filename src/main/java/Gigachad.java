import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Gigachad {
    private final Storage storage;
    private final TaskList listOfTasks;
    private final Ui ui;

    public Gigachad(Path filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.listOfTasks = new TaskList(storage.initStorage());
    }

    public void run() {
        ui.welcomeUser();

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
                            Task removedTask = listOfTasks.deleteTask(taskNumber);
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
                            Task task = listOfTasks.getTask(taskNumber);
                            ui.markTask(task);
                            task.markAsDone();
                            storage.saveToStorage(listOfTasks);
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
                            Task task = listOfTasks.getTask(taskNumber);
                            ui.unmarkTask(task);
                            task.unmark();
                            storage.saveToStorage(listOfTasks);
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
                        listOfTasks.addTask(todo);
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
                    listOfTasks.addTask(deadline);
                    storage.saveToStorage(listOfTasks);

                    ui.addTask(deadline, listOfTasks);
                } catch (GigachadException e) {
                    System.out.println(e.getMessage());
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid usage! Format for the date is: yyyy-MM-dd HHmm");
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
                    listOfTasks.addTask(event);
                    storage.saveToStorage(listOfTasks);

                    ui.addTask(event, listOfTasks);
                } catch (GigachadException e) {
                    System.out.println(e.getMessage());
                }  catch (DateTimeParseException e) {
                    System.out.println("Invalid usage! Format for the date is: yyyy-MM-dd HHmm");
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

    public static void main(String[] args) {
        new Gigachad(Paths.get("data/tasks.txt")).run();
    }
}
