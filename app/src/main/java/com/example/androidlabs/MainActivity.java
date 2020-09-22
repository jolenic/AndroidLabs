package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_grid);

        final Button button1 = findViewById(R.id.button1);
        final String toast_message = getResources().getString(R.string.toast_message);

        final Switch switch1 = findViewById(R.id.switch1);
        final CheckBox checkbox1 = findViewById(R.id.checkbox1);
        final boolean switchOnOff = switch1.isChecked();
        boolean checkOnOff = checkbox1.isChecked();
        final String snackbar_on = getResources().getString(R.string.snackbar_on);
        final String snackbar_off = getResources().getString(R.string.snackbar_off);
        View view = findViewById(R.id.view);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            Toast.makeText(MainActivity.this, toast_message, Toast.LENGTH_LONG).show();
        }});

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Snackbar.make(view, (isChecked ? snackbar_on : snackbar_off), Snackbar.LENGTH_LONG)
                        .setAction("Undo", click->switch1.setChecked(!isChecked))
                        .show();
            }
        });

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Snackbar.make(view, (isChecked ? snackbar_on : snackbar_off), Snackbar.LENGTH_LONG)
                        .setAction("Undo", click->checkbox1.setChecked(!isChecked))
                        .show();
            }
        });



    }
}