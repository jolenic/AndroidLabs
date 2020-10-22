package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        //fq.execute("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

    } //end method onCreate

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String current;
        private String min;
        private String max;
        private String uv;
        private String iconName;
        private String fname;
        private Bitmap icon;

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
                            publishProgress(25);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        }

                        else if(xpp.getName().equals("weather"))
                        {
                            iconName = xpp.getAttributeValue(null, "icon");
                            publishProgress(75);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                fname = iconName+".png";
                Log.i("Looking for file", fname);

                if (fileExistance(fname)){
                    Log.i("Image found locally", fname);
                    FileInputStream fis = null;
                    try {    fis = openFileInput(fname);   }
                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                    icon = BitmapFactory.decodeStream(fis);
                    publishProgress(100);
                }
                else {

                    Log.i("Not found locally", "beginning download");
                    String urlString = "http://openweathermap.org/img/w/" + fname;

                    URL url2 = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        icon = BitmapFactory.decodeStream(connection.getInputStream());
                    }
                    publishProgress(100);
                }


                FileOutputStream outputStream = openFileOutput( fname, Context.MODE_PRIVATE);
                icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();



            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            Log.i("File exists in memory", String.valueOf(file.exists()));
            return file.exists();
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
            weatherImg.setImageBitmap(icon);

            progress.setVisibility(View.INVISIBLE);
        }
    } //end class ForecastQuery

} //end class WeatherForecast