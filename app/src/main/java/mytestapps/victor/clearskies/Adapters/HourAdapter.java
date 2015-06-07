package mytestapps.victor.clearskies.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import mytestapps.victor.clearskies.R;
import mytestapps.victor.clearskies.WeatherClasses.Hour;

/**
 * Created by Victor on 6/2/2015.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] mHours;

    public HourAdapter(Hour[] hours) {
        mHours = hours;
    }
    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hourly_list_item, viewGroup, false);
        HourViewHolder viewHolder = new HourViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder hourViewHolder, int i) {
        hourViewHolder.bindHour(mHours[i]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {
        public TextView mTimeTv;
        public TextView mDescriptionTv;
        public TextView mTemperatureTv;
        public ImageView mIcon;

        public HourViewHolder(View itemView) {
            super(itemView);
            mTimeTv = (TextView) itemView.findViewById(R.id.time_tv);
            mDescriptionTv = (TextView) itemView.findViewById(R.id.description_tv);
            mTemperatureTv = (TextView) itemView.findViewById(R.id.degrees_tv);
            mIcon = (ImageView) itemView.findViewById(R.id.icon_iv);
        }

        public void bindHour(Hour hour) {
            mTimeTv.setText(hour.getHour());
            mDescriptionTv.setText(hour.getDescription());
            mTemperatureTv.setText(hour.getTemperature() + "");
            mIcon.setImageResource(hour.getIconId());
        }
    }
}
