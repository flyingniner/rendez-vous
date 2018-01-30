/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.BussApptMgntSyst;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    @FXML private TextField txtState;
    @FXML private TextField txtPostal;
    @FXML private TextField txtCountry;
    @FXML private TextField txtPhone;
    @FXML private Label lblCustSince;
    @FXML private CheckBox chkActive;
    @FXML private TableView tblCustomers;
    @FXML private TableColumn colCustId;
    @FXML private TableColumn colCustName;
    @FXML private TableColumn colCity;
    @FXML private TableColumn colCountry;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        btnClose.setOnAction((event) -> sceneMgr.displayScene(root, child, "Main"));
    }    
    
    private boolean addCustomer()
    {
        return false;
    }
}
