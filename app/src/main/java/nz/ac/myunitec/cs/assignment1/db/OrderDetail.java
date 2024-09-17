package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import nz.ac.myunitec.cs.assignment1.db.Customer;
import nz.ac.myunitec.cs.assignment1.db.Product;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Customer.class, parentColumns = "cid", childColumns = "cid", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Product.class, parentColumns = "pid", childColumns = "pid", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Supplier.class, parentColumns = "sid", childColumns = "sid", onDelete = ForeignKey.CASCADE)
        },
        indices = { // 添加索引以提高查询性能
                @Index(value = "cid"),
                @Index(value = "pid"),
                @Index(value = "sid")
        }
)
public class OrderDetail {
    @PrimaryKey(autoGenerate = true)
    int oid;
    int cid; // 客户ID（外键）
    int pid; // 产品ID（外键）
    int sid; // 供应商ID（外键）
    int qty;
    double price, total;
    String name;
    Date purchaseDate, expiryDate;

    public OrderDetail(int cid, int pid, int sid, int qty, double price, double total, String name, Date purchaseDate, Date expiryDate) {
        this.cid = cid;
        this.pid = pid;
        this.sid = sid;
        this.qty = qty;
        this.price = price;
        this.total = total;
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
    }

    public int getOid() {
        return oid;
    }

    public int getCid() {
        return cid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
