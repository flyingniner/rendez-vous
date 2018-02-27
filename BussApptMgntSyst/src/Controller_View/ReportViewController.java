/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.Appointment;
import Model.BussApptMgntSyst;
import Model.UserClass;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import static javax.management.Query.value;

/**
 *
 * @author chip
 */
public class ReportViewController implements Initializable
{
    @FXML private Button btnConsultant;
    @FXML private Button btnType;
    @FXML private Button btnMonth;
    @FXML private Label lblReportName;
    @FXML private TableView<Appointment> tblReport;
    @FXML private TableColumn<Appointment, LocalDateTime> colStartDate;
    @FXML private TableColumn<Appointment, LocalDateTime> colEndDate;    
    @FXML private TableColumn<Appointment, String> colCustomerName;
    @FXML private TableColumn<Appointment, String> colAppointmentType;
    @FXML private TableColumn<Appointment, String> colUser;
    
    private static ObservableList<Appointment> filteredAppoinments = FXCollections.observableArrayList();
    private String userName = null;
    ResourceBundle resource;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        resource = rb;
        btnConsultant.setText(resource.getString("btnConsultant"));
        btnType.setText(resource.getString("btnType"));
        btnMonth.setText(resource.getString("btnMonth"));
        
        btnConsultant.setOnAction(event -> loadConsultantReport());
//        
//        btnType.setOnAction(value);
//        
//        btnMonth.setOnAction(value);
                
    }
    
    private String selectUser()
    {
        Dialog dialog = new Dialog();
        dialog.setHeaderText("Select User:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        //final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> cmbUsers = new ComboBox<String>();
        cmbUsers.setItems(UserClass.getAllUsers());
        
        grid.add(cmbUsers, 0, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter((diagButton)->
        {
           if (diagButton == ButtonType.OK)
            {
                return cmbUsers.selectionModelProperty().getValue().toString();
            }
            return null;
        });
        
        //String userName;
        dialog.showAndWait().ifPresent(response -> 
        {                        
            userName = (String)response;            
        });
        
        return userName;
    }
    
    private void loadConsultantReport()
    {
        selectUser();
        tblReport.setItems(BussApptMgntSyst.appointments.stream()
                .filter(a -> a.getUserName().equals(userName))
                .collect(Collectors
                        .collectingAndThen(toList(), l -> FXCollections.observableArrayList(l))));
        
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);                     
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
    
    
    
}
