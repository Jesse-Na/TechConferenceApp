package MessagingSystem;

import java.util.*;
import java.io.Serializable;

/**
 * Chatroom that stores message history and has methods for sending messages.
 *
 * @author Elliot, Chrisee
 */
public class Chatroom implements Serializable {
    private ArrayList<Message> history;

    /**
     * Constructor for Chatroom, creates a new empty ArrayList.
     */
    public Chatroom(){
        this.history = new ArrayList<>();
    }

    /**
     * Method for adding a message to history.
     *
     * @param message    Message that was sent.
     */
    public void sendMessage(Message message){
        history.add(message);
    }

    /**
     * Method for removing a message from history.
     *
     * @param position   the index of the message that is to be deleted
     */
    public void deleteMessage(int position){
        history.remove(position);
    }

    /**
     * Returns the indices in history as an ArrayList.
     *
     * @return          ArrayList of indices in history
     */
    public ArrayList<Integer> getMessagePositions() {
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0;i < history.size(); i++){
            positions.add(i);
        }
        return positions;
    }

    /**
     * Returns the message at the index.
     *
     * @param position  The index of the message
     * @return          Message corresponding with the key
     */
    public String getMessage(Integer position) {
        return history.get(position).getMessage();
    }

    /**
     * Returns all messages
     *
     * @return          Returns the history of messages
     */
    public ArrayList<Message> getAllMessages() { return history; }

    /**
     * Returns the date the message at the index.
     *
     * @param position  The position of the message
     * @return          Date the message corresponding with the key was sent
     */
    public String getDate(Integer position) {
        return history.get(position).getLocalDate();
    }

    /**
     * Returns the sender of the message at the index.
     *
     * @param position  The position of the message
     * @return          The sender of the message corresponding with the key
     */
    public String getSender(Integer position) {
        return history.get(position).getSender();
    }

    /**
     * Returns the number of messages in history.
     *
     * @return    Number of elements of history
     */
    public int getSize(){
        return history.size();
    }

    /**
     * Method for checking if message is read.
     *
     * @param username   Username of the currently logged in user
     * @param position   Index of the message
     *
     * @return           True if the message at index is read, false otherwise
     */
    public boolean isUnread(String username, Integer position) {
        return !username.equals(history.get(position).getSender()) && !history.get(position).isRead();
    }

    /**
     * Method for setting the message at the index to be read or unread.
     *
     * @param position   Index of the message
     */
    public void markReadUnread(Integer position) {
        boolean read = history.get(position).isRead();
        history.get(position).setRead(!read);
    }

    /**
     * Method for getting the number of unread messages sent to the user.
     *
     * @param username   Username of the currently logged in user
     *
     * @return           Number of unread messages for recipient
     */
    public int getUnread(String username){
        int unread = 0;
        for(Message m : history){
            if(!username.equals(m.getSender()) && !m.isRead()){
                unread++;
            }
        }
        return unread;
    }

    /**
     * Checks whether a message with id index is pinned.
     *
     * @param index     the index/id of the message in history
     * @return          true if the message is pinned
     */
    public boolean isPinned(int index){
        return history.get(index).isPinned();
    }

    /**
     * Method for getting all pinned messages in the Chatroom.
     *
     * @return           HashMap of all pinned messages and their indices
     */
    public ArrayList<Integer> getPinned(){
        ArrayList<Integer> pinned = new ArrayList<>();
        for(int i = 0; i < history.size(); i++){
            if(history.get(i).isPinned()){
                pinned.add(i);
            }
        }
        return pinned;
    }

    /**
     * Method for getting all pinned messages in the Chatroom.
     *
     * @param position    Index of the message
     */
    public void pinUnpin(Integer position) {
        boolean current = history.get(position).isPinned();
        history.get(position).setPinned(!current);
    }

    /**
     * Updates all sender usernames in this chatroom matching prevSender to newSender.
     *
     * @param prevSender    username current in use in this chatroom
     * @param newSender     the uew username
     */
    public void updateSenders(String prevSender, String newSender) {
        for (Message m : history) {
            if (m.getSender().equals(prevSender)) {
                m.setSender(newSender);
            }
        }
    }

}
