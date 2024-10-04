package com.example.treeplantation;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private List<Place> placesList;
    private Context context;
    public PlacesAdapter(Context context,List<Place> placesList) {
        this.placesList = placesList;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }
    Place place;
    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        place = placesList.get(position);
        holder.nameTextView.setText(place.getName());
        holder.descriptionTextView.setText(place.getDescription());
        holder.stateTextView.setText(place.getState());
        holder.cityTextView.setText(place.getCity());
        holder.countryTextView.setText(place.getCountry());
        holder.coordinatesTextView.setText("Lat: " + place.getLatitude() + ", Lon: " + place.getLongitude());
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("IDToken", null);

        // Set click listener for the "Plant Here" button
        holder.plantHereButton.setOnClickListener(v -> {
                sendDataToBackend(place.getId(),token);
                Intent intent = new Intent(context, PlantStatus.class);
                intent.putExtra("placeData", place); // Pass the entire Place object
                context.startActivity(intent);

        });
    }

    private void sendDataToBackend(String id,String token) {
        OkHttpClient client = new OkHttpClient();

        String json = getJson(id);
        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json; charset=utf-8"));


        // Build the request with the ID token in the header
        Request request = new Request.Builder()
                .url("https://tree-plantation-delta.vercel.app/user/initializePlantation")
                .post(body)
                .addHeader("Authorization", "Bearer "+token)  // Add ID token to the header
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    return;
                } else {
                    // Handle error
                    Toast.makeText(context, "FAILURE", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getJson(String id)
    {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("placeId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        return json;
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, descriptionTextView, stateTextView, cityTextView, countryTextView, coordinatesTextView;
        Button plantHereButton;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            countryTextView = itemView.findViewById(R.id.countryTextView);
            coordinatesTextView = itemView.findViewById(R.id.coordinatesTextView);
            plantHereButton = itemView.findViewById(R.id.plant_here); // Get the button reference
        }
    }
}
