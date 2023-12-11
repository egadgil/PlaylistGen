package com.example.sumon.androidvolley;


import static com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/** @author: gigi harrabi */
public class SLogin extends AppCompatActivity{
    private static final String REDIRECT_URI = "http://localhost:8080/api/get-user-code";
    private Button login;
    public static String playlist_id;
    private static final int REQUEST_CODE = 1337;
    public String finalHashed = "";

    static String client_id = "c1674e1ab75943bd8b230ee88a57f47f";
    static String client_secret = "ef45d175b89e4cbbbe22879df19fce63";

    private static String code = "";
    private static String TOKEN = "";
    private static String ver = "";
    String redirectUri = "http://192.168.56.1:8080/callback";
    //Response
     //String redirectUri = "https://challenge.spotify.com/c/01cdd327-2a8c-437a-9c28-b315d5b567fc-3/a557b4d5-4faa-49ef-abe4-d11cfc041b20/recaptcha";

    @Override
   /** Make a new intent to verify a client log in and sending a request*/
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.spotify_login);

        login = (Button) findViewById(R.id.spotifyLogin);

        String codeVer;
        String hash;
        try {
            codeVer = generateCodeVerifier();
            ver = codeVer;
            Log.d("CODE VERIFIER", codeVer);
            hash = generateCodeChallange(codeVer);
            Log.d("CODE CHALLENGE", hash);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        //hard to log a user out of their spotify account
        //COULD direct them to sign in intent each time
        //even if their information is saved, click not you and relog in

       // if(GlobalUser.SPOTIFY_LOGGED_IN && playlist_id != null){
            //so user cannot try to log in again
            //will cause spotify auth error
         //   login.setEnabled(false);
           // login.setClickable(false);
          //  export(playlist_id);
        //}


        finalHashed = hash;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AuthorizationClient.createLoginActivityIntent(SLogin.this,
                        new AuthorizationRequest.Builder(client_id,
                                AuthorizationResponse.Type.CODE, redirectUri)
                                .setScopes(new String[]{"playlist-read-private","playlist-modify-private",
                        "playlist-modify-public", "user-read-private"})
                                .setCustomParam("code_challenge_method", "S256")
                                .setCustomParam("code_challenge", hash)
                                .build()
                );

                startActivityForResult(intent,REQUEST_CODE);
            }});


    }

    @Override
    protected void onStart() {
        super.onStart();
        // We will start writing our code here.
    }

    private void connected() {
        // Then we will write some more code here.
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            Log.d(String.valueOf(REQUEST_CODE), String.valueOf(requestCode));

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("TOKEN TOKEN", String.valueOf(resultCode));
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.d("err", response.toString());
                    break;

                case CODE:
                    //grab code to exchange
                    Log.d("code resp", String.valueOf(response.getType()));
                    code = response.getCode();
                    getAccessToken(code, finalHashed, ver);

                    break;
                // Most likely auth flow was cancelled
                default:
                    Log.d("default", String.valueOf(response.describeContents()));
                    // Handle other cases
            }
        }


    }


    private void getAccessToken(String code, String finalHashed, String ver){
        Log.d("finalHashed", finalHashed);


        String tokenURL = "https://accounts.spotify.com/api/token";
        StringRequest token_req = new StringRequest(Request.Method.POST, tokenURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SUCCESS", response.toString());
                JSONObject obj;



                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    String token = obj.getString("access_token");
                    Log.d("TOKEN", token);
                    TOKEN = token;
                    tokenPost(TOKEN);

                    if(playlist_id != null){
                        export(playlist_id);
                    }
                    GlobalUser.SPOTIFY_LOGGED_IN = true;


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            /** Throw error if there is issue connecting to spotify; may be network response
             * is null or data issue. Or even unsupported encoding */
            public void onErrorResponse(VolleyError error) {

                Log.d("ERR", "Issue connecting to spotify token");
                Log.d("ERR", error.toString());
                String body;
                //get status code here
                String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                if(error.networkResponse.data!=null) {
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        Log.d("ERRRR", body);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        }) {
            @Override
            /** set appropriate spotifyAPI headers */
            public Map<String,String> getHeaders() throws AuthFailureError{
                //set appropriate spotifyAPI headers
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");

                return params;
            }

            @Override
            /** Get type of authorization code granted and the code.
             * Get the redirect url. Get client id
             * get code verifier
             * return params and token response
             * Make a string that verifies code and has a payload of 32 bytes
             * Make sure the build version is valid to encode into URL
             * Encodes auth to be secure*/
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("grant_type", "authorization_code");
                params.put("code", code);
                params.put("redirect_uri", "http://192.168.56.1:8080/callback");
                params.put("client_id", client_id);
                params.put("code_verifier", ver);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(token_req, "token resp");



    }
    String generateCodeVerifier() throws UnsupportedEncodingException {
        SecureRandom secureRandom = new SecureRandom();
        byte[] codeVerifier = new byte[32];
        secureRandom.nextBytes(codeVerifier);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifier);
        }
        return null;
    }

    String generateCodeChallange(String codeVerifier) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        byte[] bytes = codeVerifier.getBytes("US-ASCII");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(bytes, 0, bytes.length);
        byte[] digest = messageDigest.digest();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        }
        return codeVerifier;
    }

    private static void tokenPost(String token){

        //String tokenURL = "http://192.168.56.1:8080/api/playlist/scope";
        String tokenURL = Const.SERVER + "playlist/scope";
        StringRequest token_req = new StringRequest(Request.Method.POST, tokenURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("token posted", response);




            }
        }, new Response.ErrorListener() {
            @Override
            /** Throw error if there is issue connecting to token; may be network response
             * is null or data issue. Or even unsupported encoding */
            public void onErrorResponse(VolleyError error) {

                Log.d("ERR", "Issue connecting to user token scope");
                Log.d("ERR", error.toString());
                String body;
                //get status code here
                String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                if(error.networkResponse.data!=null) {
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        Log.d("ERRRR", body);

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

            }
        }) {

            @Override
            /** Get user token params and token to hashmap and add to request queue */
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("user_token", token);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(token_req, "token resp");


    }

    public static void export(String playlist_id){
        String url = Const.SERVER + "playlist/export";

        StringRequest export = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Success","playlist exported");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err", error.toString());

            }
        }){
            @Override
            public Map<String,String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("playlist_id",playlist_id);

                return params;
            }
        };


        AppController.getInstance().addToRequestQueue(export);



    }


}