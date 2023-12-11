package com.example.sumon.androidvolley;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.View.OnClickListener;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddSongActivity extends AppCompatActivity implements OnClickListener {
    private Button btnSpotify, btnPlaylist;
    private SearchView searchTxt;

    private static ListView list;
    private String query;
    public static String song_name, artist_name, spotify_id;

    public static ArrayList<String> queries = new ArrayList<>();
    public static ArrayList<String> ids = new ArrayList<>();
    public static ArrayList<String> artists = new ArrayList<>();
    public static String id;
    public static ArrayAdapter<String> arrayAdapter;
    public static String type = "song";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.add_song);

        SpotifyService.getToken();

        btnSpotify = (Button) findViewById(R.id.btnSpotify);
        btnPlaylist = (Button) findViewById(R.id.btnPlaylist);
        searchTxt = (SearchView) findViewById(R.id.search);
        list = (ListView) findViewById(R.id.listView);

        btnSpotify.setOnClickListener(this);
        btnPlaylist.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                queries);

        /** display list*/
        list.setAdapter(arrayAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /** get position of list id and query in string form then log
             * Clear queries, add, make array adapter notify then get playlist and show
             * */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = list.getItemAtPosition(i);
                id = ids.get(i);
                query = o.toString();

                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), type + ": " + o.toString(),
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction("Add", new View.OnClickListener(){


                            @Override
                            public void onClick(View view) {
                                //clears lists
                                searchTxt.setQuery(o.toString(), false);
                                queries.clear();
                                queries.add(query);
                                song_name = query;
                                spotify_id = id;
                                artist_name = artists.get(i);

                                //updates playlist in editPlaylist screen
                                HashMap<String, String> tempMap = new HashMap<>();
                                Log.d("ADDING", song_name + " by " + artist_name);
                                tempMap.put(song_name,artist_name);
                                EditPlaylist.playlist_data.add(tempMap);

                                EditPlaylist.list_items.clear();

                                //recreates playlist to reflect change
                                EditPlaylist editPlaylist = new EditPlaylist();
                                editPlaylist.createList();

                                arrayAdapter.notifyDataSetChanged();

                                //adds song to playlist in database
                                addSong();


                            }
                        });
                snackbar.show();
            }
        });

        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query = s;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                query = s;
                return false;
            }
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btnSpotify:
                Log.d("TYPE", type);

                SpotifyService.searchId(type,query,"add_song");
                break;
            case R.id.btnPlaylist:



                startActivity(new Intent(AddSongActivity.this,
                        EditPlaylist.class));
                break;
        }

    }

    public void addSong(){
        String url = Const.SERVER + "playlist/add_song";

        StringRequest req = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String resp){
                Log.d("added", resp);


                //clears list after song has been added
                artists.clear();
                queries.clear();
                ids.clear();

                arrayAdapter.notifyDataSetChanged();
            }
        },  new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();

                //clears list in case of response error
                artists.clear();
                queries.clear();
                ids.clear();
                arrayAdapter.notifyDataSetChanged();

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("playlist_id", EditPlaylist.playlist_id);
                params.put("song_name", song_name);
                params.put("spotify_id", spotify_id);
                params.put("artist_name", artist_name);

                Log.d("params", params.toString());


                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(req);
    }
}
