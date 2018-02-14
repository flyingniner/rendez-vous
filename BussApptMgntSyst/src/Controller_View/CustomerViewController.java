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
import java.net.URL;
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
    @FXML private Button btnClose;
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
    //@FXML private Hyperlink hLinkAddCountry;
    
    @FXML private TableView<Customer> tblCustomers; // = new TableView<Customer>();
    @FXML private TableColumn<Customer, Number> colCustId; // = new TableColumn<CustomerClass, Number>();
    @FXML private TableColumn<Customer, String> colCustName; // = new TableColumn<CustomerClass, String>();
    @FXML private TableColumn<Customer, String> colCity; // = new TableColumn<CustomerClass, String>();
    @FXML private TableColumn<Customer, String> colCountry; // = new TableColumn<CustomerClass, String>();
    
    private int selectedCustId;
    private static ObservableList<Customer> filteredCustList = FXCollections.observableArrayList();
    private static String tempCountry = null;
    
    boolean alertResponse = false; //stores response from alert box
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //set form element behaviours
        btnAddSave.setOnAction((event) -> handleSaveButton());
        btnClear.setOnAction((event) -> clearFields());
        btnClose.setOnAction((event) -> handleCloseButton());                       
        
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
//        hLinkAddCountry.setOnAction((event) -> handleAddCountry());
        cmbCity.setDisable(true);
       // Address.getAllLocations();
        //cmbCountry.setItems((ObservableList) BussApptMgntSyst.cityCountryPairs.keySet());

        cmbCountry.valueProperty().addListener((observable, oldValue, newValue) ->
        {               
            cmbCity.setDisable(newValue==null);
            filterCities(newValue);                
        });

        
        
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
        prePopCustomerTable();
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
            displayAlert(headerText, contentText);
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
        Customer customer = new Customer();
        
        if (BussApptMgntSyst.customers.isEmpty())
            BussApptMgntSyst.customers = customer.getCustomers();
        tblCustomers.setItems(BussApptMgntSyst.customers.sorted());
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
        lblCustSince.setText(cust.getCustSince());
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
        lblCustSince.setText("");
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
        //create an instance of Customer using field values
        int id = Integer.parseInt(txtCustId.getText());
        Customer cust = new Customer();
        cust.setCustID(id);
        cust.setCustName(txtCustName.getText());
        cust.setAddr1(txtAddress1.getText());
        cust.setAddr2(txtAddress2.getText());
        cust.setCity(cmbCity.getValue().toString());
        cust.setCountry(cmbCountry.getValue().toString());
        cust.setPostalCode(txtPostal.getText());
        cust.setPhone(txtPhone.getText());
        cust.setActive(chkActive.isSelected());               
        if(lblCustSince.getText().isEmpty())
            cust.setCustSince(LocalDateTime.now().toString());        
        else
            cust.setCustSince(lblCustSince.getText());
            
        //check if this is a new customer
        try
        {            
            //Check if customer already exists in the Customer collection
            if (customerExists(id)!=null)
            {
                //if true, ask user to confirm the request
                String headerText = "Overwrite Customer?";
                String contentText = "Saving will override the current customer. \n"
                        + "Do you want to continue?";
                displayAlert(headerText, contentText);
                
                if (alertResponse == true)
                    updateExistingCustomer(id);     
                else
                    return;
            }
                        
            else //this is a new customer
            {                
                BussApptMgntSyst.customers.add(cust); //add to the collection in memory
                //TODO add to MySql...
            }       
        }
        catch(Exception e)
        {     
            throw e;
        }
    }
    
    private void saveCustomer(int index)
    {
        //get current instance of Customer and update values
        //int id = );
        Customer cust = BussApptMgntSyst.customers.get(index);
        cust.setCustID(Integer.parseInt(txtCustId.getText()));
        cust.setCustName(txtCustName.getText());
        cust.setAddr1(txtAddress1.getText());
        cust.setAddr2(txtAddress2.getText());
        cust.setCity(cmbCity.getValue().toString());
        cust.setCountry(cmbCountry.getValue().toString());
        cust.setPostalCode(txtPostal.getText());
        cust.setPhone(txtPhone.getText());
        cust.setActive(chkActive.isSelected());               
//        if(lblCustSince.getText().isEmpty())
//            cust.setCustSince(LocalDateTime.now().toString());        
//        else
//            cust.setCustSince(lblCustSince.getText());
        BussApptMgntSyst.customers.set(index, cust);
        Customer.updateCustomerInDb(cust, index);
        //Customer.updateCustomAddressIndDb(cust, index);
        //Customer.udpateCustomerCityInDb(cust, index);
        //Customer.udpateCustomerCountryInDb(cust, index);
    }
    
    private Optional<Customer> customerExists(int id)
    {
        return BussApptMgntSyst.customers.stream()
                .filter(x -> ((Integer) x.getCustID()).equals(id)).findFirst();                   
    }
    
    private void updateExistingCustomer(int id)
    {
        Optional<Customer> c = BussApptMgntSyst.customers.stream()
                .filter(x -> ((Integer) x.getCustID()).equals(id))
                .findFirst();
        //int index = customers.indexOf(c.get());
        saveCustomer(BussApptMgntSyst.customers.indexOf(c.get()));
        
        
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
                displayAlert("Data Input Error", message);            
                
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
     * Displays a Confirmation Alert box to the user.
     * @param headerText 
     * @param contentText 
     */
    private void displayAlert(String headerText, String contentText)
    {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(headerText);
                alert.setContentText(contentText);            

               alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(x -> alertResponse = true);      
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
        //TODO: this is wrong, need to get country info from locations
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
        dialog.setHeaderText("New Location");
        dialog.setContentText("Add a new location: ");                
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //form controls
        TextField cityName = new TextField();
        cityName.setPromptText("city name");                        
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
                displayAlert("Missing City Value", "You must enter a city name.");
                handleNewLocation();
            }            
        });  
        
        
    }

    private void handleAddCountry()
    {
        TextInputDialog diag = new TextInputDialog();
        diag.setContentText("Add new country name: ");
        diag.setHeaderText("Add Country");
       
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField countryName = new TextField();
        countryName.setPromptText("Country name");
        
        grid.add(new Label("Enter Country name: "), 0, 0);
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
