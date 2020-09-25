package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    EditText user_email;
    EditText user_password;
    SharedPreferences pref;
    public static final String Email = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_form);
        View view = findViewById(R.id.view);
        user_email = findViewById(R.id.user_email);
        pref = getApplicationContext().getSharedPreferences("Prefs", MODE_PRIVATE);
        if (pref.contains(Email)) {
            user_email.setText(pref.getString(Email, ""));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pref = getApplicationContext().getSharedPreferences("Prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        EditText user_email = findViewById(R.id.user_email);
        String email = user_email.getText().toString();
        editor.putString(Email, "email");
    }


}