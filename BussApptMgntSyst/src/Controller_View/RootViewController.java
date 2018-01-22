/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Chip
 */
public class RootViewController implements Initializable
{    
//    @FXML private AnchorPane rootPane; //root pane that holds other panes
    
    @FXML private Hyperlink hlinkSignOut;
    @FXML private Label dtNow;
    
    
    @FXML
    private void handleButtonAction(ActionEvent event)
    {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
//        AnchorPane pane;
//        try
//        {
//            pane = FXMLLoader.load(getClass().getResource("/Controller_View/MainView.fxml"));
//            rootPane.getChildren().setAll(pane);
//        } 
//        catch (IOException ex)
//        {
//            Logger.getLogger(RootViewController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }    
    
    
//    private void loadDefaultScene(ActionEvent event) throws IOException
//    {
//        
//        rootPane.getChildren().setAll(pane);
//    }
    
}
