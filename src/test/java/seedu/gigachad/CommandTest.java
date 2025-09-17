package seedu.gigachad;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gigachad.Command;
import gigachad.Storage;
import gigachad.TaskList;
import gigachad.Ui;
import gigachad.exception.GigachadException;
import gigachad.task.Task;

/**
 * Unit tests for the {@link Command} class.
 * <p>
 * This test suite validates both the success and failure paths of
 * all commands supported by the gigachad application, such as:
 * - list
 * - todo
 * - deadline
 * - event
 * - find
 * - mark/unmark
 * - delete
 * - help
 * - invalid commands
 * </p>
 */
public class CommandTest {
    @TempDir
    File tempDir;
    /**
     * Stub class for {@link TaskList} to expose its internal task list directly.
     */
    static class TaskListStub extends TaskList {
        public TaskListStub() {
            super();
        }

        public TaskListStub(ArrayList<Task> listOfTasks) {
            super(listOfTasks);
        }

        public void addTask(Task task) {
            listOfTasks.add(task);
        }

        public ArrayList<Task> allTasks() {
            return this.listOfTasks;
        }
    }

    /**
     * Stub class for {@link Storage} to avoid side effects while still allowing file persistence.
     */
    static class StorageStub extends Storage {
        public StorageStub(Path filePath) {
            super(filePath);
        };
    }

    /**
     * Stub class for {@link Ui} to simplify interaction checks.
     */
    static class UiStub extends Ui {
    }

    /**
     * Verifies that executing a "list" command on an empty task list throws a {@link GigachadException}.
     */
    @Test
    public void execute_listEmptyList_exceptionThrown() {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("list", "list", new String[] {});

        GigachadException thrown = assertThrows(
                GigachadException.class,
                () -> command.execute(taskListStub, uiStub, storageStub),
                "Expected execute() to throw GigachadException"
        );

        assertTrue(thrown.getMessage().contains("Empty list!"));
    }

    /**
     * Verifies that adding a todo, deadline, and event task
     * successfully persists them to storage in the expected format.
     */
    @Test
    public void execute_taskListWithTodoDeadlineEvent_success() throws GigachadException, IOException {
        Path filePath = tempDir.toPath().resolve("tasks.txt");
        StorageStub storageStub = new StorageStub(filePath);
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command commandTodo = new Command("todo", "todo borrow book",
                new String[] { "todo", "borrow", "book" });
        commandTodo.execute(taskListStub, uiStub, storageStub);

        Command commandDeadline = new Command("deadline", "deadline return book /by 2025-09-09 1900",
                new String[] { "deadline", "return", "book", "2025-09-09", "1900" });
        commandDeadline.execute(taskListStub, uiStub, storageStub);

        Command commandEvent = new Command("event",
                "event project meeting /from 2025-10-10 1000 /to 2025-11-10 1000",
                new String[] { "event", "project", "meeting", "2025-10-10", "1000", "2025-11-10", "1000" });
        commandEvent.execute(taskListStub, uiStub, storageStub);

        String content = readString(filePath);

        assertAll(
                () -> assertTrue(content.contains("T | 0 | borrow book")),
                () -> assertTrue(content.contains("D | 0 | return book | 2025-09-09 1900")),
                () -> assertTrue(content.contains("E | 0 | project meeting | 2025-10-10 1000 - 2025-11-10 1000"))
        );
    }

    /**
     * Verifies that a "todo" command with no description throws a {@link GigachadException}.
     */
    @Test
    public void execute_todoMissingDescription_exceptionThrown() {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("todo", "todo", new String[] { "todo" });
        assertThrows(GigachadException.class, () ->
                command.execute(taskListStub, uiStub, storageStub));
    }

    /**
     * Verifies that a "deadline" command with an invalid date format throws a {@link GigachadException}.
     */
    @Test
    public void execute_deadlineInvalidDate_exceptionThrown() {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("deadline", "deadline task /by not-a-date",
                new String[] { "deadline", "task", "not-a-date" });

        assertThrows(GigachadException.class, () ->
                command.execute(taskListStub, uiStub, storageStub));
    }

    /**
     * Verifies that an "event" command missing required fields throws a {@link GigachadException}.
     */
    @Test
    public void execute_eventMissingFields_exceptionThrown() {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("event", "event something /from onlyStart",
                new String[] { "event", "something", "onlyStart" });

        assertThrows(GigachadException.class, () ->
                command.execute(taskListStub, uiStub, storageStub));
    }

    /**
     * Verifies that a "find" command with no keyword throws a {@link GigachadException}.
     */
    @Test
    public void execute_findWithNoKeyword_exceptionThrown() {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("find", "find", new String[] { "find" });

        assertThrows(GigachadException.class, () ->
                command.execute(taskListStub, uiStub, storageStub));
    }

    /**
     * Verifies that a "mark" command with an invalid task number throws a {@link GigachadException}.
     */
    @Test
    public void execute_markInvalidTaskNumber_exceptionThrown() {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("mark", "mark 5", new String[] { "mark", "5" });

        assertThrows(GigachadException.class, () ->
                command.execute(taskListStub, uiStub, storageStub));
    }

    /**
     * Verifies that the "help" command returns a help message containing expected usage instructions.
     */
    @Test
    public void execute_helpCommand_returnsHelpMessage() throws GigachadException {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("help", "help", new String[] { "help" });
        String result = command.execute(taskListStub, uiStub, storageStub);

        assertTrue(result.contains("To add todos"));
        assertTrue(result.contains("To exit: bye"));
    }

    /**
     * Verifies that an invalid command returns an error message to the user.
     */
    @Test
    public void execute_invalidCommand_returnsInvalidMessage() throws GigachadException {
        StorageStub storageStub = new StorageStub(Paths.get("data/tasks.txt"));
        TaskListStub taskListStub = new TaskListStub();
        UiStub uiStub = new UiStub();

        Command command = new Command("nonsense", "nonsense", new String[] { "nonsense" });
        String result = command.execute(taskListStub, uiStub, storageStub);

        assertTrue(result.toLowerCase().contains("invalid"));
    }
}
