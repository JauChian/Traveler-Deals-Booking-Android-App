package nz.ac.myunitec.cs.assignment1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.OrderDetailDao;

public class OrderDetailRVHolder extends RecyclerView.ViewHolder {
    TextView orderName, orderDate,moreDetail;
    public OrderDetailRVHolder(@NonNull View itemView) {
        super(itemView);
        orderName = itemView.findViewById(R.id.card_order_product_name);
        orderDate = itemView.findViewById(R.id.card_order_product_date);
        moreDetail = itemView.findViewById(R.id.more_detail);
    }
}