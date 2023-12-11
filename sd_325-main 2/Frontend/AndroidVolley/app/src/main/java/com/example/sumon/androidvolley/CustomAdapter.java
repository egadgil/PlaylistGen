package com.example.sumon.androidvolley;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//custom adapter playlist makes it possible to customize list views in android studio
//now we can add buttons, images, ect to a list item, and handle all new changes with this class
public class CustomAdapter extends BaseAdapter implements ListAdapter {
   //the items this list holds
    private ArrayList<String> list = new ArrayList<String>();
    public boolean isPlaying = false;

    //constructor
    private Context context;public CustomAdapter(ArrayList<String> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int pos){
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos){
       // return list.get(pos).getId();
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.deleteBtn);
        ImageButton playPauseBtn = (ImageButton)view.findViewById(R.id.playPauseBtn);


        //delete button removes item from list and removes song from playlist in database
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
                String song_name = EditPlaylist.playlist_data.get(position).toString();
                String[] split = song_name.split("=");
                song_name = split[0].substring(1);
                Log.d("REMOVE", song_name);
                String url = Const.SERVER + "playlist/remove_song";

                String finalSong_name = song_name;
                StringRequest req = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String resp){
                        Log.d("resp", resp);
                        EditPlaylist.playlist_data.remove(position);

                        SavedPlaylist.getUserPlaylist();

                    }
                },  new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();
                    }
                }){
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<>();
                        params.put("playlist_id", EditPlaylist.playlist_id);
                        params.put("song_name", finalSong_name);

                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(req);

            }
        });

        playPauseBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                //toggle playing state
                //pause and play use same button for cleaner appearance
                isPlaying = !isPlaying;

                //grab mp3 link
                String url = Const.SERVER + "playlist/get_preview";

                StringRequest req = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String resp){

                        //response is link to audio file - if available
                        Log.d("resp", resp);

                        if(resp.contains("unavailable")){
                            //no preview
                            EditPlaylist.available = false;

                            playPauseBtn.setClickable(false);
                            playPauseBtn.setEnabled(false);

                            playPauseBtn.setBackgroundResource(R.color.gray_500);
                            isPlaying = false;

                        } else {
                            playPauseBtn.setClickable(true);
                            playPauseBtn.setEnabled(true);

                            EditPlaylist.available = true;
                            //toggle to playing
                            if(isPlaying){
                                EditPlaylist.mediaPlayer(resp);
                            } else {
                                EditPlaylist.mediaPlayerPause(resp);
                            }
                        }
                    }
                },  new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        error.printStackTrace();

                        String body = null;
                        //get status code here
                        String statusCode = String.valueOf(error.networkResponse.statusCode);
                        //get response body and parse with appropriate encoding
                        if(error.networkResponse.data!=null) {
                          try {
                            body = new String(error.networkResponse.data,"UTF-8");
                        //  Log.d("ERRRR", body);

                        } catch (UnsupportedEncodingException e) {
                          e.printStackTrace();
                        }
                        }
                    }
                }){
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        Map<String, String> temp = EditPlaylist.playlist_data.get(position);
                        String[] split = temp.toString().split("=");
                        String name = split[0].replace("{","");
                        params.put("song_name", name);
                        Log.d(String.valueOf(position),name);
                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(req);


            }
        });

        return view;
    }
}
