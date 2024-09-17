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
public class Supplier extends User{
    @PrimaryKey(autoGenerate = true)
    int sid;
    @Nullable
    String companyName, representativeName ,contactNum;
    @Nullable
    Integer  s_img;
    int uid;

    public Supplier(String userType, String email, String password, @Nullable String companyName, @Nullable String representativeName, @Nullable String contactNum, @Nullable Integer s_img, int uid) {
        super(userType, email, password);
        this.companyName = companyName;
        this.representativeName = representativeName;
        this.contactNum = contactNum;
        this.s_img = s_img;
        this.uid = uid;
    }

    public int getSid() {
        return sid;
    }


    @Nullable
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(@Nullable String companyName) {
        this.companyName = companyName;
    }

    @Nullable
    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(@Nullable String representativeName) {
        this.representativeName = representativeName;
    }

    @Nullable
    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(@Nullable String contactNum) {
        this.contactNum = contactNum;
    }

    @Nullable
    public Integer getS_img() {
        return s_img;
    }

    public void setS_img(@Nullable Integer s_img) {
        this.s_img = s_img;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
