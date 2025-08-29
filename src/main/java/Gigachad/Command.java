package Gigachad;

import Gigachad.exception.GigachadException;
import Gigachad.task.Deadline;
import Gigachad.task.Event;
import Gigachad.task.Task;
import Gigachad.task.ToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Represents a command that can be executed in the Gigachad application.
 * This class parses and executes various user commands for task management, including
 * adding, deleting, marking, and listing tasks.
 */
public class Command {
    private final String command;
    private final String[] parts;
    private final String rawInput;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

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
    public void execute(TaskList listOfTasks, Ui ui, Storage storage) throws GigachadException {
        switch (this.command) {
        case "list":
            if (listOfTasks.isEmpty()) {
                throw new GigachadException("Empty list!");
            }

            ui.listTasks(listOfTasks);
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
                    ui.deleteTask(removedTask, listOfTasks);
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
                    ui.markTask(task);
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
                    ui.unmarkTask(task);
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

                ui.addTask(todo, listOfTasks);
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
                    throw new GigachadException("Invalid usage! Usage: deadline <task> /by <due date/time>. " +
                            "Format for the date is: yyyy-MM-dd HHmm");
                }

                String deadlineDescription = rawInput.substring(firstSpaceDeadline + 1, byIndexDeadline).trim();
                String deadlineDueDate = rawInput.substring(byIndexDeadline + 4).trim();

                if (deadlineDescription.isEmpty() || deadlineDueDate.isEmpty()) {
                    throw new GigachadException("Invalid usage! Gigachad.task.Task description or datetime missing.");
                }

                Deadline deadline = new Deadline(deadlineDescription,
                        LocalDateTime.parse(deadlineDueDate, formatter));
                listOfTasks.addTask(deadline);
                storage.saveToStorage(listOfTasks);

                ui.addTask(deadline, listOfTasks);
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
                    throw new GigachadException("Invalid usage! Usage: event <task> /from <beginning date> " +
                            "/to <end date>. Format for the date is: yyyy-MM-dd HHmm");
                }

                String eventDescription = rawInput.substring(firstSpace + 1, fromIndexEvent).trim();
                String from = rawInput.substring(fromIndexEvent + 7, toIndexEvent).trim();
                String to = rawInput.substring(toIndexEvent + 5).trim();

                if (eventDescription.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    throw new GigachadException("Invalid usage! Gigachad.task.Task description or date missing.");
                }

                Event event = new Event(eventDescription,
                        LocalDateTime.parse(from, formatter),
                        LocalDateTime.parse(to, formatter));
                listOfTasks.addTask(event);
                storage.saveToStorage(listOfTasks);

                ui.addTask(event, listOfTasks);
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
