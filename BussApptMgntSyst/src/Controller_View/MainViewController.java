/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author chip
 */
public class MainViewController implements Initializable
{
    @FXML private Button btnCustomers;
    @FXML private Button btnSchedules;
    @FXML private Button btnReports;
    @FXML private Button btnPrevPeriod;
    @FXML private Button btnNextPeriod;
    @FXML private TableView dtCalender;
    @FXML private RadioButton radByMonth;
    @FXML private RadioButton radByWeek;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
