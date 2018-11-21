package com.example.hagome.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PotRegisterRequest extends StringRequest {

    final static private String URL = "http://210.110.180.24/PotsRegister.php";
    private Map<String, String> parameters;

    public PotRegisterRequest(String userID, String potName, String flower1, String flower2, String flower3, String flower4, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("potName", potName);
        parameters.put("flower1", flower1);
        parameters.put("flower2", flower2);
        parameters.put("flower3", flower3);
        parameters.put("flower4", flower4);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
