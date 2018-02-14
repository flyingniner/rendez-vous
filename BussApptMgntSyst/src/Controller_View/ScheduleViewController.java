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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    @FXML private DatePicker  dtToDate;
    @FXML private TextField tstStartTime;
    @FXML private TextField txtEndTime;
    @FXML private ComboBox cmbCustomer;
    @FXML private TextArea txtNotes;
    @FXML private Button btnSave;
    @FXML private Button btnDelete;
    @FXML private Button btnClose;
    @FXML private TableView tblCalendar;
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        btnClose.setOnAction((event) -> sceneMgr.displayScene(root, child, "Main"));    
    }
    
    private void handleSaveButton()
    {}
    
    private void handleCloseButton()
    {}
    
    private void handleDeleteButton()
    {}
    
    

}
