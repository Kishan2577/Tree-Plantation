package com.example.treeplantation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainDashBoard extends AppCompatActivity {
    FloatingActionButton Add;
    View profile;
    BottomNavigationView bottomNavigationView;
    private static final int MENU_HOME = R.id.home;
    private static final int MENU_CHAT = R.id.chat;
    private static final int MENU_NEWS = R.id.news;
    private static final int MENU_PROFILE = R.id.profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_dash_board);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Home home = new Home();
        fragmentTransaction.replace(R.id.fragment_frame, home);
        fragmentTransaction.commit();


        Add = findViewById(R.id.add);
        profile = findViewById(R.id.imageView5);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Profile profile = new Profile();

                fragmentTransaction.replace(R.id.fragment_frame, profile);
                fragmentTransaction.commit();
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SelfPlant selfPlant = new SelfPlant();
                fragmentTransaction.replace(R.id.fragment_frame, selfPlant);
                fragmentTransaction.commit();
            }
        });
    }
    public void home(View view) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Home home = new Home();

        fragmentTransaction.replace(R.id.fragment_frame, home);
        fragmentTransaction.commit();
    }

    public void chat(View view) {
        Intent intent = new Intent(MainDashBoard.this,ChatActivity.class);
        startActivity(intent);
    }

    public void news(View view) {

    }

    public void profile(View view) {
//        Intent intent = new Intent(MainDashBoard.this, Profile.class);
//        startActivity(intent);
        Intent intent = new Intent(MainDashBoard.this, Profile.class);
        startActivity(intent);
    }
}