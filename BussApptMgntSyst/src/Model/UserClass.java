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
    private String userPassword;
    
    
    public UserClass(String userID, String userName, String password)
    {
        setUserID(userID);
        setUserName(userName);
        setUserPassword(password);
    }
    
    private void setUserID(String userID)
    {        
        this.userID = Integer.parseInt(userID);
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    private void setUserPassword(String userPassword)
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
    
    private String getUserPassword()
    {
        return this.userPassword;
    }
    
    public static boolean verifyUser(String userName, String password) //throws SQLException
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
                UserClass sqlUser = new UserClass(rs.getString("userId"),
                        rs.getString("userName"),
                        rs.getString("password"));

                UserClass requestingUser = new UserClass("0",userName,password);

                result = (requestingUser.equals(sqlUser)) ? true:false; 
                rs.close();
            }
            
        }
        catch (SQLException e)
        {
            System.err.println("expection at UserClass line 85");
            System.err.println(e.getMessage());            
        }

        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof UserClass)) 
            return false;       
        
        UserClass newUser = (UserClass) obj;        
        return this.userPassword.equals(newUser.userPassword);                       
    }
    
    @Override
    public String toString()
    {
        //todo
        return this.userName;
    }
}
