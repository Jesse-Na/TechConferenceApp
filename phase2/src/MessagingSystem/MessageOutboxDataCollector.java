package MessagingSystem;

import UserSystem.*;
import EventSystem.*;
import java.util.*;

/**
 * Handles text format and display for the client.
 *
 * @author Chrisee Zhu
 */
public class MessageOutboxDataCollector extends CommandPresenter {
    private String username;
    private Registrar reg;
    private EventManager em;


    public MessageOutboxDataCollector(String username, Registrar reg, EventManager em) {
        this.username = username;
        this.reg = reg;
        this.em = em;
    }

    /**
     * Sets username to that of the currently logged in user.
     *
     * @param currentUser       username of the current user
     */
    public void setLoggedInUser(String currentUser) {
        username = currentUser;
    }

    /**
     * Displays message composition options by target.
     */
    public String menuDisplay() {
        return "\n1) Message users\n2) Group message\n3) Message speakers\n";
    }

    /**
     * Formats a list of speakers hosting events at this conference, including their name and username.
     *
     * @return   text display of a menu of speakers.
     */
    public String speakerMenu() {
        StringBuilder result = new StringBuilder("\nSPEAKERS:\n------------------------");
        ArrayList<Long> events = em.getEventIDs();
        ArrayList<String> speakers = new ArrayList<>();
        for (Long event_id : em.getEventIDs()) {
            for (String speaker : em.getSpeakerList(event_id)) {
                if (!speakers.contains(speaker)) {
                    speakers.add(speaker);
                }
            }
        }
        for (String s : speakers) {
            result.append("\n")
                    .append(reg.getNameByUsername(s))
                    .append(" (@")
                    .append(s)
                    .append(")");
        }
        if (events.isEmpty()) {
            return result + "\nThere are no speakers.\n";
        }
        return result + "\n";
    }

    /**
     * Formats a list of friends that this user has, including their name and username.
     *
     * @return   text display of a menu of this user's friends.
     */
    public String friendMenu() {
        StringBuilder result = new StringBuilder("\nFRIENDS:\n------------------------");
        ArrayList<String> friends = reg.getUserFriends(username);
        for (String friend : friends) {
            result.append("\n")
                    .append(reg.getNameByUsername(friend))
                    .append(" (@")
                    .append(friend)
                    .append(")");
        }
        if (friends.isEmpty()) {
            return result + "\nYou have no friends.\n";
        }
        return result + "\n";
    }

     /**
     * Formats a list of events available at this conference, including their name, IDs, time, and room.
     *
      * @return   text display for a menu of events in this conference.
     */
    public String eventMenu() {
        StringBuilder result = new StringBuilder("\nEVENTS:\n------------------------");
        ArrayList<Long> events = new ArrayList<>();
        if (reg.isAdmin(username) || reg.isOrganizer(username)) {
            events = em.getEventIDs();
        } else if (reg.isSpeaker(username)) {
            events = reg.getSpeakerTalks(username);
        }
        for (Long evt_id : events) {
            result.append("\nName: ")
                    .append(em.getName(evt_id))
                    .append("\nid: ")
                    .append(evt_id)
                    .append("\nTime: ")
                    .append(em.getTime(evt_id))
                    .append("\nRoom: ")
                    .append(em.getRoom(evt_id))
                    .append("\n------------------------");
        }
        if (events.isEmpty()) {
            return result + "\nThere are no events.\n";
        }
        return result.toString();
    }
}
