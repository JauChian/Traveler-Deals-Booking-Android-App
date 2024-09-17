package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import nz.ac.myunitec.cs.assignment1.db.Supplier;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Supplier.class,
                parentColumns = "sid",
                childColumns = "sid",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "sid")} // 为外键列添加索引
)
public class Product {
    @PrimaryKey(autoGenerate = true)
    int pid;
    double price, discount;
    String region, category, description, name;
    int p_img;
    int sid;

    public Product(double price, double discount, String region, String category, String description, String name, int p_img, int sid) {
        this.price = price;
        this.discount = discount;
        this.region = region;
        this.category = category;
        this.description = description;
        this.name = name;
        this.p_img = p_img;
        this.sid = sid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getP_img() {
        return p_img;
    }

    public void setP_img(int p_img) {
        this.p_img = p_img;
    }

    public int getSid() {
        return sid;
    }

    public int getPid() {
        return pid;
    }
}
