package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    void insertOrder(OrderDetail o);
    @Query("SELECT * FROM OrderDetail")
    List<OrderDetail> getAllOrders();
    @Query("SELECT * FROM OrderDetail WHERE oid=:id")
    OrderDetail getOrder(int id);
    @Delete
    void deleteOrder(OrderDetail o);
    @Query("DELETE  FROM OrderDetail")
    void deleteAllOrderDetail();
    @Update
    void updateOrder(OrderDetail o);
    @Query("SELECT * FROM OrderDetail WHERE cid = :cid")
    List<OrderDetail> getOrdersByCustomerId(int cid);
    @Query("SELECT * FROM OrderDetail WHERE sid = :sid")
    List<OrderDetail> getOrdersBySupplierId(int sid);

}

