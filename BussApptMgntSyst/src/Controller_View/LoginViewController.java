/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.Appointment;
import Model.AppointmentReminder;
import Model.BussApptMgntSyst;
import Model.UserClass;
//import Model.WindowEnum;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author chip
 */
public class LoginViewController implements Initializable
{
    Stage stage;    
    Locale locale = BussApptMgntSyst.locale;
    SceneManager sceneMgr = new SceneManager();
    AnchorPane root = BussApptMgntSyst.root;
    public AnchorPane child = BussApptMgntSyst.child;
    public static AnchorPane loginView;
    private static Logger logger = Logger.getLogger("Controller_View");
    
    @FXML private Label lblUser;
    @FXML private Label lblPass;
    @FXML private Label lblError;
    @FXML private Button btnLogin;
    @FXML private Button btnCancel;
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    
    @FXML private ResourceBundle resource;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        logger.setParent(BussApptMgntSyst.logger);
        resource = rb;
    
        //set text to appropriate langauge
        lblUser.setText(resource.getString("userName"));
        lblPass.setText(resource.getString("password"));
        btnLogin.setText(resource.getString("login"));
        btnCancel.setText(resource.getString("cancel"));
        lblError.setText("");        
        
        //setup event handlers
        btnLogin.setOnAction((event) ->                
        {   
            
            login(txtUser.getText(), txtPass);
        });
        
        btnCancel.setOnAction((event) ->
        {
            System.exit(0);
        });
        
        txtUser.setOnMouseClicked((event) ->
        {
            if(lblError.getText().length()>0)
                lblError.setText("");
        });
        
        txtUser.setOnKeyPressed((event) ->
        {
            if(event.getCode().equals(KeyCode.ENTER))
            {    event.consume();
                txtPass.requestFocus();
            }
        });
        
        txtPass.setOnMouseClicked((event) ->
        {
            if(lblError.getText().length()>0)
                lblError.setText("");
        });
        
        txtPass.setOnKeyPressed((event) ->
        {
            if(event.getCode().equals(KeyCode.ENTER))
            {                
                login(txtUser.getText(), txtPass);             
            }            
        });

        btnLogin.setOnKeyPressed((event) ->
        {
            if(!event.getCode().equals(KeyCode.ENTER))
            {}    
            else                                 
                login(txtUser.getText(), txtPass);                         
        });
        
        txtUser.setText("test");
        txtPass.setText("test");              
    }

    private void login(String userName, PasswordField password) throws IllegalArgumentException
    {
        //BussApptMgntSyst.cursor.set(Cursor.WAIT);
        UserClass user = UserClass.getInstance();
        
        //user.setUserName(userName);
        
        if (UserClass.verifyUser(user, userName, password.getText().hashCode()))                    
        {

            logger.log(Level.INFO, "Login Successful: " + user.toString());
            
            sceneMgr.displayScene(root, child, "Main");   
            
        }                    
        else
        {
            lblError.setText(resource.getString("error"));
            txtUser.setText("");
            txtUser.requestFocus();
            txtPass.setText("");
            logger.log(Level.WARNING, "Unsuccessful login attempt: " + user.toString());
        }
    }  
}
