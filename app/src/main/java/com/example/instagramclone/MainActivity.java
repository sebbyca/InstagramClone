package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.instagramclone.fragments.ComposeFragment;
import com.example.instagramclone.fragments.PostsFragment;
import com.example.instagramclone.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private ImageButton btnCamera;
    private ImageButton btnDirect;
    private ImageButton btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCamera = findViewById(R.id.btnCamera);
        btnDirect = findViewById(R.id.btnDirect);
        btnLogout = findViewById(R.id.btnLogout);

        toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                if (menuItem.getItemId() == bottomNavigationView.getSelectedItemId()) return true;

                Fragment fragment;
                toolbar.setVisibility(View.INVISIBLE);

                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        fragment = new PostsFragment();
                        toolbar = findViewById(R.id.toolbarHome);
                        break;
                    case R.id.action_compose:
                        fragment = new ComposeFragment();
                        toolbar = findViewById(R.id.toolbarHome);
                        break;
                    case R.id.action_profile:
                    default:
                        fragment = new ProfileFragment();
                        toolbar = findViewById(R.id.toolbarProfile);
                        break;
                }

                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                toolbar.setVisibility(View.VISIBLE);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("");
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}