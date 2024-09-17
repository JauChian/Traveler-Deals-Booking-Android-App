package nz.ac.myunitec.cs.assignment1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class CityRVHolder extends RecyclerView.ViewHolder  {
    TextView tv_name;
    ImageView img;
    public CityRVHolder(@NonNull View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_city);
        img =  itemView.findViewById(R.id.img_city);
    }
}
