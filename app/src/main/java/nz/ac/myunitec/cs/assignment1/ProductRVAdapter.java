package nz.ac.myunitec.cs.assignment1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nz.ac.myunitec.cs.assignment1.db.Product;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVHolder>{
    List<Product> productList;
    Context context;
    int uid;
    String userType;

    public ProductRVAdapter(List<Product> productList, Context context, int uid, String userType) {
        this.productList = productList;
        this.context = context;
        this.uid = uid;
        this.userType = userType;
        Log.d("ProductRVAdaptor", "userType: " + (userType != null ? userType : "No userType received"));
    }

    @NonNull
    @Override
    public ProductRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRVHolder holder, int position) {
        final Product myProductList = productList.get(position);
        holder.tv_name.setText(myProductList.getName());
        holder.tv_discount.setText(String.format("%d", (int) myProductList.getDiscount()) + "% OFF");
        holder.tv_cat.setText(myProductList.getCategory());
        holder.tv_region.setText(myProductList.getRegion());
        holder.tv_original.setText("was $" + String.format("%.2f", myProductList.getPrice()));
        holder.tv_original.setPaintFlags(holder.tv_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        double discountPrice = myProductList.getPrice()*((100-myProductList.getDiscount())/100);
        holder.tv_price.setText("$" + String.format("%.2f", discountPrice));
        holder.img.setImageResource(myProductList.getP_img());

        // Set click listener for each product item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Create an intent to navigate to ProductDetail Activity
                    Intent intent = new Intent(context, ProductDetail.class);
                    intent.putExtra("PRODUCT_ID", myProductList.getPid());
                    intent.putExtra("USERID", uid);
                    intent.putExtra("USERTYPE", userType);
                    // Start the next activity
                    context.startActivity(intent);
                } catch (Exception e) {
                    // Handle the exception if any error occurs during starting the activity
                    Toast.makeText(context, "Error while navigating to Product Detail: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Method to update the product list after filtering
    public void setFilteredList(List<Product> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }
}
