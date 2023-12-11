package com.example.sumon.androidvolley;
/** @author: gigi harrabi */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class AvatarActivity extends AppCompatActivity implements OnClickListener {

    private Button profile;
    private ImageButton profile_clouds;
    private ImageButton profile_swirl;
    private ImageButton profile_moon;
    private ImageButton profile_headphone;
    private Button set_profile;
    private String profile_picture;

    private String baseURL = Const.SERVER + "users/set_profile";

    /**@onCreate set buttons for each profile pic and
     * use a listener to set profile*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);

        setContentView(R.layout.avatar);

        profile = (Button) findViewById(R.id.profile);
        set_profile = (Button) findViewById(R.id.set_profile);
        profile_clouds = (ImageButton) findViewById(R.id.profile_clouds);
        profile_swirl = (ImageButton) findViewById(R.id.profile_swirl);
        profile_moon = (ImageButton) findViewById(R.id.profile_moon);
        profile_headphone = (ImageButton) findViewById(R.id.profile_headphone);

        profile_clouds.setOnClickListener(this);
        profile_swirl.setOnClickListener(this);
        profile_moon.setOnClickListener(this);
        profile_headphone.setOnClickListener(this);

        set_profile.setOnClickListener(this);
        profile.setOnClickListener(this);

    }
/**@onClick make a case switch of each profile pic */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.profile:
                startActivity(new Intent(AvatarActivity.this,
                        ProfilePageActivity.class));
                break;
            case R.id.profile_clouds:
                profile_picture = "clouds";
                break;
            case R.id.profile_headphone:
                profile_picture = "headphone";
                break;
            case R.id.profile_moon:
                profile_picture = "moon";
                break;
            case R.id.profile_swirl:
                profile_picture = "swirl";
                break;
            case R.id.set_profile:
                postProfilePic(profile_picture);
                break;
        }
    }
/**@postProfilePic Use a string to pass a setter */
    public void postProfilePic(String profile_pic){
        String tag_json_obj = "jason object";
        /** showProgressDialog(); */
        GlobalUser.setAvatar(profile_pic);
        /** final String json = jsonString;
        make a string request for url and make a listener for response */
        StringRequest strReq = new StringRequest(Request.Method.POST, baseURL, new Response.Listener<String>() {
            /** @onResponse log response
             *  and log error response */
            @Override
            public void onResponse(String response) {
                Log.d("RESP", response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("rr", error.toString());
            }
        }){
            /** @getParams get exception error that may be in profile or user by getting their params
             * and add request and obj to queue */
            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", GlobalUser.getUsername());
                params.put("profile_pic", profile_pic);

                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);


    }
}
