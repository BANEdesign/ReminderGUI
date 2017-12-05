package FinalProject;

public class Reminder {

    ReminderGUI gui;

    public static void main(String[] args) {

        Reminder reminderProgram = new Reminder();
        reminderProgram.start();
    }

    public void start() {
        gui = new ReminderGUI(this);
    }
}