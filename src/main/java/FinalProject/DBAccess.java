package FinalProject;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Accesses the database, communicates
 * with the SQLite database.
 * creates Result Sets and sends them to the data model
 */
public class DBAccess {

    private final String JDBC_Driver = "org.sqlite.JDBC";
    private final String db_url = "jdbc:sqlite:reminder.db";

    private static Statement statement = null;
    private static Connection conn = null;
    private static ResultSet rs = null;

    private static String insertSQL = "INSERT INTO reminders VALUES (?,?)";
    private static String deleteSQL = "DELETE FROM reminders WHERE task = ?";
    private static String editSQL = "UPDATE reminders SET task WHERE task = ?"; //Is this necessary?


    private static ResultSet resultSet;

    DefaultListModel reminders = new DefaultListModel();

    public void connect(){
        try{
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(db_url);

        }catch (SQLException se){
            System.out.println("Error connecting to DB");
            se.printStackTrace();
        }catch (ClassNotFoundException cnfe){
            System.out.println("Error loading driver");
            cnfe.printStackTrace();
        }
    }

    public DefaultListModel loadRemindersDB() {

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
    public boolean addReminderToDB(String task, String date){

        try{
            connect();
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
    public boolean deleteReminderFromDB(String task) {

        try {
            connect();
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
//                java.util.Date utilDate = new SimpleDateFormat("YYYY-MM-dd").parse(resultSet.getString("date"));
//                java.sql.Date date = new java.sql.Date(utilDate.getTime());
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
