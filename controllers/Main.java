package sample.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage homeStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        homeStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
        root.getStylesheets().add(getClass().getResource("../stylesheet/custom.css").toExternalForm());
        homeStage.setTitle("Message App");
        homeStage.setScene(new Scene(root, 600, 400));
        homeStage.show();
        homeStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
