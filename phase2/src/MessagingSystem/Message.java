package MessagingSystem;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;

/**
 * An object that represents a message sent between users
 *
 * @author Chrisee Zhu
 */
public class Message implements Serializable {
    private String message, sender;
    private ZonedDateTime time = ZonedDateTime.now(ZoneId.systemDefault());
    private boolean read = false;
    private boolean pinned = false;

    /**
     * Initializes a new Message
     *
     * @param message       String to be stored in Message
     * @param sender        username of sender
     */
    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    /**
     * Gets the message String stored in this object.
     *
     * @return      String of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the sender of this message.
     *
     * @return      username of sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Updates the sender username of this message to newSender.
     *
     * @param newSender     the uew username
     */
    public void setSender(String newSender) {
        this.sender = newSender;
    }

    /**
     * Gets the date and time this message was sent.
     *
     * @return      date sent (zoned)
     */
    public String getDate() {
        return time.toString();
    }

    /**
     * Gets the date and time this message was sent, converted to local time.
     *
     * @return      date sent (local)
     */
    public String getLocalDate() {
        DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        ZonedDateTime currentTime = time.withZoneSameInstant(ZoneId.systemDefault());
        return currentTime.toLocalDateTime().format(d);
    }

    /**
     * Sets the date and time of the message sent.
     *
     * @param time      date sent (zoned)
     */
    public void setDate(ZonedDateTime time) {
        this.time = time;
    }

    /**
     * Method for checking if message is read by the recipient (not the sender)
     * @return           True if the message at index is read, false otherwise
     */
    public boolean isRead() {
        return read;
    }

    /**
     * Method for setting the message as read.
     */
    public void read() {
        setRead(true);
    }

    /**
     * Method for setting the message's read status.
     *
     * @param isRead   boolean of whether the message is read.
     */
    public void setRead(boolean isRead) {
        read = isRead;
    }

    /**
     * Method for checking if message is pinned.
     *
     * @return           True if message is pinned, false otherwise
     */
    public boolean isPinned(){
        return pinned;
    }

    /**
     * Method for setting the message as pinned or unpinned.
     *
     * @param setter   True or false
     */
    public void setPinned(boolean setter){
        pinned = setter;
    }
}