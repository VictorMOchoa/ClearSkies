package mytestapps.victor.clearskies.WeatherClasses;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import mytestapps.victor.clearskies.R;

/**
 * Created by Victor on 5/20/2015.
 */
public class CurrentWeather {
    private String mWeatherIcon;
    private long mTime;
    private int mTemperature;
    private double mHumidity;
    private double mChanceOfPrecipitation;
    private String mDescription;
    private String mTimeZone;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        mWeatherIcon = weatherIcon;
    }

    public int getWeatherIconID() {
        return WeatherForecast.getIconId(mWeatherIcon);
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getTimeFormatted() {
        SimpleDateFormat formatted = new SimpleDateFormat("h:mm a");
        formatted.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Date dateAndTime = new Date(getTime() * 1000);
        String timeFormatted = formatted.format(dateAndTime);
        return timeFormatted;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = (int)temperature;
    }

    public int getHumidity() {
        return (int) Math.round(100*mHumidity);
    }

    public void setHumidity(double humidity) {
        mHumidity = (humidity);
    }

    public int getChanceOfPrecipitation() {
        return (int) Math.round(100*mChanceOfPrecipitation);
    }

    public void setChanceOfPrecipitation(double chanceOfPrecipitation) {
        mChanceOfPrecipitation = chanceOfPrecipitation;
    }


}
