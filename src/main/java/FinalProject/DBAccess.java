package FinalProject;

import javax.swing.*;
import java.sql.*;

/**
 * Accesses the database, communicates
 * with the SQLite database.
 * creates Result Sets and sends them to the data model
 */
class DBAccess {

    private static Statement statement = null;
    private static Connection conn = null;
    private static ResultSet rs = null;

    //Todo implement edit functionality
    private static String editSQL = "UPDATE reminders SET task WHERE task = ?"; //Is this necessary?
    private static ResultSet resultSet;

    DefaultListModel reminders = new DefaultListModel();

    private void connect(){
        try{
            String JDBC_Driver = "org.sqlite.JDBC";
            Class.forName(JDBC_Driver);
            String db_url = "jdbc:sqlite:reminder.db";
            conn = DriverManager.getConnection(db_url);

            //todo add alert dialogs to display to users
        }catch (SQLException se){
            System.out.println("Error connecting to DB");
            se.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("Error loading driver");
            cnfe.printStackTrace();
        }
    }

    protected DefaultListModel loadRemindersDB() {

        try {
            System.out.println("Loading reminders from DB");

            connect();
            statement = conn.createStatement();
            String getDataSQL = "SELECT * FROM reminders"; //ORDER BY date ASC
            resultSet = statement.executeQuery(getDataSQL);

            if(reminders == null){
               formatResults();

            }else{
                reminders.clear();
                formatResults();
                }
            System.out.println("Loaded reminders.");
            resultSet.close();

            return reminders;

        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }catch(Exception e) {
            System.out.println("Error making list model");
            e.printStackTrace();
            return null;
        }
    }
    protected boolean addReminderToDB(String task, String date){

        try{
            connect();
            String insertSQL = "INSERT INTO reminders VALUES (?,?)";
            PreparedStatement psInsert = conn.prepareStatement(insertSQL);
            psInsert.setString(1,task);
            psInsert.setString(2,date);
            psInsert.executeUpdate();

            System.out.println("Added reminder to DB");
            return true; //could wait until connection closes for this

        }catch (SQLException se){
            System.out.println("Error adding reminder");
            se.printStackTrace();
            return false;

        }
    }
    protected boolean deleteReminderFromDB(String task) {

        try {
            connect();
            String deleteSQL = "DELETE FROM reminders WHERE task = ?";
            PreparedStatement psDelete = conn.prepareStatement(deleteSQL);
            psDelete.setString(1, task);
            psDelete.executeUpdate();
            psDelete.close();

            System.out.println("Deleted reminder from DB");
            conn.close();
            return true;
        } catch (SQLException se) {
            System.out.println("Error deleting reminder from DB");
            se.printStackTrace();
            return false;
        }
    }

    protected void formatResults() {

        try {
            while (resultSet.next()) {
                String task = resultSet.getString("task");
                String date = resultSet.getString("date");
                String output = String.format("%s due by: %s", task, date);
                reminders.addElement(output);

            }
        } catch (SQLException se) {
            System.out.println("Error formatting results");
            se.printStackTrace();
        }
    }
    protected void shutDown(){

            try {
        if (rs != null) {
            rs.close();
            System.out.println("Result set closed");
        }
    } catch (SQLException se) {
        se.printStackTrace();
    }

        try {
        if (statement != null) {
            statement.close();
            System.out.println("Statement closed");
        }
    } catch (SQLException se) {
        se.printStackTrace();
    }

        try {
        if (conn != null) {
            conn.close();
            System.out.println("Database connection closed");
        }
    } catch (SQLException se) {
        se.printStackTrace();
    }
}
}
