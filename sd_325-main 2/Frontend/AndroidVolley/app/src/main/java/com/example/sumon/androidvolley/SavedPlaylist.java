package com.example.sumon.androidvolley;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

public class SavedPlaylist extends AppCompatActivity {
    private static ListView list;

    public static ArrayAdapter<String> arrayAdapter;
    public static ArrayList<String> user_playlists_ids = new ArrayList<>();
    public static ArrayList<String> user_playlists_names = new ArrayList<>();
    public List<Map<String, String>> playlist_data = new ArrayList<Map<String,String>>();
    public static JSONArray playlists = new JSONArray();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.saved_playlist);

        //first make request for user playlist
        list = (ListView) findViewById(R.id.list);



        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
               user_playlists_ids
        );
        list.setAdapter(arrayAdapter);




        //get all playlists created by a user
        getUserPlaylist();



        //view specific playlist
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /** get position of list id and query in string form then log
             * Clear queries, add, make array adapter notify then get playlist and show
             * */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = list.getItemAtPosition(i);
                //id = ids.get(i);
                //query = o.toString();

                EditPlaylist.playlist_id = user_playlists_ids.get(i);

                //clears playlist_data and list_items in case another playlist has been viewed
                EditPlaylist.playlist_data.clear();
                EditPlaylist.list_items.clear();
                EditPlaylist.ids.clear();

                try{
                //iterate thru each playlist
                    JSONObject playlist = playlists.getJSONObject(i);
                    Log.d("Playlist " + i, playlist.toString());

                    EditPlaylist.playlist_id = String.valueOf(playlist.getInt("playlistId"));

                    EditPlaylist.playlist_name = playlist.getString("playlistName");

                    JSONArray songArr = playlist.getJSONArray("playlist_songs");

                    EditPlaylist.is_public = playlist.getBoolean("is_public");

                    Log.d("get boolean", String.valueOf(playlist.getBoolean("is_public")));
                    for (int j = 0; j < songArr.length(); j++) {

                        JSONObject tempSong = songArr.getJSONObject(j);
                        Log.d("Song " + j, tempSong.getString("name")
                                + " by " + tempSong.getString("artistName"));

                        EditPlaylist.ids.add(tempSong.getString("spotifyId"));

                        HashMap<String, String> tempMap = new HashMap<>();
                        tempMap.put(tempSong.getString("name"),tempSong.getString("artistName"));
                        EditPlaylist.playlist_data.add(tempMap);
                        EditPlaylist.ids.add("spotifyId");

                        EditPlaylist editPlaylist = new EditPlaylist();
                        editPlaylist.createList();
                    }

                    //Log.d("ids", EditPlaylist.ids.toString());

                    //start edit playlist activity
                    startActivity(new Intent(SavedPlaylist.this,
                            EditPlaylist.class));


                } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
            }
        });

    }

    static void getUserPlaylist() {

        String url = Const.SERVER + "playlist/user_playlists" + "?username=" + GlobalUser.getUsername();
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", GlobalUser.getUsername());

        StringRequest request = new StringRequest( url, new Response.Listener<String>() {
            @Override
            public void onResponse(String requestResponse) {
                Log.d("RESP", requestResponse.toString());

                ArrayList<String> list_info;
                try {
                    playlists = new JSONArray(requestResponse);
                    list_info = new ArrayList<>();

                    //iterate thru each playlist
                    for (int i = 0; i < playlists.length(); i++) {
                        JSONObject playlist = playlists.getJSONObject(i);
                        Log.d("Playlist " + i, playlist.toString());
                        String info = "Playlist ID " + String.valueOf(playlist.getInt("playlistId"))
                                + "  Playlist Name " + playlist.getString("playlistName");

                        list_info.add(info);



                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                user_playlists_ids.clear();

                for (String str : list_info) {
                    Log.d("INFO", str);
                    user_playlists_ids.add(str);
                }

                arrayAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(request, "search resp");
    }

}
