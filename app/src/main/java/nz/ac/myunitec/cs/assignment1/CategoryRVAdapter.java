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

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVHolder> {
    CategoryData[] myCategoryData;
    Context context;
    int uid;
    String userType;

    public CategoryRVAdapter(CategoryData[] myCategoryData, Context context, int uid, String userType) {
        this.myCategoryData = myCategoryData;
        this.context = context;
        this.uid = uid;
        this.userType = userType;
        Log.d("CategoryRVAdaptor", "userType: " + (userType != null ? userType : "No userType received"));
    }

    @NonNull
    @Override
    public CategoryRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_cat, parent, false);
        return new CategoryRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRVHolder holder, int position) {
        final CategoryData myCategoryDataList = myCategoryData[position];
        holder.tv_name.setText(myCategoryDataList.getCategoryName());
        holder.img.setImageResource(myCategoryDataList.getCategoryImage());
        // Set click listener for each category item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CategoryRVAdapter", "Category clicked: " + myCategoryDataList.getCategoryName());

                try {
                    Intent intent = new Intent(context, AllProduct.class);
                    intent.putExtra("CATEGORY",  myCategoryDataList.getCategoryName());
                    intent.putExtra("USERID", uid);
                    intent.putExtra("USERTYPE", userType);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e("CategoryRVAdapter", "Error starting AllProduct Activity", e);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return myCategoryData.length;
    }
}
