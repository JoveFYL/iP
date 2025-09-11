package gigachad;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import gigachad.exception.GigachadException;
import gigachad.task.Deadline;
import gigachad.task.Event;
import gigachad.task.Task;
import gigachad.task.ToDo;

/**
 * Represents a Storage object file to store the tasks of the user.
 */
public class Storage {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    protected Path filePath;
    protected ArrayList<Task> tasks;

    /**
     * Constructs a Storage object with the specified file path.
     * Initialises an empty ArrayList of tasks.
     *
     * @param filePath the path to the file where tasks will be stored
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
        this.tasks = new ArrayList<>();
    }

    /**
     * Initialises the storage system by creating directories and files,
     * then loads existing tasks from the storage file if it exists.
     * Creates the parent directories if they do not exist, creates the storage file if storage file does not exist
     * and parses existing task data from the file format:
     * "T | isDone | description" for ToDo tasks
     * "D | isDone | description | deadlineDateTime" for Deadline tasks
     * "E | isDone | description | startDateTime - endDateTime" for Event tasks
     * Prints initialisation status and current task list to console.
     * Handles corrupted file formats by catching and printing error messages.
     *
     * @return an ArrayList of Task objects loaded from storage, or empty list if file doesn't exist or error occurs
     */
    public ArrayList<Task> initStorage() {
        try {
            ArrayList<Task> listOfTasks = new ArrayList<>();

            ensureParentDirectoryExists();

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                System.out.println("File created: " + filePath.toAbsolutePath());
            } else {
                System.out.println("File already exists: " + filePath.toAbsolutePath());
                loadTasksFromFile(listOfTasks);
            }

            printInitialisedTasks(listOfTasks);
            return listOfTasks;
        } catch (IOException e) {
            System.out.println("An error occurred while initialising storage: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void ensureParentDirectoryExists() throws IOException {
        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }
    }

    private void loadTasksFromFile(ArrayList<Task> listOfTasks) throws IOException {
        try (Scanner scanner = new Scanner(filePath.toFile())) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                parseTaskLine(line, listOfTasks);
            }
        }
    }

    private void parseTaskLine(String line, ArrayList<Task> listOfTasks) {
        String[] parts = line.split(" \\| ");
        String command = parts[0];
        String isDone = parts[1];
        String description = parts[2];

        switch (command) {
        case "T" -> loadToDo(parts, isDone, description, listOfTasks);
        case "D" -> loadDeadline(parts, isDone, description, listOfTasks);
        case "E" -> loadEvent(parts, isDone, description, listOfTasks);
        default -> {
            // ignore unknown task type (matches your original behavior of "do nothing")
        }
        }
    }

    private void loadToDo(String[] parts, String isDone, String description, ArrayList<Task> listOfTasks) {
        try {
            if (parts.length == 3) {
                ToDo todo = new ToDo(description);
                listOfTasks.add(todo);
                if (Integer.parseInt(isDone) == 1) {
                    todo.markAsDone();
                }
            } else {
                throw new GigachadException("Invalid format! Corrupted file!");
            }
        } catch (GigachadException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadDeadline(String[] parts, String isDone, String description, ArrayList<Task> listOfTasks) {
        try {
            if (parts.length != 4) {
                throw new GigachadException("Invalid format! Corrupted file!");
            }
            String deadlineDueDate = parts[3];
            Deadline deadline = new Deadline(description, LocalDateTime.parse(deadlineDueDate, FORMATTER));
            listOfTasks.add(deadline);
            if (Integer.parseInt(isDone) == 1) {
                deadline.markAsDone();
            }
        } catch (GigachadException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadEvent(String[] parts, String isDone, String description, ArrayList<Task> listOfTasks) {
        try {
            if (parts.length != 4 || parts[3].split(" - ").length != 2) {
                throw new GigachadException("Invalid format! Corrupted file!");
            }

            String[] fromToDates = parts[3].split(" - ");
            String from = fromToDates[0];
            String to = fromToDates[1];

            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new GigachadException("Invalid format! Task description or date missing.");
            }

            Event event = new Event(description,
                    LocalDateTime.parse(from, FORMATTER),
                    LocalDateTime.parse(to, FORMATTER));
            listOfTasks.add(event);

            if (Integer.parseInt(isDone) == 1) {
                event.markAsDone();
            }
        } catch (GigachadException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printInitialisedTasks(ArrayList<Task> listOfTasks) {
        System.out.println("Storage initialised");
        System.out.println("You have the following tasks: ");
        for (int i = 0; i < listOfTasks.size(); i++) {
            System.out.print((i + 1) + ". ");
            System.out.println(listOfTasks.get(i));
        }
    }

    /**
     * Saves all tasks from the given TaskList to the storage file.
     * Overwrites the existing file content with the current task data.
     * Each task is written in its save format on a separate line.
     *
     * @param taskList the TaskList containing tasks to be saved to storage
     */
    public void saveToStorage(TaskList taskList) {
        try (FileWriter fw = new FileWriter(filePath.toString())) {
            for (Task task : taskList.allTasks()) {
                fw.write(task.saveFormat());
                fw.write("\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
