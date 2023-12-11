package com.example.sumon.androidvolley;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapter_Dis extends BaseAdapter implements ListAdapter {
    private static ArrayList<String> list = new ArrayList<String>();
    static ImageButton heart;
    public static boolean liked;

    public static String playlist_id;

    //constructor
    private Context context;
    public CustomAdapter_Dis(ArrayList<String> list, Context context){
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
            view = inflater.inflate(R.layout.custom_list_item_dc, null);
        }

        String pos = list.get(position);
        String[] data = pos.split("=");
        for(int j = 0; j<data.length;j++){
            Log.d(String.valueOf(j), data[j]);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(data[0]);
        TextView likes = (TextView) view.findViewById(R.id.likes);
        likes.setText(data[1]);

        //Handle buttons and add onClickListeners
       // ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.deleteBtn);
       // ImageButton playPauseBtn = (ImageButton)view.findViewById(R.id.playPauseBtn);
        heart = (ImageButton)view.findViewById(R.id.heart);


        heart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("list", String.valueOf(list));
                Log.d("position", String.valueOf(position));

                String url = Const.SERVER + "playlist/like";

                if(checkLike(position, list)){
                    url = Const.SERVER + "playlist/remove_like";
                } else {
                    url = Const.SERVER + "playlist/like";
                }
                Log.d("checked", String.valueOf(liked));


                String finalUrl = url;
                StringRequest req = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String resp){
                        Log.d("playlist liked", resp);


                        String pos = list.get(position);
                        String[] arr = pos.split("=");
                        int likes = parseInt(arr[1]);
                        if(finalUrl.contains("remove")){
                            likes--;
                        } else {
                            likes++;
                        }
                        String data_new = arr[0].concat("=");
                        list.set(position, data_new.concat(String.valueOf(likes)));
                        notifyDataSetChanged();

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
                        params.put("playlistId", playlist_id);
                        params.put("username", GlobalUser.getUsername());

                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(req);

            }
        });



        //there are no custom buttons for this class
        //a list item here will display a playlist name and its likes
        //clicking on an item will open up a view of the playlist
        //- this does not change functionality from regular adapter
        return view;
    }



    static boolean checkLike(int position, ArrayList<String> list){
        if(DiscoverPage.liked_playlists.size() != 0){
            Log.d("Check liked list", list.toString());
            Log.d("Like_playlist_objs", DiscoverPage.liked_playlist_objs.toString());
            Log.d("liked_playlists", String.valueOf(DiscoverPage.liked_playlists));
            Log.d("liked_ids", String.valueOf(DiscoverPage.liked_ids));
            Log.d("ids", String.valueOf(DiscoverPage.ids));

            String[] search_T = list.get(position).split("=");
            String search = search_T[0];

           // String[] playlist_temp = DiscoverPage.liked_playlist_objs.get(position).toString().split("=");
            String[] playlist_temp = list.get(position).toString().split("=");
            Log.d("arr", Arrays.toString(playlist_temp));

            playlist_id = DiscoverPage.ids.get(position);
            Log.d("playlist_id", playlist_id.toString());

            //remove id from liked list
            if(DiscoverPage.liked_ids.contains(playlist_id)){
                DiscoverPage.liked_playlists.remove(playlist_id);
                DiscoverPage.liked_ids.remove(playlist_id);

                return true;
            } else {
                DiscoverPage.liked_playlists.add(search);
                DiscoverPage.liked_ids.add(playlist_id);
                return false;
            }
        }
        return false;
    }
}
