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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Chip
 */
public class Customer //extends Address
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
        //if(customers.isEmpty())
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
                            "    ,DATE_FORMAT(cu.createDate,'%M %d, %Y') as 'createDate'\n" +
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
    
    public static void updateCustomerInDb(Customer customer, int index)
    {
        try
        {
            SqlHelperClass sql = new SqlHelperClass();
//             String queryString = "UPDATE customer c, address a \n" +
//                     "SET \n" +
//                     "    c.customerName = ?\n" + //1
//                     "    ,c.active = ?\n" +        //2
//                     "    ,c.lastUpdate = ?\n" +   //3
//                     "    ,c.lastUpdateBy = ?\n" + //4
//                     "WHERE \n" +
//                     "    c.customerId = ?;";      //5
//             
            String queryString = "UPDATE \n" +
                                 "    customer c\n" +                       
                                 "    ,address a\n" +                       
                                 "    ,city ci\n" +                         
                                 "    ,country co\n" +                      
                                 "SET\n" +                                  
                                 "    c.customerName = ?\n" +               //01
                                 "    ,c.active = ?\n" +                    //02
                                 "    ,c.lastUpdate = ?\n" +                //03
                                 "    ,c.lastUpdateBy = ?\n" +              //04
                                 "    ,a.address = ?\n" +                   //05
                                 "    ,a.address2 = ?\n" +                  //06
                                 "    ,a.postalCode = ?\n" +                //07
                                 "    ,a.phone = ?\n" +                     //08
                                 "    ,a.lastUpdate = ?\n" +                //09
                                 "    ,a.lastUpdateBy = ?\n" +              //10
                                 "    ,ci.city = ?\n" +                     //11
                                 "    ,ci.lastUpdate = ?\n" +               //12
                                 "    ,ci.lastUpdateBy  = ?\n" +            //13
                                 "    ,co.country = ?\n" +                  //14
                                 "    ,co.lastUpdate = ?\n" +               //15
                                 "    ,co.lastUpdateBy = ?\n" +             //16
                                 "WHERE \n" +                               
                                 "    c.customerId = ? AND\n" +             //17
                                 "    c.addressId = a.addressId AND\n" +    
                                 "    a.cityId = ci.cityId AND\n" +         
                                 "    ci.countryId = co.countryId;";        
             
            PreparedStatement stmt = SqlHelperClass.getConnection().prepareStatement(queryString);
            
            ZoneId utcZone = ZoneId.of("UTC");            
            Timestamp utcTimeStamp = Timestamp.from(LocalDateTime
                    .now().atZone(utcZone).toInstant());
            
            String userName = UserClass.getInstance().getUserName();
                      
            stmt.setString(1, customer.getCustName());
            stmt.setBoolean(2, customer.getActive());
            stmt.setTimestamp(3, utcTimeStamp);
            stmt.setString(4, userName);
            stmt.setString(5, customer.getAddr1());
            stmt.setString(6, customer.getAddr2());
            stmt.setString(7, customer.getPostalCode());
            stmt.setString(8, customer.getPhone());
            stmt.setTimestamp(9, utcTimeStamp);
            stmt.setString(10, userName);
            stmt.setString(11, customer.getCity());
            stmt.setTimestamp(12, utcTimeStamp);
            stmt.setString(13, userName);
            stmt.setString(14, customer.getCountry());
            stmt.setTimestamp(15, utcTimeStamp);
            stmt.setString(16, userName);
            stmt.setInt(17, customer.getCustID());
            
            sql.executeUpdateQuery(stmt);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }                     
    }
    
    @Override
    public String toString()
    {
        return this.getCustID() + this.getCustName() + this.getCity() + this.getCountry();
    }
    
    
    
}
