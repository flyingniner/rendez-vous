/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author chip
 */
public class UserClass
{
    private int userID;
    private String userName;
    private int userPassword;
    
    private static final UserClass instance = new UserClass();
    
    private UserClass() {};
    
    public static UserClass getInstance() { return instance; }
    
    private int getUserID() { return this.userID; }
    private void setUserID(String userID) { this.userID = Integer.parseInt(userID); }
    
    public String getUserName() { return this.userName; }    
    private void setUserName(String userName) { this.userName = userName; }
    
    private int getUserPassword() { return this.userPassword; }
    private void setUserPassword(int userPassword) { this.userPassword = userPassword; }

    
    /**
     * Checks the database for a matching user. Returns a boolean of the result
     * @param user
     * @param userName
     * @param password
     * @return TRUE if a match was found and the password matched, otherwise FALSE
     */
    public static boolean verifyUser(UserClass user, String userName, int password)
    {
        boolean result = false;
        
        String queryString = "SELECT userId, userName, password " +
                    "FROM user WHERE userName = ?;";
        
        try
        {            
            SqlHelperClass sql = new SqlHelperClass();
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            stmt.setString(1, userName);
            
            ResultSet rs = sql.executeQuery(stmt);
                       
            if(rs.next()) //query string returns a result
            {
                getInstance().setUserName(userName);
                result = (rs.getString("password").hashCode() == password) ? true:false;
                
                if (result)
                {
                    UserClass.getInstance().setUserID(rs.getString("userId"));
                    UserClass.getInstance().setUserPassword(password);
                }
                rs.close();
            }
            
        }
        catch (SQLException e)
        {            
            BussApptMgntSyst.logger.fine(e.getMessage());
            //System.err.println(e.getMessage());            
        }

        return result;
    }

    @Override
    public String toString()
    {   
        return "[userID: " + getInstance().getUserID() + ", userName: " + 
                getInstance().getUserName() + "]";
    }
    
    public static ObservableList<String> getAllUsers()
    {
        String queryString = "SELECT userName FROM user;";
        ObservableList<String> results = FXCollections.observableArrayList();
        try
        {
          SqlHelperClass sql = new SqlHelperClass();
          PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
          
          ResultSet rs = sql.executeQuery(stmt);
          
          while (rs.next())
          {
              results.add(rs.getString("userName"));
          }
          
          return results;
          
        } catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
}
