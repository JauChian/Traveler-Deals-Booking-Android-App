package nz.ac.myunitec.cs.assignment1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nz.ac.myunitec.cs.assignment1.db.AppDB;
import nz.ac.myunitec.cs.assignment1.db.Supplier;
import nz.ac.myunitec.cs.assignment1.db.SupplierDao;

public class SupplierRVAdapter extends RecyclerView.Adapter<SupplierRVHolder> {
    List<Supplier> supplierList;
    Context context;
    SupplierDao supplierDao;
    public SupplierRVAdapter(List<Supplier> supplierList, Context context) {
        this.supplierList = supplierList;
        this.context = context;
        supplierDao = AppDB.getInstance(context).supplierDao();
    }

    @NonNull
    @Override
    public SupplierRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_all_suppliers, parent, false);
        return new SupplierRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierRVHolder holder, int position) {
        final Supplier mySupplierList = supplierList.get(position);
        holder.tv_name.setText(mySupplierList.getCompanyName());
        // Set click listener for each product item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Create an intent to navigate to ProductDetail Activity
                    Intent intent = new Intent(context, SupplierDetail.class);
                    intent.putExtra("SID", mySupplierList.getSid());
                    intent.putExtra("USERID", mySupplierList.getUid());
                    Log.d("SupplierRVAdapter", "Passing SID: " + mySupplierList.getSid());

                    // Start the next activity
                    context.startActivity(intent);
                } catch (Exception e) {
                    // Handle the exception if any error occurs during starting the activity
                    Toast.makeText(context, "Error while navigating to Supplier Detail: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        // Set click listener for the delete button
        holder.img_delete.setOnClickListener(view -> {
            try {
                // Remove the supplier from the database
                supplierDao.deleteSupplier(mySupplierList);  // Delete supplier from database (DAO)
                // Remove supplier from the list and notify adapter
                supplierList.remove(position);  // Remove the supplier from the list
                notifyItemRemoved(position);    // Notify RecyclerView about removal
                notifyItemRangeChanged(position, supplierList.size());  // Refresh the RecyclerView

                Toast.makeText(context, "Supplier deleted successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error while deleting supplier: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return supplierList.size();
    }
}
