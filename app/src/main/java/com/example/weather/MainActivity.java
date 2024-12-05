package com.example.weather;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    // const variables
    private final String APIKEY = "your_api_key"; // Your OpenWeatherMap api key should be here
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
    private Handler handler;
    private Runnable updateTimeRunnable;


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
        displayCurrentDate();

        handler = new Handler();
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                String currentTime = getCurrentTime();
                textTime.setText(currentTime);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateTimeRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
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
            Toast.makeText(this, "Empty name", Toast.LENGTH_SHORT).show();
        }else {
            hideKeyboard(searchCity);
            fetchWeather(CITY);
            // Toast.makeText(this, CITY, Toast.LENGTH_SHORT).show();
        }
    }

    private String textEditor(String text, String sym, boolean is_space){
        String s = "";
        if (is_space){
            s = " ";
        }
        return text + s + sym;
    }

    private void displayCurrentDate() {
        // Формат даты: день недели (сокращённо), день, месяц
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd.MM", Locale.ENGLISH);
        // Получение текущей даты
        String currentDate = dateFormat.format(Calendar.getInstance().getTime());
        // Установка даты в TextView
        textDay.setText(currentDate);
    }

    // Метод для скрытия клавиатуры
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // Скрыть клавиатуру
        }
    }

    private String getCurrentTime() {
        // Получаем текущее время
        Date currentDate = new Date();

        // Форматируем время в формате "HH:mm" (24-часовой формат)
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Возвращаем отформатированное время
        return timeFormat.format(currentDate);
    }

    private void fetchWeather(String city){
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            String URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&appid=" + APIKEY +
                    "&units=" + METRIC +
                    "&lang=" + LANG;
            // runOnUiThread(() -> Toast.makeText(this, URL, Toast.LENGTH_LONG).show());
            Request request = new Request.Builder().url(URL).build();
            try (Response response = client.newCall(request).execute()){
                if (response.isSuccessful()){
                    // runOnUiThread(() -> Toast.makeText(this, "Получил ответ", Toast.LENGTH_SHORT).show());
                    String responseBody = response.body().string();
                    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

                    // Извлечение данных из JSON
                    String desc = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
                    String icon = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
                    String temperature = jsonObject.getAsJsonObject("main").get("temp").getAsString();
                    String feelsLike = jsonObject.getAsJsonObject("main").get("feels_like").getAsString();
                    String pressure = jsonObject.getAsJsonObject("main").get("pressure").getAsString();
                    String humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsString();
                    String visibilityValue = jsonObject.get("visibility").getAsString();
                    String windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsString();
                    String clouds = jsonObject.getAsJsonObject("clouds").get("all").getAsString();
                    String cityName = jsonObject.get("name").getAsString();

                    double toVis = Double.parseDouble(visibilityValue) / 1000;
                    String visibility = String.format(Locale.US,"%.2f", toVis);
                    String description = desc.substring(0, 1).toUpperCase() + desc.substring(1).toLowerCase();

                    // Загрузка иконки
                    String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                    runOnUiThread(() -> {
                        textCity.setText(cityName);
                        textWeather.setText(description);
                        textTemp.setText(textEditor(temperature, "°", false));
                        textFeelsTemperature.setText(textEditor(feelsLike, "°", false));
                        textWindSpeed.setText(textEditor(windSpeed, "m/s", true));
                        textHumidity.setText(textEditor(humidity, "%", true));
                        textPressure.setText(textEditor(pressure, "mbar", true));
                        textVisibility.setText(textEditor(visibility, "km", true));
                        textCloudy.setText(textEditor(clouds, "%", true));

                        // Загрузка иконки в ImageView weatherIcon
                        Picasso.get()
                                .load(iconUrl)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.stat_notify_error)
                                .into(weatherIcon);
                    });

                }else{
                    runOnUiThread(() -> Toast.makeText(this, "Uncorrect city name!", Toast.LENGTH_SHORT).show());
                }
            }catch (IOException e){
                runOnUiThread(() -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }catch (Exception e){
                runOnUiThread(() -> Toast.makeText(this, "Some kind of mistake ?", Toast.LENGTH_SHORT));
            }
        }).start();
    }

}