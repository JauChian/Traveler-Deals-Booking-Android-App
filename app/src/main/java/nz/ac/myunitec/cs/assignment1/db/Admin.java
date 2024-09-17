package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "uid",
                childColumns = "uid",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("uid")}
)
public class Admin extends User {
    @PrimaryKey(autoGenerate = true)
    int aid;
    String name;
    int uid;

    public Admin(String userType, String email, String password, String name, int uid) {
        super(userType, email, password);
        this.name = name;
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public int getAid() {
        return aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
