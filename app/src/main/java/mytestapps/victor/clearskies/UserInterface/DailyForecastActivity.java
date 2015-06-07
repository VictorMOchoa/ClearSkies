package mytestapps.victor.clearskies.UserInterface;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;

import mytestapps.victor.clearskies.Adapters.DayAdapter;
import mytestapps.victor.clearskies.R;
import mytestapps.victor.clearskies.WeatherClasses.Daily;

public class DailyForecastActivity extends ListActivity {
    private Daily[] mDays;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        TextView cityName = (TextView) findViewById(R.id.city_tv);
        cityName.setText(intent.getStringExtra("CITY_NAME"));
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Daily[].class);
        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);
    }


}
