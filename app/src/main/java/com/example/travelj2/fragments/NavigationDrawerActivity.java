package com.example.travelj2.fragments;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.example.travelj2.fragments.HomeFragment;
import com.example.travelj2.fragments.FavoritesFragment;
import com.example.travelj2.fragments.AboutUsFragment;
import com.example.travelj2.fragments.ContactUsFragment;
import com.example.travelj2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private View mHeaderView;
    private TextView textViewUsername;
    private TextView textViewEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        mHeaderView =  navigationView.getHeaderView(0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textViewUsername = mHeaderView.findViewById(R.id.username);
        textViewEmail = mHeaderView.findViewById(R.id.userEmail);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            textViewUsername.setText(user.getDisplayName());
            textViewEmail.setText(user.getEmail());
        }
//        String userText = getIntent().getStringExtra("name");
//        String emailText = getIntent().getStringExtra("email");
//        textViewUsername.setText(userText);
//        textViewEmail.setText(emailText);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            openFragment(new HomeFragment());
        } else if (id == R.id.nav_favorites) {
            openFragment(new FavoritesFragment());
        } else if (id == R.id.nav_contact) {
            openFragment(new ContactUsFragment());
        }
        else if (id == R.id.nav_about) {
            openFragment(new AboutUsFragment());
        }
        else if (id == R.id.nav_profile) {
            openFragment(new ProfileFragment());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

}
