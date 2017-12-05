package FinalProject;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Orginizes data in the model
 * and sends the data back to the gui
 */
public class ReminderModel  {
    private String reminder;
    private Date date;

    ResultSet resultSet;

    public ReminderModel(ResultSet rs)throws Exception{
        this.resultSet = rs;
        loadFromSQL(rs);
    }
    public void loadFromSQL(ResultSet rs)throws SQLException {

        reminder = rs.getString("task");
        date = rs.getDate("date");
    }
}
