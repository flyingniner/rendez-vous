/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    
    
    public static UserClass getInstance()
    {
        return instance;
    }
    
    private void setUserID(String userID)
    {        
        this.userID = Integer.parseInt(userID);
    }
    
    private void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    private void setUserPassword(int userPassword)
    {
        this.userPassword = userPassword;
    }
    
    private int getUserID()
    {
        return this.userID;
    }
    
    public String getUserName()
    {
        return this.userName;
    }
    
    private int getUserPassword()
    {
        return this.userPassword;
    }
    
    /**
     * Checks the database for a matching user. Returns a boolean of the result
     * @param user
     * @param userName
     * @param password
     * @return TRUE if a match was found and the password matched, otherwise FALSE
     */
    public static boolean verifyUser(UserClass user, String userName, int password) //throws SQLException
    {
        boolean result = false;
        
        String queryString = "SELECT 1 userId, userName, password " +
                    "FROM user WHERE userName = '" + userName +"'";
        ResultSet rs = null;
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
            rs = sql.executeQuery(queryString);
                       
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
            //System.err.println("expection at UserClass line 85");
            System.err.println(e.getMessage());            
        }

        return result;
    }

    @Override
    public String toString()
    {   
        return "[userID: " + getInstance().getUserID() + ", userName: " + 
                getInstance().getUserName() + "]";
    }
}
