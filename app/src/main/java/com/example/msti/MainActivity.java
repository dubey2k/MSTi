package com.example.msti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button logout, tocreate;
    DrawerLayout drawerLayout;
    CircleImageView nav_profilePic;
    TextView nav_userName;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.MainDrawerLayout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_profilePic = headerView.findViewById(R.id.nav_profilePic);
        nav_profilePic.setImageResource(R.drawable.default_profile_pic);
        nav_userName = headerView.findViewById(R.id.nav_UserName);

//
//        tocreate = findViewById(R.id.tocreate);
//        logout = findViewById(R.id.logout);

//        tocreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, Create_Profile.class));
//            }
//        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.getInstance().signOut();
//                startActivity(new Intent(MainActivity.this, Login.class));
//                finish();
//            }
//        });


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment_Container())
                .commit();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout/*toolbar*/
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            //add menu items
            default:
                Toast.makeText(MainActivity.this,"ok",Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
