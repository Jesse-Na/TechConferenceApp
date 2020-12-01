package MessagingSystem;

import java.util.*;
import java.util.regex.*;

/**
 * An abstract presenter that contains the base features for all messaging-related presenters.
 *
 * @author Elliot, Chrisee
 */
public abstract class CommandPresenter {

    /**
     * Displays an exit command tip.
     */
    public String exitMessage() {
        return "$q to exit.";
    }

    /**
     * Displays message when field is invalid.
     *
     * @param field     field (user input)
     */
    public String invalidCommand(String field) {
        return "invalid " + field + ".";
    }

    /**
     * Displays prompt for user input into field.
     *
     * @param field     field (user input)
     */
    public String commandPrompt(String field) {
        return "Enter a valid " + field + ".\n" + exitMessage();
    }

    /**
     * Displays a success message for a sent message.
     *
     * @param recipient (enter message)
     */
    public String success(String recipient) {
        return "\nMessage successfully sent to " + recipient + "!";
    }

}
