/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Chip
 */
public class CustomerClass
{
    private IntegerProperty CustID = new SimpleIntegerProperty();
    private StringProperty CustName = new SimpleStringProperty();
    private StringProperty CustAddr1 = new SimpleStringProperty();
    private StringProperty CustAddr2 = new SimpleStringProperty();
    private StringProperty CustCity = new SimpleStringProperty();
    private StringProperty CustCountry = new SimpleStringProperty();
    private StringProperty CustPostalCode = new SimpleStringProperty();
    private StringProperty CustPhone = new SimpleStringProperty();
    
    public CustomerClass()
    {
        super();
    }
    
    public int getCustID() { return this.CustID.get(); }
    
    public void setCustID(int custID) { this.CustID.set(custID); }
    
    public String getCustName() { return this.CustName.get(); }
    
    public void setCustName(String name) { this.CustName.set(name); }
    
    public String getCustAddr1() { return this.CustAddr1.get(); }
    
    public void setCustAddr1(String addressLine) { this.CustAddr1.set(addressLine); }
    
    public String getCustAddr2() { return this.CustAddr2.get(); }
    
    public void setCustAddr2(String addressLine) { this.CustAddr2.set(addressLine); }
    
    public String getCity() { return this.CustCity.get(); }
    
    public void setCustCity(String cityName) { this.CustCity.set(cityName); }

    public String getCustCountry() { return this.CustCountry.get(); }
    
    public void setCustCountry(String countryName) { this.CustCountry.set(countryName); }
    
    public String getCustPostalCode() { return this.CustPostalCode.get(); }
    
    public void setCustPostalCode(String postalCode) { this.CustPostalCode.set(postalCode); }
    
    public String getCustPhone() { return this.CustPhone.get(); }
    
    public void setCustPhone(String phoneNumber) { this.CustPhone.set(phoneNumber); }
    
    private CustomerClass getCustomer()
    {
        CustomerClass cust = null;
        
        String queryString = "SELECT * FROM Customer;";
        
        ResultSet rs = null;
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
            rs = sql.executeQuery(queryString);
            
            if(rs.next()) //query string returns a result
            {
               cust.setCustID(Integer.parseInt(rs.getString("customerId")));
               cust.setCustName(rs.getString("customerName"));
               
               System.out.println("you found customer: " + cust.getCustID());
            }            
        }
                        
        catch (SQLException e)
        {
            System.err.println("expection at UserClass line 85");
            System.err.println(e.getMessage());            
        }
        
        return cust;    
    }
    
}
