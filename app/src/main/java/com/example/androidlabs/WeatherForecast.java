package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WeatherForecast extends AppCompatActivity {

    ImageView weatherImg;
    TextView currentTemp;
    TextView minTemp;
    TextView maxTemp;
    TextView uvRating;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherImg = findViewById(R.id.weather_img);
        currentTemp = findViewById(R.id.current_temp);
        minTemp = findViewById(R.id.min_temp);
        maxTemp = findViewById(R.id.max_temp);
        uvRating = findViewById(R.id.uv_rating);
        progress = findViewById(R.id.progress_bar);

        progress.setVisibility(View.VISIBLE);

    } //end method onCreate

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String current;
        private String min;
        private String max;
        private String uv;
        private Bitmap weatherImg;

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

} //end class WeatherForecast