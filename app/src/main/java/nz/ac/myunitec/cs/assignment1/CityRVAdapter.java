package nz.ac.myunitec.cs.assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CityRVAdapter extends RecyclerView.Adapter<CityRVHolder>{
    CityData[] myCityData;
    Context context;
    int uid;
    String userType;

    public CityRVAdapter(CityData[] myCityData, Context context, int uid, String userType) {
        this.myCityData = myCityData;
        this.context = context;
        this.uid = uid;
        this.userType = userType;
    }

    @NonNull
    @Override
    public CityRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_city, parent, false);
        return new CityRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityRVHolder holder, int position) {
        final CityData myCityDataList = myCityData[position];
        holder.tv_name.setText(myCityDataList.getCityName());
        holder.img.setImageResource(myCityDataList.getCityImage());
        // Set click listener for each city item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to LocalDeal
                Intent intent = new Intent(context, LocalDeal.class);

                // Pass any necessary data to the next Activity (e.g., city name)
                intent.putExtra("CITY_NAME", myCityDataList.getCityName());
                intent.putExtra("CITY_IMAGE", myCityDataList.getCityImage());
                intent.putExtra("userId", uid);
                intent.putExtra("userType", userType);

                // Start the new Activity
                context.startActivity(intent);

                Toast.makeText(context, "Clicked on " + myCityDataList.getCityName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myCityData.length;
    }
}
