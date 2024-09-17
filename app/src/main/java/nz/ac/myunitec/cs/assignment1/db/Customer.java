package nz.ac.myunitec.cs.assignment1.db;

import androidx.annotation.Nullable;
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
public class Customer extends User {
    @PrimaryKey(autoGenerate = true)
    int cid;
    @Nullable
    String name;
    @Nullable
    Integer  c_img;
    @Nullable
    String mobile_num;
    int uid;

    public Customer(String userType, String email, String password, @Nullable String name, @Nullable Integer c_img, @Nullable String mobile_num, int uid) {
        super(userType, email, password);
        this.name = name;
        this.c_img = c_img;
        this.mobile_num = mobile_num;
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }


    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public Integer getC_img() {
        return c_img;
    }

    public void setC_img(@Nullable Integer c_img) {
        this.c_img = c_img;
    }

    @Nullable
    public String getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(@Nullable String mobile_num) {
        this.mobile_num = mobile_num;
    }

    public int getUid() {
        return uid;
    }

}
