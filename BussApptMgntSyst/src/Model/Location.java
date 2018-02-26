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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Chip
 */
public class Location
{    
    private StringProperty countryName = new SimpleStringProperty();
    private StringProperty cityName = new SimpleStringProperty();

    public Location() {}

    public Location(String countryName, String cityName)
    {
        setCountryName(countryName);
        setCityName(cityName);
    }

    public String getCountryName() { return this.countryName.get(); }
    public String getCityName() { return this.cityName.get(); }
    protected void setCountryName(String countryName) { this.countryName.set(countryName); }
    protected void setCityName(String cityName) { this.cityName.set(cityName); }
    protected StringProperty countryNameProperty() { return this.countryName; }
    protected StringProperty cityNameProperty() { return this.cityName; }
    
    /**
     * Loads the city - country combinations.
     */
    public static void loadlLocations()
    {
        String queryString = "SELECT co.country, ci.city "
                + "FROM country co, city ci "
                + "WHERE co.countryId = ci.countryId;";
        
        ObservableList<Location> results = FXCollections.observableArrayList();
        
        try
        {            
            SqlHelperClass sql = new SqlHelperClass();
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            ResultSet rs = stmt.executeQuery(queryString);

            while(rs.next())
            {                
                results.add(new Location(rs.getString("country"),rs.getString("city")));
            }
        }
        catch (SQLException e)
        {
            BussApptMgntSyst.logger.log(Level.SEVERE, e.getMessage());
            System.err.println(e.getMessage());
            Utils.displayAlertError("SQL ERROR", "An error occurred while getting information from the database."
                    + "\nPlease review the the log file for more information and try again.");
        }
        
        BussApptMgntSyst.locations = results;
    }
}
