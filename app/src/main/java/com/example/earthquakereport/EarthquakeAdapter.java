package com.example.earthquakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.UFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;
import android.graphics.drawable.GradientDrawable;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public class EarthquakeAdapter extends ArrayAdapter<Earthqauke> {
    private static final String LOCATION_SEPARATOR = " of ";


    public EarthquakeAdapter(Context context, List<Earthqauke> earthqaukes){
        super(context,0,earthqaukes);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView=convertView;
        if(listView==null){
            listView= LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list,parent,false);
        }
        Earthqauke currentearthquake = getItem(position);

        // to didplay @magnitude of the earthquake

        TextView magnitude = (TextView) listView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(currentearthquake.getMagnitude());
        magnitude.setText(formattedMagnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentearthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        // to display @location of the earthquake
        String primaryLocation;
        String locationOffset;
        String originalLocation = currentearthquake.getLocation();

        if(originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset= parts[0]+LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        }else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation= originalLocation;
        }

        TextView primarylocarionView = (TextView) listView.findViewById(R.id.primary_location);
        primarylocarionView.setText(primaryLocation);

        TextView locationoffsetView = (TextView) listView.findViewById(R.id.location_offset);
        locationoffsetView.setText(locationOffset);

        //to dispaly @date and @time of the earthquake
        Date dataObject = new Date(currentearthquake.getTimeInMilliseconds());

        TextView dateView = (TextView) listView.findViewById(R.id.date);
        String formatdate= formatDate(dataObject);
        dateView.setText(formatdate);

        TextView timeView = (TextView) listView.findViewById(R.id.times);
        String formattime = formatTime(dataObject);
        timeView.setText(formattime);
        return listView;

    }

    private int getMagnitudeColor(Double magnitude) {
        int magnituderesourceID;
        int magnitudeFloor= (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 0:
            case 1: magnituderesourceID= R.color.magnitude1;
            break;
            case 2: magnituderesourceID= R.color.magnitude2;
            break;
            case 3: magnituderesourceID=R.color.magnitude3;
            break;
            case 4: magnituderesourceID=R.color.magnitude4;
            break;
            case 5: magnituderesourceID=R.color.magnitude5;
            break;
            case 6: magnituderesourceID=R.color.magnitude6;
            break;
            case 7: magnituderesourceID= R.color.magnitude7;
            break;
            case 8: magnituderesourceID=R.color.mahnitude8;
            break;
            case 9: magnituderesourceID=R.color.magnitude9;
            break;
            default:magnituderesourceID=R.color.magnitude10;
            break;
        }
        return ContextCompat.getColor(getContext(),magnituderesourceID);
    }

    private String formatMagnitude(Double magnitude) {
        DecimalFormat format= new DecimalFormat("0.0");
        return format.format(magnitude);
    }

    private String formatDate(Date dataObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dataObject);
    }
    private String formatTime(Date date){
        SimpleDateFormat timeformat = new SimpleDateFormat("h:mm a");
        return timeformat.format(date);
    }


}
