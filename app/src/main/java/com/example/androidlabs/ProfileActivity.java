package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    ImageButton mImageButton;
    Button chatButton;
    Button weatherButton;
    Button toolbarButton;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int TOOLBAR_REQUEST = 2;
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
        Log.e(ACTIVITY_NAME, "In function: onActivityResult()");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mImageButton = findViewById(R.id.picture_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        chatButton = findViewById(R.id.chat_button);
        Intent chatRoom = new Intent(this, ChatRoomActivity.class);
        chatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {startActivity(chatRoom);}
        });
        weatherButton = findViewById(R.id.weather_button);
        Intent weatherForecast = new Intent(this, WeatherForecast.class);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {startActivity(weatherForecast);}
        });
        toolbarButton = findViewById(R.id.toolbar_button);
        Intent testToolbar = new Intent(this, TestToolbar.class);
        toolbarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {startActivityForResult(testToolbar, TOOLBAR_REQUEST);}
//            public void onClick(View v) {startActivity(testToolbar);}
        });
        Log.e(ACTIVITY_NAME, "In function: onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In function: onStart()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e(ACTIVITY_NAME, "In function: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In function: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(ACTIVITY_NAME, "In function: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In function: onDestroy()");
    }
}