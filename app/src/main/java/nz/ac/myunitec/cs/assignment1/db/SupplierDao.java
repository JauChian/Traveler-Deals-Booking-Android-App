package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SupplierDao {
    @Insert
    void insertSupplier(Supplier s);
    @Query("SELECT * FROM Supplier")
    List<Supplier> getAllSuppliers();
    @Query("SELECT * FROM Supplier WHERE sid=:id")
    Supplier gerSupplier(int id);
    @Delete
    void deleteSupplier(Supplier s);
    @Query("DELETE  FROM Supplier")
    void deleteAllSuppliers();
    @Query("SELECT User.email FROM User " +
            "INNER JOIN Supplier ON Supplier.uid = User.uid " +
            "WHERE Supplier.sid = :sid")
    String getEmail(int sid);
    @Query("SELECT User.password FROM User " +
            "INNER JOIN Supplier ON Supplier.uid = User.uid " +
            "WHERE Supplier.sid = :sid")
    String getPassword(int sid);

    @Query("SELECT sid FROM Supplier WHERE email = :email")
    int getSupplierIdByEmail(String email);
    @Query("SELECT sid FROM Supplier WHERE uid = :uid")
    int getSupplierIDByUid(int uid);

    @Query("SELECT * FROM Supplier WHERE uid = :uid")
    Supplier getSupplierByUid(int uid);

    @Query("SELECT * FROM Supplier WHERE sid = :sid")
    Supplier getSupplierbySid(int sid);

}
