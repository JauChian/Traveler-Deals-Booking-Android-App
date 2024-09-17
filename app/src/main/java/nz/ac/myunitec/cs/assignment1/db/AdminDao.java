package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AdminDao {
    @Insert
    void insertAdmin(Admin a);
    @Update
    void updateAdmin(Admin a);
    @Query("SELECT User.email FROM User " +
            "INNER JOIN Admin ON Admin.uid = User.uid " +
            "WHERE Admin.aid = :aid")
    String getEmail(int aid);
    @Query("SELECT User.password FROM User " +
            "INNER JOIN Admin ON Admin.uid = User.uid " +
            "WHERE Admin.aid = :aid")
    String getPassword(int aid);
}
