package nz.ac.myunitec.cs.assignment1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductRVHolder extends RecyclerView.ViewHolder {
    TextView tv_name,tv_discount,tv_cat,tv_original,tv_price,tv_region;
    ImageView img;

    public ProductRVHolder(@NonNull View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_product_name);
        tv_cat = itemView.findViewById(R.id.tv_product_category);
        tv_region = itemView.findViewById(R.id.tv_product_region);
        tv_discount = itemView.findViewById(R.id.tv_product_discount);
        tv_original = itemView.findViewById(R.id.tv_product_original_price);
        tv_price = itemView.findViewById(R.id.tv_product_price);
        img = itemView.findViewById(R.id.img_product);
    }
}
