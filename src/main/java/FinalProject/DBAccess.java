package FinalProject;

import javax.swing.*;
import java.sql.*;
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

    ReminderModel reminderModel;
    DefaultListModel reminders = new DefaultListModel();
    private final ArrayList<ResultSet> remList = new ArrayList<ResultSet>();

    //TODO clean up unused code
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

//            Class.forName(JDBC_Driver);
//            conn = DriverManager.getConnection(db_url);
            connect();
            statement = conn.createStatement();
            String getDataSQL = "SELECT * FROM reminders ORDER BY date ASC";
            resultSet = statement.executeQuery(getDataSQL);

            if (rs != null) {
                rs.close();
            }
            if(reminders == null){
                reminders.addElement(rs.getString("task"));
                reminders.addElement(rs.getDate("date").toString());
//            if (reminderModel == null) {
//                reminderModel = new ReminderModel(rs);
//            } else {
//                reminderModel.updateResultSet(rs);
            }else{
                reminders.clear();
                reminders.addElement(rs.getString("task"));
                reminders.addElement(rs.getDate("date").toString());
            }
            System.out.println("Loaded reminders.");
            return reminders;

//            statement.close();
//            rs.close();
            //conn.close()?



        } catch (SQLException se) {
            se.printStackTrace();
            return null;
//        } catch (ClassNotFoundException cnfe) {
//            System.out.println("Driver not found :(");
//            cnfe.printStackTrace();
//            System.exit(-1);

        }catch(Exception e) {
            System.out.println("Error making list model");
            e.printStackTrace();
            return null;
        }

    }
    public boolean addReminderToDB(String task, Date date){

        //TODO simplify this method

        try{

//            Class.forName(JDBC_Driver);
//            conn = DriverManager.getConnection(db_url);
            connect();
            PreparedStatement psInsert = conn.prepareStatement(insertSQL);
            psInsert.setString(1,task);
            psInsert.setDate(2,date);
            psInsert.executeUpdate();
//            psInsert.close();
            System.out.println("Added reminder to DB");
            return true; //could wait until connection closes for this

        }catch (SQLException se){
            System.out.println("Error adding reminder");
            se.printStackTrace();
            return false;

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
