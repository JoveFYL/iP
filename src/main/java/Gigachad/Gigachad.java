package Gigachad;

import Gigachad.exception.GigachadException;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

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
            try {
               Command parsedCommand = Parser.parse(command);
               parsedCommand.execute(listOfTasks, ui, storage);
            } catch (GigachadException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Gigachad(Paths.get("data/tasks.txt")).run();
    }
}
