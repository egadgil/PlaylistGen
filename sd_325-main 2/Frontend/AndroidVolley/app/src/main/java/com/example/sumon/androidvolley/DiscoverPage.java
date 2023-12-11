package com.example.sumon.androidvolley;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverPage extends AppCompatActivity {

    public ListView dynamic;
    public Button topTenBtn, allBtn;
    public static List<HashMap<String,String>> playlist_objs = new ArrayList<>();
    public static List<HashMap<String,String>> liked_playlist_objs = new ArrayList<>();

    //array adapter will read <playlist_name>=<number of likes>
    static ArrayList<String> playlist_data = new ArrayList<>();
    //find playlists already liked by user
    //used to fill out hearts
    static ArrayList<String> liked_playlists = new ArrayList<>();
    public ArrayList<String> playlist_name = new ArrayList<>();
    public static ArrayList<String> liked_ids = new ArrayList<>();
    ArrayList<String> playlist_likes = new ArrayList<>();
    static ArrayList<String> ids = new ArrayList<>();
   // public static ArrayAdapter<String> arrayAdapter;
   CustomAdapter_Dis arrayAdapter = new CustomAdapter_Dis(playlist_data,this);
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.discover_page);

        dynamic = (ListView) findViewById(R.id._dynamic);
        topTenBtn = (Button) findViewById(R.id.toptenBtn);
        allBtn = (Button) findViewById(R.id.allBtn);

        dynamic.setAdapter(arrayAdapter);


        getPlaylists();
        getLikedPlaylists();

        topTenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Const.SERVER + "playlist/top-likes";
                //change list to top top
                liked_playlists.clear();
                liked_ids.clear();
                liked_playlist_objs.clear();
                playlist_data.clear();
                playlist_objs.clear();

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String requestResponse) {
                        Log.d("RESP", requestResponse.toString());

                        try {
                            JSONArray arr = new JSONArray(requestResponse);

                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject temp = arr.getJSONObject(i);
                                String playlist_name = temp.getString("playlistName");
                                String likes = temp.getString("likes");
                                String id = temp.getString("playlistId");
                                String data = playlist_name.concat("=");
                                data = data.concat(likes);
                                HashMap<String, String> temp_hash = new HashMap<>();
                                temp_hash.put(id, playlist_name);
                                playlist_objs.add(temp_hash);
                                playlist_data.add(data);
                                ids.add(id);
                            }

                            arrayAdapter.notifyDataSetChanged();
                            getLikedPlaylists();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                AppController.getInstance().addToRequestQueue(request, "search resp");


            }
        });

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlaylists();
                getLikedPlaylists();
            }


        });
    }

    private void getLikedPlaylists() {
        String url = Const.SERVER + "playlist/liked_playlists";
        liked_playlists.clear();
        liked_ids.clear();
        liked_playlist_objs.clear();
        StringRequest request = new StringRequest( Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String requestResponse) {
                Log.d("RESP", requestResponse.toString());

                try {
                    JSONArray arr = new JSONArray(requestResponse);

                    for(int i = 0; i < arr.length(); i++){
                        JSONObject temp = arr.getJSONObject(i);
                        String playlist_name = temp.getString("playlistName");
                        Log.d("name", playlist_name);
                        liked_playlists.add(playlist_name);
                        HashMap<String,String> temp_hash = new HashMap<>();
                        temp_hash.put(temp.getString("playlistId"), playlist_name);
                        liked_playlist_objs.add(temp_hash);
                        liked_ids.add(temp.getString("playlistId"));

                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", GlobalUser.getUsername());


                return params;
            }
        };;

        AppController.getInstance().addToRequestQueue(request, "search resp");

    }

    private void getPlaylists() {
        String url = Const.SERVER + "playlist/get_all_playlists";
        playlist_data.clear();
        playlist_objs.clear();
        ids.clear();
        playlist_data.clear();
        playlist_objs.clear();
        StringRequest request = new StringRequest( Request.Method.GET,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String requestResponse) {
                Log.d("RESP", requestResponse.toString());

                try {
                    JSONArray arr = new JSONArray(requestResponse);

                    for(int i = 0; i < arr.length(); i++){
                        JSONObject temp = arr.getJSONObject(i);
                        String playlist_name = temp.getString("playlistName");
                        String likes = temp.getString("likes");
                        String id = temp.getString("playlistId");
                        String data = playlist_name.concat("=");
                        data = data.concat(likes);
                        HashMap<String,String> temp_hash = new HashMap<>();
                        temp_hash.put(id, playlist_name);
                        playlist_objs.add(temp_hash);
                        playlist_data.add(data);
                        ids.add(id);
                    }

                    arrayAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) ;

        AppController.getInstance().addToRequestQueue(request, "search resp");
    }

}
