package FinalProject;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * The UI which stores the Listeners
 * and displays info on the GUI
 */
public class ReminderGUI extends JFrame {
    private JList reminderList;
    private JTextField reminderTextField;
    private JButton addNewReminder;
    private JButton removeButton;
    private JButton Calender;
    private JSpinner dateSpinner;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JLabel newReminderLabel;
    private JLabel reminderLabel;
    protected static ReminderModel reminderModel;
    private final DBAccess dbAccess = new DBAccess();

    //TODO put resultsets into Jlist
    //private final ArrayList<ReminderModel> remList;

    public ReminderGUI(Reminder reminderProgam){

        setContentPane(mainPanel);
        pack();
        setTitle("Reminders");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initListeners();
        configureDateSpinner();
        dbAccess.loadRemindersDB();
        reminderList.setModel(reminderModel);

        //TODO setup datamodel

        //TODO add listeners

        //TODO add time by google calender api?

    }
    private void initListeners(){
        addNewReminder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String task = reminderTextField.getText();
                if(task == null || task.trim().equals("")){
                    showAlertDialog("Please enter a valid reminder");
                }

            }
        });
    }
    protected void showAlertDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    protected void configureDateSpinner() {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        SpinnerDateModel spinnerDateModel = new SpinnerDateModel(new Date(30000000000000L), new Date(), new Date(0), Calendar.DAY_OF_YEAR);
        dateSpinner.setModel(spinnerDateModel);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
        // Attempt to prevent invalid input
        formatter.setAllowsInvalid(false);
        // Allow user to type as well as use up/down buttons
        formatter.setOverwriteMode(true);
        // And tell the serviceDataSpinner to use this Editor
        dateSpinner.setEditor(editor);
    }
}
