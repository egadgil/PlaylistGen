package com.example.sumon.androidvolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONStringer;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

/**
 * SignupActivity class that allows the user to signup given that they provide a username, password, email, and their
 * full name. After they give enough info, a gmail authenication will be sent to them to their given email with a security
 * code check to ensure their email is valid and that they have access to it.
 * @author Zach
 */
public class SignupActivity extends AppCompatActivity{

    private EditText usernameSignup, passwordSignup, fullnameSignup, emailSignup, securityCodeInput;
    private TextView securityCodeText;
    private Button btnEnter, btnEnterLast;
    private String userString;
    private String passString;
    private String emailString;
    private String fullnameString;
    private ProgressDialog pDialog;
    private final String URL = "http://coms-309-058.class.las.iastate.edu:8080/api/users/register";

    /**
     * Sets the visuals to signup_request.xml, and sets the EditTexts and Buttons to java variables for functionality.
     * Has appearing and disappearing parts based on how far in registration process the user is.
     * Has security code and gmail baseplate functionality in here.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_request);

        //Setting frontend to java code
        usernameSignup = (EditText) findViewById(R.id.signUpUsername);
        passwordSignup = (EditText) findViewById(R.id.signupPassword);
        fullnameSignup = (EditText) findViewById(R.id.signupFullName);
        emailSignup = (EditText) findViewById(R.id.signupEmail);
        btnEnter = (Button) findViewById(R.id.enterBtn);
        btnEnterLast = (Button) findViewById(R.id.enterBtnLast);
        securityCodeInput = (EditText) findViewById(R.id.securityCodeText);
        securityCodeText = (TextView) findViewById(R.id.emailText);


        //Make the stuff disappear until needed
        securityCodeText.setVisibility(View.GONE);
        securityCodeInput.setVisibility(View.GONE);
        btnEnterLast.setVisibility(View.GONE);
        //Setting progress Dialog information
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        //Grabbing the information entered by the user
        btnEnter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showProgressDialog();
                //Getting info from user
                userString = String.valueOf(usernameSignup.getText());
                passString = String.valueOf(passwordSignup.getText());
                fullnameString = String.valueOf(fullnameSignup.getText());
                emailString = String.valueOf(emailSignup.getText());
                Log.d("ENTER", userString);
                Log.d("ENTER", passString);
                Log.d("ENTER", fullnameString);
                Log.d("ENTER", emailString);
                //Send email first
                Log.i("SendMailActivity", "Send Button Clicked.");

                String fromEmail = "zmans1205@gmail.com";
                String fromPassword = "xbsm xojh sbqo zsgp";
                String toEmails = emailString;
                List toEmailList = Arrays.asList(toEmails
                        .split("\\s*,\\s*"));
                Log.i("SendMailActivity", "To List: " + toEmailList);
                String emailSubject = "WILL THE WOLRD END TODAY?";
                String emailBody = "Your secret code is 440";
                new SendMailTask(SignupActivity.this).execute(fromEmail,
                        fromPassword, toEmailList, emailSubject, emailBody);
                //makeJsonObjReq();
                securityCodeText.setVisibility(View.VISIBLE);
                securityCodeInput.setVisibility(View.VISIBLE);
                btnEnterLast.setVisibility(View.VISIBLE);
                btnEnter.setVisibility(View.GONE);
                hideProgressDialog();
            }
        });
        btnEnterLast.setOnClickListener(new View.OnClickListener()
        {
            //The user has got an email sent to them now it's time to check if their security code is valid
            @Override
            public void onClick(View v){
                //Just check the value and see if it's equal to 440 (current security code) and if it is then go to dashboard and
                //Register the user
               String securityCodeString = String.valueOf(securityCodeInput.getText());
               Log.d("STriNG SEcurity", securityCodeString);
                int securityCode = Integer.parseInt(securityCodeString);
                Log.d("SECURITY INPUT", securityCodeString);
                if(securityCode == 440)
                {
                    //Success! They have entered the correct security code
                    makeJsonObjReq();
                }

            }
        });




    }
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Getter method for username
     * @return userString
     */
    public String getUsername() {
        return userString;
    }
    /*public void postUserSignup() throws IOException {
        URL url = null;
        try {
            url = new URL("http://www.android.com/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
           // writeStream(out);

        }



    }*/

    /**
     * Method that is used to save the user's information that they enter, this is called after the security check is passed
     */
    private void makeJsonObjReq() {
        String TAG = "json Obj1";
        String tag_json_obj = "jason object";
        showProgressDialog();
        final JSONObject body = new JSONObject();
        String jsonString = null;
        try {
            body.put("username", userString);
            body.put("password", passString);
            body.put("fullName", fullnameString);
            body.put("email", emailString);
            jsonString = body.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("jsonOutputPLEASE", jsonString);
        final String tryme = jsonString;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "AHHHH" + response.toString());
                        //usernameSignup.setText(response.toString());

                        if(response.toString().equals("User registered successfully"))
                        {
                            GlobalUser.setUserName(userString);
                            GlobalUser.setEmail(emailString);
                            GlobalUser.setName(fullnameString);
                            GlobalUser.setMembershipType(Const.UNPAID_MEMBER);
                        }

                        hideProgressDialog();
                        startActivity(new Intent(SignupActivity.this,
                                UserDashboardActivity.class));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error);
                VolleyLog.d("Error", error);
                Log.d("JSONOUTPUTPART2", body.toString());
                hideProgressDialog();
                //Tell the user that something went wrong

                Toast.makeText(getApplicationContext(), "Your username or email is already in use", Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return tryme == null ? null : tryme.getBytes("utf-8");
                } catch(UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", tryme,
                            "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-type","application/json; charset=utf-8");
                return params;
            }

            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", "Androidhive");
//                params.put("email", "abc@androidhive.info");
//                params.put("pass", "password123");

                return params;
            }*/

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }



}
