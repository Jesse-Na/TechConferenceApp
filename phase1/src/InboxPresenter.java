import java.util.*;

public class InboxPresenter extends CommandPresenter {

    public void menuDisplay(ArrayList<String> friends) {
        System.out.println("\nCHAT HISTORY:");
        for (String friend : friends) {
            System.out.println(friend);
        }
        System.out.println("\n");
    }

    public void chatView(Chatroom cm) {
        ArrayList<Message> history = cm.getHistory();
        for (Message m : history) {
            System.out.println(m.getSender() + " | Sent: " + m.getDate());
            System.out.println(m.getMessage() + "\n");
        }
    }
}
