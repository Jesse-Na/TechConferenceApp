package GUISystem;

import LoginSystem.LoginOptionsFacade;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A GUI menu class which allows users to reset passwords.
 *
 * @author Ziwen
 */
public class PasswordMenu extends Application {

    private LoginOptionsFacade loginOptionsFacade;
    private UserMenuGetter userMenuGetter;

    /**
     * Starts this menu.
     *
     * @param primaryStage The primaryStage of the application.
     */
    @Override
    public void start(Stage primaryStage){
        VBox vbox = new VBox(5);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setPrefSize(500, 600);
        Scene scene = new Scene (vbox);

        Text title = new Text ("Change password");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-bold.ttf"),20));

        Text passwordChangeMessage = new Text();
        passwordChangeMessage.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-bold.ttf"), 12));

        TextField usernameField = new TextField();
        usernameField.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-regular.ttf"), 12));

        PasswordField currentPassword = new PasswordField();
        currentPassword.setFont(Font.font("Times New Roman"));

        PasswordField newPassword = new PasswordField();
        newPassword.setFont(Font.font("Times New Roman"));

        PasswordField passwordConfirmField = new PasswordField();
        passwordConfirmField.setFont(Font.font("Times New Roman"));

        Text username = new Text("Username");
        username.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-regular.ttf"), 12));
        Text passwordText = new Text("Current Password");
        passwordText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-regular.ttf"), 12));
        Text newPasswordText = new Text("New Password");
        newPasswordText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-regular.ttf"), 12));
        Text confirmPasswordText = new Text("Confirm New Password");
        confirmPasswordText.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-regular.ttf"), 12));

        Button changePasswordButton = new Button("Change password");
        changePasswordButton.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-bold.ttf"), 12));
        changePasswordButton.setOnAction(event -> {
            if(newPassword.getText().length()<1|passwordConfirmField.getText().length()<1){
                passwordChangeMessage.setFill(Color.RED);
                passwordChangeMessage.setText("Please ensure you are entering a valid password");
            }
            else if(!newPassword.getText().equals(passwordConfirmField.getText())){
                passwordChangeMessage.setFill(Color.RED);
                passwordChangeMessage.setText("New password does not match password confirmation");
            }
            else if(!loginOptionsFacade.getUser().getUserName().equals(usernameField.getText())){
                passwordChangeMessage.setFill(Color.RED);
                passwordChangeMessage.setText("Invalid username");
            }
            else if(loginOptionsFacade.resetPassword(usernameField.getText(), currentPassword.getText(), newPassword.getText())){
                passwordChangeMessage.setFill(Color.GREEN);
                passwordChangeMessage.setText("Password change successful");
            }
            else{
                passwordChangeMessage.setFill(Color.RED);
                passwordChangeMessage.setText("Invalid password");
            }
        });

        Button goBack = new Button("Go back");
        goBack.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/os-bold.ttf"), 12));
        goBack.setOnAction(event -> userMenuGetter.goBack(primaryStage));

        vbox.getChildren().addAll(title,username, usernameField, passwordText, currentPassword, newPasswordText, newPassword, confirmPasswordText, passwordConfirmField, changePasswordButton,passwordChangeMessage,goBack);
        primaryStage.setTitle("Change Password");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Sets the loginOptionsFacade of this class.
     *
     * @param loginOptionsFacade An instance of LoginOptionsFacade.
     */
    public void setLogin(LoginOptionsFacade loginOptionsFacade){
        this.loginOptionsFacade = loginOptionsFacade;
    }

    /**
     * Sets the userMenuGetter interface of this class.
     *
     * @param userMenuGetter An instance of the UserMenuGetter interface.
     */
    public void setUserMenuGetter(UserMenuGetter userMenuGetter){
        this.userMenuGetter = userMenuGetter;
    }
}
