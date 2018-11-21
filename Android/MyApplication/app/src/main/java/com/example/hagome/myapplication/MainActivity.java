package com.example.hagome.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ListView sensorListView;
    private SensorListAdapter adapter;
    private List<Sensor> sensorList;
    private String userID;
    SwipeRefreshLayout mSwipeRefreshLayout;//새로고침

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userID = getIntent().getStringExtra("userID");

        final Context context = this;
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);//새로고침
        mSwipeRefreshLayout.setOnRefreshListener(this);

        sensorListView = (ListView) findViewById(R.id.sensorlistview);
        sensorList = new ArrayList<Sensor>();
        adapter = new SensorListAdapter(getApplicationContext(), sensorList);
        sensorListView.setAdapter(adapter);

        Button regipot = (Button) findViewById(R.id.addpot_btn);
        regipot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterPotActivity.class);
                intent.putExtra("userID", userID);
                MainActivity.this.startActivity(intent);
            }
        });

        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();

        sensorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String potName = ((Sensor) parent.getItemAtPosition(position)).getPots();

                Intent intent = new Intent(MainActivity.this, ManagementActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("potName", potName);
                MainActivity.this.startActivity(intent);
            }
        });

        sensorListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String potName = ((Sensor) parent.getItemAtPosition(position)).getPots();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("AlertDialog Title");
                builder.setMessage(potName + "을 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                Toast.makeText(getApplicationContext(), potName + "이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                                BackgroundTask backgroundTask = new BackgroundTask();
                                                backgroundTask.execute();
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(getApplicationContext(), potName + "삭제 실패.", Toast.LENGTH_LONG).show();
                                                ;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                PotDeleteRequest deleteRequest = new PotDeleteRequest(potName, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                                queue.add(deleteRequest);
                            }
                        });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "아니오를 선택했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();

                return false;
            }
        });
    }


    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute();
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        super.onResume();
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://210.110.180.24/SensorList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String postParameters = "userID=" + userID;


                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;

                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String sensor1, sensor2, sensor3, sensor4;
                String potname, flower1, flower2, flower3, flower4, startday, update_time;
                String auto;
                String limited, pumptime;
                if (!sensorList.isEmpty())
                    sensorList.clear();
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    potname = object.getString("potname");
                    flower1 = object.getString("flower1");
                    flower2 = object.getString("flower2");
                    flower3 = object.getString("flower3");
                    flower4 = object.getString("flower4");
                    sensor1 = object.getString("sensor1");
                    sensor2 = object.getString("sensor2");
                    sensor3 = object.getString("sensor3");
                    sensor4 = object.getString("sensor4");
                    startday = object.getString("startday");
                    update_time = object.getString("update_time");
                    auto = object.getString("auto");
                    limited = object.getString("limited");
                    pumptime = object.getString("pumptime");
                    Sensor sensor = new Sensor(potname, flower1, flower2, flower3, flower4, sensor1, sensor2, sensor3, sensor4, startday, update_time, auto.equals("1"), Integer.parseInt(limited), Integer.parseInt(pumptime));
                    sensorList.add(sensor);
                    adapter.notifyDataSetChanged();
                    count++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

