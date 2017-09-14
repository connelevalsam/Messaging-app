package sample.resource;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.model.ContactClass;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    AnchorPane viewGeneral;
    @FXML
    ScrollPane viewMessage;
    @FXML
    ComboBox<ContactClass> cbxClass, cbxLevel, cbxGender, cbxHealth, cbxAge;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("System Initialized...");
        viewGeneral.setVisible(false);

        ContactClass contact = new ContactClass();
        cbxClass = new ComboBox<>();
        cbxClass.valueProperty().bindBidirectional(contact.ClassGroupProperty());
    }

    public void msgGeneral(ActionEvent actionEvent) {
        viewGeneral.setVisible(true);
        viewMessage.setVisible(false);
    }

    public void sendMessage(ActionEvent actionEvent) {
        viewMessage.setVisible(true);
        viewGeneral.setVisible(false);
    }

    public void createNew(ActionEvent actionEvent) {
        Parent window1;
        try {
            window1 = FXMLLoader.load(getClass().getResource("../view/adminDashboard.fxml"));
            Stage window1Stage;
            Scene window1Scene = new Scene(window1, 850, 650);
            window1Stage = Main.homeStage;
            window1Stage.setTitle("Message Contact");
            window1Stage.setScene(window1Scene);
            window1Stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
