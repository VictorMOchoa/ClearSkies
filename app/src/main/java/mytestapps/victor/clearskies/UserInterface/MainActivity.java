package mytestapps.victor.clearskies.UserInterface;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mytestapps.victor.clearskies.WeatherClasses.CurrentWeather;
import mytestapps.victor.clearskies.R;
import mytestapps.victor.clearskies.WeatherClasses.Daily;
import mytestapps.victor.clearskies.WeatherClasses.GPSTracker;
import mytestapps.victor.clearskies.WeatherClasses.Hour;
import mytestapps.victor.clearskies.WeatherClasses.WeatherForecast;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static String DAILY_FORECAST = "DAILY_FORECAST";
    public static String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static String city ="";

    private WeatherForecast mForecast;

    //Create all TextView references here
    @InjectView(R.id.tv_currentTime) TextView mTimeTv;
    @InjectView(R.id.tv_temperature) TextView mTemperatureTv;
    @InjectView(R.id.tv_location) TextView mLocationTv;
    @InjectView(R.id.tv_humidPercent) TextView mHumidityTv;
    @InjectView(R.id.tv_precipPercent) TextView mPrecipitationTv;
    @InjectView(R.id.tv_weatherDescription) TextView mWeatherDescriptionTv;
    @InjectView(R.id.iv_icon) ImageView mIconIv;
    @InjectView(R.id.iv_refreshButton) ImageView mRefreshIv;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        setContentView(R.layout.activity_main);

        /*ButterKnife is a third-library that simplifies the efforts of creating the variables and
        then setting the references in two separate steps*/
        ButterKnife.inject(this);

        mProgressBar.setVisibility(View.INVISIBLE);



            mRefreshIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMyWeather();

                }
            });


            getMyWeather();

        }
        else {
            Toast.makeText(this,"Please turn on GPS Location and restart", Toast.LENGTH_LONG).show();
        }

    }

    private void getMyWeather() {
        String apiKey = "67e3506879b156fb51b646e69c250d5c";

        double latitude = 0;
        double longitude = 0;




        if(isNetworkUp()) {

            GPSTracker gps = new GPSTracker(MainActivity.this);
            if(gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } else {
                gps.showSettingsAlert();
            }

            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            city = addresses.get(0).getSubLocality();


            String forecastURL = "https://api.forecast.io/forecast/"+ apiKey +
                    "/" + latitude + "," + longitude;

            doForecastRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doForecastRefresh();
                        }
                    });

                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doForecastRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForcastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshDisplay();
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, getString(R.string.error_no_network), Toast.LENGTH_LONG).show();
        }
    }

    private void doForecastRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshIv.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshIv.setVisibility(View.VISIBLE);
        }
    }

    private void refreshDisplay() {
        CurrentWeather current = mForecast.getCurrentWeather();

        mHumidityTv.setText((current.getHumidity())+ "% ");
        mTemperatureTv.setText(current.getTemperature() + "");
        mWeatherDescriptionTv.setText(current.getDescription());
        mPrecipitationTv.setText(current.getChanceOfPrecipitation() + "% ");
        mTimeTv.setText("The temperature at " + current.getTimeFormatted() + " is...");
        Drawable drawableIcon = getResources().getDrawable(current.getWeatherIconID());
        mIconIv.setImageDrawable(drawableIcon);
        mLocationTv.setText(city);
    }

    private WeatherForecast parseForcastDetails(String jsonData) throws JSONException {
        WeatherForecast forecast = new WeatherForecast();
        forecast.setCurrentWeather(getCurrentDetails(jsonData));

        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));
        return forecast;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String userTimeZone = forecast.getString("timezone");
        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray hourlyData = hourly.getJSONArray("data");
        Hour hours[] = new Hour[hourlyData.length()];

        for(int i = 0; i < hourlyData.length(); i++){
            JSONObject jsonHour = hourlyData.getJSONObject(i);
            Hour hour = new Hour();
            hour.setDescription(jsonHour.getString("summary"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setWeatherIcon(jsonHour.getString("icon"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTimezone(userTimeZone);
            hours[i] = hour;
        }
        return hours;
    }

    private Daily[] getDailyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String userTimeZone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dailyData = daily.getJSONArray("data");

        Daily dailies[] = new Daily[dailyData.length()];

        for(int i = 0; i < dailyData.length(); i++){
            JSONObject jsonDay = dailyData.getJSONObject(i);
            Daily day = new Daily();

            day.setDescription(jsonDay.getString("summary"));
            day.setTime(jsonDay.getLong("time"));
            day.setWeatherIcon(jsonDay.getString("icon"));
            day.setMaxTemperature(jsonDay.getDouble("temperatureMax"));
            day.setMinTemperature(jsonDay.getDouble("temperatureMin"));
            day.setTimezone(userTimeZone);
            dailies[i] = day;
        }

        return dailies;
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String userTimeZone = forecast.getString("timezone");
        JSONObject currentWeatherStats = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currentWeatherStats.getDouble("humidity"));
        currentWeather.setChanceOfPrecipitation(currentWeatherStats.getDouble("precipProbability"));
        currentWeather.setTime(currentWeatherStats.getLong("time"));
        currentWeather.setWeatherIcon(currentWeatherStats.getString("icon"));
        currentWeather.setDescription(currentWeatherStats.getString("summary"));
        currentWeather.setTemperature(currentWeatherStats.getDouble("temperature"));
        currentWeather.setTimeZone(userTimeZone);
        Log.i("From JSON: ", userTimeZone);

        return currentWeather;
    }

    private boolean isNetworkUp() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isUp = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isUp = true;
        }
        return isUp;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "Error_Dialog");
    }

    @OnClick(R.id.weekly_button)
    public void startDailyActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());
        intent.putExtra("CITY_NAME", city);
        startActivity(intent);
    }

    @OnClick(R.id.hourly_button)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }
}
