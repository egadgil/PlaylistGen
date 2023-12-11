package com.example.sumon.androidvolley;

import static org.apache.http.client.HttpClient.*;

import android.content.Intent;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.spotify.sdk.android.auth.AuthorizationClient;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
/** @author: gigi harrabi */
public class SpotifyService {

    //a method to get authtoken from spotifyapi
    static String client_id = "c1674e1ab75943bd8b230ee88a57f47f";
    static String client_secret = "ef45d175b89e4cbbbe22879df19fce63";

    private static String Token;
    /**our apps specific info
     valid for around an hour, and then request will need to be remade

     request token and token url to get a response.
     *  log success; try finding a json object if no response OR try logging access token*/
    public static String getToken(){




        String tokenURL = "https://accounts.spotify.com/api/token";
        //setting parameters
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "client_credentials");

        /* do what spotify requires. Which is encoding the url and params*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String form = parameters.keySet().stream()
                    .map(key -> {
                        try {
                            return key + "=" + URLEncoder.encode(parameters.get(key), String.valueOf(StandardCharsets.UTF_8));
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.joining("&"));
        }

        String header = client_id + ":"  + client_secret;


        //a post method that retrieves a json object for access token
        //send the client variables for our app registered with spotify
        //encodes them, and posts them to /token
        try {

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
                        Token = token;

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d("ERR", "Issue connecting to spotify token");

                }
            }) {

                @Override
                /** get header that authenticates your params.  */
                public Map<String,String> getHeaders() throws AuthFailureError{
                    //set appropriate spotifyAPI headers
                    Map<String,String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        params.put("Authorization","Basic " +  Base64.getEncoder().encodeToString(header.getBytes()));
                    }

                    return params;
                }

                @Override
                /**@getParams those params will put your content type and encode the header
                 *  also will grant your type and client credential*/
                public Map<String, String> getParams() throws AuthFailureError{
                    Map<String,String> params = new HashMap<>();
                    params.put("grant_type", "client_credentials");
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(token_req, "token resp");

        }
        catch(Exception e){
            e.printStackTrace();
            Log.d("ERR", e.toString());
        }

        return null;
    }
    /**@searchId Search for type from given search string; look up song and store it in array list with type track in given url
     * then log and check searched url*/
    //not the best fix, but adding in adapterClass so method knows whether to fill
    //playlist data from SavedPlaylists or PlaylistGenerator
    public static void searchId(String type, String search, String adapterClass){

        PlaylistGenerator.queries.clear();
        AddSongActivity.artists.clear();
        PlaylistGenerator.ids.clear();

        ArrayList<String> list = new ArrayList<String>();

        if(type.equals("song") || type.equals("songs")){
            type = "track";
        }

        String base = "https://api.spotify.com/v1/search?query=";
        String search_url = base.concat(search).concat("&type=").concat(type);
        //String search_url = "https://api.spotify.com/v1/search?query="+search+"&type="+type;

        Log.d("CHECK",search_url);
        String finalType = type;
        //String finalType1 = type;
        JsonObjectRequest search_items = new JsonObjectRequest(search_url, new Response.Listener<JSONObject>() {
            @Override
            /** @onResponse Grab json obj that is equal to artist or track and see if it responds.
             * Log it; and regiter it as an item with a max limit of 5
             * Log item with name and id
             * result is displayed in a list with query and result
             * Notify if data changes*/
            public void onResponse(JSONObject response) {

                Log.d("RESPO", response.toString());

                try {

                    JSONObject obj;

                    if(finalType.equals("artist")){
                        obj = response.getJSONObject("artists");
                    } else {
                        obj = response.getJSONObject("tracks");
                    }

                    Log.d("JSON",obj.toString());
                    JSONArray items = new JSONArray(obj.getString("items"));
                    Log.d("items", items.toString());

                    //JSONObject extern = items.getJSONObject(1);

                    // JSONArray items = new JSONArray(obj.getJSONArray("items"));
                    int items_length = items.length();
                    int numItems = 5;
                    if(items_length < 5){
                        numItems = items_length;
                    }
                    //search results and add each to arraylist
                    for(int i = 0; i < numItems; i++){
                        JSONObject json = items.getJSONObject(i);
                        Log.d("OBJ" + i, json.toString());
                        list.add((json.getString("name")) + ":" + json.getString("id"));

                        if(adapterClass.contains("add")){
                            JSONArray tempArr = json.getJSONArray("artists");
                            Log.d("artist arr", tempArr.toString());

                            JSONObject obj_2 = tempArr.getJSONObject(0);

                            AddSongActivity.artists.add(obj_2.getString("name"));

                        }
                    }

                    Log.d("QUERY", list.toString());

                    Log.d("result", list.toString());
                    for(int i = 0; i < list.size(); i++)
                    {
                        String toSplit = list.get(i);
                        //should be comprised of two parts
                        //hopefully a spotify id never includes :
                        String[] parts = toSplit.split(":");

                        if(adapterClass.contains("gen")){
                            PlaylistGenerator.queries.add(parts[0]);
                            PlaylistGenerator.ids.add(parts[1]);
                        } else {
                            AddSongActivity.queries.add(parts[0]);
                            AddSongActivity.ids.add(parts[1]);
                        }

                        Log.d("TYPE", finalType);
                    }

                    if(adapterClass.contains("gen")){
                        PlaylistGenerator.arrayAdapter.notifyDataSetChanged();
                    } else {
                        AddSongActivity.arrayAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        } ,new Response.ErrorListener(){

            @Override
            /** @onErrorResponse Display message if error is found with connecting to spotify search*/
            public void onErrorResponse(VolleyError error) {

                Log.d("ERR", "Issue connecting to spotify search" + error.getMessage());


            }
        }) {
            /**@GetHeaders Get header with hash mapped string params of token autherization and return params
             *  Display search respons*/
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
                //set appropriate spotifyAPI headers
                Map<String,String> params = new HashMap<>();
                // params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization","Bearer " + Token);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(search_items, "search resp");


    }



}
