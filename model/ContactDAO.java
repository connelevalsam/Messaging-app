package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by connelblaze on 5/10/17.
 */
public class ContactDAO {
    public static ContactClass searchContact(String contactID) throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM users WHERE id=" + contactID;

        //Execute SELECT statement
        try {
            //Get ResultSet from executeQuery method
            ResultSet rsCon = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getContactFromResultSet method and get contact object
            ContactClass contact = getContactFromResultSet(rsCon);

            //Return contact object
            return contact;
        } catch (SQLException e) {
            System.out.println("While searching a contact with " + contactID + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    //Use ResultSet from DB as parameter and set Contact Object's attributes and return contact object.
    private static ContactClass getContactFromResultSet(ResultSet rs) throws SQLException {
        ContactClass contactClass = null;
        if (rs.next()) {
            contactClass = new ContactClass();
            contactClass.setContactId(rs.getInt("id"));
            contactClass.setFirstName(rs.getString("first_name"));
            contactClass.setLastName(rs.getString("last_name"));
            contactClass.setClassGroup(rs.getString("class_group"));
            contactClass.setLevelGroup(rs.getString("level_group"));
            contactClass.setGenderGroup(rs.getString("gender_group"));
            contactClass.setHealthGroup(rs.getString("health_group"));
            contactClass.setAgeGroup(rs.getInt("age_group"));
        }
        return contactClass;
    }

    //*******************************
    //SELECT Contacts
    //*******************************
    public static ObservableList<ContactClass> searchAllContacts() throws SQLException, ClassNotFoundException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM users";

        //Execute SELECT statement
        try {
            //Get ResultSet from executeQuery method
            ResultSet rsContacts = DBUtil.dbExecuteQuery(selectStmt);

            //Send ResultSet to the getContactList method and get contact object
            ObservableList<ContactClass> contactList = getContactList(rsContacts);

            //Return contact object
            return contactList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    //Select * from contacts operation
    private static ObservableList<ContactClass> getContactList(ResultSet rs) throws SQLException, ClassNotFoundException {
        //Declare a observable List which comprises of contact objects
        ObservableList<ContactClass> contactList = FXCollections.observableArrayList();

        while (rs.next()) {
            ContactClass contactClass = new ContactClass();
            contactClass.setContactId(rs.getInt("id"));
            contactClass.setFirstName(rs.getString("first_name"));
            contactClass.setLastName(rs.getString("last_name"));
            contactClass.setClassGroup(rs.getString("class_group"));
            contactClass.setLevelGroup(rs.getString("level_group"));
            contactClass.setGenderGroup(rs.getString("gender_group"));
            contactClass.setHealthGroup(rs.getString("health_group"));
            contactClass.setAgeGroup(rs.getInt("age_group"));
            //Add contact to the ObservableList
            contactList.add(contactClass);
        }
        //return empList (ObservableList of contacts)
        return contactList;
    }

    //*************************************
    //UPDATE a contact's username
    //*************************************
    public static void updateContactClass(String contactID, String classGroup) throws SQLException, ClassNotFoundException {
        //Declare a UPDATE statement
        String updateStmt = "UPDATE users SET class_group = '" + classGroup + "' WHERE id = " + contactID + ";";

        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //DELETE a contact
    //*************************************
    public static void deleteContactWithId(String contactID) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt ="DELETE FROM users WHERE id ='" + contactID + "';";

        //Execute UPDATE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //INSERT a contact
    //*************************************
    public static void insertContact(String firstName, String lastName, String classGroup, String levelGroup, String genderGroup, String healthGroup, int ageGroup) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt = "INSERT INTO users (first_name, last_name, class_group, level_group, gender_group, health_group, age_group) VALUES ('" + firstName + "', '" + lastName + "', '" + classGroup + "', '" + levelGroup + "', '" + genderGroup + "', '" + healthGroup + "', '" + ageGroup + "');";

        //Execute DELETE operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
}