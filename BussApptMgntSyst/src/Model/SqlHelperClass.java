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
import java.util.logging.Level;

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
    private static Connection conn = null;
    
    public SqlHelperClass() //throws SQLException
    {
        establishSqlConnenction();
    }
    
    public static Connection getConnection()
    {
        return conn;
    }
    
    private void establishSqlConnenction()
    {
        try
        {
            connect(); 
//            if (conn.isClosed())
//                throw new SQLException("");
//                System.err.println("connection is closed @ SqlHelperClass line 41");
        }

        catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
            System.out.println("SQLException: "+ e.getMessage()); 
            Utils.displayAlertError("SQL Connection Error", "Unable to establish a connection with the server."
                    + "\nReview the log file and try again.");
        }                             
    }

    
    public ResultSet executeQuery(PreparedStatement stmt)
    {
        ResultSet rs = null;
        try
        {            
            rs = stmt.executeQuery();            
        }        
        catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
            System.err.println(e.getMessage());
            return null;
        }
        return rs;
    }
    
    /**
     * Executes an insert, delete, or update query using a prepared statement. 
     * Returns a ResultSet to the caller.
     * @param stmt
     * @return ResultSet
     * @throws SQLException 
     */
    public int executeUpdateQuery(PreparedStatement stmt) throws SQLException
    {        
        try
        {
            return stmt.executeUpdate();            
        }
        catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
            System.err.println(e.getMessage());
            
            return 0;
        }                 
    }
    
    private void connect() throws SQLException
    {
        try
        {
            Connection c = DriverManager.getConnection(url, userName, password);
            conn = c;
        }
        catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
            System.err.println(e.getMessage());
            conn.close();
        }
       
    }   
}
