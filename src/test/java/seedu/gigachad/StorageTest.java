package seedu.gigachad;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gigachad.Storage;
import gigachad.TaskList;
import gigachad.task.Task;

/**
 * Unit tests for {@link gigachad.Storage}.
 * Tests both saving tasks to file and loading tasks back from storage,
 * including handling of corrupted input data.
 */
public class StorageTest {
    @TempDir
    File tempDir;

    /**
     * Stub TaskList implementation for testing {@link Storage#saveToStorage(TaskList)}.
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

        @Override
        public ArrayList<Task> allTasks() {
            return this.listOfTasks;
        }
    }

    /**
     * Base stub for Task, allows testing without full Task implementation.
     */
    static class TaskStub extends Task {
        private final boolean done;

        public TaskStub(String description, boolean done) {
            super(description);
            this.done = done;
        }

        @Override
        public int getNumericIsDone() {
            return this.done ? 1 : 0;
        }
    }

    /**
     * Stub ToDo task used for testing.
     */
    static class TodoStub extends TaskStub {
        public TodoStub(String description, boolean done) {
            super(description, done);
        }

        @Override
        public String saveFormat() {
            return "T | " + this.getNumericIsDone() + " | " + this.description;
        }
    }

    /**
     * Stub Deadline task used for testing.
     */
    static class DeadlineStub extends TaskStub {
        private final LocalDateTime by;

        public DeadlineStub(String description, LocalDateTime by, boolean done) {
            super(description, done);
            this.by = by;
        }

        @Override
        public String saveFormat() {
            String formattedBy = by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            return "D | " + this.getNumericIsDone() + " | " + this.description + " | " + formattedBy;
        }
    }

    /**
     * Stub Event task used for testing.
     */
    static class EventStub extends TaskStub {
        private final LocalDateTime from;
        private final LocalDateTime to;

        public EventStub(String description, LocalDateTime from, LocalDateTime to, boolean done) {
            super(description, done);
            this.from = from;
            this.to = to;
        }

        @Override
        public String saveFormat() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            String formattedFrom = from.format(formatter);
            String formattedTo = to.format(formatter);
            return "E | " + this.getNumericIsDone() + " | " + this.description + " | " + formattedFrom
                    + " - " + formattedTo;
        }
    }

    /**
     * Tests that tasks saved to storage are written in the correct format.
     */
    @Test
    public void saveToStorage_writesCorrectFormat() throws IOException {
        Path filePath = tempDir.toPath().resolve("tasks.txt");
        Storage storage = new Storage(filePath);

        ArrayList<Task> tasksToSave = new ArrayList<>();
        tasksToSave.add(new TodoStub("read book", false));
        tasksToSave.add(new DeadlineStub("submit assignment",
                LocalDateTime.of(2025, 12, 15, 23, 59), false));
        tasksToSave.add(new EventStub("conference",
                LocalDateTime.of(2025, 9, 9, 18, 0),
                LocalDateTime.of(2025, 9, 9, 20, 30),
                true));

        TaskListStub taskListToSave = new TaskListStub(tasksToSave);
        storage.saveToStorage(taskListToSave);

        String content = readString(filePath);
        assertAll(
                () -> assertTrue(content.contains("T | 0 | read book")),
                () -> assertTrue(content.contains("D | 0 | submit assignment | 2025-12-15 2359")),
                () -> assertTrue(content.contains("E | 1 | conference | 2025-09-09 1800 - 2025-09-09 2030"))
        );
    }

    /**
     * Tests that {@link Storage#initStorage()} correctly creates a new empty file
     * if it does not already exist.
     */
    @Test
    public void initStorage_createsNewFile() {
        Path filePath = tempDir.toPath().resolve("new_tasks.txt");
        Storage storage = new Storage(filePath);
        ArrayList<Task> tasks = storage.initStorage();

        assertTrue(filePath.toFile().exists(), "File should be created if not present");
        assertTrue(tasks.isEmpty(), "Task list should be empty for a new file");
    }

    /**
     * Tests that {@link Storage#initStorage()} loads tasks from an existing file correctly.
     */
    @Test
    public void initStorage_loadsExistingTasks() throws IOException {
        Path filePath = tempDir.toPath().resolve("tasks.txt");

        // Write mock tasks to file
        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write("T | 1 | read book\n");
            writer.write("D | 0 | submit assignment | 2025-12-15 2359\n");
            writer.write("E | 1 | conference | 2025-09-09 1800 - 2025-09-09 2030\n");
        }

        Storage storage = new Storage(filePath);
        ArrayList<Task> tasks = storage.initStorage();

        assertEquals(3, tasks.size(), "Should load all tasks from file");
        assertTrue(tasks.get(0).toString().contains("[T][X] read book"));
        assertTrue(tasks.get(1).toString().contains("[D][ ] submit assignment"));
        assertTrue(tasks.get(2).toString().contains("[E][X] conference"));
    }

    /**
     * Tests that {@link Storage} loads tasks from an existing file correctly, even if parent directories are missing
     */
    @Test
    public void initStorage_createsMissingParentDirectories() {
        Path filePath = tempDir.toPath().resolve("nested/folder/tasks.txt");
        Storage storage = new Storage(filePath);
        ArrayList<Task> tasks = storage.initStorage();

        assertTrue(filePath.toFile().exists(), "File should be created even if parent directories are missing");
        assertTrue(tasks.isEmpty(), "Task list should be empty for a new file with missing parent directories");
    }
}
