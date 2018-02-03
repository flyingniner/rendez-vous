/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Chip
 */
public class Customer extends Address
{
    static private final ObservableList<Customer> customers = BussApptMgntSyst.customers;
    
    private final IntegerProperty custId = new SimpleIntegerProperty();
    private final StringProperty custName = new SimpleStringProperty();
    private final StringProperty addr1 = new SimpleStringProperty();
    private final StringProperty addr2 = new SimpleStringProperty();
    private final StringProperty city = new SimpleStringProperty();
    private final StringProperty country = new SimpleStringProperty();
    private final StringProperty postalCode = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty custSince = new SimpleStringProperty(); 
    private final BooleanProperty active = new SimpleBooleanProperty();    
    
    public final void setCustID(int custId) { this.custId.set(custId); }
    public int getCustID() { return this.custId.get(); }    
    public IntegerProperty custIdProperty() { return custId; }
    
    public final void setCustName(String name) { this.custName.set(name); }
    public String getCustName() { return this.custName.get(); }
    public StringProperty custNameProperty() { return this.custName; }
    
    public final void setAddr1(String addr1) { this.addr1.set(addr1); }
    public String getAddr1() { return this.addr1.get(); }
    public StringProperty addr1Property() { return addr1; }
    
    public final void setAddr2(String addr2) { this.addr2.set(addr2); }
    public String getAddr2() { return this.addr2.get(); }
    public StringProperty addr2Property() { return addr2; }
    
    public final void setCity(String city) { this.city.set(city); }
    public String getCity() { return this.city.get(); }
    public StringProperty cityProperty() { return city; }
    
    public final void setCountry(String country) { this.country.set(country); }
    public String getCountry() { return this.country.get(); }
    public StringProperty countryProperty() { return country; }
    
    public final void setPostalCode(String postalCode) { this.postalCode.set(postalCode); }
    public String getPostalCode() { return this.postalCode.get(); }
    public StringProperty postalCodeProperty() { return postalCode; }
    
    public final void setPhone(String phone) { this.phone.set(phone); }
    public String getPhone() { return this.phone.get(); }
    public StringProperty phoneProperty() { return phone; }
    
    public final void setCustSince(String custSince) { this.custSince.set(custSince); }
    public String getCustSince() { return this.custSince.get(); }
    public StringProperty custSinceProperty() { return custSince; }
    
    public final void setActive(boolean active) { this.active.set(active); }
    public boolean getActive() { return this.active.get(); }
    public BooleanProperty activeProperty() { return active; }
    
    public Customer()
    {
        super();
    }
    
    public Customer(int id, String name, String City, String country)
    {
        setCustID(id);
        setCustName(name);
        setCity(City);
        setCountry(country);
    }
    
    public ObservableList<Customer> getCustomers()
    {
        if(customers.isEmpty()||customers.size()==0)
            getCustomersFromDB();        
        return customers;
    }
    
    static public void addCustomer(Customer customer)
    {
        customers.add(customer);
    }
    
    public void getCustomersFromDB()
    {     
        String queryString = "SELECT \n" +
                            "    cu.customerId\n" +
                            "    ,cu.customerName\n" +
                            "    ,a.address\n" +
                            "    ,a.address2\n" +
                            "    ,ci.city\n" +
                            "    ,co.country\n" +
                            "    ,a.postalCode\n" +
                            "    ,a.phone\n" +
                            "    ,cu.createDate\n" +
                            "    ,cu.active\n" + 
                            "FROM \n" +
                            "    customer cu, address a, city ci, country co\n" +
                            "WHERE\n" +
                            "    cu.addressId = a.addressId AND\n" +
                            "    a.cityId = ci.cityId AND\n" +
                            "    ci.countryId = co.countryId;\n";

        ResultSet rs;
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
            rs = sql.executeQuery(queryString);

            while(rs.next()) //query string returns a result
            { 
               Customer cust = new Customer();
               cust.setCustID(rs.getInt("customerId"));
               cust.setCustName(rs.getString("customerName"));
               cust.setAddr1(rs.getString("address"));
               cust.setAddr2(rs.getString("address2"));
               cust.setCity(rs.getString("city"));
               cust.setCountry(rs.getString("country"));
               cust.setPostalCode(rs.getString("postalCode"));
               cust.setPhone(rs.getString("phone"));
               cust.setCustSince(rs.getString("createDate"));
               cust.setActive(rs.getBoolean("active"));

               customers.add(cust);
            }            
        }

        catch (SQLException e)
        {          
            System.err.println(e.getMessage());            
        }          
    }
    
    
    
    private void addCustomerToDb(Customer customer)
    {
        
        //1) add address to DB -> this should be done in the address class
        
        //2) lookup addressId from Db
        //3) add customer to Db
        
        //TODO querySTring is not yet complete
        String queryString = "INSERT INTO Customer (customerName, addressId, active,"
                + "createdDate, createdBy, lastUpdate, lastUpdateBy) VALUES (";
        StringBuilder builder = new StringBuilder(queryString);
        builder.append(customer.custName + ", ");
       
        
        
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
            ResultSet rs = sql.executeQuery(queryString);
            
            if(rs.next()) //query string returns a result
            {
               
               
            }            
        }
        catch (SQLException e)
        {
            
        }
        
    }
    
    private void updateCustomerInDb(Customer customer)
    {
        //TODO
    }
    
    //private void getAddressIdFromDb()
}
