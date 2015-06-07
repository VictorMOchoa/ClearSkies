package mytestapps.victor.clearskies.WeatherClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Victor on 5/21/2015.
 */
public class Daily implements Parcelable{
    private long mTime;
    private String mTimezone;
    private double mMaxTemperature;
    private String mDescription;
    private double mMinTemperature;
    private String mWeatherIcon;

    public Daily(){

    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getMaxTemperature() {
        return (int)Math.round(mMaxTemperature);
    }

    public void setMaxTemperature(double maxTemperature) {
        mMaxTemperature = maxTemperature;
    }

    public double getMinTemperature() {
        return mMinTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        mMinTemperature = minTemperature;
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

    public String getDayOfTheWeek(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime * 1000);
        return formatter.format(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mDescription);
        dest.writeDouble(mMaxTemperature);
        dest.writeString(mWeatherIcon);
        dest.writeString(mTimezone);
    }

    private Daily(Parcel in){
        mTime = in.readLong();
        mDescription = in.readString();
        mMaxTemperature = in.readDouble();
        mWeatherIcon = in.readString();
        mTimezone = in.readString();
    }

    public static final Creator<Daily> CREATOR = new Creator<Daily>() {
        @Override
        public Daily createFromParcel(Parcel source){
            return new Daily(source);
        }
        @Override
        public Daily[] newArray(int size){
            return new Daily[size];
        }
    };
}
