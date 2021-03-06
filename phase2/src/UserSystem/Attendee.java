package UserSystem;

/**
 * Standard user class that represents users who will be signing up and attending conference events.
 *
 * @author Fred
 */

public class Attendee extends User {

    /**
     * Constructor for Attendee. Takes in String variables and calls the superclass constructor to assign to variables
     * inherited from the User class.
     *
     * @param name          The name of the attendee.
     * @param userName      The user name of the attendee.
     * @param password      The password of the attendee.
     */
    public Attendee(String name, String userName, String password) {
        super(name, userName, password);
    }

    /**
     * Gets the type of user this user is.
     *
     * @return "attendee"
     */
    @Override
    public String getUserType(){
        return "attendee";
    };
}
