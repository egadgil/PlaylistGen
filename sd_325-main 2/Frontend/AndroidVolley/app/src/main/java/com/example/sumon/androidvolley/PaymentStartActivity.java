package com.example.sumon.androidvolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
 * PaymentStartActivity class that tells the user the benefits of becoming a member and prompts them
 * to continue to PaymentEndActivity so they can pay, or to return to the activity they came from
 * @author Zach
 */
public class PaymentStartActivity extends AppCompatActivity{

    private char bulletSymbol = '\u2023';
    private TextView benefitsText;
    private Button confirmBtn;
    private TextView paymentText;

    /**
     * onCreate method that sets the visuals to payment_request.xml and displays the member benefts
     * to the user. Sets the functionality of the button so that when clicked, the user will be taken
     * to PaymentEndActivity
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
        setContentView(R.layout.payment_request);

        //Set frontend variables to java variables
        paymentText = (TextView) findViewById(R.id.paymentText);
        benefitsText = (TextView) findViewById(R.id.paymentBenefitsText);
        confirmBtn = (Button) findViewById(R.id.paymentYesBtn);
            benefitsText.setText(bulletSymbol + "Ability to export playlist to spotify\n" +
                    bulletSymbol + "Create a playlist off genre as well as artist name or song\n" +
                    bulletSymbol + "Choose specified length of playlist 1-100 songs\n" +
                    bulletSymbol + "Add or remove specific songs from playlist\n" +
                    bulletSymbol + "Many more benefits as time goes on!");
        if(GlobalUser.getMembershipType() != "null" && GlobalUser.getMembershipType() != "Unpaid Member")
        {
            paymentText.setText("Thank you for being a paid member. It's your continued support" +
                    " that allows us to do what we love. Keep enjoying these benefits:");
            paymentText.setTextSize(17);
            //confirmBtn.setVisibility(View.INVISIBLE);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(PaymentStartActivity.this,
                        PaymentEndActivity.class));
            }
        });
    }
}
