package sample.resource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.ContactClass;
import sample.model.ContactDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by connelblaze on 9/14/17.
 */
public class Dashboard implements Initializable {
    @FXML
    TextField txtFullName,txtPhone,txtClass,txtLevel,txtGender,txtHealth,txtAge,txtContactID,txtNewClass;
    @FXML
    TextArea txtResultConsole;

    @FXML
    private TableView contactTable;
    @FXML
    private TableColumn<ContactClass, Integer> contactIDColumn;
    @FXML
    private TableColumn<ContactClass, String>  contactFullNameColumn;
    @FXML
    private TableColumn<ContactClass, String>  contactPhoneColumn;
    @FXML
    private TableColumn<ContactClass, String> contactClassGroupColumn;
    @FXML
    private TableColumn<ContactClass, String> contactLevelGroupColumn;
    @FXML
    private TableColumn<ContactClass, String> contactGenderGroupColumn;
    @FXML
    private TableColumn<ContactClass, String> contactHealthGroupColumn;
    @FXML
    private TableColumn<ContactClass, Integer> contactAgeGroupColumn;

    //**********************************************************************
    //contact control
    //**********************************************************************

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactIDColumn.setCellValueFactory(cellData -> cellData.getValue().contactIdProperty().asObject());
        contactFullNameColumn.setCellValueFactory(cellData -> cellData.getValue().FullNameProperty());
        contactPhoneColumn.setCellValueFactory(cellData -> cellData.getValue().PhoneProperty());
        contactClassGroupColumn.setCellValueFactory(cellData -> cellData.getValue().ClassGroupProperty());
        contactLevelGroupColumn.setCellValueFactory(cellData -> cellData.getValue().LevelGroupProperty());
        contactGenderGroupColumn.setCellValueFactory(cellData -> cellData.getValue().GenderGroupProperty());
        contactHealthGroupColumn.setCellValueFactory(cellData -> cellData.getValue().HealthGroupProperty());
        contactAgeGroupColumn.setCellValueFactory(cellData -> cellData.getValue().AgeGroupProperty().asObject());
    }

    //Populate Contact
    @FXML
    private void populateContact (ContactClass contact) throws ClassNotFoundException {
        //Declare and ObservableList for table view
        ObservableList<ContactClass> contactData = FXCollections.observableArrayList();
        //Add contact to the ObservableList
        contactData.add(contact);
        //Set items to the contactTable
        contactTable.setItems(contactData);
    }
    //Set contact information to Text Area
    @FXML
    private void setContactInfoToTextArea ( ContactClass contact) {
        txtResultConsole.setText("Name: " + contact.getFullName() +"\nPhone "+ contact.getPhone()+"\n" +
                "Contact ID: " + contact.getContactId());
    }
    //Populate Contact for TableView and Display Contact on TextArea
    @FXML
    private void populateAndShowContact(ContactClass contact) throws ClassNotFoundException {
        if (contact != null) {
            populateContact(contact);
            setContactInfoToTextArea(contact);
        } else {
            txtResultConsole.setText("This Contact does not exist!\n");
        }
    }
    //Populate Contact for TableView
    @FXML
    private void populateAllContacts (ObservableList<ContactClass> contactData) throws ClassNotFoundException {
        //Set items to the contactTable
        contactTable.setItems(contactData);
    }

    //insert contact
    public void addContact(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            int contactAge = Integer.parseInt(txtAge.getText());
            ContactDAO.insertContact(txtFullName.getText(),txtPhone.getText(),txtClass.getText(),txtLevel.getText(),txtGender.getText(),txtHealth.getText(),contactAge);

            txtResultConsole.setText("contact added! \n");
        } catch (SQLException e) {
            txtResultConsole.setText("Problem occurred while creating contact " + e);
            throw e;
        }
    }

    //search a contact
    public void searchContact(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            //Get Contact information
            ContactClass contact = ContactDAO.searchContact(txtContactID.getText());
            //Populate Contact on TableView and Display on TextArea
            populateAndShowContact(contact);
        } catch (SQLException e) {
            e.printStackTrace();
            txtResultConsole.setText("Error occurred while getting contact information from DB.\n" + e);
            throw e;
        }
    }

    //update a contact that's been promoted
    public void updateContact(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ContactDAO.updateContactClass(txtContactID.getText(),txtNewClass.getText());
            txtResultConsole.setText("update for, contact id: " + txtContactID.getText() + "\n");
        } catch (SQLException e) {
            txtResultConsole.setText("Problem occurred while updating class: " + e);
        }
    }

    //delete a contact
    public void deleteContact(ActionEvent actionEvent) throws SQLException, ClassNotFoundException  {
        try {
            ContactDAO.deleteContactWithId(txtContactID.getText());
            txtResultConsole.setText("Contact deleted! Contact id: " + txtContactID.getText() + "\n");
        } catch (SQLException e) {
            txtResultConsole.setText("Problem occurred while deleting contact " + e);
            throw e;
        }
    }

    //search all contacts
    public void searchAllContact(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            //Get all Contacts information
            ObservableList<ContactClass> contactData = ContactDAO.searchAllContacts();
            //Populate Contacts on TableView
            populateAllContacts(contactData);
        } catch (SQLException e){
            System.out.println("Error occurred while getting contacts information from book.\n" + e);
            throw e;
        }
    }

    public void closeOperation(ActionEvent actionEvent) {
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
    }
}
