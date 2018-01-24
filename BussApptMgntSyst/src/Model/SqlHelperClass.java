/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author chip
 */
public class SqlHelperClass
{
    private String driver = "com.mysql.jdbc.Driver";
    private String dbName = "U03xmi";
    private String url = "jdbc:mysql://52.206.157.109/" + dbName;
    private String userName = "U03xmi";
    private String password = "53688113371";
    private Connection conn = null;
    
    public SqlHelperClass() //throws SQLException
    {
        //establishSqlConnenction();
    }
    
    
    private void establishSqlConnenction()
    {
        try
        {
            connect(); 
            if (conn.isClosed())
                System.err.println("connection is closed @ SqlHelperClass line 41");
        }

        catch (SQLException e)
        {
            System.out.println("SQLException: "+e.getMessage()); 
            System.out.println("SQLState: "+e.getSQLState()); 
            System.out.println("VendorError: "+e.getErrorCode());            
        }
        
//        catch (ClassNotFoundException e)
//        {
//            System.out.println(e.getMessage());
//        }                        
    }
    
    public ResultSet executeQuery(String queryString) 
    {
        establishSqlConnenction();
        
        
            
        try 
        {        
            PreparedStatement stmt = conn.prepareStatement(queryString);
            ResultSet rs = stmt.executeQuery();
        
            return rs;
        }  
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            return null;
        }        
    }
    
    
//    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE userName = ?");
//    stmt.setString(1, "admin");
//
//    ResultSet rs = stmt.executeQuery();
//
//
//    if (rs.next())
//        System.out.println(rs.getString("userName") + ":" + rs.getString("password"));
    private void connect() throws SQLException
    {
        try 
        {
            Connection c = DriverManager.getConnection(url, userName, password);
            conn = c;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            conn.close();
        }
       
    }   
}
