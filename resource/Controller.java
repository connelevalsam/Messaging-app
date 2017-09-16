package sample.resource;

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
import sample.model.ContactDAO;
import sample.util.DBUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    AnchorPane viewGeneral;
    @FXML
    ScrollPane viewMessage;
    @FXML
    ComboBox cbxClass, cbxLevel, cbxGender, cbxHealth, cbxAge;
    @FXML
    TextArea txtMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("System Initialized...");
        viewGeneral.setVisible(false);

        String queryAge = "SELECT age_group FROM users";
        String ageTable = "age_group";

        try {
            comboRun(queryAge, ageTable, cbxAge);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
    Outside
    * */

    public void comboRun(String query, String result, ComboBox box) throws SQLException, ClassNotFoundException {
        ObservableList options = FXCollections.observableArrayList();
        Connection dbUtil = DBUtil.getConn();
        ResultSet rs = dbUtil.createStatement().executeQuery(query);
        while (rs.next()) {
            options.add(rs.getString(result));
        }
        dbUtil.close();
        rs.close();
        box.setItems(options);
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

    public void resetApp(ActionEvent actionEvent) {
        Dashboard reset = new Dashboard();
        reset.closeOperation(actionEvent);
    }

    public void sendMsg(ActionEvent actionEvent) {
        if ((cbxClass.getValue() == null) ||
                (cbxLevel.getValue() == null) ||
                (cbxGender.getValue() == null) ||
                (cbxHealth.getValue() == null) ||
                (cbxAge.getValue() == null)) {
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
            int a = Integer.parseInt(cbxAge.getValue().toString());
            String t = txtMessage.getText();

            /*================================================/
            *   API Call
            *===============================================*/
            try {
                ArrayList recipients = ContactDAO.getEmContacts(c,l,a);
                String xmlrecipients = "";
                String username = "your_email_address";
                String apikey = "your_apikey";
                String sendername = "SenderName";
                String message = t;
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
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
}
