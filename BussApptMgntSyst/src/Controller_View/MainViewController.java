/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.Appointment;
import Model.BussApptMgntSyst;
import static Model.BussApptMgntSyst.appointments;
import Model.Customer;
import Model.AppointmentFilter;
import Model.AppointmentReminder;
import Model.UserClass;
import Model.Utils;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author chip
 */
public class MainViewController implements Initializable
{
    Stage stage;    
    Locale locale = BussApptMgntSyst.locale;
    SceneManager sceneMgr = new SceneManager();
    AnchorPane root = BussApptMgntSyst.root;
    AnchorPane child;
    
    @FXML private Button btnCustomers;
    @FXML private Button btnSchedules;
    @FXML private Button btnReports;
    @FXML private Button btnPrevPeriod;
    @FXML private Button btnNextPeriod;
    @FXML private RadioButton radByMonth;
    @FXML private RadioButton radByWeek;
    @FXML private RadioButton radAll;
    @FXML private ToggleGroup group = new ToggleGroup();
    @FXML private TableView<Appointment> tblCalendar;
    @FXML private TableColumn<Appointment, LocalDateTime> colStartDate;
    @FXML private TableColumn<Appointment, LocalDateTime> colEndDate;    
    @FXML private TableColumn<Appointment, String> colCustomerName;
    @FXML private TableColumn<Appointment, String> colAppointmentType;
    @FXML private TableColumn<Appointment, String> colUser;
    @FXML private Label selectedTimePeriod;
    
    private static ObservableList<Appointment> filteredAppoinments = FXCollections.observableArrayList();
    private ResourceBundle resource;
    AppointmentFilter filter;
    String selectFilter;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        resource = rb;
        setControlText();
        loadAppointments(); //initilizes the appointments collection
        loadAppointmentTable(); //loads the appoinment table
        Customer.loadCustomers();
        
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                checkForUpcommingAppointments();
            }
        });
        
        //setDefaultToggles
        radByMonth.setToggleGroup(group);
        radByWeek.setToggleGroup(group);        
        radAll.setToggleGroup(group);
        radByMonth.selectedProperty().set(false);
        radByWeek.selectedProperty().set(false);
        radAll.selectedProperty().set(true);
        createAppointmentFilter(); 
        
        btnCustomers.setOnAction((event) ->
        {           
           sceneMgr.displayScene(root, child, "Customer");
        });
        
        btnSchedules.setOnAction((event) ->
        {
            sceneMgr.displayScene(root, child, "Schedule");
        });
        
        btnReports.setOnAction((event) ->
        {
            sceneMgr.displayScene(root, child, "Reports");
        });
        
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
        {
            
            RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
            String value = selectedRadioButton.getId();
            
            if (value.equals("radByMonth"))
            {
                filter.setByMonth(true);
                filter.setByWeek(false);                            
            }
            
            else if (value.equals("radByWeek"))
            {
                filter.setByMonth(false);
                filter.setByWeek(true); 
                
                LocalDate currentDate = filter.getCurrentDatePosition();
                currentDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
                currentDate = currentDate.minusDays(currentDate.getDayOfWeek().compareTo(DayOfWeek.MONDAY));
                filter.setCurentDatePosition(currentDate);                
            }
                
            else //All is selected
            {
                filter.setCurrentMonthPosition();
                filter.setByMonth(false);
                filter.setByWeek(false);
                tblCalendar.setItems(appointments);
                selectedTimePeriod.setText("Showing all Appointments");
                return;
            }                                        
            
            filterAppoinmentTable(filter);
            
            
        });  
        
        btnPrevPeriod.setOnAction(x -> 
        {
            RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
            String value = selectedRadioButton.getText();
            
            if (value.equals("Monthly"))            
                filter.changeMonthlyPosition(-1);   
                        
            else if (value.equals("Weekly"))
                filter.changeWeeklyPosition(-1);
            
            else //All is selected
            {
                filter.setCurrentMonthPosition();
                filter.setCurrentWeekPosition();
            }
                        
            filterAppoinmentTable(filter);
        });
        
        btnNextPeriod.setOnAction(x -> 
        {
            RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
            String value = selectedRadioButton.getText();
            
            if (value.equals("Monthly"))            
                filter.changeMonthlyPosition(1);   
                        
            else if (value.equals("Weekly"))
                filter.changeWeeklyPosition(1);
            
            else //All is selected
            {
                filter.setCurrentMonthPosition();
                filter.setCurrentWeekPosition();
            }
                        
            filterAppoinmentTable(filter);
        });
            
       
    }    

    private void setControlText()
    {
        btnCustomers.setText(resource.getString("customers"));
        btnSchedules.setText(resource.getString("schedules"));
        btnReports.setText(resource.getString("reports"));
        btnPrevPeriod.setText(resource.getString("prev"));
        btnNextPeriod.setText(resource.getString("next"));
        radByMonth.setText(resource.getString("monthly"));
        radByWeek.setText(resource.getString("weekly"));
        radAll.setText(resource.getString("all"));
    }
    
    private void loadAppointments()
    {
        try
        {
            appointments = Appointment.getAppointments();            
        } 
        catch (SQLException e)
        {
        }
    }
    
    private void loadCustomers()
    {
           Customer.getCustomersFromDB();        
    }
    
    /**
     * Loads the appointment table with all appointments.
     */
    private void loadAppointmentTable()
    {
        selectedTimePeriod.setText("Showing all Appointments");
        Appointment apt = new Appointment();
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);                     
        
        tblCalendar.setItems(BussApptMgntSyst.appointments);
        
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
        colUser.setCellValueFactory(x -> x.getValue().userNameProperty());
    }
    
    
    private void createAppointmentFilter()
    {
        filter = new AppointmentFilter();
        filter.setByMonth(radByMonth.selectedProperty().getValue());
        filter.setByWeek((radByWeek.selectedProperty().getValue()));
        filter.setCurentDatePosition(LocalDate.now());
    }
    
    private void filterAppoinmentTable(AppointmentFilter filter)            
    {
        
        LocalDate currentDate;
        filteredAppoinments.clear();
        
        if (filter.isMonthlySelected())
        {
            //currentDate = LocalDate.now().plusMonths(filter.getCurrentMonthPosition());
            
            currentDate = filter.getCurrentDatePosition();
            
            BussApptMgntSyst.appointments.stream()
                .forEach(x -> 
                {
                    if (x.getStart().getMonthValue() == currentDate.getMonthValue())
                        filteredAppoinments.add(new Appointment(
                                x.getAppointmentId()
                                ,x.getCustomerId()
                                ,x.getAppointmentType()
                                ,x.getDescription()
                                ,x.getStart()
                                ,x.getEnd()
                                ,x.getUserName()
                                ,String.valueOf(x.getReminded())));
                });
                        
            selectedTimePeriod.setText(currentDate.getMonth().toString() + " " + currentDate.getYear());
        }
        
        else if (filter.isWeeklySelected())
        {
            currentDate = filter.getCurrentDatePosition();
            
            BussApptMgntSyst.appointments.stream()
                .forEach(x -> 
                {
                    if (x.getStart().isAfter(currentDate.atStartOfDay()) && 
                            x.getStart().isBefore(currentDate.atStartOfDay().plusDays(6)))
                        filteredAppoinments.add(new Appointment(
                            x.getAppointmentId()
                            ,x.getCustomerId()
                            ,x.getAppointmentType()
                            ,x.getDescription()
                            ,x.getStart()
                            ,x.getEnd()
                            ,x.getUserName()
                            ,String.valueOf(x.getReminded())));
                });
            
            
            selectedTimePeriod.setText(currentDate.toString() + " -> " +
                    currentDate.plusDays(6).toString());
        }        

        if (filteredAppoinments.isEmpty())
            tblCalendar.placeholderProperty().set(new Label("No appointments found for this period."));
        
                
        tblCalendar.setItems(filteredAppoinments); 
        
    }
    
    
    /**
     * Checks up-coming appointments for the currently logged in user. If one
     * or more appointments are found, the user will be presented with pop-up
     * reminder with name of the customer and the start time. Once the user
     * clears the pop-up message, the appointment is marked as having been 
     * reminded and will no longer display to the user.
     */
    private void checkForUpcommingAppointments()
    {
        AppointmentReminder.getUpcomingAppointments(UserClass.getInstance());
        
        AppointmentReminder.upcommingAppoitments.stream()
                .forEach(x -> 
                {
                    if (!x.getReminded())
                    {
                        boolean response = Utils.displayInformational("APPOINTMENT REMINDER", "You have an upcomming "
                                + "appointment at " + String.valueOf(x.getStart()
                                        .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                                + "with "+ Customer.getCustomerNameFromId(x.getCustomerId()) + ".");
                        
                        x.updateReminded(response);                        
                    }
                });
        
    }
}


