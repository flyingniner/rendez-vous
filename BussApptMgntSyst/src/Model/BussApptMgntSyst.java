/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;
import java.util.logging.*;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Chip
 */
public class BussApptMgntSyst extends Application
{
    //public static ObjectProperty<Cursor> cursor = new SimpleObjectProperty<>(Cursor.DEFAULT);
    public static Locale locale;
    public static AnchorPane root;
    public static AnchorPane child;
    public static Logger logger;
    public static ObservableList<Customer> customers = FXCollections.observableArrayList(); 
    public static ObservableList<Location> locations = FXCollections.observableArrayList();  
    public static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
   
    
    @Override
    public void start(Stage stage) throws Exception
    {
        locale = Locale.getDefault();               
        //locale = new Locale("es","VE");        
                
        
        ResourceBundle rb = ResourceBundle.getBundle("Model.BAMS", locale);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FxmlView.ROOT.getFxmlFile()), rb);
                
        root = loader.load();                
        loader =new FXMLLoader(getClass().getResource(FxmlView.LOGIN.getFxmlFile()),rb);
        child = loader.load();
        root.getChildren().add(child);

        Scene scene = new Scene(root);
                
        stage.setScene(scene);
        stage.show(); 
        //scene.setCursor(Cursor.DEFAULT);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {   
//        FileHandler handler = new FileHandler("./BussApptMgntSystLog.xml");        
//        logger = Logger.getLogger("Model");
//        logger.addHandler(handler);        
//        logger.setLevel(Level.ALL);
//        logger.log(Level.INFO, "Application launch");
        
        
        FileHandler handler = new FileHandler("./BussApptMgntSystLog.xml",10000,2, true);        
        SimpleFormatter formatter = new SimpleFormatter();
        handler.setFormatter(formatter);
        
        logger = Logger.getLogger("Model");
        logger.addHandler(handler);        
        logger.setLevel(Level.ALL);
        logger.log(Level.INFO, "Application launch");
        
        
        
        launch(args);  

    }    
}
