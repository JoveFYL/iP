package Gigachad;

import Gigachad.exception.GigachadException;

public class Parser {
    public static Command parse(String input) throws GigachadException {
        if (input.isEmpty()) throw new GigachadException("Input cannot be blank!");

        String[] parts = input.trim().split(" ");
        String firstWord = parts[0].toLowerCase();

        String[] args = new String[parts.length];
        System.arraycopy(parts, 0, args, 0, args.length);
        return new Command(firstWord, input, args);
    }
}
