/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Chip
 */
public class Appointment
{
    private IntegerProperty appointmentId = new SimpleIntegerProperty();
    private IntegerProperty customerId = new SimpleIntegerProperty();
    private StringProperty title = new SimpleStringProperty(); 
    private StringProperty description = new SimpleStringProperty();; //notes
    private ObjectProperty<LocalDateTime> start = new SimpleObjectProperty();
    private ObjectProperty<LocalDateTime> end = new SimpleObjectProperty();
    
    public Appointment() {};
    
    public Appointment(int appiontmentId, int customerId, String title, String notes, LocalDateTime start, LocalDateTime end)
    {
        setAppointmentId(appiontmentId);
        setCustomerId(customerId);
        setTitle(title);
        setDescription(notes);
        setStart(start);
        setEnd(end);
        
    }

    public static ObservableList<Appointment> getAppointments(Locale locale) throws SQLException
    {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();        
        
        String queryString = "SELECT * FROM appointment";
        
        SqlHelperClass sql = new SqlHelperClass();
        
        try
        {
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
           // stmt.setString(0, "appointment");
            
            ResultSet rs = sql.executeQuery(stmt);
           
            while (rs.next())
            {
                appointments.add(new Appointment(rs.getInt("appointmentId"),
                        rs.getInt("customerId"), 
                        rs.getString("title"), 
                        rs.getString("description"), 
                        rs.getTimestamp("start").toLocalDateTime(), 
                        rs.getTimestamp("end").toLocalDateTime()));
            }
            
        }
        catch (SQLException e)
        {
            System.out.println("sql error in appointment: \n" + e.getMessage());
            
        }

        return appointments;
    }
    
    public static void addAppointmentToDb(Appointment appointment)
    {
        String queryString = "INSERT INTO appointment \n"  
                + "\t(appointmentId\n"
                + "\t,customerId\n"
                + "\t,title\n"
                + "\t,description\n"
                + "\t,start\n"
                + "\t,end\n"
                + "\t,createDate\n"
                + "\t,createdBy\n"
                + "\t,lastUpdate\n"
                + "\t,lastUpdateBy\n"
                + "\t,location,contact,url)"
                + "VALUES (\n"
                + "\t?\n"                       //1 appointmentId
                + "\t,?\n"                      //2 customerId
                + "\t,?\n"                      //3 title
                + "\t,?\n"                      //4 description
                + "\t,?\n"                      //5 start
                + "\t,?\n"                      //6 end
                + "\t,?\n"                      //7 createDate
                + "\t,?\n"                      //8 createdBy
                + "\t,?\n"                      //9 lastUpdate
                + "\t,?\n"                      //10 latUpdatedBy
                + "\t,?,?,?);";                 //11-13 location,contact,url
                   
        SqlHelperClass sql = new SqlHelperClass();
        try
        {
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            
            ZoneId utcZone = ZoneId.of("UTC");            
            Timestamp utcTimeStamp = Timestamp.from(LocalDateTime
                    .now().atZone(utcZone).toInstant());
                        
            String userName = UserClass.getInstance().getUserName();
            
            //stmt.setString(1, "appointment");
            stmt.setInt(1, appointment.getAppointmentId());
            stmt.setInt(2, appointment.getCustomerId());
            stmt.setString(3, appointment.getTitle());
            stmt.setString(4, appointment.getDescription());
            stmt.setTimestamp(5, Timestamp.from(appointment.getStart().atZone(utcZone).toInstant()));
            stmt.setTimestamp(6, Timestamp.from(appointment.getEnd().atZone(utcZone).toInstant()));
            stmt.setTimestamp(7, utcTimeStamp);
            stmt.setString(8, userName);
            stmt.setTimestamp(9, utcTimeStamp);
            stmt.setString(10, userName);
            stmt.setString(11, "");
            stmt.setString(12, "");
            stmt.setString(13, "");
            
            
            sql.executeUpdateQuery(stmt);
            
        } catch (SQLException e)
        {
            System.out.println("sql add error in appiontment: \n" + e.getMessage());
        }                
    }
    
        static void updateAppointmentInDb(Appointment appointment)
    {
        String queryString = "UPDATE ? \n"          //0
                + "SET "                            
                + "\tappointmentId = ? \n"          //1
                + "\t,customerId = ? \n"            //2
                + "\t,title = ? \n"                 //3
                + "\t,description = ? \n"           //4
                + "\t,start = ? \n"                 //5
                + "\t,end = ? \n"                   //6
//                + "\t,createDate = ? \n"            //7
//                + "\t,createdBy = ? \n"             //8
                + "\t,lastUpdate = ? \n"            //7
                + "\t,lastUpdateBy = ?) \n"         //8
                + "\tWHERE ? = ?;";                 //9 & 10
                
                   
        SqlHelperClass sql = new SqlHelperClass();
        try
        {
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            
            ZoneId utcZone = ZoneId.of("UTC");            
            Timestamp utcTimeStamp = Timestamp.from(LocalDateTime
                    .now().atZone(utcZone).toInstant());
                        
            String userName = UserClass.getInstance().getUserName();
            
            stmt.setString(0, "appointment");
            stmt.setInt(1, appointment.getAppointmentId());
            stmt.setInt(2, appointment.getCustomerId());
            stmt.setString(3, appointment.getTitle());
            stmt.setString(4, appointment.getDescription());
            stmt.setTimestamp(5, Timestamp.from(appointment.getStart().atZone(utcZone).toInstant()));
            stmt.setTimestamp(6, Timestamp.from(appointment.getEnd().atZone(utcZone).toInstant()));
//            stmt.setTimestamp(7, utcTimeStamp);
//            stmt.setString(8, userName);
            stmt.setTimestamp(7, utcTimeStamp);
            stmt.setString(8, userName);
            stmt.setString(9,"appointmentId");
            stmt.setInt(10,appointment.getAppointmentId());
            
            sql.executeQuery(stmt);
            
        } catch (SQLException e)
        {
            System.out.println("sql add error in appiontment: \n" + e.getMessage());
        }                
    }
    
    public IntegerProperty appointmentIdProperty() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId.set(appointmentId); }
    public int getAppointmentId() { return this.appointmentId.get(); }

    public IntegerProperty customerIdProperty() { return customerId; }
    public void setCustomerId(int customer) { this.customerId.set(customer); }
    public int getCustomerId() { return this.customerId.get(); }
    
    public StringProperty titleProperty() { return title; }
    public void setTitle(String title) { this.title.set(title); }
    public String getTitle() { return this.title.get(); }

    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }
    public String getDescription() { return this.description.get(); }

    public ObjectProperty<LocalDateTime> startProperty() { return start; }
    public void setStart(LocalDateTime start) { this.start.set(start); }
    public LocalDateTime getStart() { return this.start.get(); }

    public ObjectProperty<LocalDateTime> endProperty() { return end; }
    public void setEnd(LocalDateTime end) { this.end.set(end); }
    public LocalDateTime getEnd() { return this.end.get(); }
    
    
}
