package com.example.hagome.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SensorListAdapter extends BaseAdapter {
    private Context context;
    private List<Sensor> sensorList;

    public SensorListAdapter(Context context, List<Sensor> sensorList) {
        this.context = context;
        this.sensorList = sensorList;
    }

    @Override
    public int getCount() {
        return sensorList.size();
    }

    @Override
    public Object getItem(int position) {
        return sensorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder;
        if (v == null) {
            v = View.inflate(context, R.layout.sensor, null);
            holder = new ViewHolder();

            holder.pots = (TextView) v.findViewById(R.id.pot);
            holder.flower1 = (TextView) v.findViewById(R.id.flower1);
            holder.flower2 = (TextView) v.findViewById(R.id.flower2);
            holder.flower3 = (TextView) v.findViewById(R.id.flower3);
            holder.flower4 = (TextView) v.findViewById(R.id.flower4);
            holder.sensor1 = (TextView) v.findViewById(R.id.sensor1);
            holder.sensor2 = (TextView) v.findViewById(R.id.sensor2);
            holder.sensor3 = (TextView) v.findViewById(R.id.sensor3);
            holder.sensor4 = (TextView) v.findViewById(R.id.sensor4);
            holder.date = (TextView) v.findViewById(R.id.startdate);
            holder.update_time = (TextView) v.findViewById(R.id.lastupdate);
            holder.auto = (TextView) v.findViewById(R.id.sensor_auto);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.pots.setText(sensorList.get(position).getPots());
        holder.flower1.setText(sensorList.get(position).getFlower1());
        holder.flower2.setText(sensorList.get(position).getFlower2());
        holder.flower3.setText(sensorList.get(position).getFlower3());
        holder.flower4.setText(sensorList.get(position).getFlower4());
        holder.sensor1.setText(sensorList.get(position).getSensor1());
        holder.sensor2.setText(sensorList.get(position).getSensor2());
        holder.sensor3.setText(sensorList.get(position).getSensor3());
        holder.sensor4.setText(sensorList.get(position).getSensor4());
        holder.update_time.setText("마지막 업데이트 : " + sensorList.get(position).getUpdate_time());

        String startDate = sensorList.get(position).getDate();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String todate = formatter.format(new Date());

        String ago = "";

        try {
            Date todate_date = formatter.parse(todate);
            Date test_date = formatter.parse(startDate);

            long diff = todate_date.getTime() - test_date.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            ago = String.valueOf(diffDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date.setText(ago + " 일차");

        if (sensorList.get(position).getAuto()) {
            holder.auto.setBackgroundColor(Color.parseColor("#ff99cc00"));
            holder.auto.setText("자동 급수");
        } else {
            holder.auto.setBackgroundColor(Color.parseColor("#ffff4444"));
            holder.auto.setText("수동 급수");
        }

        final String potname = sensorList.get(position).getPots();

        return v;
    }

    static class ViewHolder {
        TextView pots;
        TextView flower1;
        TextView flower2;
        TextView flower3;
        TextView flower4;
        TextView sensor1;
        TextView sensor2;
        TextView sensor3;
        TextView sensor4;
        TextView date;
        TextView update_time;
        TextView auto;
    }
}