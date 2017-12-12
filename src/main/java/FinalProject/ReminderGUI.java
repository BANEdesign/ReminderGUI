package FinalProject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
//import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * The UI which stores the Listeners
 * and displays info on the GUI
 */
public class ReminderGUI extends JFrame implements WindowListener{

    protected JList reminderList;
    protected JTextField reminderTextField;
    protected JButton addNewReminder;
    protected JButton removeButton;
    protected JButton Calender;
    protected JPanel mainPanel;
    protected JScrollPane scrollPane;
    protected JLabel newReminderLabel;
    protected JLabel reminderLabel;
    private JSpinner dateSpinner;
    private JLabel currentTimeLabel;

    DBAccess db = new DBAccess();

    public ReminderGUI(Reminder reminderProgam) {

        setContentPane(mainPanel);
        pack();
        setTitle("Reminders");
        setVisible(true);
        addWindowListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initListeners();
        configureDateSpinner();
        getCurrentTime();

        reminderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Set to single selection
        reminderList.setModel(db.loadRemindersDB());
        setNumberOfReminders();

    }

    private void initListeners(){
        addNewReminder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String task = reminderTextField.getText();
                if(task == null || task.trim().equals("")){
                    showAlertDialog("Please enter a valid reminder");
                }
                SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
                String date = format.format(dateSpinner.getValue());

                db.addReminderToDB(task,date);
                updateList();

                //This is the yes/no prompt to add to calendar and create alert
                int reply = JOptionPane.showConfirmDialog(null,
                        "Would you like to add this reminder to your calender and " +
                                "create an alert?","Add To Calendar",JOptionPane.YES_NO_OPTION);
                if(reply==JOptionPane.YES_OPTION){
                    //adds reminder to calender
                    GoogleAPI.createEvent(task,date);
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            //TODO add joption pane to ask user if they want to delete
                String value = reminderList.getSelectedValue().toString();
                String taskOnly = value.substring(0, value.indexOf("due")-1); //extracts only the task
                db.deleteReminderFromDB(taskOnly);
                updateList();
            }
        });
        Calender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String url = "https://calendar.google.com/calendar/";
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
                }
                catch (java.io.IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }
        });
    }


    protected void showAlertDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * // Create a DateEditor to configure the way dates are displayed and edited
     // Define format the dates will have
     // Attempt to prevent invalid input
     // Allow user to type as well as use up/down buttons
     //
     */
    protected void configureDateSpinner(){

        SpinnerDateModel spinnerDateModel = new SpinnerDateModel(new Date(), new Date(0), new Date(30000000000000L), Calendar.DAY_OF_YEAR);
        dateSpinner.setModel(spinnerDateModel);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();

        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        dateSpinner.setEditor(editor);
    }
    protected void getCurrentTime(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
        Date exampleDate = new Date();     // test date
        String dateStr = format.format(exampleDate);
        currentTimeLabel.setText(dateStr);
    }
    public void setNumberOfReminders(){
        String number = String.valueOf(db.reminders.size());
        String reminderStr = String.format("Reminders (%s)",number);
        reminderLabel.setText(reminderStr);
    }
    public void updateList(){
        reminderList.setModel(db.loadRemindersDB());
        setNumberOfReminders();
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        System.out.println("Closing Time");
        db.shutDown();
    }

    public void windowClosed(WindowEvent e) {

    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
}
