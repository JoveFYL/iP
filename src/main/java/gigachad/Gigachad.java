package gigachad;

import java.nio.file.Path;
import java.nio.file.Paths;

import gigachad.exception.GigachadException;

/**
 * Main class for gigachad chatbot.
 * Initialises application components and manages the main execution loop.
 * Handles user input, command parsing and execution of task operations.
 */
public class Gigachad {
    private final Storage storage;
    private final TaskList listOfTasks;
    private final Ui ui;

    /**
     * Constructs new gigachad chatbot instance.
     * Initialises application components: UI, Storage and TaskList.
     * @param filePath the path to the file where tasks will be stored
     */
    public Gigachad(Path filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.listOfTasks = new TaskList(storage.initStorage());
    }

    public String getResponse(String input) {
        String response = "";
        try {
            Command parsedCommand = Parser.parse(input);
            response = parsedCommand.execute(listOfTasks, ui, storage);
        } catch (GigachadException e) {
            return e.getMessage();
        }

        return response;
    }

    /**
     * Starts main execution loop of gigachad.
     * Displays welcome message, and reads, parses and executes user commands until "bye" command is received.
     * Handles any exceptions that occur during user command execution
     */
    public void run() {
        ui.welcomeUser();

        // ask for user input
        String command = "";
        while (!command.equals("bye")) {
            command = ui.readCommand();
            getResponse(command);
        }
    }

    /**
     * Main entry point of gigachad.
     * Creates new gigachad instance with default file path and starts the gigachad chatbot.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new Gigachad(Paths.get("data/tasks.txt")).run();
    }
}
