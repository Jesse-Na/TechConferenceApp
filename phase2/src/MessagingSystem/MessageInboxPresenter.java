package MessagingSystem;

import java.util.*;

/**
 * For taking in user inputs for viewing chat history and replying to messages.
 *
 * @author Chrisee, Elliot
 */
public class MessageInboxPresenter {
    private MessageInboxController ic;
    private MessageInboxDataCollector ip;
    private IView view;

    /**
     * Initiates a new InboxController
     *
     * @param ip        InboxPresenter
     * @param ic        InboxController
     */
    public MessageInboxPresenter(MessageInboxDataCollector ip, MessageInboxController ic) {
        this.ip = ip;
        this.ic = ic;
    }

    public void setView(IView view) {
        this.view = view;
    }

    /**
     * Sets username to that of the currently logged in user.
     *
     * @param currentUser       username of the current user
     */

    public void setLoggedInUser(String currentUser) {
        ip.setLoggedInUser(currentUser);
        ic.setLoggedInUser(currentUser);
    }

    private ArrayList<ArrayList<String>> sortChatrooms(ArrayList<ArrayList<String>> chatrooms) {
        ArrayList<ArrayList<String>> sortedChatrooms = new ArrayList<>();
        int i = 0;
        for (ArrayList<String> chatroom : chatrooms) {
            while (i < sortedChatrooms.size()) {
                if (Integer.parseInt(sortedChatrooms.get(i).get(2)) < Integer.parseInt(chatroom.get(2))) {
                    sortedChatrooms.add(i, chatroom);
                    break;
                }
                i += 1;
            }
            if (i == sortedChatrooms.size()) {
                sortedChatrooms.add(i, chatroom);
            }
            i = 0;
        }
        return sortedChatrooms;
    }

    public void loadChatroomCanvasView() {
        view.setChatroomCanvasTitle(ip.getTotalUnread());
        for (ArrayList<String> option: sortChatrooms(ip.getChatroomOptions())) {
            view.setChatroomOption(option);
        }
    }

    public void loadMessageCanvasView(String recipientName, String username) {
        view.setMessageCanvasTitle(recipientName);
        for (ArrayList<String> messageData : ip.getMessages(username, "all")) {
            view.setMessageArea(messageData);
        }
        for (ArrayList<String> messageData : ip.getMessages(username, "pinned")) {
            view.setPinnedMessage(messageData);
        }
    }

    public void sendMessage(String message, String recipient) {
        if (ic.validateMessage(message)) {
            ic.sendMessage(recipient, message);
            updateMessageCanvasView(recipient);
        }
    }

    public void updateMessageCanvasView(String recipient) {
        view.setMessageArea(ip.getNewestMessage(recipient));
    }

    public void updateChatroomCanvasView() {

    }

//        /**
//         * Prompts the user to choose a chatlog from a list of existing chatlogs by username.
//         */
//    public void promptChatChoice() {
//        view.setChatroomCanvasTitle(ip.getTotalUnread());
//        System.out.println(ip.commandPrompt("username"));
//        String recipient = sc.nextLine();
//        while (!recipient.equals("$q")) {
//            recipient = recipient.replace("@", "");
//            if (recipient.equals("1")) {
//                mo.promptChatChoice();
//            } else if (ic.canViewChat(recipient)) {
//                chatViewer(recipient);
//                ic.markAllRead(recipient);
//            } else {
//                System.out.println(ip.invalidCommand("username"));
//            }
//            System.out.println(ip.commandPrompt("username"));
//            recipient = sc.nextLine();
//        }
//    }
//
//    /**
//     * Prompts the user for whether they want to reply (if able) or whether they want to exit the chat.
//     *
//     * @param recipient     username of a user that the logged in user has chatted with
//     */
//    public void chatViewer(String recipient) {
//        System.out.println(ip.chatView(recipient));
//        String e = "";
//        while (!e.equals("$q")) {
//            System.out.println(ip.messageMenu());
//            if (ic.canReply(recipient)) {
//                System.out.println(ip.replyMessage());
//            }
//            System.out.println(ip.exitMessage());
//            e = sc.nextLine();
//            if (e.equals("1")){
//                promptDelete(recipient);
//            } else if (e.equals("2")){
//                promptPin(recipient);
//            } else if (e.equals("3")){
//                System.out.println(ip.viewPinned(recipient));
//                promptPin(recipient);
//            }else if (e.equals("4") && ic.canReply(recipient)){
//                mo.promptMessage(recipient);
//            }
//            System.out.println(ip.chatView(recipient));
//        }
//    }
//
//    /**
//     * Prompts the user for which message user wants to delete
//     *
//     * @param recipient     username of a user that the logged in user has chatted with
//     */
//    public void promptDelete(String recipient){
//        System.out.println(ip.whichMessage("delete"));
//        System.out.println(ip.exitMessage());
//        String re = sc.nextLine();
//        while (!re.equals("$q")){
//            if(re.matches("[0-9]+") && ic.canDelete(recipient, re)){
//                ic.deleteMessage(recipient, re);
//                break;
//            }else{
//                System.out.println(ip.invalidCommand("number"));
//            }
//            System.out.println(ip.whichMessage("delete"));
//            re = sc.nextLine();
//        }
//    }
//
//    public void promptPin(String recipient){
//        System.out.println(ip.whichMessage("pin/unpin"));
//        System.out.println(ip.exitMessage());
//        String re = sc.nextLine();
//        while (!re.equals("$q")){
//            if(re.matches("[0-9]+")){
//                ic.pinUnpinMessage(recipient, re);
//                break;
//            }else{
//                System.out.println(ip.invalidCommand("number"));
//            }
//            System.out.println(ip.whichMessage("pin/unpin"));
//            re = sc.nextLine();
//        }
//    }

    public interface IView {
        void setChatroomOption(ArrayList<String> option);
        void setMessageCanvasTitle(String newTitle);
        void setChatroomCanvasTitle(String newTitle);
        void setMessageArea(ArrayList<String> messageData);
        void setPinnedMessage(ArrayList<String> messageData);
    }
}
