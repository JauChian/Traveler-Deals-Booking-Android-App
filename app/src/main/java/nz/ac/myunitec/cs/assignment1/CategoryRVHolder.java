package nz.ac.myunitec.cs.assignment1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryRVHolder extends RecyclerView.ViewHolder {
    TextView tv_name;
    ImageView img;
    public CategoryRVHolder(@NonNull View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_cat);
        img =  itemView.findViewById(R.id.img_cat);
    }
}
