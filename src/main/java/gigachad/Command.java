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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final String DEADLINE_DELIMITER = "/by";
    private static final String EVENT_FROM = " /from";
    private static final String EVENT_TO = " /to";

    private final String command;
    private final String[] parts;
    private final String rawInput;

    /**
     * Command Constructor
     * @param type type of command
     * @param rawInput the full input string by the user
     * @param parts full input string split into parts and stored in an array
     */
    public Command(String type, String rawInput, String[] parts) {
        this.command = type;
        this.rawInput = rawInput;
        this.parts = parts;
    }

    public String execute(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        return switch (command) {
        case "list" -> handleList(listOfTasks, ui);
        case "find" -> handleFind(listOfTasks, ui);
        case "delete" -> handleDelete(listOfTasks, ui, storage);
        case "mark" -> handleMark(listOfTasks, ui, storage);
        case "unmark" -> handleUnmark(listOfTasks, ui, storage);
        case "todo" -> handleTodo(listOfTasks, ui, storage);
        case "deadline" -> handleDeadline(listOfTasks, ui, storage);
        case "event" -> handleEvent(listOfTasks, ui, storage);
        case "bye" -> ui.goodbyeUser();
        default -> ui.invalidCommand();
        };
    }

    // --- Handlers for each command ---

    private String handleList(TaskList listOfTasks, Ui ui) throws GigachadException {
        if (listOfTasks.isEmpty()) {
            throw new GigachadException("Empty list!");
        }
        return ui.listTasks(listOfTasks);
    }

    private String handleFind(TaskList listOfTasks, Ui ui) throws GigachadException {
        if (listOfTasks.isEmpty()) {
            throw new GigachadException("Empty list! Nothing can be found.");
        }
        if (parts.length < 2) {
            throw new GigachadException("Input keyword to match!");
        }

        String keyword = rawInput.split(" ", 2)[1];
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : listOfTasks.allTasks()) {
            if (task.toString().contains(keyword)) {
                matches.add(task);
            }
        }
        return ui.findTasks(new TaskList(matches));
    }

    private String handleDelete(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        int taskNumber = parseTaskNumber(listOfTasks);
        Task removedTask = listOfTasks.deleteTask(taskNumber);
        storage.saveToStorage(listOfTasks);
        return ui.deleteTask(removedTask, listOfTasks);
    }

    private String handleMark(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        int taskNumber = parseTaskNumber(listOfTasks);
        Task task = listOfTasks.getTask(taskNumber);
        storage.saveToStorage(listOfTasks);
        return ui.markTask(task);
    }

    private String handleUnmark(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        int taskNumber = parseTaskNumber(listOfTasks);
        Task task = listOfTasks.getTask(taskNumber);
        storage.saveToStorage(listOfTasks);
        return ui.unmarkTask(task);
    }

    private String handleTodo(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        if (parts.length < 2 || rawInput.length() <= 4) {
            throw new GigachadException("Invalid usage! Usage: todo <task>");
        }
        String description = rawInput.substring(5).trim();
        ToDo todo = new ToDo(description);
        listOfTasks.addTask(todo);
        storage.saveToStorage(listOfTasks);
        return ui.addTask(todo, listOfTasks);
    }

    private String handleDeadline(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        try {
            int byIndex = rawInput.indexOf(DEADLINE_DELIMITER);
            if (byIndex == -1) {
                throw new GigachadException("Invalid usage! Usage: deadline <task> /by <due date>. Format: yyyy-MM-dd HHmm");
            }
            String description = rawInput.substring(9, byIndex).trim(); // skip "deadline "
            String dueDate = rawInput.substring(byIndex + DEADLINE_DELIMITER.length()).trim();

            if (description.isEmpty() || dueDate.isEmpty()) {
                throw new GigachadException("Deadline description or date missing.");
            }

            Deadline deadline = new Deadline(description, LocalDateTime.parse(dueDate, FORMATTER));
            listOfTasks.addTask(deadline);
            storage.saveToStorage(listOfTasks);
            return ui.addTask(deadline, listOfTasks);
        } catch (DateTimeParseException e) {
            throw new GigachadException("Invalid date format! Use yyyy-MM-dd HHmm.");
        }
    }

    private String handleEvent(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        try {
            int firstSpace = rawInput.indexOf(' ');
            int fromIndex = rawInput.indexOf(EVENT_FROM);
            int toIndex = rawInput.indexOf(EVENT_TO);

            if (firstSpace == -1 || fromIndex == -1 || toIndex == -1) {
                throw new GigachadException("Invalid usage! Usage: event <task> /from <start> /to <end>. "
                        + "Format: yyyy-MM-dd HHmm");
            }

            String description = rawInput.substring(firstSpace + 1, fromIndex).trim();
            String from = rawInput.substring(fromIndex + 7, toIndex).trim();
            String to = rawInput.substring(toIndex + 5).trim();

            System.out.println(LocalDateTime.parse(from, FORMATTER));
            System.out.println(LocalDateTime.parse(to, FORMATTER));

            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new GigachadException("Event description or dates missing.");
            }

            Event event = new Event(description,
                    LocalDateTime.parse(from, FORMATTER),
                    LocalDateTime.parse(to, FORMATTER));
            listOfTasks.addTask(event);
            storage.saveToStorage(listOfTasks);
            return ui.addTask(event, listOfTasks);

        } catch (DateTimeParseException e) {
            throw new GigachadException("Invalid date format! Use yyyy-MM-dd HHmm.");
        }
    }

    private int parseTaskNumber(TaskList listOfTasks) throws GigachadException {
        if (parts.length != 2) {
            throw new GigachadException("Invalid usage! Usage: <command> <task number>");
        }
        int taskNumber = Integer.parseInt(parts[1]) - 1;
        if (taskNumber < 0 || taskNumber >= listOfTasks.size()) {
            throw new GigachadException("Invalid task number! You only have " + listOfTasks.size() + " tasks.");
        }
        return taskNumber;
    }
}
