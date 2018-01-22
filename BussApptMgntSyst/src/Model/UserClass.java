/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

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
        setUserPassword(userName);
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
    
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof UserClass)) 
            return false;       
        
        UserClass newUser = (UserClass) obj;
        return (this.userPassword == newUser.userPassword) ? true:false;                       
    }
    
    @Override
    public String toString()
    {
        int result = (getUserID() + getUserName() + getUserPassword()).hashCode();
        return String.valueOf(result);
    }
}
