/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.Address;
import Model.BussApptMgntSyst;
import Model.Customer;
import Model.Location;
import Model.Utils;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

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
    @FXML private Button btnCancel;
    @FXML private Button btnClear;
    @FXML private TextField txtSearch;
    @FXML private TextField txtCustId;
    @FXML private TextField txtCustName;
    @FXML private TextField txtAddress1;
    @FXML private TextField txtAddress2;
    @FXML private ComboBox cmbCity;
    @FXML private TextField txtPostal;
    @FXML private ComboBox cmbCountry;
    @FXML private TextField txtPhone;
    @FXML private Label lblCustSince;
    @FXML private CheckBox chkActive;
    @FXML private Hyperlink hLinkAddCity;
    @FXML private Label lblCustId;
    @FXML private Label lblCustName;
    @FXML private Label lblAddress1;
    @FXML private Label lblAddress2;
    @FXML private Label lblCity;
    @FXML private Label lblCountry;
    @FXML private Label lblPostal;
    @FXML private Label lblPhone;
    @FXML private Label lblCustSinceValue;
    
    
    @FXML private TableView<Customer> tblCustomers; // = new TableView<Customer>();
    @FXML private TableColumn<Customer, Number> colCustId; // = new TableColumn<CustomerClass, Number>();
    @FXML private TableColumn<Customer, String> colCustName; // = new TableColumn<CustomerClass, String>();
    @FXML private TableColumn<Customer, String> colCity; // = new TableColumn<CustomerClass, String>();
    @FXML private TableColumn<Customer, String> colCountry; // = new TableColumn<CustomerClass, String>();
    
    private int selectedCustId;
    private static ObservableList<Customer> filteredCustList = FXCollections.observableArrayList();
    private static String tempCountry = null;
    private ResourceBundle resource;
    
    boolean alertResponse = false; //stores response from alert box
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        resource = rb;
        //get language for form controls
        btnAddSave.setText(resource.getString("save"));
        btnCancel.setText(resource.getString("cancel"));
        btnClear.setText(resource.getString("clear"));
        lblCustId.setText(resource.getString("custId"));
        lblCustName.setText(resource.getString("custName"));
        lblAddress1.setText(resource.getString("address"));
        lblAddress2.setText(resource.getString("address"));
        lblCity.setText(resource.getString("city"));
        lblCountry.setText(resource.getString("country"));
        lblPhone.setText(resource.getString("phone"));
        lblPostal.setText(resource.getString("postal"));
        hLinkAddCity.setText(resource.getString("addCityCountry"));
        lblCustSince.setText(resource.getString("custSince"));
        lblCustSince.setVisible(false);
        chkActive.setText(resource.getString("active"));
        txtCustId.setEditable(false);
        txtCustId.setText(String.valueOf(Customer.getNextCustomerId()));
               
        
        //set form element behaviours
        btnAddSave.setOnAction((event) -> handleSaveButton());
        btnClear.setOnAction((event) -> clearFields());
        btnCancel.setOnAction((event) -> handleCloseButton());                       
        
        tblCustomers.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
        {
            populateCustomerFields(newValue);
        });
        
        txtSearch.textProperty()
                .addListener((observable, oldValue, newValue) ->
        {
            handleSearchCustomers(oldValue, newValue);            
        });
        
        hLinkAddCity.setOnAction((event) -> 
        {
            handleNewLocation();
            loadComboBoxValues();
        });

        cmbCountry.valueProperty().addListener((observable, oldValue, newValue) ->
        {               
            cmbCity.setDisable(newValue==null);
            filterCities(newValue);                
        });

        cmbCity.setDisable(true);
        
        
        //initialize form data elements
        prePopCustomerTable();
        loadComboBoxValues();                
        
        //use this model to track changes to the field for data validation...
        txtCustId.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (oldValue == true)
                isInt(txtCustId);                   
        });     
    }    

    
    private void filterCities(Object newValue)
    {
        if (newValue!=null)
        {            
            ObservableList<String> tmp = FXCollections.observableArrayList();
            BussApptMgntSyst.locations.forEach((t ->
            {                
                if(t.getCountryName().equals(newValue))
                        tmp.add(t.getCityName());
            }));

            cmbCity.setItems(tmp.sorted());
        }
    }

    /**
     * Handles saving of a customer record.
     */
    private void handleSaveButton()
    {
        saveCustomer();
        clearFields();
    }

    /**
     * Handles closing of the Customer View. If the form has been updated or
     * an existing customer has been modified but not saved, user is presented 
     * with an alert.
     */
    private void handleCloseButton()
    {
        if (customerChange())
        {
            String headerText = "Unsaved Changes";
            String contentText = "You have unsaved changes. Do you want"
                    + "\nto continue without saving?";
            Utils.displayAlertConfirmation(headerText, contentText);
            if (!alertResponse)
                return;
        }
        
        sceneMgr.displayScene(root, child, "Main");
    }
    
    /**
     * Loads the list of current customers into the table.
     */
    private void prePopCustomerTable()
    {
//        if (BussApptMgntSyst.customers.isEmpty())
//        {
            BussApptMgntSyst.customers = Customer.getCustomers();
            
//        }
        tblCustomers.setItems(BussApptMgntSyst.customers);
        colCustId.setCellValueFactory(x -> x.getValue().custIdProperty());      
        colCustName.setCellValueFactory(x -> x.getValue().custNameProperty());     
        colCity.setCellValueFactory(x -> x.getValue().cityProperty());
        colCountry.setCellValueFactory(x -> x.getValue().countryProperty());
    }
    
    /**
     * Populates update fields with the currently selected customer. 
     * @param cust 
     */
    private void populateCustomerFields(Customer cust)
    {
        txtCustId.setText(String.valueOf(cust.getCustID()));
        txtCustId.setEditable(false);
        txtCustName.setText(cust.getCustName());
        txtAddress1.setText(cust.getAddr1());
        txtAddress2.setText(cust.getAddr2());
        cmbCity.setValue(cust.getCity());
        cmbCountry.setValue(cust.getCountry());
        txtPostal.setText(cust.getPostalCode());
        txtPhone.setText(cust.getPhone());
        chkActive.setSelected(cust.getActive());
        lblCustSinceValue.setText(cust.getCustSince());
        lblCustSince.setVisible(true);        
    }
    
    /**
     * Clears the fields.
     */
    private void clearFields()
    {
        txtCustId.setText("");
        txtCustId.setEditable(true);
        txtCustName.setText("");
        txtAddress1.setText("");
        txtAddress2.setText("");
        cmbCity.setValue(null);  
        cmbCity.setDisable(true);
        cmbCountry.setValue(null);        
        txtPostal.setText("");
        txtPhone.setText("");
        chkActive.setSelected(false);
        lblCustSince.setVisible(false);
        lblCustSinceValue.setVisible(false);
        
    }
        
    /**
     * Filters the Customer table based on the user's entry
     * and selects the first result by default. The table dynamically 
     * filters out values which do not match the search entry.
     * @param oldValue The previous user entry
     * @param newValue The new/current user entry
     */
    private void handleSearchCustomers(String oldValue, String newValue)
    {
        //if this is the first time in the search, set the filtered list
        if (oldValue.isEmpty())
        {    
            filteredCustList=BussApptMgntSyst.customers;
            //reset the parts table to equal the current collection
            tblCustomers.setItems(filteredCustList);
        }
        
        //if this is a subsequent search, and the user has hit backspace, reset
        //the filtered list.
        if (oldValue.length() > newValue.length())
            filteredCustList=BussApptMgntSyst.customers;

        //create a temporary list to hold the filtered results
        ObservableList<Customer> tempList = FXCollections.observableArrayList();
        
        //iterate through the filtered matches and look for a matching part
        //search is on the part id or part name
        //if this is the first time through, the filtered list is the full collection
        for (Customer cust : filteredCustList)
        {
            String custDetail = cust.toString().toUpperCase();
            if (custDetail.contains(newValue.toUpperCase()))
                tempList.add(cust);            
        }
        
        //set the filtered matches to equal the temp list
        filteredCustList = tempList;
        
        //display the filtered results in the table
        tblCustomers.setItems(filteredCustList);
        //select the first row of table as being the most likely result
        //tblCustomers.getSelectionModel().selectFirst();        
    }
    
    /**
     * Saves the details as either a new customer or updates an existing 
     * customer. If this this is an existing customer, user is prompted to 
     * confirm they want to update the record.
     */
    private void saveCustomer()
    {        
        int id = Integer.parseInt(txtCustId.getText());
        Customer cust = new Customer();
      //  cust.setCustID(id);
        cust.setCustName(txtCustName.getText());
        cust.setAddr1(txtAddress1.getText());
        cust.setAddr2(txtAddress2.getText());
        cust.setCity(cmbCity.getValue().toString());
        cust.setCountry(cmbCountry.getValue().toString());
        cust.setPostalCode(txtPostal.getText());
        cust.setPhone(txtPhone.getText());
        cust.setActive(chkActive.isSelected());               
       
            
        //check if this is a new customer
        try
        {            
            //Check if customer already exists in the Customer collection
            if (customerExists(id).isPresent())
            {
                cust.setCustID(id);
                cust.setCustSince(lblCustSinceValue.getText());
                //if true, ask user to confirm the request
                String headerText = "Overwrite Customer?";
                String contentText = "Saving will override the current customer. \n"
                        + "Do you want to continue?";
                alertResponse = Utils.displayAlertConfirmation(headerText, contentText);
                
                if (alertResponse == true)
                    Customer.updateCustomerInDb(cust, id);
                    //updateExistingCustomer(id);     
                else
                    return;
            }
                        
            else //this is a new customer
            { 
                cust.setCustID(id);                
                Customer.addCustomerToDb(cust);
            }       
        }
        catch(Exception e)
        {   
            //TODO handle the exception
            throw e;
        }
    }
    
    
    private Optional<Customer> customerExists(int id)
    {
        return BussApptMgntSyst.customers.stream()
                .filter(x -> ((Integer) x.getCustID()).equals(id)).findFirst();                   
    }
    
//    private void updateExistingCustomer(int id)
//    {
//        Optional<Customer> c = BussApptMgntSyst.customers.stream()
//                .filter(x -> ((Integer) x.getCustID()).equals(id))
//                .findFirst();
//        //int index = customers.indexOf(c.get());
//        saveCustomer(BussApptMgntSyst.customers.indexOf(c.get()));               
//    }
    
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
                Utils.displayAlertConfirmation("Data Input Error", message);            
                
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

    private boolean customerChange()
    {
        //TODO 
        //Return true if the record has been changed but not saved
        //Return false otherwise
        
        return false;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    

    /**
     * Loads the city and country combo boxes with the available options. If 
     * this is the first time that cities or countries have been accessed since
     * the application was launched, the values are returned from SQL.
     */
    private void loadComboBoxValues()
    {
        if(BussApptMgntSyst.locations.isEmpty())
            Location.loadlLocations();
        
        ObservableList<String> results = FXCollections.observableArrayList();
        BussApptMgntSyst.locations.forEach((x) -> 
        {
            if (!results.contains(x.getCountryName()))
                results.add(x.getCountryName());
        });
        
        cmbCountry.setItems(results.sorted());
    }
    

    /**
     * Presents the user with a dialog box to add a city and associate it 
     * with a country.
     */
    private void handleNewLocation()
    {
        Dialog<Pair<String,String>> dialog = new Dialog<>();
        dialog.setHeaderText(resource.getString("newLocation"));
        dialog.setContentText(resource.getString("newLocationContextText")); 
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //form controls
        TextField cityName = new TextField();
        cityName.setPromptText(resource.getString("cityName"));            
        ComboBox<String> countrySelect = new ComboBox<String>();
        countrySelect.setItems((ObservableList<String>)cmbCountry.getItems());
        Hyperlink addNewCountry = new Hyperlink("Add new country");
        
        
        //add listern to the country combobox
        countrySelect.valueProperty().addListener((observable, oldValue, newValue) ->
        {           
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        //add elements to the dialog
        grid.add(new Label("Enter city name: "), 0, 0);
        grid.add(cityName, 1, 0);
        grid.add(new Label("Choose country: "), 0, 1);
        grid.add(countrySelect,1,1);
        grid.add(addNewCountry,2,1);
        
        //load dialog for adding new countries, then return a new sorted list
        addNewCountry.setOnAction((event) ->
        {           
            handleAddCountry();  
            ObservableList<String> results = FXCollections.observableArrayList();
            BussApptMgntSyst.locations.forEach((x) -> 
            {
               if (!results.contains(x.getCountryName()))
                       results.add(x.getCountryName());               
            });
            
            results.add(tempCountry);
            countrySelect.setItems(results.sorted());            
        });
        
        //add grid to the pane
        dialog.getDialogPane().setContent(grid);
        
        //get user input values from the dialog
        dialog.setResultConverter((diagButton) ->
        {            
            if (diagButton == ButtonType.OK)            
                return new Pair<String, String>(cityName.getText(),countrySelect.getValue());                           
            return null;
        });
        
        cityName.requestFocus();
        dialog.showAndWait().ifPresent((userInput) ->
        {
            
            Boolean cityMissing = true;
            Boolean countryMissing = true;

            //get user values
            String cityValue = userInput.getKey().trim();
            String countryValue = userInput.getValue().trim();
            
            //check if fields are complete
            if (!cityValue.isEmpty() && cityValue.length()>2)
                cityMissing = false;
            if (!countryValue.isEmpty() && cityValue.length()>2)
                countryMissing = false;
            
            //if userinput passes validation, add it to the collection
            if (cityMissing == false && countryMissing == false)
            {
                Location location = new Location(countryValue, cityValue);
                BussApptMgntSyst.locations.add(location);
//                ();
                filterCities(location);
//                Address.AddCityToDb(newCity.getKey(), newCity.getValue());
            }            
            else if (cityMissing == true)
            {
                Utils.displayAlertConfirmation("Missing City Value", "You must enter a city name.");
                handleNewLocation();
            }            
        });  
    }

    /**
     * Adds a new country to the collection. 
     */
    private void handleAddCountry()
    {
        TextInputDialog diag = new TextInputDialog();
        diag.setContentText(resource.getString("addCountryContextText"));
        diag.setHeaderText(resource.getString("addCountry"));
       
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField countryName = new TextField();
        countryName.setPromptText(resource.getString("addCountryName"));
        
        grid.add(new Label(resource.getString("countryNameLabel")), 0, 0);
        grid.add(countryName, 1, 0);
        diag.getDialogPane().setContent(grid);
                
        //set focus on the country name field
        Platform.runLater(() -> countryName.requestFocus());
        
        diag.setResultConverter((diagButton)->
        {
           if (diagButton == ButtonType.OK)
            {
                return countryName.getText();
            }
            return null;
        });
        
        
        diag.showAndWait().ifPresent(response -> 
        {            
            System.out.println(response);  
            tempCountry= response;
//            Address.addCountryToDb(response);
        });
    }
}
