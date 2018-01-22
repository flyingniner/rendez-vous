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
import javafx.scene.control.Label;

/**
 *
 * @author chip
 */
public class ReminderViewController implements Initializable
{
    @FXML private Label lblTitle;
    @FXML private Label lblMessage;
    @FXML private Button btnViewAppt;
    @FXML private Button btnClose;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }  
}
