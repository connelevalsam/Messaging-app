package sample.model;

import sample.util.DBUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageDAO {

    //list out all contact phones into array
    public static ArrayList getAllContacts() throws SQLException, ClassNotFoundException {
        ArrayList list = new ArrayList();
        String query = "SELECT phone FROM users";
        ResultSet rsContacts = DBUtil.dbExecuteQuery(query);
        while (rsContacts.next()) {
            list.add(rsContacts.getString("phone"));
        }
        return list;
    }

    //list out selected contacts phone into array
    public static ArrayList getEmContacts(String classGroup, String levelGroup, String genderGroup, String healthGroup) throws SQLException, ClassNotFoundException {
        ArrayList list = new ArrayList();
        String query = "SELECT phone FROM users WHERE class_group = '"+ classGroup + "' AND level_group = '" + levelGroup + "' AND (gender_group = '" + genderGroup + "' AND health_group = '"+ healthGroup +"');";
        ResultSet rsContacts = DBUtil.dbExecuteQuery(query);
        while (rsContacts.next()) {
            list.add(rsContacts.getString("phone"));
        }
        return list;
    }

    //return messages
    public static String getMessages(String messageTitle) throws SQLException, ClassNotFoundException {
        String list ="";
        String query = "SELECT message FROM message_box WHERE title = "+ messageTitle;
        ResultSet rsContacts = DBUtil.dbExecuteQuery(query);
        while (rsContacts.next()) {
            list = rsContacts.getString("message");
        }
        return list;
    }

    //*************************************
    //INSERT a message
    //*************************************
    public static void insertMessage(String title, String message, Date created_at, Date updated_at) throws SQLException, ClassNotFoundException {
        //Declare a DELETE statement
        String updateStmt = "INSERT INTO message_box (title, message, created_at, updated_at) VALUES ('" + title + "', '" + message + "', '" + created_at + "', '" + updated_at + "');";

        //Execute INSERT operation
        try {
            DBUtil.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while saving...: " + e);
            throw e;
        }
    }
}
