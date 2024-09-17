package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User u);
    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Query("SELECT * FROM User WHERE uid=:id")
    User getUser(int id);

    @Query("DELETE  FROM User")
    void deleteAllUsers();

    @Delete
    void deleteUser(User u);

    @Query("SELECT uid FROM User WHERE email = :email")
    int getUserIdByEmail(String email);

    @Query("SELECT email FROM User WHERE uid=:id")
    String getEmail(int id);

    @Query("SELECT password FROM User WHERE uid=:id")
    String getPassword(int id);

    @Query("SELECT userType FROM User WHERE uid=:id")
    String getUserType(int id);

}
