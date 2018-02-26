/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;

/**
 *
 * @author Chip
 */
public class Address extends Location
{
    private IntegerProperty addressId = new SimpleIntegerProperty();
    private StringProperty address1 = new SimpleStringProperty();
    private StringProperty address2 = new SimpleStringProperty();
    public static Location location;
    private StringProperty postalCode = new SimpleStringProperty();   
    private Alert alertError = new Alert(Alert.AlertType.ERROR);
    
    private Address(String city, String country)
    {
        location = new Location(country, city); 
    }

    public Location getLocation() { return this.location; }
    public void setLocation(Location location) { location = this.location; }    
    
    public void setAddressId(int id) 
    {
        try
        {
            int minVal = 0;

            if(id < minVal || String.valueOf(id).length() > 1)
                throw new IllegalArgumentException("AddressId [" + id + "] must be between [0] and [9,999,999,999].");           
            this.addressId.set(id);             
        }
        catch(IllegalArgumentException e)
        {
            //Todo
            alertError.setContentText(e.getMessage());
            alertError.showAndWait()
                    .filter(response -> response == ButtonType.OK);
                    
        }
    }
    
    
    public int getAddressId() { return this.addressId.get(); }        
    
    public void setAddress1(String addr1) throws IllegalArgumentException
    {
            if(addr1.length()==0 || addr1==null)
                throw new IllegalArgumentException("Address may not be left blank.");
            if(addr1.length()>50)
                throw new IllegalArgumentException("Address may not exceed 50 charachters.");
            this.address1.set(addr1);    
//        }
//        catch (IllegalArgumentException e)
//        {
//            //TODO
//        }        
    }
    
    
    public String getAddress1() { return this.address1.get(); }
        
    public void setAddress2(String addr2) throws IllegalArgumentException
    { 
//        try
//        {
            if(addr2.length()>50)
                throw new IllegalArgumentException("Address may not exceed 50 charachters.");
            this.address2.set(addr2); 
//        }
//        catch(IllegalArgumentException e)
//        {
//            //TODO
//        }                
    }
    
    
    public String getAddress2() { return this.address2.get(); }
        
    public void setPostalCode(String postalCode) 
    {
        try
        {
            if(postalCode.length()>10)
                throw new IllegalArgumentException("Postal code may not exceed 10 charachters.");
            this.postalCode.set(postalCode); 
        }
        catch(IllegalArgumentException e)
        {
            //TODO
        }       
    } 
    
    
    public String getPostalCode() { return this.postalCode.get(); }   
    
//    public static ObservableList<Address> getCityCountry()
//    {
//        ObservableList<Address> results = FXCollections.observableArrayList();
//        
//        //TODO: this query is wrong...
//        String queryString = "SELECT co.country, ci.city "
//                + "FROM country co, city ci "
//                + "WHERE co.countryId = ci.countryId;";
//
//     
//        try
//        {
//            SqlHelperClass sql = new SqlHelperClass();
//            ResultSet rs = sql.executeQuery(queryString);
//
//            while(rs.next())
//            {                
//                results.add(new Address(rs.getString("city"),rs.getString("country")));
//            }
//        }
//        catch (SQLException e)
//        {
//            System.err.println(e.getMessage());
//        }
//        return results;
//    }
    
    /**
     * Inserts a new address to the database and returns the address Id assigned
     * to the address. 
     * @param cust The Customer object
     * @return The addressId
     * @throws SQLException 
     */
    public static int addAddressToDB(Customer cust) throws SQLException
    {
        int nextAddressId = getNextAddressIdFromDB();
        
        String queryString = "INSERT INTO address "
                + "VALUES (?,"      //addressId  (1)
                + "?,"              //address    (2)
                + "?,"              //address2   (3)
                + "?,"              //cityId     (4) 
                + "?,"              //postalCode (5)
                + "?,"              //phone      (6)
                + "?,"              //createDate (7)
                + "?,"              //createdBy  (8)
                + "?,"              //lastUpdate (9)
                + "?);";            //lastUpdateBy (10)

            SqlHelperClass sql = new SqlHelperClass();
            
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            

            stmt.setInt(1, nextAddressId);
            stmt.setString(2, cust.getAddr1());
            stmt.setString(3, cust.getAddr2());
            stmt.setInt(4, getCityIdFromDb(cust.getCity(), cust.getCountry()));
            stmt.setString(5, cust.getPostalCode());
            stmt.setString(6, cust.getPhone());
            stmt.setTimestamp(7, Utils.convertLocaLDateTimeToUtc(LocalDateTime.now()));
            stmt.setString(8, UserClass.getInstance().getUserName());
            stmt.setTimestamp(9, Utils.convertLocaLDateTimeToUtc(LocalDateTime.now()));
            stmt.setString(10, UserClass.getInstance().getUserName());
                        
            int result = sql.executeUpdateQuery(stmt);    
            if (result == 0)
                throw new SQLException("An error occured while trying to save the address."
                        + "\nReview the log for more details.");
            return nextAddressId;              
    }
    
    /**
     * Queries the database and returns the next available addressId from the
     * address table. If an exception is thrown it is caught and logged and the
     * method returns a zero (0) to the caller.
     
     * @return The addressId as an integer.
     */
    private static int getNextAddressIdFromDB()
    {
        int result = 0;
        
        String queryString = "SELECT addressId FROM address ORDER BY 1 DESC LIMIT 1;";
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            ResultSet rs = sql.executeQuery(stmt);
            
            while (rs.next())
            {
                result = rs.getInt("addressId");
            }
            
            return result +1;
                
        } catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
            return 0;
        }
    }
    
    /**
     * Queries the database and returns the cityId.
     * @param cityName
     * @param countryName
     * @return cityId
     */
    private static int getCityIdFromDb(String cityName, String countryName)
    {
        int result = 0;
        
        String queryString = "SELECT cityId FROM city WHERE city = "
                + "? AND countryId = (SELECT countryId from country WHERE country = ?);";
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            
            stmt.setString(1, cityName);
            stmt.setString(2, countryName);
                    
            ResultSet rs = sql.executeQuery(stmt);
            
            while (rs.next())
            {
                result = rs.getInt("cityId");
            }
            
            return result;
                
        } catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.WARNING, e.getMessage());
            return 0;
        }
    }    
}

