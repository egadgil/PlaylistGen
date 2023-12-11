package com.example.sumon.androidvolley;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPlaylist extends AppCompatActivity {
    public static boolean available = true;
    private static ListView list;
    static MediaPlayer mediaPlayer;
    public TextView public_status;

    private EditText edit_name;

    private Button addSongsBtn, btnSaved;
    private ImageButton btnExport, btnPublic;

    public static String playlist_id;

    public static String playlist_name;

    public Button button;

    public String name;
    //Song : Artist
    public static List<Map<String, String>> playlist_data = new ArrayList<Map<String,String>>();

    public static ArrayList<String> list_items = new ArrayList<>();

    public static ArrayList<String> ids = new ArrayList<>();
    public static boolean is_public;

    CustomAdapter customAdapter = new CustomAdapter(list_items, this);

    public static void mediaPlayer(String audioUrl) {
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try{
             mediaPlayer.setDataSource(audioUrl);

                mediaPlayer.prepare();
                mediaPlayer.start();


        }catch(IOException e){
            e.printStackTrace();
        }

        Log.d("audio manager", "audio started playing...");
    }

    public static void mediaPlayerPause(String resp) {
        if(mediaPlayer.isPlaying()){
            //pause if playing
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            Log.d("audio manager", "pauing audio...");
        }
        else {
            Log.d("audio manager", "audio is not playing");
        }
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.edit_playlist);

        //runs through playlist data populated in SavedPlaylist Activity
        //displays <song name> by <artist name>
        createList();

        list = (ListView) findViewById(R.id.list);
        edit_name = (EditText) findViewById(R.id.editName);
        button = (Button) findViewById(R.id.button);
        btnSaved = (Button) findViewById(R.id.savedBtn);
        addSongsBtn = (Button) findViewById(R.id.btnAddSongs);
        btnExport = (ImageButton) findViewById(R.id.btnExport);
        btnPublic = (ImageButton) findViewById(R.id.publicBtn);
        public_status = (TextView) findViewById(R.id.publicTxt);


        //sets general playlist name
        if(playlist_name != null){
            edit_name.setText(playlist_name);
        }

        Log.d("is public", String.valueOf(is_public));
        if(!is_public){
            public_status.setText("Private");
        }
        else {
            public_status.setText("Public");
        }
        //sets custom adapter to list view
        list.setAdapter(customAdapter);


        //edit playlist name
        edit_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = String.valueOf(charSequence);
                //edit_name.setText(charSequence);
                playlist_name = name;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //toggle privacy
        btnPublic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                is_public = !is_public;

                String url = Const.SERVER + "playlist/set_public";

                StringRequest req = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String resp){
                        if(is_public){
                            public_status.setText("Public");
                        } else {
                            public_status.setText("Private");
                        }
                    }
                },  new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("playlist_id", EditPlaylist.playlist_id);
                        params.put("set_public", String.valueOf(is_public));

                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(req);

            }
        });

        //rename playlist
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                String url = Const.SERVER + "playlist/rename";

                StringRequest req = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String resp){
                        Log.d("resp", resp);
                    }
                },  new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("playlist_id", EditPlaylist.playlist_id);
                        params.put("name", name);

                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(req);


            }
        });


        //add song
        //opens AddSongActivity
        addSongsBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditPlaylist.this,
                        AddSongActivity.class));
            }
        });


        //takes user back to saved playlists
        btnSaved.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditPlaylist.this,
                        SavedPlaylist.class));            }
        });

        btnExport.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //open spotify login activity
                SLogin.playlist_id = playlist_id;
                startActivity(new Intent(EditPlaylist.this,
                        SLogin.class));

            }
        });


    }


    //fill list view from playlist_data
    public void createList() {
        Log.d("C", "CRESTE Playlist");
        list_items.clear();
        for(Map<String,String> map : playlist_data){
            list_items.add(map.toString().replace("{","")
                    .replace("}","")
                    .replace("=", " by "));
        }

        customAdapter.notifyDataSetChanged();
    }



}
