package com.example.treeplantation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PlantStatus extends AppCompatActivity {


    private RecyclerView recyclerView;
    private PlantationAdapter plantationAdapter;
    private List<Plantation> plantationList;

//    String token = sharedPreferences.getString("IDToken", null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plant_status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        String token = sharedPreferences.getString("IDToken", null);
        fetchPlantationData();

    }
    private void fetchPlantationData() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://tree-plantation-delta.vercel.app/user/getPlantations")
                .addHeader("Authorization", "Bearer ")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(PlantStatus.this, "Failed to fetch data", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();

                    // Parse the JSON data
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Plantation>>(){}.getType();
                    plantationList = gson.fromJson(jsonResponse, listType);

                    // Update the RecyclerView on the main thread
                    runOnUiThread(() -> {
                        plantationAdapter = new PlantationAdapter(plantationList);
                        recyclerView.setAdapter(plantationAdapter);
                    });
                }
            }
        });
    }
}