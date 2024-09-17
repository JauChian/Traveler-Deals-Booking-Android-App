package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CustomerDao {
    @Insert
    void insertCustomer(Customer c);
    @Query("SELECT * FROM Customer")
    List<Customer> getAllCustomers();
    @Query("SELECT * FROM Customer WHERE cid=:id")
    Customer getCustomer(int id);
    @Query("SELECT * FROM Customer WHERE uid=:uid")
    Customer getCustomerByUid(int uid);
    @Delete
    void deleteCustomer(Customer c);
    @Update
    void updateCustomer(Customer c);
    @Query("SELECT User.email FROM User " +
            "INNER JOIN Customer ON Customer.uid = User.uid " +
            "WHERE Customer.cid = :cid")
    String getEmail(int cid);
    @Query("SELECT User.password FROM User " +
            "INNER JOIN Customer ON Customer.uid = User.uid " +
            "WHERE Customer.cid = :cid")
    String getPassword(int cid);

    @Query("SELECT cid FROM Customer WHERE email = :email")
    int getCustomerIdByEmail(String email);

    @Query("UPDATE Customer SET name = :name, mobile_num = :mobile_num WHERE cid = :cid")
    void updateCustomerDetail(String name, String mobile_num, int cid);
}
