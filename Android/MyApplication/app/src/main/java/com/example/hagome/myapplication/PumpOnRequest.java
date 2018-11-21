package com.example.hagome.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PumpOnRequest extends StringRequest {

    final static private String URL = "http://210.110.180.24/PumpOn.php";
    private Map<String, String> parameters;

    public PumpOnRequest(String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
