package com.example.treeplantation;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.Url;

public class Register extends AppCompatActivity {
    EditText user_name,user_email,user_pass,user_phone,user_address;
    FirebaseAuth auth;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        user_name = findViewById(R.id.name);
        user_email = findViewById(R.id.email);
        user_pass = findViewById(R.id.pass);
        user_address = findViewById(R.id.address);
        user_phone = findViewById(R.id.phone);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_name = user_name.getText().toString();
                String u_email = user_email.getText().toString();
                String u_pass = user_pass.getText().toString();
                String u_address = user_address.getText().toString();
                String u_phone = user_phone.getText().toString();
                signUpAndSendData(u_email,u_pass,u_name,u_phone,u_address);
            }
        });

    }
    private void signUpAndSendData(String email, String password, String name, String phone, String address) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Sign up the user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Get the ID token after successful sign-up
                        auth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                // Call the method to send data to the backend
                                sendDataToBackend(email, name, phone, address,idToken);
                            } else {
                                Toast.makeText(Register.this, "Failed to get ID Token", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Sign-up Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendDataToBackend(String email, String name, String phone,String address,String idToken) {
        OkHttpClient client = new OkHttpClient();

        String json = getJson(email,name,phone,address);
        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json; charset=utf-8"));


        // Build the request with the ID token in the header
        Request request = new Request.Builder()
                .url("https://tree-plantation-delta.vercel.app/auth/register/user")
                .post(body)
                .addHeader("Authorization", "Bearer "+idToken)  // Add ID token to the header
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Handle failure
                runOnUiThread(() -> {
                    Toast.makeText(Register.this, "Request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle success
                    runOnUiThread(() -> {
                        Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                        // Optionally, redirect to another activity
                        startActivity(new Intent(Register.this, MainActivity.class));
                    });
                } else {
                    // Handle error
                    runOnUiThread(() -> {
                        Toast.makeText(Register.this, "Failed to send data", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    public String getJson(String email, String name, String phone,String address)
    {
        JSONObject locality = getLatLongFromAddress(this, address);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("name", name);
            jsonObject.put("phone", phone);
            jsonObject.put("locality", locality);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        return json;
    }
    public static JSONObject getLatLongFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        JSONObject localityJson = new JSONObject();

        try {
            // Get a list of addresses matching the provided address
            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);

                // Store latitude and longitude in a JSONObject
                localityJson.put("latitude", location.getLatitude());
                localityJson.put("longitude", location.getLongitude());
            } else {
                // Handle case where no location was found
                localityJson.put("latitude", 0.0);
                localityJson.put("longitude", 0.0);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return localityJson;
    }
}