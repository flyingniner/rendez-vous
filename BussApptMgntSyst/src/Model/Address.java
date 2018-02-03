/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Chip
 */
public class Address extends CityClass
{
    private IntegerProperty addressId = new SimpleIntegerProperty();
    private StringProperty address1 = new SimpleStringProperty();
    private StringProperty address2 = new SimpleStringProperty();
    private CityClass city = new CityClass();
    private StringProperty postalCode = new SimpleStringProperty();   
    private Alert alertError = new Alert(Alert.AlertType.ERROR);
    
    public void setCity(String cityName, String countryName)
    {
        city.setCityName(cityName);
        city.country.setCityName(countryName);
    }
    
    public StringProperty cityProperty() 
    { 
        return this.city.cityProperty();
    }
    
    public StringProperty countryProperty()
    {
        return this.city.countryNameProperty();
    }
    
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
    
    
    public int getAddressId()
    {
        return this.addressId.get();
    }
        

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
    
    
    public String getAddress1()
    {
        return this.address1.get();
    }
    
    
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
    
    
    public String getAddress2()
    {
        return this.address2.get();
    }
    
    
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
    
    
    public String getPostalCode()
    {
        return this.postalCode.get();
    }        
}



class CityClass extends CountryClass
{
    private IntegerProperty cityId = new SimpleIntegerProperty();
    private StringProperty cityName = new SimpleStringProperty();
    protected CountryClass country = new CountryClass();
    
    protected String getCityName()
    {
        return this.cityName.get();
    }
    
    protected void setCityName(String cityName)
    {
        this.cityName.set(cityName);
    }
    
    protected StringProperty cityProperty() { return this.cityName; }
}

class CountryClass
{
    private IntegerProperty countryId = new SimpleIntegerProperty();
    private StringProperty countryName = new SimpleStringProperty();     
    
    protected String getCountryName()
    {
        return this.countryName.get();
    }
    
    protected StringProperty countryNameProperty() { return this.countryName; }
    
    protected void setCityName(String countryName)
    {
        this.countryName.set(countryName);
    }   
}
