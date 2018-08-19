package com.example.rkukadia.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edittext);
        textView = findViewById(R.id.textview);
        textView2 = findViewById(R.id.answer);
        button = findViewById(R.id.button);
    }

    public void getWeather(View view) {
            String city = String.valueOf(editText.getText());
            new DownloadTask().execute(city);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            String result = "";
            String target = "https://api.openweathermap.org/data/2.5/weather?q="+strings[0]+"&appid=cbed2decf2eacf11c80bffd8e214d021";
            HttpsURLConnection urlConnection = null;
            try {
                url  = new URL(target);
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.connect();
                int status = urlConnection.getResponseCode();
                InputStream in;
                if(status >= 400)
                    in = urlConnection.getErrorStream();
                else
                    in = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int data = inputStreamReader.read();
                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }finally {
                urlConnection.disconnect();
            }
        }
            protected void onPostExecute (String s){
                super.onPostExecute(s);
                String main = "";
                String desc = "";
                try {
                    JSONArray jsonArray = new JSONObject(s).getJSONArray("weather");
                    main = new JSONObject(jsonArray.get(0).toString()).getString("main");
                    desc = new JSONObject(jsonArray.get(0).toString()).getString("description");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("result", s);
                Log.d("main", main);
                Log.d("desc", desc);

                textView2.setText(main + ", description: "+desc);
            }
        }
    }
