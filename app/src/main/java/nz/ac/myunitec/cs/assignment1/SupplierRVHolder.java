package nz.ac.myunitec.cs.assignment1;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SupplierRVHolder  extends RecyclerView.ViewHolder{
    public TextView tv_name;
    ImageView img_delete;
    public SupplierRVHolder(@NonNull View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_all_suppliers_name);
        img_delete = itemView.findViewById(R.id.btn_all_suppliers_delete);

    }
}
