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
import java.time.ZonedDateTime;
import java.util.logging.Level;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private StringProperty appointmentType = new SimpleStringProperty(); 
    private StringProperty description = new SimpleStringProperty();; //notes
    private ObjectProperty<LocalDateTime> start = new SimpleObjectProperty();
    private ObjectProperty<LocalDateTime> end = new SimpleObjectProperty();
    private StringProperty userName = new SimpleStringProperty();
    private BooleanProperty reminded = new SimpleBooleanProperty();
    
    public Appointment() {};
    
    public Appointment(int appiontmentId, int customerId, String appointmentType, String notes, 
            LocalDateTime start, LocalDateTime end, String userName, String reminded)
    {
        setAppointmentId(appiontmentId);
        setCustomerId(customerId);
        setAppointmentType(appointmentType);
        setDescription(notes);
        setStart(start);
        setEnd(end);
        setUserName(userName);
        setReminded(Boolean.valueOf(reminded));
    }

    public static ObservableList<Appointment> getAppointments() throws SQLException
    {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();        
        
        String queryString = "SELECT * FROM appointment ORDER BY start";
        
        SqlHelperClass sql = new SqlHelperClass();
        
        try
        {
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
           
            ResultSet rs = sql.executeQuery(stmt);
           
            while (rs.next())
            {   

                
                System.out.println();
                appointments.add(new Appointment(rs.getInt("appointmentId"),
                        rs.getInt("customerId"), 
                        rs.getString("title"), //appointmentType
                        rs.getString("description"), 
                        Utils.convertUtcToLocalDateTime(rs.getTimestamp("start")),
                        Utils.convertUtcToLocalDateTime(rs.getTimestamp("end")),
                        rs.getString("contact"),
                        rs.getString("url")));
            }
           
        }
        catch (SQLException e)
        {
            //TODO: handle exepection. Printing out a message is not sufficent)
            System.out.println("sql error in appointment: \n" + e.getMessage());            
        }

        return appointments;
    }
    
    
    
    public static void addAppointmentToDb(Appointment appointment) throws SQLException
    {
        String queryString = "INSERT INTO appointment "  
                + "(appointmentId, customerId, title, description, contact, "
                + "start, end, createDate, createdBy, lastUpdate, "
                + "lastUpdateBy, location, url)"
                + "VALUES (\n"
                + "\t?\n"                       //1 appointmentId
                + "\t,?\n"                      //2 customerId
                + "\t,?\n"                      //3 title aka "appointmentType"
                + "\t,?\n"                      //4 description
                + "\t,?\n"                      //5 contact aka userName
                + "\t,?\n"                      //6 start
                + "\t,?\n"                      //7 end
                + "\t,?\n"                      //8 createDate
                + "\t,?\n"                      //9 createdBy
                + "\t,?\n"                      //10 lastUpdate
                + "\t,?\n"                      //11 latUpdatedBy
                + "\t,?,"                       //12 location
                + "\t,?);";                     //13 url aka "reminded"
                   
        SqlHelperClass sql = new SqlHelperClass();

        PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);

        String userName = UserClass.getInstance().getUserName();

        stmt.setInt(1, appointment.getAppointmentId());
        stmt.setInt(2, appointment.getCustomerId());
        stmt.setString(3, appointment.getAppointmentType());
        stmt.setString(4, appointment.getDescription());
        stmt.setString(5, appointment.getUserName());
        stmt.setTimestamp(6, Utils.convertLocaLDateTimeToUtc(appointment.getStart()));
        stmt.setTimestamp(7, Utils.convertLocaLDateTimeToUtc(appointment.getEnd()));
        stmt.setTimestamp(8, Utils.convertLocaLDateTimeToUtc(LocalDateTime.now())); //created date
        stmt.setString(9, userName);
        stmt.setTimestamp(10, Utils.convertLocaLDateTimeToUtc(LocalDateTime.now())); //last modified date
        stmt.setString(11, userName);
        stmt.setString(12, "");
        stmt.setString(13, "false");

        int result = sql.executeUpdateQuery(stmt);
        if (result == 0)
            throw new SQLException("An error occurred while trying to add the appointment.");

        BussApptMgntSyst.appointments = Appointment.getAppointments();            
    }
    
    public static void updateAppointmentInDb(Appointment appointment)
    {
        String queryString = "UPDATE appointment \n"          
                + "SET customerId = ?"            //2
                + ",title = ?"                 //3
                + ",description = ?"           //4
                + ",start = ?"                 //5
                + ",end = ?"                   //6
                + ",lastUpdate = ?"            //7
                + ",lastUpdateBy = ? "         //8
                + "WHERE appointmentId = ?;";     //9

                   
        SqlHelperClass sql = new SqlHelperClass();
        try
        {
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
                                    
            String userName = UserClass.getInstance().getUserName();
                       
//            stmt.setInt(1, appointment.getAppointmentId());
            stmt.setInt(1, appointment.getCustomerId());
            stmt.setString(2, appointment.getAppointmentType());
            stmt.setString(3, appointment.getDescription());
            stmt.setTimestamp(4, Utils.convertLocaLDateTimeToUtc(appointment.getStart()));
            stmt.setTimestamp(5, Utils.convertLocaLDateTimeToUtc(appointment.getEnd()));
            stmt.setTimestamp(6, Utils.convertLocaLDateTimeToUtc(LocalDateTime.now()));
            stmt.setString(7, userName);            
            stmt.setInt(8,appointment.getAppointmentId());
            
            sql.executeUpdateQuery(stmt);
            BussApptMgntSyst.appointments = Appointment.getAppointments();
            
        } catch (SQLException e)
        {
            //TODO: handle exepection. Printing out a message is not sufficent)
            System.out.println("sql add error in appiontment: \n" + e.getMessage());
            System.out.println(e.getSQLState());
        }                
    }
        
        
   public static void removeAppointment(Appointment appointment)
   {
       String queryString = "DELETE FROM appointment \n"
               + "\t WHERE appointmentId = ?;";             
       
       SqlHelperClass sql = new SqlHelperClass();
       
       try
       {
           PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
           
           stmt.setInt(1, appointment.getAppointmentId());
           
           sql.executeUpdateQuery(stmt);
           BussApptMgntSyst.appointments = getAppointments();
           
       } catch (SQLException e)
       {
           //TODO: handle exepection. Printing out a message is not sufficent)
           System.out.println(e.getMessage());
       }
       
   }
    
    public IntegerProperty appointmentIdProperty() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId.set(appointmentId); }
    public int getAppointmentId() { return this.appointmentId.get(); }    

    public IntegerProperty customerIdProperty() { return customerId; }
    public void setCustomerId(int customer) { this.customerId.set(customer); }
    public int getCustomerId() { return this.customerId.get(); }
    
    public StringProperty appointmentTypeProperty() { return appointmentType; }
    public void setAppointmentType(String appointmentType) { this.appointmentType.set(appointmentType); }
    public String getAppointmentType() { return this.appointmentType.get(); }

    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }
    public String getDescription() { return this.description.get(); }

    public ObjectProperty<LocalDateTime> startProperty() { return start; }
    public void setStart(LocalDateTime start) { this.start.set(start); }
    public LocalDateTime getStart() { return this.start.get(); }

    public ObjectProperty<LocalDateTime> endProperty() { return end; }
    public void setEnd(LocalDateTime end) { this.end.set(end); }
    public LocalDateTime getEnd() { return this.end.get(); }
    
    public StringProperty userNameProperty() { return userName; }
    public void setUserName(String userName) { this.userName.set(userName); }
    public String getUserName() { return this.userName.get(); }
    
    public BooleanProperty remindedProperty() { return reminded; }
    public void setReminded(boolean reminded) { this.reminded.set(reminded); }
    public boolean getReminded() { return this.reminded.get(); }
    
    public void updateReminded(boolean reminded)
    {
        String queryString = "UPDATE appointment SET url = ? WHERE appointmentId = ?;";
        
        try
        {
          SqlHelperClass sql = new SqlHelperClass();
          PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
          
          stmt.setString(1, String.valueOf(reminded));
          stmt.setInt(2, this.getAppointmentId());
          sql.executeUpdateQuery(stmt);
        } 
        catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
        }        
    }
    
    
    public int getNextAppointmentId()
    {
        Appointment apt;
        if (BussApptMgntSyst.appointments.isEmpty())
            return 1;
        else        
            apt = BussApptMgntSyst.appointments.stream()
                .max((id1, id2) -> Integer.compare(id1.getAppointmentId(), id2.getAppointmentId())).get();            
        
        return apt.getAppointmentId() + 1;
    }
}
