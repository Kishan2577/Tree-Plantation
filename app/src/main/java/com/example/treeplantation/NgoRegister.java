package com.example.treeplantation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NgoRegister extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private ImageView imageView;
    EditText ngo_name,ngo_pass,ngo_email,ngo_phone,ngo_country,ngo_state,ngo_city,ngo_address;
    Button submit;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ngo_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ngo_email = findViewById(R.id.ngo_email);
        ngo_pass = findViewById(R.id.ngo_pass);
        ngo_name = findViewById(R.id.ngo_name);
        ngo_phone = findViewById(R.id.ngo_phone);
        ngo_address = findViewById(R.id.ngo_address);
        ngo_country = findViewById(R.id.ngo_country);
        ngo_state = findViewById(R.id.ngo_state);
        ngo_city = findViewById(R.id.ngo_city);
        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_ngo = ngo_email.getText().toString();
                String name_ngo = ngo_name.getText().toString();
                String pass_ngo = ngo_pass.getText().toString();
                String phone_ngo = ngo_phone.getText().toString();
                String city_ngo = ngo_city.getText().toString();
                String address_ngo = ngo_address.getText().toString();
                String country_ngo = ngo_country.getText().toString();
                String state_ngo = ngo_state.getText().toString();

                signUpAndSendData(email_ngo,pass_ngo,name_ngo,phone_ngo,address_ngo,country_ngo,state_ngo,city_ngo);
            }
        });

        Button openGalleryButton = findViewById(R.id.openGalleryButton);
        imageView = findViewById(R.id.avatar);

        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }
    private void signUpAndSendData(String email, String password, String name, String phone, String address,String country,String state, String city) {
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
                                sendDataToBackend(email, name, phone, address,country,state,city,idToken);
                            } else {
                                Toast.makeText(NgoRegister.this, "Failed to get ID Token", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NgoRegister.this, "Sign-up Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendDataToBackend(String email, String name, String phone,String address,String country,String state, String city, String idToken) {
        OkHttpClient client = new OkHttpClient();

        // Create the form body with the key-value pairs
//        MultipartBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("email", email)
//                .addFormDataPart("name", name)
//                //.add("phone", phone)
//                .addFormDataPart("city", city)
//                .build();
        String json = getJson(email,name,phone,address,country,state,city);
        RequestBody body = RequestBody.create(
                json, MediaType.parse("application/json; charset=utf-8"));


        // Build the request with the ID token in the header
        Request request = new Request.Builder()
                .url("https://tree-plantation-delta.vercel.app/auth/register/ngo")
                .post(body)
                .addHeader("Authorization", "Bearer "+idToken)  // Add ID token to the header
                .build();

        // Send the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                runOnUiThread(() -> {
                    Toast.makeText(NgoRegister.this, "Request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle success
                    runOnUiThread(() -> {
                        Toast.makeText(NgoRegister.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                        // Optionally, redirect to another activity
                        startActivity(new Intent(NgoRegister.this, MainDashBoard.class));
                    });
                } else {
                    // Handle error
                    runOnUiThread(() -> {
                        Toast.makeText(NgoRegister.this, "Failed to send data", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    public String getJson(String email, String name, String phone,String address,String country,String state, String city)
    {
        // Create a JSON object with your data
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("name", name);
            jsonObject.put("phone", phone);
            jsonObject.put("address", address);
            jsonObject.put("country", country);
            jsonObject.put("state", state);
            jsonObject.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }

// Convert the JSONObject to a string
        String json = jsonObject.toString();
        return json;

    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            // Optionally, you can convert the Uri to Bitmap if needed
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Use the bitmap as needed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}