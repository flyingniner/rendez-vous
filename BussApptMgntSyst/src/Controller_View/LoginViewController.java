/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_View;

import Model.UserClass;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author chip
 */
public class LoginViewController implements Initializable
{
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
            login(txtUser.textProperty().get(),txtPass);
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
        
        txtPass.setOnMouseClicked((event) ->
        {
            if(lblError.getText().length()>0)
                lblError.setText("");
        });
                
    }

    private void login(String userName, PasswordField password) throws IllegalArgumentException
    {
        String deleteme = password.getText();
        if (UserClass.verifyUser(userName,password.getText()))
        {
            //todo
            //set global variable to hold user info
            //load RootView and MainView
            //close LoginView
            lblError.setText("you did it!");
        }
        else
        {
            lblError.setText(resource.getString("error"));
            txtUser.setText("");
            txtPass.setText("");
        }
    }    
}
