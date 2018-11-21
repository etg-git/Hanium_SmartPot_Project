package com.example.hagome.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PotDeleteRequest extends StringRequest {

    final static private String URL = "http://210.110.180.24/PotsDelete.php";
    private Map<String, String> parameters;

    public PotDeleteRequest(String potName, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("potName", potName);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
