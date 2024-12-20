# Weather App

This is a simple Android application that fetches and displays the current weather data for a given city. The app uses the OpenWeatherMap API to retrieve weather information, including temperature, humidity, wind speed, and more. The app also displays a weather icon based on the current weather.

## Features

- Search for weather by city name.
- Displays current weather conditions including:
    - Temperature
    - Feels Like Temperature
    - Wind Speed
    - Humidity
    - Pressure
    - Visibility
    - Cloudiness
    - Weather Description
    - Weather Icon

## Screenshots

![Weather App Screenshot](app/src/main/res/drawable/weatherappinphone.png)

## Setup

To run this project locally, follow these steps:

1. Clone this repository:
    ```bash
    git clone https://github.com/yourusername/weather-app.git
    ```

2. Open the project in Android Studio.

3. Add your OpenWeatherMap API key:
    - Open `MainActivity.java` and replace `APIKEY` with your actual OpenWeatherMap API key.

4. Build and run the app on an Android emulator or device.

## Dependencies

- [Picasso](https://square.github.io/picasso/) for image loading
- [OkHttp](https://square.github.io/okhttp/) for making HTTP requests

## API

The app uses the [OpenWeatherMap API](https://openweathermap.org/api) to fetch weather data. You will need an API key to access the service.

### Example API Request

```text
https://api.openweathermap.org/data/2.5/weather?q={city_name}&appid={API_KEY}&units=metric&lang=en
