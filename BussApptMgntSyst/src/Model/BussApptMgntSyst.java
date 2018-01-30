/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Chip
 */
public class BussApptMgntSyst extends Application
{
    public static Locale locale;
    public static AnchorPane root;
    public static AnchorPane child;
//    public static UserClass currentUser;
    
//    @FXML private AnchorPane subPane;   
    
    @Override
    public void start(Stage stage) throws Exception
    {
        locale = Locale.getDefault();
//        Locale locale = new Locale("es","VE");
//        
                
        ResourceBundle rb = ResourceBundle.getBundle("Model.BAMS", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.ROOT.getFxmlFile()), rb);
                
        root = loader.load();                
        loader =new FXMLLoader(getClass().getResource(FxmlView.LOGIN.getFxmlFile()),rb);
        child = loader.load();
        root.getChildren().add(child);
;        
//        Parent root = FXMLLoader.load(getClass().getResource("/Controller_View/LoginView.fxml"),rb);
//        subPane = FXMLLoader.load(getClass().getResource("/Controller_View/MainView.fxml"));
//        subPane.getChildren().setAll(root);
        
        Scene scene = new Scene(root);
        
        
        stage.setScene(scene);
        stage.show(); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
        
    }    
}
