package mytestapps.victor.clearskies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import mytestapps.victor.clearskies.R;
import mytestapps.victor.clearskies.WeatherClasses.Daily;

/**
 * Created by Victor on 5/25/2015.
 */
public class DayAdapter extends BaseAdapter {

    private Context mContext;
    private Daily[] mDailies;

    public DayAdapter(Context context, Daily[] days) {
        mContext = context;
        mDailies = days;
    }

    @Override
    public int getCount() {
        return mDailies.length;
    }

    @Override
    public Object getItem(int position) {
        return mDailies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.icon_iv);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayname_label);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperature_label);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        Daily day = mDailies[position];



        holder.iconImageView.setImageResource(day.getWeatherIconID());
        holder.temperatureLabel.setText(day.getMaxTemperature() + "");
        if(position == 0){
            holder.dayLabel.setText("Today");
        }else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }


        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
