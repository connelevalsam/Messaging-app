package sample.resource;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by connelblaze on 9/13/17.
 */
public class Login {
    @FXML
    TextField txtUsername;
    @FXML
    PasswordField txtPassword;

    Boolean isLoggedIn    = false;
    final String username = "admin";
    final String password = "admin";

    public void login(ActionEvent actionEvent) {
        if (txtUsername.getText() == "" && txtPassword.getText() == "") {
            String title   = "An ERROR!";
            String header  = "Error while logging in";
            String content = "Fields are empty, input username and password please";
            printErrMessage(title, header, content);
            clears();
        } else if (txtUsername.getText().equals(username) && txtPassword.getText().equals(password)) {
            isLoggedIn = true;
            Parent window1;
            try {
                window1 = FXMLLoader.load(getClass().getResource("../view/sample.fxml"));
                Stage window1Stage;
                Scene window1Scene = new Scene(window1, 850, 650);
                window1Stage = Main.homeStage;
                window1Stage.setTitle("Message Contact");
                window1Stage.setScene(window1Scene);
                window1Stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String title   = "System Information";
            String header  = "Incorrect details";
            String content = "Username and password incorrect. Rectify.";
            printInfoMessage(title, header, content);
            clears();
        }
    }

    public void printErrMessage(String title, String header, String content){
        try {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void printInfoMessage(String title, String header, String content){
        try {
            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clears() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtUsername.setPromptText("username");
        txtPassword.setPromptText("password");
    }
}
