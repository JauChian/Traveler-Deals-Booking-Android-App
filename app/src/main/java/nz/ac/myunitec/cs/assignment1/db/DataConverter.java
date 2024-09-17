package nz.ac.myunitec.cs.assignment1.db;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataConverter  {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // 将 LocalDate (Date) 转换为 String 以存储到数据库中
    @TypeConverter
    public static String fromLocalDate(Date date) {
        return date == null ? null : formatter.format(date);
    }

    // 将数据库中的 String 转换回 LocalDate (Date)
    @TypeConverter
    public static Date toLocalDate(String dateString) {
        try {
            return dateString == null ? null : formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
