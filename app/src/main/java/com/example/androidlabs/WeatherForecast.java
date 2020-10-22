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
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;

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

        ForecastQuery fq = new ForecastQuery();
        fq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");


    } //end method onCreate

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String current;
        private String min;
        private String max;
        private String uv;
        private String iconName;
        private Bitmap weatherImg;

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                String parameter = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            current = xpp.getAttributeValue(null,    "value");
                            publishProgress(25, 50, 75);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(25, 50, 75);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(25, 50, 75);
                        }

                        else if(xpp.getName().equals("weather"))
                        {
                            iconName = xpp.getAttributeValue(null, "icon");
                            publishProgress(25, 50, 75);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer ... args) {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(args[0]);
        }

        @Override
        public void onPostExecute(String s) {
            currentTemp.setText("Current Temperature: " +current);
            minTemp.setText("Min Temperature: " +min);
            maxTemp.setText("Max Temperature: " +max);

            progress.setVisibility(View.INVISIBLE);
        }
    } //end class ForecastQuery

} //end class WeatherForecast