package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.model.MessageDAO;
import sample.util.DBUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.NoConnectionPendingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    SplitPane splitPane;
    @FXML
    AnchorPane viewGeneral, leftPane, rightPane, viewMsg, landingPage;
    @FXML
    ScrollPane viewMessage;
    @FXML
    ComboBox cbxClass, cbxLevel, cbxGender, cbxHealth, cbxSearchTitle;
    @FXML
    TextArea txtMessage, txtSavedMsg, txtGeneralMessage;
    @FXML
    ToggleGroup sendTo;
    @FXML
    TextField txtTitle;
    @FXML
    Button btnSendMsg, btnSaveMsg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("System Initialized...");
        viewGeneral.setVisible(false);
        viewMsg.setVisible(false);
        viewMessage.setVisible(false);
        leftPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.25));
    }

    //===========================================================
    //general view
    public void msgGeneral(ActionEvent actionEvent) {
        viewGeneral.setVisible(true);
        viewMessage.setVisible(false);
        viewMsg.setVisible(false);
        landingPage.setVisible(false);
    }

    //send messages
    public void sendGeneralMsg(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ArrayList arr = MessageDAO.getAllContacts();
        String t = txtGeneralMessage.getText();
        for (int i = 0; i < arr.size(); i++) {
            System.out.println("- " + i + " Number: " + arr.get(i) + "\nMessage: " + t);
        }
    }

    //===========================================================================
    //create and view contacts
    public void createNew(ActionEvent actionEvent) {
        Parent window1;
        try {
            window1 = FXMLLoader.load(getClass().getResource("../view/adminDashboard.fxml"));
            window1.getStylesheets().add(getClass().getResource("../stylesheet/custom.css").toExternalForm());
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

    //reset the app
    public void resetApp(ActionEvent actionEvent) {
        Dashboard reset = new Dashboard();
        reset.closeOperation(actionEvent);
    }


    //========================================================================
    //groups view
    public void sendMessage(ActionEvent actionEvent) {
        viewMessage.setVisible(true);
        viewGeneral.setVisible(false);
        viewMsg.setVisible(false);
        landingPage.setVisible(false);
    }
    //groups send msg
    public void sendMsg(ActionEvent actionEvent) {
        if (    (cbxClass.getValue() == null) ||
                (cbxLevel.getValue() == null) ||
                (cbxGender.getValue() == null) ||
                (cbxHealth.getValue() == null) ) {
            String title   = "An ERROR!";
            String header  = "Error while sending message.";
            String content = "Not selected groups to send to.";
            printErrMessage(title, header, content);
        } else if (txtMessage.getText() == "") {
            String title   = "An ERROR!";
            String header  = "Error while sending message.";
            String content = "No message entered.";
            printErrMessage(title, header, content);
        } else {
            String c = cbxClass.getValue().toString();
            String l = cbxLevel.getValue().toString();
            String g = cbxGender.getValue().toString();
            String h = cbxHealth.getValue().toString();
            String t = txtMessage.getText();


                /*================================================/
                *    TEST
                *    ArrayList arr = ContactDAO.getEmContacts(c,l,g,h);
                for (int i = 0; i < arr.size(); i++) {
                    System.out.println("- "+i +" Number: "+ arr.get(i) + "\nMessage: "+ t);
                }
                * ==============================================*/
            try {
                ArrayList recipients = MessageDAO.getEmContacts(c,l,g,h);
                messageAPI(recipients, t);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            resetApp(actionEvent);
        }
    }

    //print error message
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

    //print info message
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

    /*================================================/
     *   API Call
     *===============================================*/
    public void messageAPI(ArrayList recipients, String messageBox) {
        try {
            String xmlrecipients = "";
            String username = "david.ekefre@gmail.com";
            String apikey = "852b5f81b9dab0f60df72cd21c671caf4646a012";
            String sendername = "connelblaze";
            String message = messageBox;
            String flash = "0";
            String theoutput = "";
            String randmsgid = Double.toString(Math.random());
            for (int i =1; i < recipients.size(); i++) {
                xmlrecipients += "<gsm><msidn>"+ recipients.get(i) + "</msidn><msgid>" + randmsgid + "_" + i + "</msgid>" + "</gsm>";
            }
            String xmlrequest =
                    "<SMS>\n"
                            + "<auth>"
                            + "<username>" + username + "</username>\n"
                            + "<apikey>" + apikey + "</apikey>\n"
                            + "</auth>\n"
                            + "<message>"
                            + "<sender>" + sendername + "</sender>\n"
                            + "<messagetext>" + message + "</messagetext>\n"
                            + "<flash>" + flash + "</flash>\n"
                            + "</message>\n"
                            + "<recipients>\n"
                            + xmlrecipients
                            + "</recipients>\n"
                            + "</SMS>";

            String theurl = "http://api.ebulksms.com:8080/sendsms.xml";
            Controller requester = new Controller();
            theoutput = requester.postXMLData(xmlrequest, theurl);
            if(theoutput.contains("100")){
                String title   = "System Information";
                String header  = "contains 100";
                String content = "100";
                printInfoMessage(title, header, content);
            }
            else{
                String title   = "System Information";
                String header  = "output of message";
                String content = theoutput;
                printInfoMessage(title, header, content);
            }
        } catch (NoConnectionPendingException e) {
            String title   = "Error";
            String header  = "Timeout";
            String content = "Check your internet connection";
            printErrMessage(title, header, content);
        }
    }

    /*=========================================/
    *    the API for BULK SMS
    * ========================================*/
    public String postXMLData(String xmldata, String urlpath){
        String result = "";
        try {
            URL myurl = new URL(urlpath);
            URLConnection urlconn = myurl.openConnection();

            urlconn.setRequestProperty("Content-Type", "text/xml");
            urlconn.setDoOutput(true);
            urlconn.setDoInput(true);
            urlconn.connect();

            //Create a writer to the url
            PrintWriter pw = new PrintWriter(urlconn.getOutputStream());
            pw.write(xmldata, 0, xmldata.length());
            pw.close();

            //Create a reader for the output of the connection
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                result = result + line + "\n";
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //==============================================================
    //saved messages view
    public void savedMsg(ActionEvent actionEvent) {
        viewMsg.setVisible(true);
        viewGeneral.setVisible(false);
        viewMessage.setVisible(false);

        //check for messages
        String queryTitle = "SELECT title FROM message_box";
        String titleColumn = "title";

        try {
            if (cbxSearchTitle.getSelectionModel().getSelectedItem() == null) {
                searchTitle(queryTitle, titleColumn, cbxSearchTitle);
            } else {
                searchTitle(queryTitle, titleColumn, cbxSearchTitle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //save messages
    public void saveMsg(ActionEvent actionEvent) {
        String title       = txtTitle.getText();
        String message     = txtSavedMsg.getText();
        Date createDate    = new Date(System.currentTimeMillis());
        Date updateDate    = new Date(System.currentTimeMillis());
        try {
            MessageDAO.insertMessage(title,message,createDate,updateDate);
            String alertTitle   = "Message Information";
            String header  = "Saving Message";
            String content = title+"\nMessage saved successfully";
            printInfoMessage(alertTitle, header, content);
            resetApp(actionEvent);
        } catch (SQLException e) {
            String alertTitle = "Message Error!";
            String header     = "What went wrong?";
            String content    = "Failed to connect to database";
            printErrMessage(alertTitle, header, content);
        } catch (ClassNotFoundException e) {
            String alertTitle = "Message Error!";
            String header     = "What went wrong?";
            String content    = "Service couldn't connect.";
            printErrMessage(alertTitle, header, content);
        }
    }

    //send messages
    public void sendSavedMessage(ActionEvent actionEvent) {
        RadioButton selectedRadioButton = (RadioButton) sendTo.getSelectedToggle();
        String toggleGroupValue = selectedRadioButton.getText();
        if (toggleGroupValue.equals("General")) {
            String savedMsg = txtSavedMsg.getText();
            msgGeneral(actionEvent);
            txtSavedMsg.setText("");
            txtTitle.setText("");
            txtGeneralMessage.setText(savedMsg);
        } else {
            String savedMsg = txtSavedMsg.getText();
            sendMessage(actionEvent);
            txtSavedMsg.setText("");
            txtTitle.setText("");
            txtMessage.setText(savedMsg);
        }
    }

    //search titles and pupolate combobox
    public void searchTitle(String query, String titleColumn, ComboBox box) throws SQLException, ClassNotFoundException {
        ObservableList options = FXCollections.observableArrayList();
        Connection dbUtil = DBUtil.getConn();
        ResultSet rs = dbUtil.createStatement().executeQuery(query);
        while (rs.next()) {
            options.add(rs.getString(titleColumn));
        }
        dbUtil.close();
        rs.close();
        box.setItems(options);
    }

    //button for search
    public void searchMessage(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String getTitle   = cbxSearchTitle.getValue().toString();
        String setTitle = "\""+getTitle+"\"";
        String getMessage = MessageDAO.getMessages(setTitle);
        txtTitle.setText(getTitle);
        txtSavedMsg.setText(getMessage);
    }

    public void logout(ActionEvent actionEvent) {
        String alertTitle   = "App Information";
        String header       = "Logging out...";
        String content      = "Good bye...";
        printInfoMessage(alertTitle, header, content);
        Parent window1;
        try {
            window1 = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            window1.getStylesheets().add(getClass().getResource("../stylesheet/custom.css").toExternalForm());
            Stage window1Stage;
            Scene window1Scene = new Scene(window1, 600, 400);
            window1Stage = Main.homeStage;
            window1Stage.setTitle("Login");
            window1Stage.setScene(window1Scene);
            window1Stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
