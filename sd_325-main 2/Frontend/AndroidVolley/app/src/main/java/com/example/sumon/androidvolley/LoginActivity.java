package com.example.sumon.androidvolley;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

/**
 * LoginActivity class that allows the user to input a username and password and attempts to log them in, which would take
 * them to the user dashboard. If they enter credentials that don't match an account that exists, they won't be able to login.
 * @author Zach
 */
public class LoginActivity extends AppCompatActivity {

    /*
    Two fields that user enters to try to login
     */
    private EditText usernameLogin, passwordLogin;
    /*
    Button that is used to check if they entered valid credientals
     */
    private Button btnEnter;
    /*
    Username string for requests
     */
    private String userString;
    /*
    password string for requests
     */
    private String passString;
    /*
    Request progress dialog
     */
    private ProgressDialog pDialog;
    /*
    Progress dialog used when user is logged in
     */
    private ProgressDialog progressDialog;

        private String URL = Const.SERVER + "users/login";

    /**
     * Overriden onCreate method that sets the visuals to login_request.xml and sets the buttons and text fields to java variables
     * for functionality. Sets up the baseplate for the user to enter their information and attempt to login, as well as a button
     * that performs the attempt
      * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_request);

        //Setting frontend screen to java code
        usernameLogin = (EditText) findViewById(R.id.usernameText);
        passwordLogin = (EditText) findViewById(R.id.passwordText);
        btnEnter = (Button) findViewById(R.id.loginEnterBtn);

        //Setting progress Dialog information
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                //Getting info from user
                userString = String.valueOf(usernameLogin.getText());
                passString = String.valueOf(passwordLogin.getText());
                Log.d("ENTER", userString);
                Log.d("ENTER", passString);
                makeJsonObjReq();

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
     * Method that is called when the user presses the "enter" button, will take their entered information and makes a call
     * to a backend endpoint that checks whether the information that the user entered matches a registered user, if the
     * information is validated by backend, login method is called and globalUser variables are set.
     */
    public void makeJsonObjReq() {
        String TAG = "jsonObjLogin";
        String tag_json_obj = "jason login object";
        showProgressDialog();
        JSONObject body = new JSONObject();
        String jsonString = null;
        try {
            body.put("username", userString);
            body.put("password", passString);
            jsonString = body.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JsonObjectInfo", jsonString);
        final String tryme = jsonString;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        GlobalUser.setUserName(userString);
                        //This response means that we successfully logged in

                        //this is not testing
                        //keep
                        try {
                            GlobalUser.refreshUser(userString);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        login();


                        //This means that we got a response, time to check
                        //and do what is required
                        hideProgressDialog();
                        //Check to see if user has logged in successfully
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String body = null;
                //get status code here
                //String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                //if(error.networkResponse.data!=null) {
                  //  try {
                    //    body = new String(error.networkResponse.data,"UTF-8");
                      //  Log.d("ERRRR", body);

                    //} catch (UnsupportedEncodingException e) {
                      //  e.printStackTrace();
                    //}
                //}

                Log.d("ERROR RESPONSE", error.toString());
                hideProgressDialog();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return tryme == null ? null : tryme.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s"
                            , tryme, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    /**
     * Login method that is called after the user information is verified, just sends the user to userdashboard
     * because they have successfully logged in.
     */
    public void login()  {
        //Test this on and off if it doesn't work
        startActivity(new Intent(LoginActivity.this,
                UserDashboardActivity.class));
    }

}
