package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    void insertProduct(Product p);
    @Query("SELECT * FROM Product")
    List<Product> getAllProducts();
    @Query("SELECT * FROM Product WHERE pid=:id")
    Product getProduct(int id);
    @Delete
    void deleteProduct(Product p);
    @Query("SELECT pid FROM Product WHERE name = :name")
    int getProductIdByName(String name);
    @Query("DELETE  FROM Product")
    void deleteAllProducts();
    // 获取折扣后的价格
    @Query("SELECT price * (1 - discount / 100.0) FROM Product WHERE pid = :id")
    double getDiscountPrice(int id);

    // 获取原价
    @Query("SELECT price FROM Product WHERE pid = :id")
    double getOriginalPrice(int id);

    // 获取产品名称
    @Query("SELECT name FROM Product WHERE pid = :id")
    String getProductName(int id);

    // 获取折扣百分比
    @Query("SELECT discount FROM Product WHERE pid = :id")
    double getDiscount(int id);
    @Query("SELECT * FROM Product WHERE category = :category")
    List<Product> getAllProductsByCategory(String category);
    @Query("SELECT * FROM Product WHERE region = :region")
    List<Product> getAllProductsByCity(String region);

    // Query to get products by both city and category
    @Query("SELECT * FROM Product WHERE region = :region AND category = :category")
    List<Product> getAllProductsByCityAndCategory(String region, String category);

    @Query("SELECT * FROM Product WHERE sid = :sid")
    List<Product> getAllProductsBySid(int sid);


}
