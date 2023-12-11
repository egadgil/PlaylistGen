package com.example.sumon.androidvolley;

import android.content.Intent;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/** @author: gigi harrabi */
/** Set button for each theme*/
public class UIThemeActivity extends AppCompatActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener{

    private ProgressDialog pDialog;
    private Button profile;
    private RadioGroup radioGroup;
    private Button lilac;
    private Button moss;
    private Button plain;
    private Button sky_blue;
    private Button set_UI;
    private Button preview;

    private String theme;

    private String baseURL = Const.SERVER + "/users/set_ui";

    @Override
    /** Create the buttons and listeners*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.ui_theme);

        profile = (Button) findViewById(R.id.profile);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        lilac = (RadioButton) findViewById(R.id.radio_lilac);
        moss = (RadioButton) findViewById(R.id.radio_moss);
        plain = (RadioButton) findViewById(R.id.radio_plain);
        sky_blue = (RadioButton) findViewById(R.id.radio_skyblue);
        set_UI = (Button) findViewById(R.id.btnSetUI);
        preview = (Button) findViewById(R.id.btnPreview);

        profile.setOnClickListener(this);
        set_UI.setOnClickListener(this);
        preview.setOnClickListener(this);

        pDialog = new ProgressDialog(this);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener(){
                    //a radio button has been clicked
                    @Override
                    /** Name ID for themes*/
                    public void onCheckedChanged(RadioGroup group, int checkedId){
                        theme = ("");
                        //which button has been selected
                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);

                        if(radioButton.getId() == lilac.getId())
                        {
                            theme = "lilac";
                        }
                        else if (radioButton.getId() == moss.getId()){
                            theme = "moss";
                        }
                        else if (radioButton.getId() == plain.getId()){
                            theme = "plain";
                        }
                        else if (radioButton.getId() == sky_blue.getId()){
                            theme = "skyblue";
                        }
                    }
                }
        );
    }

    @Override
    /** Find themes for profile, use preview button to see how it looks like, use set button when
     * deciding which theme you like*/
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.profile:
                startActivity(new Intent(UIThemeActivity.this,
                        ProfilePageActivity.class));
                break;
            case R.id.btnPreview:
                Log.d("THEME", theme);
                themeChange(theme);
                break;
            case R.id.btnSetUI:
                Log.d("THEME", theme);
                themeChange(theme);
                postUiTheme(theme);
                break;

        }


    }
    /** Declare radio group button as a way to check all changes made by your buttons*/
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }
    /** Theme string  and check if there is a change*/
    public void themeChange(String theme){
        Log.d("THEME", theme);

                //setTheme(R.style.moss);
                themeUtils.changeToTheme(this, theme);

    }
    /** Grab json obj of changed theme from a certain user*/
    public void postUiTheme(String theme){
        themeUtils.changeToTheme(this, theme);
        String tag_json_obj = "jason object";
       // showProgressDialog();
        final JSONObject body = new JSONObject();
        String jsonString = null;

        String req = GlobalUser.getUsername() + ":" + theme;

        StringRequest strReq = new StringRequest(Request.Method.POST, baseURL, new Response.Listener<String>() {
            @Override
            /** Check if there is a response*/
            public void onResponse(String response) {
                Log.d("RESP", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            /** Check if there is no response*/
            public void onErrorResponse(VolleyError error) {
               // Log.d("ERR", error.getMessage());
                Log.d("rr", error.toString());
                Log.d("ERROR", body.toString());
            }
        }){

            @Override
            /** Get String params from username and ui theme
             * use app controller to add request to queue by grabbing the json obj and request*/
            public Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", GlobalUser.getUsername());
                params.put("uiTheme", theme);

                return params;
            }


        };


        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);


    }
}
