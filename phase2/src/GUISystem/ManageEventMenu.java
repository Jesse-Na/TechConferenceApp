package GUISystem;

import EventSystem.EventCreatorPresenter;
import LoginSystem.LoginOptionsFacade;
import RoomSystem.RoomPresenter;
import UserSystem.Registrar;
import UserSystem.Speaker;
import UserSystem.User;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * A menu for creating, modifying, deleting events.
 *
 * @author Fred, Tao
 */
public class ManageEventMenu extends Application implements EventCreatorPresenter.EventCreatorInterface{

    private EventCreatorPresenter ecp;
    private RoomPresenter rp;
    private UserMenuGetter mg;
    private ListView<VBox> allEvents;
    private LoginOptionsFacade facade;

    /**
     * Sets the presenters for this class.
     *
     * @param ecp An instance of EventCreatorPresenter.
     * @param rp An instance of RoomPresenter.
     */
    public void setEventCreatorElements(EventCreatorPresenter ecp, RoomPresenter rp) {
        this.ecp = ecp;
        this.rp = rp;
    }

    /**
     * Sets the userMenuGetter interface of this class.
     *
     * @param userMenuGetter An instance of the UserMenuGetter Interface.
     */
    public void setUserMenuGetter(UserMenuGetter userMenuGetter) {
        this.mg = userMenuGetter;
    }

    /**
     * Sets the loginOptionsFacade for this class.
     *
     * @param facade An instance of LoginOptionsFacade.
     */
    public void setFacade(LoginOptionsFacade facade) {this.facade = facade;}

    /**
     * Starts the manageEvents menu.
     *
     * @param primaryStage The primaryStage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setPrefSize(500, 600);
        HBox topView = new HBox(5);
        VBox botView = new VBox();
        allEvents = new ListView<>();
        Label title = new Label("Events");
        botView.getChildren().addAll(title, allEvents);

        root.add(topView, 0,0);
        root.add(botView, 0, 1);

        Button createButton = new Button("Create");
        Button modifyButton = new Button("Modify");
        Button removeButton = new Button("Remove");
        Button goBack = new Button("Back");
        topView.getChildren().addAll(createButton, modifyButton, removeButton, goBack);

        //preliminary loading
        ecp.viewEvents();

        // event handlers
        createButton.setOnAction(e -> {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Create Event");

            VBox parent = new VBox();
            parent.setPrefSize(500, 600);
            HBox name = new HBox();
            Label nameLabel = new Label("Name");
            TextField nameInput = new TextField();
            name.getChildren().addAll(nameLabel, nameInput);


            HBox time = new HBox();
            Label timeLabel = new Label("Date format(yyyy-MM-dd HH:mm):");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            TextField timeInput = new TextField();
            time.getChildren().addAll(timeLabel, timeInput);

            HBox duration = new HBox();
            Label durationLabel = new Label("Duration");
            TextField durationInput = new TextField();
            duration.getChildren().addAll(durationLabel, durationInput);

            //populate a list view with all speakers
            VBox speaker_list = new VBox();
            Label speakerLabel = new Label("Select Speakers (hold ctrl to multi select)");
            ListView<String> speakers = new ListView<>();
            ArrayList<String> allSpeakers = getSpeakers();

            for (String s: allSpeakers) {
                speakers.getItems().add(s);
            }
            speakers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            speaker_list.getChildren().addAll(speakerLabel, speakers);

            //populate a list view with all rooms
            VBox roomBox = new VBox();
            Label roomListLabel = new Label("Rooms");
            ListView<String> room_list = new ListView<>();
            listRooms(room_list);
            roomBox.getChildren().addAll(roomListLabel, room_list);

            Button submitButton = new Button("Submit");
            submitButton.setOnAction(ae -> {
                String selectedRoom = room_list.getSelectionModel().getSelectedItem();
                String[] list = selectedRoom.split("\n");
                String roomName = list[0];
                String roomName1 = roomName.substring(roomName.indexOf(":")+1).trim();
                String capacityInput = list[1];
                int capacityInput1 = Integer.parseInt(capacityInput.substring(capacityInput.indexOf(":")+1).trim());
                if (isValidTime(timeInput, formatter)  && isInt(durationInput)) {
                    ObservableList<String> selectedItems = speakers.getSelectionModel().getSelectedItems();
                    Registrar registrar = facade.getRegistrar();
                    boolean allSpeakersValid = true;
                    ArrayList<String> speakersNameList = new ArrayList<>();
                    for(String s: selectedItems) {
                        if (registrar.userExisting(s)) {
                            User user = registrar.getUserByUserName(s);
                            if (user instanceof Speaker) {
                                speakersNameList.add(s);
                            } else {
                                allSpeakersValid = false;
                            }
                        }
                    }
                    long duration1 = Integer.parseInt(durationInput.getText());
//                    int capacity1 = Integer.parseInt(capacityInput.getText());
                    if(allSpeakersValid){
                        ecp.promptEventCreation(nameInput.getText(), roomName1,
                                LocalDateTime.parse(timeInput.getText(), formatter), duration1, speakersNameList, capacityInput1);
                    }
                    else {
                        createPopUp("Please input a valid Speaker. If you don't have any, please create a speaker account.");
                    }

                } else {
                    createPopUp("Please enter in proper format");
                }
                allEvents.getItems().clear();
                ecp.viewEvents();

            });

            Button closeButton = new Button("Close");
            closeButton.setOnAction(ae -> window.close());

            parent.getChildren().addAll(name, time, duration, speaker_list, roomBox, submitButton, closeButton);
            Scene scene = new Scene(parent);
            window.setScene(scene);
            window.showAndWait();
        });

        modifyButton.setOnAction(e -> {
            Stage window = new Stage();
            window.initModality(Modality.WINDOW_MODAL);
            window.setTitle("Modify Event");
            VBox parent = new VBox();

            HBox input = new HBox();
            TextField inputText = new TextField();
            Label inputLabel = new Label("Enter the event ID to modify: ");
            input.getChildren().addAll(inputLabel, inputText);

            Button submitButton = new Button("Submit");
            Button closeButton = new Button("Close");

            submitButton.setOnAction(sb -> {
                try {
                    long id = Long.parseLong(inputText.getText());

                    Stage secondWindow = new Stage();
                    secondWindow.initModality(Modality.APPLICATION_MODAL);
                    secondWindow.setTitle("Modify Event");

                    VBox child = new VBox();

                    HBox name = new HBox();
                    TextField nameInput = new TextField();
                    Label nameLabel = new Label("Adjust event name: ");
                    name.getChildren().addAll(nameLabel, nameInput);

                    HBox capacity = new HBox();
                    TextField capacityInput = new TextField();
                    Label capacityLabel = new Label("Adjust capacity: ");
                    capacity.getChildren().addAll(capacityLabel, capacityInput);

                    Button changeButton = new Button("Modify");
                    Button exitButton = new Button("Close");

                    changeButton.setOnAction(mb -> {
                        try {
                            if(!nameInput.getText().equals("") || !nameInput.getText().trim().isEmpty()) {
                                ecp.promptSetName(id, nameInput.getText());
                            }

                            if(!capacityInput.getText().equals("") || !capacityInput.getText().trim().isEmpty()) {
                                ecp.promptSetCapacity(id, Integer.parseInt(capacityInput.getText()));
                            }
                            secondWindow.close();
                        } catch(Exception ex){
                            createPopUp("Error, please check arguments");
                        }
                    });

                    child.setOnKeyPressed(k -> {
                        if(k.getCode() == KeyCode.ENTER) {
                            try {
                                if(!nameInput.getText().equals("") || !nameInput.getText().trim().isEmpty()) {
                                    ecp.promptSetName(id, nameInput.getText());
                                }

                                if(!capacityInput.getText().equals("") || !capacityInput.getText().trim().isEmpty()) {
                                    ecp.promptSetCapacity(id, Integer.parseInt(capacityInput.getText()));
                                }
                                secondWindow.close();
                            } catch(Exception ex){
                                createPopUp("Error, please check arguments");
                            }
                        }
                    });

                    exitButton.setOnAction(c -> secondWindow.close());

                    child.getChildren().addAll(name, capacity, changeButton, exitButton);
                    Scene newScene = new Scene(child);
                    secondWindow.setScene(newScene);
                    secondWindow.showAndWait();

                } catch (NumberFormatException n) {
                    createPopUp("Invalid Event ID");
                }

                allEvents.getItems().clear();
                ecp.viewEvents();

            });

            closeButton.setOnAction(cb -> window.close());

            parent.getChildren().addAll(input, submitButton, closeButton);
            Scene scene = new Scene(parent);
            window.setScene(scene);
            window.showAndWait();
        });


        removeButton.setOnAction(e -> {
            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Delete Event");

            VBox parent = new VBox();
            TextField input = new TextField();
            Button submitButton = new Button("Remove");
            Button closeButton = new Button("Close");

            submitButton.setOnAction(ae -> {

                try{
                    long id = Long.parseLong(input.getText());
                    ecp.promptEventDeletion(id);
                } catch(NumberFormatException n) {
                    createPopUp("Must be an Number");
                }
                allEvents.getItems().clear();
                ecp.viewEvents();
            });

            closeButton.setOnAction(ae -> window.close());

            parent.getChildren().addAll(input, submitButton, closeButton);
            Scene scene = new Scene(parent);
            window.setScene(scene);
            window.showAndWait();
        });

        goBack.setOnAction(e -> mg.goBack(primaryStage));

        //Scene scene = new Scene (root, 1280, 720);
        Scene scene = new Scene (root);
        primaryStage.setTitle("Manage Events");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void listRooms(ListView<String> roomList) {
        ArrayList<String> rooms = rp.displayRooms();
        for (String r: rooms) {
            roomList.getItems().add(r);
        }
    }
    private ArrayList<String> getSpeakers() {
        Registrar registrar = facade.getRegistrar();
        ArrayList<String> list = new ArrayList<>();
        for (User s : registrar.getUsersByType("Speaker")) {
            list.add(s.getUserName());
        }
        return list;
    }

    private boolean isValidTime(TextField time, DateTimeFormatter formatter) {
        try {
            LocalDateTime time1 = LocalDateTime.parse(time.getText(), formatter);
            return true;

        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isInt(TextField input) {
        try {
            long number = Long.parseLong(input.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Creates a visual representation of an event and adds it to a list of all events.
     *
     * @param name The name of the event.
     * @param id The id of the event.
     * @param time The time the event starts.
     * @param duration The duration of the event.
     * @param room The room of the event.
     * @param capacity The capacity of the event.
     * @param speakers The list of speakers at this event.
     */
    @Override
    public void loadAllEvents(String name,String id, String time, String duration, String room, String capacity, String speakers) {
        VBox eventContainer = new VBox();
        Label name1 = new Label(name);
        TextField id1 = new TextField(id);
        id1.setEditable(false);
        Label time1 = new Label(time);
        Label duration1 = new Label(duration);
        Label room1 = new Label(room);
        Label capacity1 = new Label(capacity);
        Label speakers1 = new Label(speakers);
        eventContainer.getChildren().addAll(name1, id1, time1, duration1, room1, capacity1, speakers1);
        allEvents.getItems().add(eventContainer);
    }

    /**
     * Creates a popup window that can be closed.
     *
     * @param message The message to display.
     */
    @Override
    public void createPopUp(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Pop up");

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);

        Label label = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(ae -> window.close());

        layout.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ENTER) {
                window.close();
            }
        });

        layout.getChildren().addAll(label, closeButton);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

}
