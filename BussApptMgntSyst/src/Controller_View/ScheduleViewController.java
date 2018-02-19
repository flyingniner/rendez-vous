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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    ResourceBundle resource;
    
    @FXML private Label lblTitle;
    @FXML private Label lblFromDate;
    @FXML private Label lblToDate;
    @FXML private Label lblCustomer;
    @FXML private Label lblStartTime;
    @FXML private Label lblEndTime;
    @FXML private Label lblNotes;
    @FXML private TextField txtTitle;
    @FXML private DatePicker dtFromDate;
    @FXML private DatePicker dtToDate;
    @FXML private ChoiceBox<Integer> chStartHour;
    @FXML private ChoiceBox<Integer> chStartMinute;
    @FXML private ChoiceBox<String> chStartAmPm;
    @FXML private ChoiceBox<Integer> chEndHour;
    @FXML private ChoiceBox<Integer> chEndMinute;
    @FXML private ChoiceBox<String> chEndAmPm;
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
        resource = rb;
        lblTitle.setText(resource.getString("title"));
        lblFromDate.setText(resource.getString("startDate"));
        lblToDate.setText(resource.getString("endDate"));
        lblCustomer.setText(resource.getString("customer"));
        lblStartTime.setText(resource.getString("startTime"));
        lblEndTime.setText(resource.getString("endTime"));
        btnSave.setText(resource.getString("save"));
        lblNotes.setText(resource.getString("notes"));
        btnDelete.setText(resource.getString("delete"));
        btnClose.setText(resource.getString("close"));
        colStartDate.setText(resource.getString("start"));
        colEndDate.setText(resource.getString("end"));
        colTitle.setText(resource.getString("title"));
        colCustomerName.setText(resource.getString("customer"));
        
        
        loadCustomers();
        loadTimeFields();
        loadAppointmentTable();
        btnClose.setOnAction((event) -> sceneMgr.displayScene(root, child, "Main"));    
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
        
        
        int startHour = chStartHour.getValue();
        int startMinute = chStartMinute.getValue();
        String startPeriod = chStartAmPm.getValue();
        
        int endHour = chEndHour.getValue();
        int endMinute = chEndMinute.getValue();
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
    {}
    
    private void handleDeleteButton()
    {}
    
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

    private void loadTimeFields()
    {
        //create values to populate fields
        ObservableList<Integer> startHours = FXCollections.observableArrayList();
        ObservableList<Integer> startMinutes = FXCollections.observableArrayList();
        ObservableList<String> startAmPm = FXCollections.observableArrayList();
        ObservableList<Integer> endHours = FXCollections.observableArrayList();
        ObservableList<Integer> endMinutes = FXCollections.observableArrayList();
        ObservableList<String> endAmPm = FXCollections.observableArrayList();        
        
        //assign values to start time choice boxes
        IntStream.rangeClosed(1, 12).boxed().forEach(x -> startHours.add(0, x));
        IntStream.rangeClosed(0, 59).boxed().forEach(x -> startMinutes.add(0, x));
        startAmPm.add("AM");
        startAmPm.add("PM");
        chStartHour.setItems(startHours.sorted());        
        chStartMinute.setItems(startMinutes.sorted());
        chStartAmPm.setItems(startAmPm);
        
        //assign values to end time choice boxes
        IntStream.rangeClosed(1, 12).boxed().forEach(x -> endHours.add(0, x));
        IntStream.rangeClosed(0, 59).boxed().forEach(x -> endMinutes.add(0, x));
        endAmPm.add("AM");
        endAmPm.add("PM");
        chEndHour.setItems(startHours.sorted());        
        chEndMinute.setItems(startMinutes.sorted());
        chEndAmPm.setItems(endAmPm);        

    }

    //private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:MM");
    
    private void loadAppointmentTable()
    {
        Appointment apt = new Appointment();
        
        tblCalendar.setItems(BussApptMgntSyst.appointments);
        colStartDate.setCellValueFactory(x -> x.getValue().startProperty());
        colEndDate.setCellValueFactory(x -> x.getValue().endProperty());
        colCustomerName.setCellValueFactory(x -> x.getValue().customerIdProperty().asString());
        colTitle.setCellValueFactory(x -> x.getValue().titleProperty());
    }
    
    
    
    

}
