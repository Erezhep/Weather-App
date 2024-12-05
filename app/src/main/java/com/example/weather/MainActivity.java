package com.example.weather;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    // const variables
    private final String APIKEY = "ec59c8a90a22534a271e8e5928d69b05";
    private final String METRIC = "metric";
    private final String LANG = "en";

    // Из activity_main.xml
    private EditText searchCity;
    private TextView textCity;
    private TextView textDay;
    private TextView textTime;
    private TextView textWeather;
    private ImageView weatherIcon;
    private TextView textTemp;
    private TextView textFeelsTemperature;
    private TextView textWindSpeed;
    private TextView textHumidity;
    private TextView textPressure;
    private TextView textVisibility;
    private TextView textCloudy;

    // Нужные информации
    String CITY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }

    private void init(){
        searchCity = findViewById(R.id.searchCity);
        textCity = findViewById(R.id.textCity);
        textDay = findViewById(R.id.textDay);
        textTime = findViewById(R.id.textTime);
        textWeather = findViewById(R.id.textWeather);
        weatherIcon = findViewById(R.id.weatherIcon);
        textTemp = findViewById(R.id.textTemp);
        textFeelsTemperature = findViewById(R.id.feels_temperature);
        textWindSpeed = findViewById(R.id.wind_speed);
        textHumidity = findViewById(R.id.humidity);
        textPressure = findViewById(R.id.pressure);
        textVisibility = findViewById(R.id.visibility);
        textCloudy = findViewById(R.id.cloudy);
    }

    public void onClickSearchCity(View view){
        CITY = searchCity.getText().toString();
        if (TextUtils.isEmpty(CITY)){
            Toast.makeText(this, "Пустое названия", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, CITY, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchWeather(String city){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String URL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + APIKEY + "&units=" + METRIC + "&lang=" + LANG;
        });
    }

}