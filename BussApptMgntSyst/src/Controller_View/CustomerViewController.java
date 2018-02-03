/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.BussApptMgntSyst;
import Model.Customer;
import Model.UserClass;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chip
 */
public class CustomerViewController implements Initializable
{
    Stage stage;    
    Locale locale = BussApptMgntSyst.locale;
    SceneManager sceneMgr = new SceneManager();
    AnchorPane root = BussApptMgntSyst.root;
    AnchorPane child;

    @FXML private Button btnAddSave;
    @FXML private Button btnClose;
    @FXML private TextField txtSearch;
    @FXML private TextField txtCustId;
    @FXML private TextField txtCustName;
    @FXML private TextField txtAddress1;
    @FXML private TextField txtAddress2;
    @FXML private TextField txtCity;
    @FXML private TextField txtPostal;
    @FXML private TextField txtCountry;
    @FXML private TextField txtPhone;
    @FXML private Label lblCustSince;
    @FXML private CheckBox chkActive;
    
    @FXML private TableView<Customer> tblCustomers; // = new TableView<Customer>();
    @FXML private TableColumn<Customer, Number> colCustId; // = new TableColumn<CustomerClass, Number>();
    @FXML private TableColumn<Customer, String> colCustName; // = new TableColumn<CustomerClass, String>();
    @FXML private TableColumn<Customer, String> colCity; // = new TableColumn<CustomerClass, String>();
    @FXML private TableColumn<Customer, String> colCountry; // = new TableColumn<CustomerClass, String>();
    
    private int selectedCustId;
    ObservableList<Customer> customers = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        btnClose.setOnAction((event) -> sceneMgr.displayScene(root, child, "Main"));
        btnAddSave.setOnAction((event) ->
        {
            saveCustomer();
            prePopCustomerTable();
            clearFields();
            
        });
        
        //use this model to track changes to the field for data validation...
        txtCustId.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (oldValue == true)
                isInt(txtCustId);                   
        });
        
        prePopCustomerTable();
        
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            populateCustomerFields(newValue);
        });
        
        
        
        
        
    }    
    
    private void prePopCustomerTable()
    {
        Customer customer = new Customer();
        
        customers = customer.getCustomers();
        tblCustomers.setItems(customers);
        colCustId.setCellValueFactory(x -> x.getValue().custIdProperty());      
        colCustName.setCellValueFactory(x -> x.getValue().custNameProperty());     
        colCity.setCellValueFactory(x -> x.getValue().cityProperty());
        colCountry.setCellValueFactory(x -> x.getValue().countryProperty());
    }
    
    private void populateCustomerFields(Customer cust)
    {
        //selectedCustId = tblCustomers.getSelectionModel().getSelectedItem().getCustID();
        //Customer cust = customers.get(selectedCustId);
        txtCustId.setText(String.valueOf(cust.getCustID()));
        txtCustName.setText(cust.getCustName());
        txtAddress1.setText(cust.getAddr1());
        txtAddress2.setText(cust.getAddr2());
        txtCity.setText(cust.getCity());
        txtCountry.setText(cust.getCountry());
        txtPostal.setText(cust.getPostalCode());
        txtPhone.setText(cust.getPhone());
        chkActive.setSelected(cust.getActive());
        lblCustSince.setText(cust.getCustSince());
    }
    
    private void clearFields()
    {
        txtCustId.setText("");
        txtCustName.setText("");
        txtAddress1.setText("");
        txtAddress2.setText("");
        txtCity.setText("");
        txtCountry.setText("");
        txtPostal.setText("");
        txtPhone.setText("");
        chkActive.setSelected(false);
        lblCustSince.setText("");
    }
    
    private boolean saveCustomer()
    {
        int id = Integer.parseInt(txtCustId.getText());
        Customer cust = new Customer();
        cust.setCustID(id);
        cust.setCustName(txtCustName.getText());
        cust.setAddr1(txtAddress1.getText());
        cust.setAddr2(txtAddress2.getText());
        cust.setCity(txtCity.getText());
        cust.setCountry(txtCountry.getText());
        cust.setPostalCode(txtPostal.getText());
        cust.setPhone(txtPhone.getText());
        cust.setActive(chkActive.isSelected());
        
        if(lblCustSince.getText().isEmpty())
        {
//            LocalDate date = LocalDate.now();
//            LocalTime time = LocalTime.now();
//            LocalDateTime now = LocalDateTime.of(date,time);
            cust.setCustSince(LocalDateTime.now().toString());
        }
        else
            cust.setCustSince(lblCustSince.getText());
            
        
        try
        {
            if (customerExists(id)!=null)
            {
                //check if current obj == sql obj
                    //if match, alert user there are now changes
                        //return
                    //if not match, alert user to confirm they want to make changes
                        //if response == no
                            //return
                ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Overwrite Customer?");
                alert.setContentText("Saving will override the current customer. \n"
                        + "Do you want to continue?");            

                alert.showAndWait()
                    .filter(response -> response == ButtonType.CANCEL)
                    .ifPresent(x -> 
                    {                        
                        return;
                    });      
            }
            else
            {
                customers.add(cust);
                return true;
                //save the record    
            }
            
                
            
//              
        }
        catch(Exception e)
        {
            
        }
        
        
        
        return false;
    }
    
    private Optional<Customer> customerExists(int id)
    {
        return customers.stream()
                .filter(x -> ((Integer) x.getCustID()).equals(id)).findFirst();                   
    }
    
    /**
     * Validates the user's input value can be cast to a integer. If it cannot,
     * user is presented with an error in the form of an Alert box.
     * @param field The TextField from the view.
     */
    private boolean isInt(TextField field)
    {
        if (field.textProperty().getValueSafe().length()!=0)
        {
            try
            {
                int i = Integer.parseInt(field.textProperty().getValueSafe());
                field.setStyle("");
                return true;
            }
            catch(NumberFormatException e)
            {               
                field.setStyle("-fx-text-fill: Red; -fx-font-weight: Bold");
                
                String message = "The input value for [" +field.getId()+ "] "+
                "must be a valid number data type. Required data type: Integer.";                
                //displayAlert("Data Input Error", message);            
                
                field.requestFocus();
                return false;
            }
        }
        else
        {
            field.setStyle("");
            return false;
        }        
    }
}
