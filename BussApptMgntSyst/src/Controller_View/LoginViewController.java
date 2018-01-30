/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.BussApptMgntSyst;
import Model.FxmlView;
import Model.UserClass;
//import Model.WindowEnum;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
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
//            login(txtUser.textProperty().get(),txtPass, event);
            //closeWindow(event);
        });
        
        btnCancel.setOnAction((event) ->
        {
            System.exit(0);//todo
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
        UserClass user = UserClass.getInstance();
        //user.setUserName(userName);
    
        if (UserClass.verifyUser(user, userName, password.getText().hashCode()))                    
        {
            //TODO: add logger
            sceneMgr.displayScene(root, child, "Main");
            
        }                    
        else
        {
            lblError.setText(resource.getString("error"));
            txtUser.setText("");
            txtUser.requestFocus();
            txtPass.setText("");
        }
    }  
}
