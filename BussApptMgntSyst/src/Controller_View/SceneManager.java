/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.BussApptMgntSyst;
import Model.FxmlView;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author chip
 */
public class SceneManager
{
    Stage stage;    
    Locale locale = BussApptMgntSyst.locale;
    
    public void displayScene(AnchorPane root, AnchorPane child, String screenName) 
    {
        String fileName = null;
        switch (screenName)
        {
            case "Customer":
                fileName = FxmlView.CUSTOMER.getFxmlFile();
                break;
            case "Main":
                fileName = FxmlView.MAIN.getFxmlFile();
                break;
            case "Schedule":
                fileName = FxmlView.SCHEDULE.getFxmlFile();
                break;
            case "Reports":
                fileName = FxmlView.REPORTS.getFxmlFile();
                break;
            case "Login":
                fileName = FxmlView.LOGIN.getFxmlFile();
                break;
            default:
                throw new AssertionError();
        }
        try
        {
            ResourceBundle rb = ResourceBundle.getBundle("Model.BAMS", locale);
            
            if (root != null)
            {    
                //root.getChildren().stream().forEach(System.out::println);      
                root.getChildren().remove(1);
            }
            else
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.ROOT.getFxmlFile()));
                root = loader.load();
            }
                                    
            FXMLLoader childScene = new FXMLLoader(getClass().getResource(fileName), rb);    
            
            child = childScene.load();
            
            root.getChildren().addAll(child);
            
//            root.getChildren().stream().forEach(System.out::println);      
            Scene scene = root.getScene();
                  
            stage = (Stage) root.getScene().getWindow();
//         
            stage.setScene(scene);
            stage.show();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
