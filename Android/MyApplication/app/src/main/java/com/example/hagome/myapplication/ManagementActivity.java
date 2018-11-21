package com.example.hagome.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagementActivity extends AppCompatActivity {
    private String userID;
    private String potName;
    private String auto;
    private ArrayAdapter adapter1;
    private ArrayAdapter adapter2;
    private Spinner limited;
    private Spinner pumptime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        userID = getIntent().getStringExtra("userID");
        potName = getIntent().getStringExtra("potName");

        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute();

        final Button btn1 = (Button) findViewById(R.id.management_sensor1_btn);
        final Button btn2 = (Button) findViewById(R.id.management_sensor2_btn);
        final Button btn3 = (Button) findViewById(R.id.management_sensor3_btn);
        final Button btn4 = (Button) findViewById(R.id.management_sensor4_btn);

        limited = (Spinner) findViewById(R.id.management_limited);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.sensor_limited, android.R.layout.simple_spinner_dropdown_item);
        limited.setAdapter(adapter1);

        pumptime = (Spinner) findViewById(R.id.management_pumptime);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.pump_time, android.R.layout.simple_spinner_dropdown_item);
        pumptime.setAdapter(adapter2);


        limited.setEnabled(false);
        pumptime.setEnabled(false);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute("http://210.110.180.24/PumpOn.php", potName, "1");
                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute("http://210.110.180.24/PumpOn.php", potName, "2");
                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute("http://210.110.180.24/PumpOn.php", potName, "3");
                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData task = new InsertData();
                task.execute("http://210.110.180.24/PumpOn.php", potName, "4");
                BackgroundTask backgroundTask = new BackgroundTask();
                backgroundTask.execute();
            }
        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.management_rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.management_auto) {
                    pumptime.setEnabled(true);
                    limited.setEnabled(true);
                    btn1.setText("disalbe");
                    btn2.setText("disalbe");
                    btn3.setText("disalbe");
                    btn4.setText("disalbe");
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                    btn4.setEnabled(false);
                    auto = "1";
                } else {
                    pumptime.setEnabled(false);
                    limited.setEnabled(false);
                    btn1.setText("supply");
                    btn2.setText("supply");
                    btn3.setText("supply");
                    btn4.setText("supply");
                    btn1.setEnabled(true);
                    btn2.setEnabled(true);
                    btn3.setEnabled(true);
                    btn4.setEnabled(true);
                    auto = "0";
                }
            }
        });

        Button applyButton = (Button) findViewById(R.id.management_apply_btn);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String limitedValue = limited.getSelectedItem().toString();
                String pumptimeValue = pumptime.getSelectedItem().toString();
                UpdateSupply task = new UpdateSupply();
                task.execute("http://210.110.180.24/Update.php", potName, auto, limitedValue, pumptimeValue);
                Toast.makeText(getApplicationContext(),"변경 사항이 적용되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://210.110.180.24/SensorValue.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String postParameters = "userID=" + userID + "&potName=" + potName;


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
                String sensor1, sensor2, sensor3, sensor4;
                String potname, flower1, flower2, flower3, flower4, startday, update_time;
                String limited, pumptime;
                JSONObject object = jsonArray.getJSONObject(0);

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

                changeApply(sensor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void changeApply(Sensor sensor) {
        TextView pots = (TextView) findViewById(R.id.management_pot);
        TextView flower1 = (TextView) findViewById(R.id.management_flower1);
        TextView flower2 = (TextView) findViewById(R.id.management_flower2);
        TextView flower3 = (TextView) findViewById(R.id.management_flower3);
        TextView flower4 = (TextView) findViewById(R.id.management_flower4);
        TextView sensor1 = (TextView) findViewById(R.id.management_sensor1);
        TextView sensor2 = (TextView) findViewById(R.id.management_sensor2);
        TextView sensor3 = (TextView) findViewById(R.id.management_sensor3);
        TextView sensor4 = (TextView) findViewById(R.id.management_sensor4);
        TextView date = (TextView) findViewById(R.id.management_startdate);
        TextView update_time = (TextView) findViewById(R.id.management_lastupdate);

        pots.setText(sensor.getPots());
        flower1.setText(sensor.getFlower1());
        flower2.setText(sensor.getFlower2());
        flower3.setText(sensor.getFlower3());
        flower4.setText(sensor.getFlower4());
        sensor1.setText(sensor.getSensor1());
        sensor2.setText(sensor.getSensor2());
        sensor3.setText(sensor.getSensor3());
        sensor4.setText(sensor.getSensor4());
        update_time.setText("마지막 업데이트 : " + sensor.getUpdate_time());
        date.setText("심은 날짜 : " + sensor.getDate());

        RadioButton rdself = (RadioButton) findViewById(R.id.management_self);
        RadioButton rdauto = (RadioButton) findViewById(R.id.management_auto);

        if(auto.equals("1")){
            rdself.setChecked(false);
            rdauto.setChecked(true);
        }
        else {
            rdauto.setChecked(false);
            rdself.setChecked(true);
        }

        adapter1.getPosition(String.valueOf(sensor.getLimited()));
        int limitedPosition = sensor.getLimited()/50-10;
        int pumptimePositon = sensor.getPumptime()/500-2;
        limited.setSelection(limitedPosition);
        pumptime.setSelection(pumptimePositon);


    }


    class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String) params[0];
            String potName = (String) params[1];
            String value = (String) params[2];
            String postParameters = "potName=" + potName + "&value=" + value;

            try {
                URL url = new URL(serverURL);
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


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


    }


    class UpdateSupply extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String serverURL = (String) params[0];
            String potName = (String) params[1];
            String auto = (String) params[2];
            String limited = (String) params[3];
            String pumptime = (String) params[4];
            String postParameters = "potName=" + potName + "&auto=" + auto
                    + "&limited=" + limited + "&pumptime=" + pumptime;

            System.out.println(postParameters);

            try {
                URL url = new URL(serverURL);
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


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


    }
}
