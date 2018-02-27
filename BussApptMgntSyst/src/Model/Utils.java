/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Chip
 */
public class Utils        
{
    static boolean alertResponse = false; //stores response from alert box
        
    
    public static LocalDateTime convertUtcToLocalDateTime(Timestamp timestamp)
    {
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZoneId sysZoneid = ZoneId.systemDefault();        
        
        return timestamp.toLocalDateTime()
                            .atZone(utcZoneId)
                            .withZoneSameInstant(sysZoneid)
                            .toLocalDateTime();
    }
    
   public static Timestamp convertLocaLDateTimeToUtc(LocalDateTime localDatetime)
   {
        ZoneId utcZoneId = ZoneId.of("UTC");
        ZoneId sysZoneid = ZoneId.systemDefault();        
        
        return Timestamp.valueOf(localDatetime
                .atZone(sysZoneid)
                .withZoneSameInstant(utcZoneId)
                .toLocalDateTime());      
   }
   
   /**
     * Displays a Confirmation Alert box to the user.
     * @param headerText 
     * @param contentText 
     * @return 
     */
    public static boolean displayAlertConfirmation(String headerText, String contentText)
    {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(headerText);
                alert.setContentText(contentText);            

               alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(x -> alertResponse = true);    
               
        return alertResponse;
    }
    
    /**
     * Displays an informational pop-up message to the user.
     * @param headerText
     * @param contentText
     * @return 
     */
    public static boolean displayInformational(String headerText, String contentText)
    {
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(headerText);
                alert.setContentText(contentText);            

               alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(x -> alertResponse = true);    
               
        return alertResponse;
    }
    
    public static void displayAlertError(String headerText, String contentText)
    {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(headerText);
                alert.setContentText(contentText);            

               alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(x -> alertResponse = true);      
    }
    
}
