package gigachad;

import gigachad.exception.GigachadException;

/**
 * Handles parsing of user commands for the gigachad chatbot
 * and passes executable commands on to Command class.
 * Provides parse method for parsing user input.
 */
public class Parser {
    /**
     *
     * @param input the user input String that parse will split to obtain the command and arguments
     * @return a Command object which can be executed to trigger the UI and store tasks
     * @throws GigachadException if user enters an empty input
     */
    public static Command parse(String input) throws GigachadException {
        if (input.isEmpty()) throw new GigachadException("Input cannot be blank!");

        String[] parts = input.trim().split(" ");
        String firstWord = parts[0].toLowerCase();

        String[] args = new String[parts.length];
        System.arraycopy(parts, 0, args, 0, args.length);
        return new Command(firstWord, input, args);
    }
}
