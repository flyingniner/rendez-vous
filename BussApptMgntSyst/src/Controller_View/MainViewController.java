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
import javafx.scene.control.RadioButton;
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
    @FXML private TableView dtCalender;
    @FXML private RadioButton radByMonth;
    @FXML private RadioButton radByWeek;
    @FXML private ToggleGroup group;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
        //FXMLLoader childScene = new FXMLLoader(getClass().getResource(FxmlView.MAIN.getFxmlFile()), rb);    
        //child = LoginViewController.getChild();
        
        
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
    }    
    
}
