/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.BussApptMgntSyst;
import Model.UserClass;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.logging.*;
/**
 *
 * @author Chip
 */
public class RootViewController implements Initializable
{    
    Stage stage;    
    Locale locale = BussApptMgntSyst.locale;
    SceneManager sceneMgr = new SceneManager();
    AnchorPane root = BussApptMgntSyst.root;
    AnchorPane child;
        
    @FXML private Hyperlink hlinkSignOut;
    @FXML private Label dtNow;
    Logger logger = Logger.getLogger("Controller_View.RootViewController");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        logger.setParent(BussApptMgntSyst.logger);
        hlinkSignOut.setOnAction((event) -> 
        {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.NO);
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Signout?");
            alert.setContentText("Are you sure want to sign out?");
            //alert.getButtonTypes().addAll(yes, no);                       
           // Optional<ButtonType> response = alert.showAndWait();
            
            alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(x -> 
                {                        
                    logger.log(Level.INFO, "Logout Successful: " + 
                            UserClass.getInstance());
                    System.exit(0);
                });
        });
        
        dtNow.setText(getCurrentDate());
    }

    private String getCurrentDate()
    {        
        String result = "";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDateTime now = LocalDateTime.of(date, time);
        
        result = now.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        return result;
    }      
    
}
