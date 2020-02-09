package com.example.msti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.msti.model.userData;
import com.example.msti.nav_Fragments.Home_Fragment;
import com.example.msti.nav_Fragments.Setting_Fragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.msti.staticStrings.FILE_NAME;
import static com.example.msti.staticStrings.USER_KEY;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    CircleImageView nav_profilePic;
    TextView nav_userName;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    NavigationView navigationView;

    public static userData user;

    public static String USER_GENDER ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpNavBarHeader();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        openFragment(new Home_Fragment());

    }

    private void init() {
        drawerLayout = findViewById(R.id.MainDrawerLayout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_profilePic = headerView.findViewById(R.id.nav_profilePic);
        nav_profilePic.setImageResource(R.drawable.default_profile_pic);
        nav_userName = headerView.findViewById(R.id.nav_UserName);
    }

    private void setUpNavBarHeader() {

        SharedPreferences preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);

        String s=preferences.getString(USER_KEY,"N/A");
        Gson gson = new Gson();
        user = gson.fromJson(s,userData.class);
        nav_userName.setText(user.getName());
        if(user.getGender().equals("Male"))
            USER_GENDER="MALE_USER";
        else
            USER_GENDER="FEMALE_USER";

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_Home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                openFragment(new Home_Fragment());
                return true;
            case R.id.nav_Setting:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                openFragment(new Setting_Fragment());
                return true;
            case R.id.logout:
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                mAuth.signOut();
                Intent i = new Intent(MainActivity.this, Login.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
        }
        return false;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
