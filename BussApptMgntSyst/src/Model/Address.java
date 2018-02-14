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
    
    public void setAddress1(String addr1) 
    {
        try
        {
            if(addr1.length()==0 || addr1==null)
                throw new IllegalArgumentException("Address may not be left blank.");
            if(addr1.length()>50)
                throw new IllegalArgumentException("Address may not exceed 50 charachters.");
            this.address1.set(addr1);    
        }
        catch (IllegalArgumentException e)
        {
            //TODO
        }        
    }
    
    
    public String getAddress1() { return this.address1.get(); }
        
    public void setAddress2(String addr2) 
    { 
        try
        {
            if(addr2.length()>50)
                throw new IllegalArgumentException("Address may not exceed 50 charachters.");
            this.address2.set(addr2); 
        }
        catch(IllegalArgumentException e)
        {
            //TODO
        }                
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
    
    public static ObservableList<Address> getCityCountry()
    {
        ObservableList<Address> results = FXCollections.observableArrayList();
        
        //TODO: this query is wrong...
        String queryString = "SELECT co.country, ci.city "
                + "FROM country co, city ci "
                + "WHERE co.countryId = ci.countryId;";

     
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
            ResultSet rs = sql.executeQuery(queryString);

            while(rs.next())
            {                
                results.add(new Address(rs.getString("city"),rs.getString("country")));
            }
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        return results;
    }
}




//class CityClass extends CountryClass
//{
//    private IntegerProperty cityId = new SimpleIntegerProperty();
//    private StringProperty cityName = new SimpleStringProperty();
//    protected CountryClass country = new CountryClass();
//    
//    public String getCityName() { return this.cityName.get(); }
//    protected void setCityName(String cityName) { this.cityName.set(cityName); }    
//    protected StringProperty cityProperty() { return this.cityName; }
//    
//    /**
//     * Queries the DB and instantiates ObservableList<Pair<String,String>> where
//     * the key = country and value = city.
//     */
//    public static void getAllLocations()
//    {
//        //ObservableList<String> allCities = FXCollections.observableArrayList();
//        //ObservableList<Map<String, String>> cityCountryPairs = FXCollections.observableArrayList();        
//        String queryString = "SELECT co.country, ci.city "
//                + "FROM country co, city ci "
//                + "WHERE co.countryId = ci.countryId;";
//      
//        
////String queryString = "SELECT city FROM city ORDER BY 1;";
//
//
//        try
//        {
//            SqlHelperClass sql = new SqlHelperClass();
//            ResultSet rs = sql.executeQuery(queryString);
//            
//            while(rs.next())
//            {
////                Map<String, String> map = new HashMap<String, String>();
//                map.put(rs.getString("country"), rs.getString("city"));
//                //rs.getString("country"),rs.getString("city"));
//               // cityCountryPairs.add(map);
//                //allCities.add(rs.getString("city"));
//            }            
//        }
//        catch (SQLException e)
//        {
//            System.err.println(e.getMessage());
//        }
////        BussApptMgntSyst.cityCountryPairs = cityCountryPairs;        
//            BussApptMgntSyst.cityCountryPairs = map;        
//    }
//    
//    
//    
//    public static void AddCityToDb(String cityName, String countryName)
//    {
//        String nextCityIdQuery = "SELECT cityId FROM city ORDER BY cityId DESC LIMIT 1;";        
//        String countrySelectQuery = "SELECT countryId FROM country WHERE country = ? LIMIT 1;";
//                
//        String cityUpdateQuery = "INSERT INTO city "
//                + "(cityid, city, countryId,createDate,createdBy,lastUpdate,lastUpdateBy)"
//                + "VALUES (?,?,?,?,?,?,?)";
//        
//        int nextCityId = 0;
//        int countryId = 0;
//        
//        try
//        {
//            SqlHelperClass sql = new SqlHelperClass();
//            
//            PreparedStatement stmtNextCityid = SqlHelperClass.getConnection().prepareStatement(nextCityIdQuery);
//            ResultSet rsCityId = sql.executeQuery(stmtNextCityid);
//            
//            while (rsCityId.next())
//            {
//                nextCityId = rsCityId.getInt("cityId")+1;
//            }
//
//
//            //get countryId from DB
//            PreparedStatement stmtCountry = SqlHelperClass.getConnection().prepareStatement(countrySelectQuery);
//            stmtCountry.setString(1, countryName);
//            ResultSet rsCountryId = sql.executeQuery(stmtCountry);
//            
//            while (rsCountryId.next())
//            {
//                countryId = rsCountryId.getInt("countryId");
//            }
//            
//            PreparedStatement stmtCity = SqlHelperClass.getConnection().prepareStatement(cityUpdateQuery);
//            
//            ZoneId utcZone = ZoneId.of("UTC");            
//            Timestamp utcTimeStamp = Timestamp.from(LocalDateTime
//                    .now().atZone(utcZone).toInstant());
//            
//            stmtCity.setInt(1, nextCityId);
//            stmtCity.setString(2,cityName);
//            stmtCity.setInt(3, countryId);
//            stmtCity.setTimestamp(4, utcTimeStamp);
//            stmtCity.setString(5, UserClass.getInstance().getUserName());
//            stmtCity.setTimestamp(6, utcTimeStamp);
//            stmtCity.setString(7, UserClass.getInstance().getUserName());            
//            sql.executeUpdateQuery(stmtCity);
//            
//        }
//        catch (SQLException e)
//        {
//            System.err.println(e.getMessage());
//        }
//    }
//        
//}
//
//class CountryClass
//{
//    private IntegerProperty countryId = new SimpleIntegerProperty();
//    private StringProperty countryName = new SimpleStringProperty();     
//    
//    public String getCountryName() { return this.countryName.get(); }
//    protected StringProperty countryNameProperty() { return this.countryName; }
//    protected void setCountryName(String countryName) { this.countryName.set(countryName); }   
//    
//    public static void getAllCountries()
//    {
//        ObservableList<String> allCountries = FXCollections.observableArrayList();
//        
//        String queryString = "SELECT country FROM country ORDER BY 1;";
//     
//        try
//        {
//            SqlHelperClass sql = new SqlHelperClass();
//            ResultSet rs = sql.executeQuery(queryString);
//
//            while(rs.next())
//            {
//                allCountries.add(rs.getString("country"));
//            }            
//        }
//        catch (SQLException e)
//        {
//            System.err.println(e.getMessage());
//        }
//        BussApptMgntSyst.countries = allCountries;        
//    }
//    
//    public static void addCountryToDb(String countryName)
//    {
//        String nextCountryIdQuery = "SELECT countryId FROM country ORDER BY countryId DESC LIMIT 1;";
//        int nextCountryId = 0;
//        
//        String countryUpdateQuery = "INSERT INTO country "
//                + "(countryId, country, createDate, createdBy, lastUpdate, lastUpdateBy) "
//                + "(?,?,?,?,?,?)";
//        
//        SqlHelperClass sql = new SqlHelperClass();
//                
//        try
//        {
//            PreparedStatement stmtNextCountryId = SqlHelperClass.getConnection().prepareStatement(nextCountryIdQuery);
//            ResultSet rsCountryId = sql.executeQuery(stmtNextCountryId);
//            
//            while (rsCountryId.next())
//            {
//                nextCountryId = rsCountryId.getInt("countryId")+1;
//            }
//
//            PreparedStatement stmtCountryUpdate = SqlHelperClass.getConnection().prepareStatement(nextCountryIdQuery);
//
//            ZoneId utcZone = ZoneId.of("UTC");            
//            Timestamp utcTimeStamp = Timestamp.from(LocalDateTime
//                    .now().atZone(utcZone).toInstant());
//            
//            stmtCountryUpdate.setInt(1, nextCountryId);
//            stmtCountryUpdate.setString(2,countryName);
//            stmtCountryUpdate.setTimestamp(3, utcTimeStamp);
//            stmtCountryUpdate.setString(4, UserClass.getInstance().getUserName());
//            stmtCountryUpdate.setTimestamp(5, utcTimeStamp);
//            stmtCountryUpdate.setString(6, UserClass.getInstance().getUserName());            
//            sql.executeUpdateQuery(stmtCountryUpdate);            
//        }
//        catch(SQLException e)
//        {
//           System.err.println(e.getMessage());
//        }
//    }
//    
//}
