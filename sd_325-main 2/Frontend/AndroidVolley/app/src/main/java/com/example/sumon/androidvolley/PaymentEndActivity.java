package com.example.sumon.androidvolley;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
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

import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * PaymentEndActivity class which is where the user will enter their payment information directly before becoming a paid member,
 * only accessible through PaymentStartActivity. Note: Payments are not validated since we are not
 * trying to make money off of this application.
 * @author Zach
 */
public class PaymentEndActivity extends AppCompatActivity {
    private EditText textExpirationDate, textCardNumber, textSecurityCode, textNameOnCard, textZipCode;
    private Button btnConfirmPayment, btnReturnToDash;
    private TextView paymentEndText;
    private ProgressDialog pDialog;
    private String expirationDateString, cardNumberString, securityCodeString, nameOnCardString, zipCodeString;
    private String URL = "http://coms-309-058.class.las.iastate.edu:8080/payments/post_payment";
//private final String URL = "http://localhost:8080/payments/post_payment";
    private long zipCodeLong;

    /**
     * onCreate method that sets the visuals to payment_cardinfo.xml which is where the user enters their card info to pay.
     * Sets EditTexts, Buttons, etc. to java variables for functionality. After the user enters their information and presses
     * the "enter" button, their information is send to backend and they are a member.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.payment_cardinfo);

        //Setting frontend to java code
        paymentEndText = (TextView) findViewById(R.id.cardInfoText);
        textCardNumber = (EditText) findViewById(R.id.cardNumberText);
        textSecurityCode = (EditText) findViewById(R.id.cardSecurityCodeText);
        textNameOnCard = (EditText) findViewById(R.id.cardNameText);
        textZipCode = (EditText) findViewById(R.id.cardZipCodeText);
        textExpirationDate = (EditText) findViewById(R.id.expirationDateText);
        btnConfirmPayment = (Button) findViewById(R.id.paymentConfirmBtn);
        btnReturnToDash = (Button) findViewById(R.id.returnToDashFromPayment);
        btnReturnToDash.setVisibility(View.INVISIBLE);
        //Setting progress Dialog information
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        //This means that the user has entered their information
        //And is ready to pay
        btnConfirmPayment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showProgressDialog();
                //Getting info from user
                cardNumberString = String.valueOf(textCardNumber.getText());
                securityCodeString = String.valueOf(textSecurityCode.getText());
                expirationDateString = String.valueOf(textExpirationDate.getText());
                nameOnCardString = String.valueOf(textNameOnCard.getText());
                zipCodeString = String.valueOf(textZipCode.getText());
                //convert zipcode to a long
                //zipCodeLong = Long.parseLong(zipCodeString);

                Log.d("ENTER", cardNumberString);
                Log.d("ENTER", securityCodeString);
                Log.d("ENTER", expirationDateString);
                Log.d("ENTER", nameOnCardString);
                Log.d("ENTER", zipCodeString);
                makeJsonObjReq();

            }
        });
        btnReturnToDash.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //This is called when the user wants to return to user dashboard
                startActivity(new Intent(PaymentEndActivity.this,
                        UserDashboardActivity.class));
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
     * private method that saves the user's information in backend, and gives an approval message if backend
     * successfully receives and saves the information.
     */
    private void makeJsonObjReq() {
        URL = URL + "?username=" + GlobalUser.getUsername();
        String TAG = "json Obj1";
        String tag_json_obj = "jason object";
        showProgressDialog();
        final JSONObject body = new JSONObject();
        String jsonString = null;
        try {
            body.put("cardNumber", cardNumberString);
            body.put("securityCode", securityCodeString);
            body.put("expiryDate", expirationDateString);
            body.put("zipcode", zipCodeString);
            body.put("fullName", nameOnCardString);
            body.put("reoccuring", "true");
            jsonString = body.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("jsonOutputBeforeSent", jsonString);
        final String tryme = jsonString;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response " + response.toString());
                        //The user has paid and all has gone well.
                        paymentEndText.setText("Thank you for becoming a paid member. It's people like you that" +
                                " allow us to keep doing what we love!");
                        GlobalUser.setMembershipType("Paid Member");
                        btnReturnToDash.setVisibility(View.VISIBLE);
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d("JSONOUTPUTPART2", body.toString());


                hideProgressDialog();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                    try {
                        Log.d("body", tryme);
                        return tryme == null ? null : tryme.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        Log.d("body", uee.toString());
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
        };
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
}
