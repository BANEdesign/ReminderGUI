package FinalProject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    //TODO put resultsets into list to display in Jlist
    //private final ArrayList<ReminderModel> remList;

    public ReminderGUI(Reminder reminderProgam){

        setContentPane(mainPanel);
        pack();
        setTitle("Reminders");
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initListeners();

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

}
