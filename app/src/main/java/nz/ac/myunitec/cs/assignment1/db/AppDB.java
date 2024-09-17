package nz.ac.myunitec.cs.assignment1.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import nz.ac.myunitec.cs.assignment1.db.OrderDetail;
import nz.ac.myunitec.cs.assignment1.db.OrderDetailDao;
import nz.ac.myunitec.cs.assignment1.db.DataConverter;

@Database(entities = {Admin.class,Customer.class, OrderDetail.class,Product.class,Supplier.class, User.class},version = 3,exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDB extends RoomDatabase {

    private static AppDB appDB = null;
    public abstract AdminDao adminDao();
    public abstract CustomerDao customerDao();
    public abstract OrderDetailDao orderDetailDao();
    public abstract ProductDao productDao();
    public abstract SupplierDao supplierDao();
    public abstract UserDao userDao();

    public static AppDB getInstance(Context context){
        if(appDB == null){
            appDB = Room.databaseBuilder(context.getApplicationContext(),
                    AppDB.class, "AppDB").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }//fallbackToDestructiveMigration() 开发阶段并不关心旧数据
        //allowMainThreadQueries()
        return appDB;
    }

}
