import EventSystem.*;
import LoginSystem.*;
import MessagingSystem.*;
import UserSystem.*;

import java.util.*;

/**
 * This class represents the executable for the application.
 *
 * @author  Fred, Ziwen
 */
public class ConferenceSimulator {

    ReadEvents readEvents;
    ReadUsers readUsers;

    SaveEvents saveEvents;
    StoreUsers storeUsers;
    Registrar registrar;
    EventManager eventManager;

    ReadChat readChat;
    StoreChat storeChat;
    ChatroomManager chatroomManager;

    /**
     *  Constructor for Conference simulator. Creates gateways and necessary use cases to store data.
     */
    public ConferenceSimulator() {
        String userFilepath = "phase2/src/UserSystem/userData.ser";
        String eventFilepath = "phase2/src/EventSystem/eventData.ser";
        String chatFilepath = "phase2/src/MessagingSystem/chatlog.ser";

        readEvents = new ReadEvents(eventFilepath);
        readUsers = new ReadUsers(userFilepath);

        saveEvents = new SaveEvents(eventFilepath);
        storeUsers = new StoreUsers(userFilepath);
        registrar = new Registrar(readUsers.read());
        eventManager = new EventManager(readEvents.read());

        readChat = new ReadChat(chatFilepath);
        storeChat = new StoreChat(chatFilepath);
        chatroomManager = readChat.readChatlog();
    }

    /**
     * This method will save all users and events data to a ser file.
     */
    public void save(){
        storeUsers.store(registrar.getUsers());
        saveEvents.saveEvents(eventManager.getEventsList());
        storeChat.storeChat(chatroomManager);
    }

    /**
     * The static main method will run this method to start the application.
     */
    public void run() {

        EventSignup eventSignup = new EventSignup();

        // Make administrator account
        AdminCreationScript adminCreationScript = new AdminCreationScript();
        adminCreationScript.createAdmin(registrar);

        // Make organizer account
        OrganizerCreationScript organizerCreationScript = new OrganizerCreationScript();
        organizerCreationScript.createOrganizers(registrar);

        // Add necessary presenters and controllers
        LoginOptionsFacade loginFacade = new LoginOptionsFacade(registrar);
        EventCreatorPresenter eventCreatorPresenter = new EventCreatorPresenter(eventManager, registrar);
        EventSignupPresenter eventSignupPresenter = new EventSignupPresenter(eventSignup, eventManager);
        ChatMenuPresenter chatMenuPresenter = new ChatMenuPresenter();
        FriendsPresenter friendsPresenter = new FriendsPresenter();

        MessageOutboxPresenter outboxPresenter = new MessageOutboxPresenter("", registrar, eventManager);
        MessageOutboxController outboxController = new MessageOutboxController("", registrar, eventManager, chatroomManager);
        MessageOutboxUI outboxUI = new MessageOutboxUI(outboxController, outboxPresenter);

        MessageInboxPresenter inboxPresenter = new MessageInboxPresenter(registrar, "", chatroomManager);
        MessageInboxController inboxController = new MessageInboxController(registrar, "", chatroomManager);
        MessageInboxUI inboxUI = new MessageInboxUI(inboxPresenter, inboxController);

        // Main user UI
        UserOptionsInterface ui = new UserOptionsInterface(loginFacade, eventCreatorPresenter, eventSignupPresenter,
                chatMenuPresenter, friendsPresenter, outboxUI, inboxUI);



        // Run the program
        Scanner sc = new Scanner(System.in);
        boolean exit;
        do{
            do {
                ui.loggedIn();
                save();
            } while (loginFacade.getUser() != null);
            System.out.println("Press any key to log in again, or press Q to close the program.");
            exit = sc.nextLine().equals("Q");

        } while (!exit);



        // Reset user and event data
        // ArrayList<User> emptyUserList = new ArrayList<>();
        // ArrayList<Event> emptyEventList = new ArrayList<>();
        // storeUsers.store(emptyUserList);
        // saveEvents.saveEvents(emptyEventList);
    }

}
