package com.example.androidlabs;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class TestToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView nav;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                message = "You clicked on item 1";
                break;
            case R.id.item2:
                message = "You clicked on item 2";
                break;
            case R.id.item3:
                message = "You clicked on item 3";
                break;
            case R.id.item4:
                message = "You clicked on the overflow menu";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

//    @Override
//    public boolean onCreateNavigationMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.nav_menu, menu);
//
//        return true;
//    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        Intent goTo = this.getIntent();
        switch(item.getItemId())
        {
            case R.id.toChat:
                goTo = new Intent(this, ChatRoomActivity.class);
                break;
            case R.id.toWeather:
                goTo = new Intent(this, WeatherForecast.class);
                break;
            case R.id.toLogin:
                goTo = new Intent(this, MainActivity.class);
                finish();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(goTo);

        return true;
    }

}