package com.example.sumon.androidvolley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
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
import java.util.List;

/**
 * Leaderboard class that displays usernames with a like value associated with it, which are all ranked. Has multiple
 * different leaderboards based on time.
 * *Not fully functional yet, still being implemented*
 * @author Zach
 */
public class LeaderboardActivity extends AppCompatActivity {

    private Button dailyBtn, weeklyBtn, lifetimeBtn, str_req_Btn;
    private TextView txtThirdPlace, txtSecondPlace, txtFirstPlace, txtRestofPlaces;
    private TextView numThirdPlace, numSecondPlace, numFirstPlace, numRestOfPlaces;
    private ProgressDialog pDialog, progressDialog;
    private String TAG = "StrReq";
    //JSON url
    public static final String URL_STRING_REQ = "http://coms-309-058.class.las.iastate.edu:8080/api/users/leaderboard/" + GlobalUser.getUsername();
    private String tag_string_req = "string_req";

    //Arraylists for json objects
    private ArrayList<String> dailyLeaderboardArray = new ArrayList<String>();
    private ArrayList<String> weeklyLeaderboardArray = new ArrayList<String>();
    private ArrayList<String> lifetimeLeaderboardArray = new ArrayList<String>();
    private ArrayList<String> likeArray = new ArrayList<String>();
    //Starting for strings for json objects
    private String startLeaderBoard = "";
    private String startLikes = "";

    private String test = "[{\"id\":19,\"title\":\"Lavender Haze\"}," +
            "{\"id\":20,\"title\":\"Back to December\"}," +
            "{\"id\":21,\"title\":\"Shake it Off\"}," +
            "{\"id\":22,\"title\":\"Style\"}]";


    /**
     * Standard onCreate method that sets the visuals to leaderboard_request.xml and connects the buttons, textFields, etc. to
     * java variables so that functionality exists. Also sets variable values so that the default "daily" leaderboard is
     * displayed when user goes to this page. Sets the onClickListeners of all of the buttons as well, which each display a
     * different leaderboard with different name and like values.
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

        setContentView(R.layout.leaderboard_request);



        //Do JSON stuff for leaderboard
        //makeStringReq();
        //txtRestofPlaces.setText(jsonStr);
        //Progress Dialog initialization
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        //Button and text Initializations
        dailyBtn = findViewById(R.id.btnDaily);
        weeklyBtn = findViewById(R.id.btnWeekly);
        lifetimeBtn = findViewById(R.id.btnLifetime);
        txtThirdPlace = findViewById(R.id.thirdPlaceText);
        txtSecondPlace = findViewById(R.id.secondPlaceText);
        txtFirstPlace = findViewById(R.id.firstPlaceText);
        txtRestofPlaces = findViewById(R.id.restOfPlacesText);
        numThirdPlace = findViewById(R.id.thirdPlaceNum);
        numFirstPlace = findViewById(R.id.firstPlaceNum);
        numSecondPlace = findViewById(R.id.secondPlaceNum);
        numRestOfPlaces = findViewById(R.id.restofPlaceNums);


        str_req_Btn = findViewById(R.id.stringRequestButton);

        //Setting sizes for text and numbers
        txtThirdPlace.setTextSize(18);
        txtSecondPlace.setTextSize(18);
        txtFirstPlace.setTextSize(18);
        txtRestofPlaces.setTextSize(18);
        numThirdPlace.setTextSize(16);
        numSecondPlace.setTextSize(16);
        numFirstPlace.setTextSize(16);
        numRestOfPlaces.setTextSize(18);

        //Colors for clicked vs unclicked buttons
        int clickedColor = android.graphics.Color.rgb(139, 129, 201);
        int unClickedColor = android.graphics.Color.rgb(221, 220, 226);

        //Phony arrays
        String[] dailyNames = new String[]{"John bob", "wilky fraud", "Zachary Harveyiuyiyu", "gboi ifob", "poal", "pioifv", "oibsu", "aoifv", "afuouk baoug", "oagmblmg"};
        String[] weeklyNames = new String[]{"V", "my goat", "ouasd was", "oabu", "bgsf", "ljalj oubud", "reaf", "ousbn", "lmpiun", "bogsut"};
        String[] lifetimeNames = new String[]{"Z", "king lebron", "squirmy pants", "silky road", "cannot", "swear", "too bebeef", "wanna kurosun", "tantrn", "oulaj ojso"};
        int[] dailyLikeNums = new int[]{10,9,8,7,6,5,4,3,2,1};
        int[] weeklyLikeNums = new int[]{20,18,16,14,12,10,8,6,4,2};
        int[] lifetimeLikeNums = new int[]{40,36,32,28,24,20,16,12,8,4};

        //Setting default leaderboard to be daily
        //Text and numbers loading on click
        txtThirdPlace.setText(dailyNames[2]);
        txtSecondPlace.setText(dailyNames[1]);
        txtFirstPlace.setText(dailyNames[0]);
        txtRestofPlaces.setText("");
        numThirdPlace.setText(""+dailyLikeNums[2]);
        numSecondPlace.setText(""+dailyLikeNums[1]);
        numFirstPlace.setText(""+dailyLikeNums[0]);
        numRestOfPlaces.setText("");

        for(int i = 3; i < 10; ++i)
        {
            txtRestofPlaces.append(dailyNames[i]+ "\n");
            numRestOfPlaces.append(dailyLikeNums[i]+ "\n");
        }
        //Button color changes on click
        dailyBtn.getBackground().setColorFilter(clickedColor, PorterDuff.Mode.MULTIPLY);
        weeklyBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);
        lifetimeBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);


        str_req_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                txtRestofPlaces.setText("");
                makeStringReq(URL_STRING_REQ, true);
               // for(int i= 0; i < likeArray.size(); ++i)
                {
                  //  txtRestofPlaces.append(likeArray.get(i));
                }
            }

        });


        dailyBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Text and numbers loading on click
                txtThirdPlace.setText(dailyNames[2]);
                txtSecondPlace.setText(dailyNames[1]);
                txtFirstPlace.setText(dailyNames[0]);
                txtRestofPlaces.setText("");
                numThirdPlace.setText(""+dailyLikeNums[2]);
                numSecondPlace.setText(""+dailyLikeNums[1]);
                numFirstPlace.setText(""+dailyLikeNums[0]);
                numRestOfPlaces.setText("");

                for(int i = 3; i < 10; ++i)
                {
                    txtRestofPlaces.append(dailyNames[i]+ "\n");
                    numRestOfPlaces.append(dailyLikeNums[i]+ "\n");
                }
                //Button color changes on click
                dailyBtn.getBackground().setColorFilter(clickedColor, PorterDuff.Mode.MULTIPLY);
                weeklyBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);
                lifetimeBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);



            }
        });
        weeklyBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Text and numbers loading on click
                txtThirdPlace.setText(weeklyNames[2]);
                txtSecondPlace.setText(weeklyNames[1]);
                txtFirstPlace.setText(weeklyNames[0]);
                txtRestofPlaces.setText("");
                numThirdPlace.setText(""+weeklyLikeNums[2]);
                numSecondPlace.setText(""+weeklyLikeNums[1]);
                numFirstPlace.setText(""+weeklyLikeNums[0]);
                numRestOfPlaces.setText("");
                for(int i = 3; i < 10; ++i)
                {
                    txtRestofPlaces.append(weeklyNames[i]+ "\n");
                    numRestOfPlaces.append(weeklyLikeNums[i]+ "\n");
                }
                //Button color changes on click
                dailyBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);
                weeklyBtn.getBackground().setColorFilter(clickedColor, PorterDuff.Mode.MULTIPLY);
                lifetimeBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);

            }
        });
        lifetimeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Text and numbers loading on click
                txtThirdPlace.setText(lifetimeNames[2]);
                txtSecondPlace.setText(lifetimeNames[1]);
                txtFirstPlace.setText(lifetimeNames[0]);
                txtRestofPlaces.setText("");
                numThirdPlace.setText(""+lifetimeLikeNums[2]);
                numSecondPlace.setText(""+lifetimeLikeNums[1]);
                numFirstPlace.setText(""+lifetimeLikeNums[0]);
                numRestOfPlaces.setText("");
                for(int i = 3; i < 10; ++i)
                {
                    txtRestofPlaces.append(lifetimeNames[i]+ "\n");
                    numRestOfPlaces.append(lifetimeLikeNums[i]+ "\n");
                }
                //Button color changes on click
                dailyBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);
                weeklyBtn.getBackground().setColorFilter(unClickedColor, PorterDuff.Mode.MULTIPLY);
                lifetimeBtn.getBackground().setColorFilter(clickedColor, PorterDuff.Mode.MULTIPLY);

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
    private void makeStringReq(String URL, boolean isLikeList) {
       // showProgressDialog();

        ArrayList<String> arrNames = new ArrayList<String>();
        ArrayList<String> arrLikes = new ArrayList<String>();

        StringRequest strReq = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d(TAG, response.toString());
                txtRestofPlaces.setText(response.toString());
              //  hideProgressDialog();

               /* if(isLikeList)
                {
                    startLikes = response.toString();
                    try {
                        JSONArray jsonArr = new JSONArray(startLikes);
                        int size = jsonArr.length();
                        for(int i = 0; i < size; ++i)
                        {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            String jsonObjtoString = jsonObj.keys().next();

                            Log.d("LikeListJson", jsonObj.getString("title"));
                            arrLikes.add(jsonObj.getString("title"));
                            Log.d("Should be added", arrLikes.get(i));
                        }

                    }
                    catch (JSONException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
                else
                {
                    startLeaderBoard = response.toString();

                    try {
                        JSONArray jsonArr = new JSONArray(startLeaderBoard);
                        int size = jsonArr.length();
                        for(int i = 0; i < size; ++i)
                        {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            String jsonObjtoString = jsonObj.keys().next();

                            Log.d("LikeLeaderboardJson", jsonObj.getString("names"));
                            arrNames.add(jsonObj.getString("names"));
                        }

                    }
                    catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }*/



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });

        // Adding request to request queue

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        /*if(isLikeList)
        {
            Log.d("HELP", arrLikes.toString());
            return arrLikes;
        }
        else
        {
            Log.d("HELP2", "HI");
            return arrNames;
        }*/
    }

}
