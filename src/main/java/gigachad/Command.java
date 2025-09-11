package gigachad;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import gigachad.exception.GigachadException;
import gigachad.task.Deadline;
import gigachad.task.Event;
import gigachad.task.Task;
import gigachad.task.ToDo;


/**
 * Represents a command that can be executed in the gigachad application.
 * This class parses and executes various user commands for task management, including
 * adding, deleting, marking, and listing tasks.
 */
public class Command {
    private final String command;
    private final String[] parts;
    private final String rawInput;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Constructs a Command with the specified parameters.
     *
     * @param type the command type
     * @param rawInput the raw user input
     * @param parts the parsed command parts
     */
    public Command(String type, String rawInput, String[] parts) {
        this.command = type;
        this.rawInput = rawInput;
        this.parts = parts;
    }

    public String getType() {
        return this.command;
    }

    public String[] getParts() {
        return this.parts;
    }

    public String getRawInput() {
        return this.rawInput;
    }

    /**
     * Executes this command using provided TaskList, UI and Storage.
     * Performs different operations based on command type, such as adding tasks,
     * marking tasks as done/undone, deleting tasks, or listing all tasks.
     *
     * @param listOfTasks the TaskList containing tasks to be saved to storage
     * @param ui the user interface for displaying messages
     * @param storage the storage system for persisting tasks
     * @throws GigachadException if the command execution fails due to invalid input or other errors
     */
    public String execute(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        StringBuilder response = new StringBuilder();

        switch (this.command) {
        case "list":
            if (listOfTasks.isEmpty()) {
                throw new GigachadException("Empty list!");
            }

            response.append(ui.listTasks(listOfTasks));
            break;
        case "find":
            if (listOfTasks.isEmpty()) {
                throw new GigachadException("Empty list! Nothing can be deleted.");
            }

            String[] rawInputParts = rawInput.split(" ", 2);
            assert rawInputParts.length >= 2 : "x should be more than 2";

            if (rawInputParts.length >= 2) {
                String taskSubstring = rawInputParts[1];
                ArrayList<Task> tasksMatchingSubstring = new ArrayList<>();

                for (Task task : listOfTasks.allTasks()) {
                    if (task.toString().contains(taskSubstring)) {
                        tasksMatchingSubstring.add(task);
                    }
                }

                response.append(ui.findTasks(new TaskList(tasksMatchingSubstring)));
            } else {
                throw new GigachadException("Input keyword to match!");
            }
            break;
        case "delete":
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
                    response.append(ui.deleteTask(removedTask, listOfTasks));
                    storage.saveToStorage(listOfTasks);
                }
            }
            break;
        case "mark":
            if (parts.length == 2) {
                int taskNumber = Integer.parseInt(parts[1]) - 1;
                if (taskNumber >= listOfTasks.size()) {
                    throw new GigachadException("Invalid task number!  You only have " + listOfTasks.size()
                            + " tasks.");
                } else {
                    Task task = listOfTasks.getTask(taskNumber);
                    response.append(ui.markTask(task));
                    task.markAsDone();
                    storage.saveToStorage(listOfTasks);
                }
            } else {
                throw new GigachadException("Invalid usage! Usage: mark <int>");
            }
            break;
        case "unmark":
            if (parts.length == 2) {
                int taskNumber = Integer.parseInt(parts[1]) - 1;
                if (taskNumber >= listOfTasks.size()) {
                    throw new GigachadException("Invalid task number!  You only have " + listOfTasks.size()
                            + " tasks.");
                } else {
                    Task task = listOfTasks.getTask(taskNumber);
                    response.append(ui.unmarkTask(task));
                    task.unmark();
                    storage.saveToStorage(listOfTasks);
                }
            } else {
                throw new GigachadException("Invalid usage! Usage: mark <int>");
            }
            break;
        case "todo":
            if (parts.length >= 2 && rawInput.length() > 4) {
                String todoDescription = rawInput.substring(5); // skip "todo "
                ToDo todo = new ToDo(todoDescription);
                listOfTasks.addTask(todo);
                storage.saveToStorage(listOfTasks);

                response.append(ui.addTask(todo, listOfTasks));
            } else {
                throw new GigachadException("Invalid usage! Usage: todo <task>");
            }
            break;
        case "deadline":
            try {
                int firstSpaceDeadline = rawInput.indexOf(' ');
                int byIndexDeadline = rawInput.indexOf('/');
                if (firstSpaceDeadline == -1 || byIndexDeadline == -1
                        || byIndexDeadline <= firstSpaceDeadline) {
                    throw new GigachadException("Invalid usage! Usage: deadline <task> /by <due date/time>. "
                            + "Format for the date is: yyyy-MM-dd HHmm");
                }

                String deadlineDescription = rawInput.substring(firstSpaceDeadline + 1, byIndexDeadline).trim();
                String deadlineDueDate = rawInput.substring(byIndexDeadline + 4).trim();

                if (deadlineDescription.isEmpty() || deadlineDueDate.isEmpty()) {
                    throw new GigachadException("Invalid usage! gigachad.task.Task description or datetime missing.");
                }

                Deadline deadline = new Deadline(deadlineDescription,
                        LocalDateTime.parse(deadlineDueDate, formatter));
                listOfTasks.addTask(deadline);
                storage.saveToStorage(listOfTasks);

                response.append(ui.addTask(deadline, listOfTasks));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid usage! Format for the date is: yyyy-MM-dd HHmm");
            }
            break;
        case "event":
            try {
                int firstSpace = rawInput.indexOf(' ');
                int fromIndexEvent = rawInput.indexOf(" /from");
                int toIndexEvent = rawInput.indexOf(" /to");

                if (firstSpace == -1 || fromIndexEvent == -1 || toIndexEvent == -1) {
                    throw new GigachadException("Invalid usage! Usage: event <task> /from <beginning date> "
                            + "/to <end date>. Format for the date is: yyyy-MM-dd HHmm");
                }

                String eventDescription = rawInput.substring(firstSpace + 1, fromIndexEvent).trim();
                String from = rawInput.substring(fromIndexEvent + 7, toIndexEvent).trim();
                String to = rawInput.substring(toIndexEvent + 5).trim();

                if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    throw new GigachadException("Invalid usage! gigachad.task.Task description or date missing.");
                }

                Event event = new Event(eventDescription,
                        LocalDateTime.parse(from, formatter),
                        LocalDateTime.parse(to, formatter));
                listOfTasks.addTask(event);
                storage.saveToStorage(listOfTasks);

                response.append(ui.addTask(event, listOfTasks));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid usage! Format for the date is: yyyy-MM-dd HHmm");
            }
            break;
        case "bye":
            response.append(ui.goodbyeUser());
            break;
        default:
            response.append(ui.invalidCommand());
        }
        return response.toString();
    }
}
