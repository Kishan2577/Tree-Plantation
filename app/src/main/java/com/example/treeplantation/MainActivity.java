package com.example.treeplantation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button login,register;
    EditText email,pass;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_pass);
        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Auth_email = email.getText().toString();
                String Auth_pass = pass.getText().toString();
                if(!Auth_email.isEmpty() && !Auth_pass.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(Auth_email).matches())
                {
                    auth.signInWithEmailAndPassword(Auth_email,Auth_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            auth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String idToken = task.getResult().getToken();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("IDToken", idToken);
                                    editor.apply();
                                    Toast.makeText(MainActivity.this, "TOKEN STORED "+idToken , Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, MainDashBoard.class);
                                    startActivity(intent);
                                    // Send the ID token to your Node.js backend
                                    //sendIdTokenToBackend(idToken);
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to get ID Token", Toast.LENGTH_SHORT).show();
                                }
                            });
//                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(MainActivity.this, MainDashBoard.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Login Unsuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(Auth_email.isEmpty() || Auth_pass.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Email or Password cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,NgoRegister.class);
//                startActivity(intent);

                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                // Inflate your menu resource
                popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                // Set a click listener for menu item clicks
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.option_one) {
                            // Handle Option 1 click
                            startActivity(new Intent(MainActivity.this,Register.class));
                            return true;
                        } else if (id == R.id.option_two) {
                            // Handle Option 2 click
                            startActivity(new Intent(MainActivity.this,NgoRegister.class));
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                // Show the popup menu
                popupMenu.show();
            }
        });
    }
}