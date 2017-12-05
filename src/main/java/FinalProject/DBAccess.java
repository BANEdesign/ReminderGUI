package FinalProject;

import java.sql.*;
import java.util.ArrayList;

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

    //TODO make SQLite db in terminal

    public ArrayList<ReminderModel> loadRemindersDB() {
        ArrayList<ReminderModel> remindersList = new ArrayList<ReminderModel>();
        try {
            System.out.println("Loading reminders from DB");

            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(db_url);
            statement = conn.createStatement();
            String getDataSQL = "SELECT * FROM reminders ORDER BY date ASC";
            resultSet = statement.executeQuery(getDataSQL);

            while (rs.next()) {
                remindersList.add(new ReminderModel(rs));
            }
            System.out.println("Loaded " + remindersList.size() + "reminders.");
            statement.close();
            rs.close();
            //conn.close()?
            //TODO make if/else statement to handle empty list
            return remindersList;

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Driver not found :(");
            cnfe.printStackTrace();
            System.exit(-1);
            return null;
        }
        return remindersList;
    }
    public boolean addReminderToDB(String task, Date date){
        //TODO format date before it goes into db, when you get it from the spinner
        try{
            System.out.println("Adding reminder to DB");
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(db_url);
            PreparedStatement psInsert = conn.prepareStatement(insertSQL);
            psInsert.setString(1,task);
            psInsert.setDate(2,date);
            psInsert.executeUpdate();
            psInsert.close();

            return true; //could wait until connection closes for this

        }catch (SQLException se){
            System.out.println("Error adding reminder");
            se.printStackTrace();
            return false;
        }catch (ClassNotFoundException cnfe){
            System.out.println("Driver error");
            cnfe.printStackTrace();
            System.exit(-1);
            return false;
        }
    }
}
