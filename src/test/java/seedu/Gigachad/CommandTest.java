package seedu.Gigachad;

import Gigachad.Command;
import Gigachad.Storage;
import Gigachad.TaskList;
import Gigachad.Ui;
import Gigachad.exception.GigachadException;
import Gigachad.task.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {
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

    static class StorageStub extends Storage {
        public StorageStub(Path filePath) {
            super(filePath);
        };
    }

    static class UiStub extends Ui {
    }

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

    @TempDir
    File tempDir;

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
}
