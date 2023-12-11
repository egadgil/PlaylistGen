package com.example.sumon.androidvolley;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View.OnClickListener;
import android.widget.Toast;
/** @author: gigi harrabi */
public class PlaylistGenerator extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, OnClickListener {

    private static ListView list;
    public static ArrayList<String> queries = new ArrayList<>();
    public static ArrayList<String> ids = new ArrayList<>();
    public static String id;
    public static ArrayAdapter<String> arrayAdapter;
    public static String type = "song";
    private RadioGroup radioGroup;
    //private RadioButton radioButton;
    private Button btnSong, btnArtist, btnSpotify;
    private SearchView searchTxt;

    private static EditText playlistName;
    private String query;
    private TextView txtHeader;
    private static EditText lengthText;
    private TextView lengthStaticText;
    private static String lengthSend = "20";
    /** make activity theme and view artist song*/
    @Override
    /** get spotify token
     * make id button for radio group, artist, song, apotify, search, list
     * make queries from list
     * display list
     * make listener for adapter
     * sets the array adapter to the list
     * */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.song_artist_request);

        SpotifyService.getToken();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnArtist = (Button) findViewById(R.id.radio_artist);
        btnSong = (Button) findViewById(R.id.radio_song);
        btnSpotify = (Button) findViewById(R.id.btnSpotify);
        searchTxt = (SearchView) findViewById(R.id.search);
        list = (ListView) findViewById(R.id.listView);
        playlistName = (EditText) findViewById(R.id.playlistName);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        lengthText = (EditText) findViewById(R.id.lengthOfPlaylistTxt);
        lengthStaticText = (TextView) findViewById(R.id.lengthTxt);

        playlistName.setVisibility(View.INVISIBLE);
        if(GlobalUser.getMembershipType() == "null" || GlobalUser.getMembershipType() == "Unpaid Member") {
            lengthText.setVisibility(View.INVISIBLE);
            lengthStaticText.setVisibility(View.INVISIBLE);
        }

        radioGroup.check(btnSong.getId());


        //set list header to invisible
        // txtHeader.setVisibility(View.INVISIBLE);
        btnSpotify.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                queries);

        /** display list*/
        list.setAdapter(arrayAdapter);
        /** make listener for adapter*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            /** get position of list id and query in string form then log
             * Clear queries, add, make array adapter notify then get playlist and show
             * */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = list.getItemAtPosition(i);
                id = ids.get(i);
                Log.d("ID", id);
                query = o.toString();

                Log.d("CLICLICL", "CLIK");
                Log.d("o", o.toString());
                /**  */
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), type + ": " + o.toString(),
                                Snackbar.LENGTH_INDEFINITE)
                        .setAction("Confirm", new View.OnClickListener(){


                            @Override
                            /**make call to backend*/
                            public void onClick(View view) {
                                //searchTxt.set
                                searchTxt.setQuery(o.toString(), false);
                                queries.clear();
                                queries.add(query);
                                arrayAdapter.notifyDataSetChanged();

                                //make call to backend
                                lengthSend = String.valueOf(lengthText.getText());
                                if(Integer.parseInt(lengthSend) > 100)
                                {
                                    lengthText.setText("100");
                                    lengthSend = "100";
                                }
                                Log.d("SENDING LENGTH", lengthSend);
                                getPlaylist();

                            }
                        });
                snackbar.show();
            }
        });


        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener(){

                    /**a button has been clicked
                     *  clear matches after change
                     *  which button has been selected
                     *  log song, artist and their types
                     *  look at text
                     * sets type to song is song radio button has been picked
                     * */
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId){

                        RadioButton radioButton = (RadioButton)group.findViewById(checkedId);

                        if(radioButton.getId() == btnSong.getId())
                        {
                            type = "song";
                            Log.d("type",type);
                        }
                        else if (radioButton.getId() == btnArtist.getId())
                        {
                            type = "artist";
                            Log.d("type",type);
                        }
                        Log.d("Button Checked", String.valueOf(radioButton.getId()));
                    }
                }
        );

        searchTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            /** true or false? is the txt there*/
            public boolean onQueryTextSubmit(String s) {
                query = s;
                return false;
            }

            @Override
            /** is txt changed*/
            public boolean onQueryTextChange(String s) {
                query = s;
                return false;
            }
            /** has it been changing*/
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    /**
     * searches for type song or artist then logged and searches for it
     * here we set the type to artist or song - this will be needed when making
     //later calls to SpotifyAPI
     //query is our search parameters
     //searchId() will apply these changes to the search parameters and return
     //matching candidates*/
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.btnSpotify:
                Log.d("TYPE", type);

                SpotifyService.searchId(type,query,"playlist_gen");
                break;
        }
    }
    /**postman url
     *
     *would send to backend at genererate playlist endpoint
     *with parameters: username, search type, search id
     *would receive a json object back
     * request a json obj of songs and wait for response
     *return into array of songs as response
     */
    public static void getPlaylist(){



        String url = Const.SERVER + "playlist/gen";
        StringRequest songs = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            JSONArray arr;
            @Override
            public void onResponse(String response) {
               // txtHeader.setVisibility(View.INVISIBLE);
                //playlistName.setVisibility(View.VISIBLE);
                Log.d("RESP", response.toString());
                //HashMap<String,String> song_artist_map = new HashMap<>();
                List<HashMap<String,String>> list_songs = new ArrayList<>();

                try {
                    JSONObject playlist = new JSONObject(response);
                    JSONArray songs = (JSONArray) playlist.getJSONArray("playlist_songs");
                    Log.d("songs", songs.toString());

                    for(int i = 0; i < songs.length(); i++){

                        HashMap<String,String> song_artist_map = new HashMap<>();
                        JSONObject temp = new JSONObject();
                        temp = songs.getJSONObject(i);


                        song_artist_map.put(temp.getString("name"), temp.getString("artistName"));

                        list_songs.add(song_artist_map);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                queries.clear();
                queries.add("New Playlist!");

                for(int j = 0; j < list_songs.size(); j ++){
                    HashMap<String, String> temphash  = new HashMap<>();
                    temphash = list_songs.get(j);
                    String str = temphash.toString();
                    str.replace("}","");
                    str.replace("{","");
                    String[] list_str = str.split("=");
                    queries.add(list_str[0].replace("{",""));
                }


                arrayAdapter.notifyDataSetChanged();


            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("ERR", "Issue connecting to spotify search" + error.getMessage());
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
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("type", type);
                params.put("query", id);
                params.put("username", GlobalUser.getUsername());
                params.put("playlist_name", GlobalUser.getUsername() +"'s playlist");
                params.put("playlist_length", lengthSend);
                return params;
            }
        };

        //50 second time out outrageous
        //but the backend method takes at least 30 to execute
        songs.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        AppController.getInstance().addToRequestQueue(songs, "search resp");



    }


}


