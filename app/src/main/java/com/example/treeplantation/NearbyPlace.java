package com.example.treeplantation;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearbyPlace extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlacesAdapter placesAdapter;
    private List<Place> placesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nearby_place);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placesList = new ArrayList<>();

        String responseData = getIntent().getStringExtra("responseData");

        try {
            JSONArray jsonArray = new JSONArray(responseData);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject placeJson = jsonArray.getJSONObject(i);
                String id = placeJson.getString("id");
                String name = placeJson.getString("name");
                String description = placeJson.getString("description");
                String state = placeJson.getString("state");
                String city = placeJson.getString("city");
                String country = placeJson.getString("country");
                JSONObject coordinates = placeJson.getJSONObject("coordinates");
                double latitude = coordinates.getDouble("_latitude");
                double longitude = coordinates.getDouble("_longitude");

                Place place = new Place(id,name, description, state, city, country, latitude, longitude);
                placesList.add(place);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        placesAdapter = new PlacesAdapter(getApplicationContext(),placesList);
        recyclerView.setAdapter(placesAdapter);
    }
}