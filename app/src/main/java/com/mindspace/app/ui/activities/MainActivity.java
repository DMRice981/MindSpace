package com.mindspace.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.mindspace.R;
import com.mindspace.app.ui.fragments.CommunityFragment;
import com.mindspace.app.ui.fragments.HomeFragment;
import com.mindspace.app.ui.fragments.NotesFragment;
import com.mindspace.app.ui.fragments.DataFragment;
import com.mindspace.app.ui.fragments.ProfileFragment;
import com.mindspace.app.utils.SessionManager;
import com.mindspace.app.utils.ThemeManager;

public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigationView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ThemeManager.getInstance(this).applyTheme();
        
        setContentView(R.layout.activity_main);
        
        sessionManager = new SessionManager(this);
        
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        initViews();
        setupBottomNavigation();
        
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_community) {
                fragment = new CommunityFragment();
            } else if (itemId == R.id.nav_notes) {
                fragment = new NotesFragment();
            } else if (itemId == R.id.nav_data) {
                fragment = new DataFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }
            
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        
        MenuItem adminItem = menu.findItem(R.id.menu_admin);
        adminItem.setVisible(sessionManager.isAdmin());
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.menu_admin) {
            startActivity(new Intent(this, AdminActivity.class));
            return true;
        } else if (itemId == R.id.menu_logout) {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit();
    }
}
