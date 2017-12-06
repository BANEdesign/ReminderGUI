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


    public void connect() throws Exception {
        try {
            Class.forName(JDBC_Driver);
            conn = DriverManager.getConnection(db_url);
        } catch (SQLException se) {
            System.out.println("Error Connecting to DB");
            se.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error can't find driver");
            cnfe.printStackTrace();
        }
    }

        //TODO use this method in GUI at addNew event handler
        public boolean loadRemindersDB (){
            ArrayList<ReminderModel> remindersList = new ArrayList<ReminderModel>();
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
                if (reminderModel == null) {
                    reminderModel = new ReminderModel(rs);
                } else {
                    reminderModel.updateResultSet(rs);
                }
                System.out.println("Loaded " + remindersList.size() + "reminders.");
                return true;
//            statement.close();
//            rs.close();
                //conn.close()?
                //TODO delete unused code


            } catch (SQLException se) {
                se.printStackTrace();
                return false;
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Driver not found :(");
                cnfe.printStackTrace();
                System.exit(-1);
                return false;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
        }
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
