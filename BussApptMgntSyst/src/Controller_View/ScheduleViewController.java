/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.Appointment;
import Model.BussApptMgntSyst;
import Model.Customer;
import Model.UserClass;
import Model.Utils;
import java.net.URL;
import java.sql.SQLException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import java.util.stream.IntStream;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author chip
 */
public class ScheduleViewController implements Initializable
{
    Stage stage;    
    Locale locale = BussApptMgntSyst.locale;
    SceneManager sceneMgr = new SceneManager();
    AnchorPane root = BussApptMgntSyst.root;
    AnchorPane child;
    
    @FXML private Label lblAptType;
    @FXML private Label lblFromDate;
    @FXML private Label lblToDate;
    @FXML private Label lblCustomer;
    @FXML private Label lblStartTime;
    @FXML private Label lblEndTime;
    @FXML private ChoiceBox chAppointmentType;
    @FXML private DatePicker dtFromDate;
    @FXML private DatePicker dtToDate;
    @FXML private ComboBox<String> chStartHour;
    @FXML private ComboBox<String> chStartMinute;
    @FXML private ComboBox<String> chStartAmPm;
    @FXML private ComboBox<String> chEndHour;
    @FXML private ComboBox<String> chEndMinute;
    @FXML private ComboBox<String> chEndAmPm;
    @FXML private ComboBox<String> cmbCustomer;
    @FXML private TextArea txtNotes;
    @FXML private Button btnSave;
    @FXML private Button btnDelete;
    @FXML private Button btnClose;
    @FXML private Button btnClear;
    @FXML private TableView<Appointment> tblCalendar;
    @FXML private TableColumn<Appointment, LocalDateTime> colStartDate;
    @FXML private TableColumn<Appointment, LocalDateTime> colEndDate;    
    @FXML private TableColumn<Appointment, String> colCustomerName;
    @FXML private TableColumn<Appointment, String> colAppointmentType;
    
    Appointment selectedAppointment = null;
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //load data elements
        Customer.loadCustomers();
        loadTimeFields();
        setAppointmentOptions();
        loadAppointmentTable();
        
        //assign actions and listeners to form controls
        btnSave.setOnAction(x -> handleSaveButton());        
        btnDelete.setOnAction(x -> handleDeleteButton());        
        btnClose.setOnAction((event) -> handleCloseButton());            
        btnClear.setOnAction(x -> handleClearButton());        
        cmbCustomer.setItems(setCustComboBox());
        
        tblCalendar.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
        {
            if (newValue!=null)
            {
                selectedAppointment = new Appointment();
                selectedAppointment.setAppointmentId(newValue.getAppointmentId());
                selectedAppointment.setCustomerId(newValue.getCustomerId());
                selectedAppointment.setDescription(newValue.getDescription());
                selectedAppointment.setEnd(newValue.getEnd());
                selectedAppointment.setStart(newValue.getStart());
                selectedAppointment.setAppointmentType(newValue.getAppointmentType());
                selectedAppointment.setUserName(newValue.getUserName());
                populateAppointmentFields();                  
            }
        });
                                
    }
    
    private void setAppointmentOptions()
    {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Referral", "Sales Call", "Service Call", "Weekly Follow-up");
        chAppointmentType.setItems(list);        
    }
    
    private void handleSaveButton()
    {
        String aptType = null;
        String custName = null;
//        int startHour, startMinute, endHour, endMinute;
//        LocalTime startTime = null;
//        LocalTime endTime = null;
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        String notes;
        
        try
        {   
            //exception controls
            if((startDateTime = getStartDateTime())==null)
                throw new IllegalArgumentException("Please pick a valid start date/time.");
                        
            if((endDateTime = getEndDateTime())==null)
                throw new IllegalArgumentException("Please pick a valid end date/time.");
                    
            //TODO: change end time back to 17 on line 155; delete this row
            if ((startDateTime.toLocalTime().isBefore(LocalTime.of(8, 0))) ||
                    endDateTime.toLocalTime().isAfter(LocalTime.of(19,0)))
                throw new IllegalArgumentException("Appointments must be maded with the business hours of 8:00AM and 5:00PM.");
                        
            if ((aptType = chAppointmentType.getValue().toString())==null || aptType.trim().isEmpty())
                throw new IllegalArgumentException("The appointment type must not be left blank.");
                        
            if (endDateTime.isBefore(startDateTime))
                throw new IllegalArgumentException("The end date/time must be later than the start date/time.");

            if ((cmbCustomer.getSelectionModel().getSelectedItem())==null || 
                    (custName = cmbCustomer.getSelectionModel().getSelectedItem().toString()).isEmpty())
                throw new IllegalArgumentException("You must include a customer to save this appointment.");

            if ((notes = txtNotes.getText().trim()).isEmpty())
                notes = "";
            else 
                notes = txtNotes.getText();
            
            //create a new appointment object
            Appointment newAppointment = new Appointment();
            
            newAppointment.setAppointmentId(newAppointment.getNextAppointmentId());
            newAppointment.setCustomerId(Customer.getCustomerIdFromName(custName));
            newAppointment.setAppointmentType(aptType);
            newAppointment.setDescription(notes);
            newAppointment.setStart(startDateTime);
            newAppointment.setEnd(endDateTime);
            newAppointment.setUserName(UserClass.getInstance().getUserName());

                        
            if (selectedAppointment != null)
            {
                if (Utils.displayAlertConfirmation("Confirm Overwrite?", "You are about to save changes to an existing appointment."
                        + "\nDo you want to continue?" ))
                {
                    int selId = selectedAppointment.getAppointmentId();
                    
//                    for (Appointment apt : BussApptMgntSyst.appointments)
//                    {
//                        if (apt.equals(selectedAppointment))
//                            BussApptMgntSyst.appointments.remove(apt);
//                    }
                    
                    BussApptMgntSyst.appointments.removeIf(item -> item.equals(selectedAppointment));
                    
                    checkForOverlap(newAppointment);                    
                
                    selectedAppointment.setAppointmentId(selId);
                    selectedAppointment.setCustomerId(newAppointment.getCustomerId());
                    selectedAppointment.setAppointmentType(newAppointment.getAppointmentType());
                    selectedAppointment.setDescription(newAppointment.getDescription());
                    selectedAppointment.setStart(newAppointment.getStart());
                    selectedAppointment.setEnd(newAppointment.getEnd());
                    selectedAppointment.setUserName(newAppointment.getUserName());
                    

                   // BussApptMgntSyst.appointments.add(selectedAppointment);
                    //update DB
                    Appointment.updateAppointmentInDb(selectedAppointment);     
                }                                                                           
            }
            
            else
            {
                checkForOverlap(newAppointment);   
                //BussApptMgntSyst.appointments.add(newAppointment);
                Appointment.addAppointmentToDb(newAppointment);                   
            }
            

            //add or update the appointment in the DB 
            
            //clear form imputs
            handleClearButton();  
            loadAppointmentTable();
        }
        catch (IllegalArgumentException e)
        {
            Utils.displayAlertError("Appointment Error", e.getMessage());             
        }
        catch (SQLException e)
        {
            Utils.displayAlertError("SQL ERROR", e.getMessage());
        }
        
        
    }

    private void checkForOverlap(Appointment appointment)
    {
        //verify appointment does not overlap with an existing appointment
        BussApptMgntSyst.appointments.stream()
                .forEach(x ->
                {
                    if ((appointment.getStart().isBefore(x.getEnd())) &&
                            appointment.getEnd().isAfter(x.getStart()))
                        throw new IllegalArgumentException("This appoinment overlaps with another appointment.");
                });        
    }

    /**
     * Converts user input into a LocalDateTime object. 
     * @return ending date/time of the appointment.
     */
    private LocalDateTime getEndDateTime()
    {
        String strEndHour, strEndMinute, endPeriod;
        LocalTime endTime = null;
        LocalDate date = null;
        
        try
        {
            //check data exists, if not throw exception so user can fix the inputs
            if ((strEndHour=chEndHour.getValue()) == null || strEndHour.trim().isEmpty())
                throw new IllegalArgumentException("You must select a valid end time.");
            if ((strEndMinute=chEndMinute.getValue()) == null || strEndMinute.trim().isEmpty())
                throw new IllegalArgumentException("You must select a valid end time.");
            if ((endPeriod = chEndAmPm.getValue()) == null || endPeriod.isEmpty())
                throw new IllegalArgumentException("You must select either AM or PM.");                    
            if ((date = dtToDate.getValue()) == null)
                throw new NumberFormatException("You must enter a valid end date.");
        
            //cast the strings to ints
            int endHour = Integer.parseInt(strEndHour);
            int endMinute = Integer.parseInt(strEndMinute);
            
            //get the local time representation
            endTime = parseTimeField(endHour,endMinute,endPeriod);
        }
        catch (NumberFormatException e)
        {
            Utils.displayAlertError("DateTime Error", e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            Utils.displayAlertError("DateTime Error", e.getMessage());
        }
             
        return LocalDateTime.of(date, endTime);
    }

    /**
     * Converts user input into a LocalDateTime object. 
     * @return starting date/time of the appointment
     */
    private LocalDateTime getStartDateTime()
    {
        String strStartHour, strStartMinute, startPeriod;
        LocalTime startTime = null;
        LocalDate date = null;        
        
        try
        {
            //check data exists, if not throw exception so user can fix the inputs
            if ((strStartHour=chStartHour.getValue()) == null || strStartHour.trim().isEmpty())
                throw new IllegalArgumentException("You must select a valid start time.");
            if ((strStartMinute=chStartMinute.getValue()) == null || strStartMinute.trim().isEmpty())
                throw new IllegalArgumentException("You must select a valid start time.");
            if ((startPeriod = chStartAmPm.getValue()) == null || startPeriod.isEmpty())
                throw new IllegalArgumentException("You must select either AM or PM.");                    
            if ((date = dtFromDate.getValue()) == null)
                throw new NumberFormatException("You must enter a valid start date.");
            
            //cast the strings to ints
            int startHour = Integer.parseInt(strStartHour);
            int startMinute = Integer.parseInt(strStartMinute);
            
            //get the local time representation
            startTime = parseTimeField(startHour,startMinute,startPeriod);                      
        }
        catch (NumberFormatException e)
        {
            Utils.displayAlertError("DateTime Error", e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            Utils.displayAlertError("DateTime Error", e.getMessage());
        }
        
        return LocalDateTime.of(date, startTime);          
    }
    
    private void handleCloseButton()
    {
        //TODO: check for unsaved (new or changed) record
        sceneMgr.displayScene(root, child, "Main");
    }
    
    private void handleDeleteButton()
    {
        //Confirm delete request
        Appointment apt = tblCalendar.getSelectionModel().getSelectedItem();
        //int aptId = apt.getAppointmentId();
        //call delete query
        Appointment.removeAppointment(apt);
        handleClearButton();
        //remove for collection in memory
       // BussApptMgntSyst.appointments.remove(apt);
    }
    
    /**
     * Converts int hour and int minute inputs into a local time object using
     * accounting for AM/PM.
     * @param hour
     * @param minute
     * @param period
     * @return 
     */
    public LocalTime parseTimeField(int hour, int minute, String period)
    {
        if (period.equals("AM") && hour == 12)
            return LocalTime.of(hour - 12, minute);
        else if (period.equals("AM"))
            return LocalTime.of(hour, minute);
        else if (period.equals("PM") && hour == 12)
            return LocalTime.of(hour, minute);
        else if (period.equals("PM"))
            return LocalTime.of(hour + 12, minute);
        else
            return null;
    }
     
    
    private ObservableList<String> setCustComboBox()
    {
        ObservableList<String> tmp = FXCollections.observableArrayList();
        
        BussApptMgntSyst.customers.forEach((x) -> 
        {
            if (!tmp.contains(x.getCustName()))
                tmp.add(x.getCustName());
        });
        
        return tmp.sorted();
    }
    
    
    /**
     * 
     */
    

    /**
     * Loads the hour, minute and am/pm drop down boxes.
     */
    private void loadTimeFields()
    {        
        ObservableList<String> startHours = FXCollections.observableArrayList();
        ObservableList<String> startMinutes = FXCollections.observableArrayList();
        ObservableList<String> startAmPm = FXCollections.observableArrayList();
        ObservableList<String> endHours = FXCollections.observableArrayList();
        ObservableList<String> endMinutes = FXCollections.observableArrayList();
        ObservableList<String> endAmPm = FXCollections.observableArrayList();        
        
        //assign values to Start time dropdowns
        IntStream.rangeClosed(1, 12).boxed().forEach(x -> startHours.add(0, String.format("%02d", x)));
        IntStream.rangeClosed(0, 59).boxed().forEach(x -> startMinutes.add(0, String.format("%02d", x)));
        startAmPm.add("AM");
        startAmPm.add("PM");
        
        chStartHour.setItems(startHours.sorted());        
        chStartMinute.setItems(startMinutes.sorted());
        chStartAmPm.setItems(startAmPm.sorted());
        
        //assign values to End time dropdowns 
//        IntStream.rangeClosed(1, 12).forEach(x -> endHours.add(0, String.format("%02d", x)));
        IntStream.rangeClosed(1, 12).boxed().forEach(x -> endHours.add(0, String.format("%02d", x)));
        IntStream.rangeClosed(0, 59).boxed().forEach(x -> endMinutes.add(0, String.format("%02d", x)));
        endAmPm.add("AM");
        endAmPm.add("PM");
        chEndHour.setItems(endHours.sorted());        
        chEndMinute.setItems(endMinutes.sorted());
        chEndAmPm.setItems(endAmPm.sorted());        

        //set maximum visable rows
        chStartHour.setVisibleRowCount(10);
        chStartMinute.setVisibleRowCount(10);
        chEndHour.setVisibleRowCount(10);
        chEndMinute.setVisibleRowCount(10);
        
        //set default values 
        chStartHour.getSelectionModel().select(11); //select '12'
        chStartMinute.getSelectionModel().select(0); //select '00'
        chStartAmPm.getSelectionModel().selectFirst(); //select 'AM'
        chEndHour.getSelectionModel().select(11); //select '12'
        chEndMinute.getSelectionModel().select(0); //select '00'
        chEndAmPm.getSelectionModel().selectFirst(); //select 'AM'
    }

    /**
     * Loads the appointment table with all appointments.
     */
    private void loadAppointmentTable()
    {
        Appointment apt = new Appointment();
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);                     
        
        //TODO: filter table to show only those appointments for the current user using lambda
        tblCalendar.setItems(BussApptMgntSyst.appointments.stream()
            .filter(a -> a.getUserName().equals(UserClass.getInstance().getUserName()))
            .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l))));
        
        
        //get and format start date/time
        colStartDate.setCellValueFactory(x -> x.getValue().startProperty());
        colStartDate.setCellFactory(col -> new TableCell<Appointment, LocalDateTime>()
        {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(String.format(item.format(formatter)));
            }
        });
        
        
        //get and format end date/time
        colEndDate.setCellValueFactory(x -> x.getValue().endProperty());
        colEndDate.setCellFactory(col -> new TableCell<Appointment,LocalDateTime>() 
        {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(String.format(item.format(formatter)));                           
            }
        });
                
        //get and format customer name
        colCustomerName.setCellValueFactory(x -> x.getValue().customerIdProperty().asString());
        colCustomerName.setCellFactory(col -> new TableCell<Appointment, String>()
        {
            @Override
            protected void updateItem(String item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                {
                    BussApptMgntSyst.customers.forEach((x) -> 
                    {
                        if (x.getCustID()==Integer.parseInt(item))
                        {
                            setText(x.getCustName());
                            return;
                        }
                    });                                        
                }                           
            }
        });
        
        colAppointmentType.setCellValueFactory(x -> x.getValue().appointmentTypeProperty());
        
        
    }

    /**
     * Populates the appointment form fields based on the currently selected
     * appointment in the appointment table.
     * @param selectedAppointment 
     */
    private void populateAppointmentFields()
    {
        Object[] startTime = parseTime(selectedAppointment.getStart().toLocalTime());
        Object[] endTime = parseTime(selectedAppointment.getEnd().toLocalTime());
        
        //TODO: check fields are empty before loading fields, if not, throw exception
        //chAptType.setValue(null);
        chAppointmentType.setValue(selectedAppointment.getAppointmentType());
        //chAptType.setText(selectedAppointment.getTitle());
        //set start date & time
        dtFromDate.setValue(selectedAppointment.getStart().toLocalDate());
        chStartHour.setValue(String.format("%02d",(Integer)startTime[0]));
        chStartMinute.setValue(String.format("%02d",(Integer)startTime[1]));
        chStartAmPm.setValue((String)startTime[2]);        
        //set end date & time
        dtToDate.setValue(selectedAppointment.getEnd().toLocalDate());
        chEndHour.setValue(String.format("%02d", (Integer)endTime[0]));
        chEndMinute.setValue(String.format("%02d", (Integer)endTime[1]));
        chEndAmPm.setValue((String)endTime[2]);
       
        cmbCustomer.setValue(Customer.getCustomerNameFromId(selectedAppointment.getCustomerId()));
        txtNotes.setText(selectedAppointment.getDescription());
    }
    
    /**
     * Parses a LocalTime object into hours, minutes, and AM/PM, returning an 
     * Object array.
     * @param localTime
     * @return 
     */
    private Object[] parseTime(LocalTime localTime)
    {
        int hour, minute;
        String period = "AM";
        Object[] parsedResult = new Object[3];
        
        if ((hour = localTime.getHour())>12)
        {
            hour = hour-12;
            period = "PM";
        }
        
        if (hour == 12)
            period = "PM";
        
        if (hour == 0)
        {
            hour = 12;
            period = "AM";
        }
        
        minute = localTime.getMinute();               
                
        parsedResult[0] = hour;
        parsedResult[1] = minute;
        parsedResult[2] = period;
        
        return parsedResult;
    }

    private void handleClearButton()
    {
        tblCalendar.getSelectionModel().clearSelection();
        loadTimeFields();
        chAppointmentType.setValue(null);
        //chAptType.setText(null);
        txtNotes.setText(null);
        dtFromDate.setValue(null);
        dtToDate.setValue(null);                
        cmbCustomer.getSelectionModel().clearSelection();
        selectedAppointment = null;
    }
    
    

}
