/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.Appointment;
import Model.BussApptMgntSyst;
import Model.Customer;
import java.net.URL;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
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
    
    @FXML private Label lblTitle;
    @FXML private Label lblFromDate;
    @FXML private Label lblToDate;
    @FXML private Label lblCustomer;
    @FXML private Label lblStartTime;
    @FXML private Label lblEndTime;
    @FXML private TextField txtTitle;
    @FXML private DatePicker dtFromDate;
    @FXML private DatePicker dtToDate;
    @FXML private ComboBox<String> chStartHour;
    @FXML private ComboBox<String> chStartMinute;
    @FXML private ComboBox<String> chStartAmPm;
    @FXML private ComboBox<String> chEndHour;
    @FXML private ComboBox<String> chEndMinute;
    @FXML private ComboBox<String> chEndAmPm;
    @FXML private ComboBox cmbCustomer;
    @FXML private TextArea txtNotes;
    @FXML private Button btnSave;
    @FXML private Button btnDelete;
    @FXML private Button btnClose;
    @FXML private TableView<Appointment> tblCalendar;
    @FXML private TableColumn<Appointment, LocalDateTime> colStartDate;
    @FXML private TableColumn<Appointment, LocalDateTime> colEndDate;    
    @FXML private TableColumn<Appointment, String> colCustomerName;
    @FXML private TableColumn<Appointment, String> colTitle;
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        loadCustomers();
        loadTimeFields();
        loadAppointmentTable();
        btnClose.setOnAction((event) -> handleCloseButton());    
        cmbCustomer.setItems(setCustComboBox());
        btnSave.setOnAction(x -> {
           handleSaveButton();
           sceneMgr.displayScene(root, child, "Main");
        });
        
    }
    
    private void handleSaveButton()
    {
        //TODO:
            //check that appointment does not overlap another appointment        
        
        
        int startHour = Integer.parseInt(chStartHour.getValue());
        int startMinute = Integer.parseInt(chStartMinute.getValue());
        String startPeriod = chStartAmPm.getValue();
        
        int endHour = Integer.parseInt(chEndHour.getValue());
        int endMinute = Integer.parseInt(chEndMinute.getValue());
        String endPeriod = chEndAmPm.getValue();
        
        //parse values into time objects
        LocalTime startTime = parseTimeField(startHour,startMinute,startPeriod);
        LocalTime endTime = parseTimeField(endHour,endMinute,endPeriod);
        
        //create a new appointment object
        Appointment apt = new Appointment();
        apt.setAppointmentId(2);
        apt.setCustomerId(Customer.getCustomerId(cmbCustomer.getSelectionModel().getSelectedItem().toString()));
        apt.setTitle(txtTitle.getText());
        apt.setDescription(txtNotes.getText());
        apt.setStart(LocalDateTime.of(dtFromDate.getValue(), startTime));
        apt.setEnd(LocalDateTime.of(dtToDate.getValue(), endTime));
        
        
        //add or update the appointment in the appointments collection
        BussApptMgntSyst.appointments.add(apt);
        //add or update the appointment in the DB 
        Appointment.addAppointmentToDb(apt);
        
    }
    
    private void handleCloseButton()
    {
        //TODO: check for unsaved (new or changed) record
        sceneMgr.displayScene(root, child, "Main");
    }
    
    private void handleDeleteButton()
    {
        //Confirm delete request
        //Delete current appointment from DB and ObservableList<Appointment>
    }
    
    private LocalTime parseTimeField(int hour, int minute, String period)
    {
        if (period == "PM")
            hour += 12;
        return LocalTime.of(hour, minute);
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
    
    private void loadCustomers ()
    {
        Customer customer = new Customer();
        
        if (BussApptMgntSyst.customers.isEmpty())
            BussApptMgntSyst.customers = customer.getCustomers();        
    }

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
        chStartAmPm.setItems(startAmPm);
        
        //assign values to End time dropdowns 
        IntStream.rangeClosed(1, 12).boxed().forEach(x -> endHours.add(0, String.format("%02d", x)));
        IntStream.rangeClosed(0, 59).boxed().forEach(x -> endMinutes.add(0, String.format("%02d", x)));
        endAmPm.add("AM");
        endAmPm.add("PM");
        chEndHour.setItems(startHours.sorted());        
        chEndMinute.setItems(startMinutes.sorted());
        chEndAmPm.setItems(endAmPm);        

        //set maximum visable rows
        chStartHour.setVisibleRowCount(10);
        chStartMinute.setVisibleRowCount(10);
        chEndHour.setVisibleRowCount(10);
        chEndMinute.setVisibleRowCount(10);
        
    }

    /**
     * Loads the appointment table with all appointments.
     */
    private void loadAppointmentTable()
    {
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
        
        colTitle.setCellValueFactory(x -> x.getValue().titleProperty());
    }
    
    
    
    

}
