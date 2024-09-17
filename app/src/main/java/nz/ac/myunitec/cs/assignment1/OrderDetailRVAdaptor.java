package nz.ac.myunitec.cs.assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import nz.ac.myunitec.cs.assignment1.db.OrderDetail;

public class OrderDetailRVAdaptor extends RecyclerView.Adapter<OrderDetailRVHolder>{
    private List<OrderDetail> orderList;
    private Context context;

    public OrderDetailRVAdaptor(List<OrderDetail> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_order, parent, false);
        return new OrderDetailRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailRVHolder holder, int position) {
        OrderDetail orderDetail = orderList.get(position);
        holder.orderName.setText(orderDetail.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(orderDetail.getPurchaseDate());
        holder.orderDate.setText("Order Date: "+formattedDate);
        // handle moreDetail event
        holder.moreDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "More details for: " + orderDetail.getName(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, CustomerBookingDetail.class);
                intent.putExtra("ORDERID", orderDetail.getOid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
