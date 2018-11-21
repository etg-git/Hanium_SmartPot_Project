package com.example.hagome.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterPotActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private String pots;
    private String pot1;
    private String pot2;
    private String pot3;
    private String pot4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pot);

        final EditText potsName = (EditText) findViewById(R.id.potsName);
        final EditText flower1 = (EditText) findViewById(R.id.potName_1);
        final EditText flower2 = (EditText) findViewById(R.id.potName_2);
        final EditText flower3 = (EditText) findViewById(R.id.potName_3);
        final EditText flower4 = (EditText) findViewById(R.id.potName_4);
        final Button potRegisterButton = (Button) findViewById(R.id.potRegisterButton);


        potRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pots = potsName.getText().toString();
                pot1 = flower1.getText().toString();
                pot2 = flower2.getText().toString();
                pot3 = flower3.getText().toString();
                pot4 = flower4.getText().toString();
                Intent intent = getIntent();
                String userID = intent.getStringExtra("userID");
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPotActivity.this);
                                dialog = builder.setMessage("화분 등록에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPotActivity.this);
                                dialog = builder.setMessage("화분 등록에 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                PotRegisterRequest registerRequest = new PotRegisterRequest(userID, pots, pot1, pot2, pot3, pot4, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterPotActivity.this);
                queue.add(registerRequest);
            }
        });


    }
}
