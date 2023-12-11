package com.example.sumon.androidvolley;

import static com.example.sumon.androidvolley.GlobalUser.user;
import static com.example.sumon.androidvolley.PlaylistGenerator.queries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class pending extends AppCompatActivity implements View.OnClickListener {

    private Button btnReject, btnAccept, PendBtn;
    private SearchView searchView;
    private String recieverUser;


    //users = users();

    //  searchView = (SearchView) findViewById(R.id.searchView);
    private ListView list_of_users;
    private TextView textView;
    public static TextView latest;
    private String senderUser;
    private String URL_Friend_send = Const.SERVER + "users/send-friend-request";
    private String URL_Friend_user = Const.SERVER + "users/friend-requests/{username}";
    private String URL_Friend_accept = Const.SERVER + "users/accept-friend-request";
    // private String WEB_S = Const.SERVER + "notif/admin";

    public static String message;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> filterList = new ArrayList<>();

    private ArrayAdapter<String> arrayAdapter;
    private final String URL = Const.SERVER + "users/get_usernames";
    private List<String> users;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // super.onCreate(savedInstanceState)
      //  friendrequest.onActivityCreateSetTheme(this);

        setContentView(R.layout.pending);
        getUsers();
        searchView = (SearchView) findViewById(R.id.searchView);
        list_of_users = (ListView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.user);
        activity = this;


        //latest = (TextView) findViewById(R.id.latest);
        //btnReject = (Button) findViewById(R.id.btnReject);
        //PendBtn = (Button) findViewById(R.id.PendBtn);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        //??
        // btnGit = (Button) findViewById(R.id.btnGit);
       // BPending.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        // btnGit.setOnClickListener(this);

      //  PendBtn.setOnClickListener(this);


      //  void pending() {

            // create an instance of the
            // intent of the type image
         //   Intent i = new Intent();

         //   i.setAction(Intent.ACTION_GET_CONTENT);

            // pass the constant to compare it
            // with the returned requestCode
           // startActivityForResult(Intent.createChooser(i, "Pending requests"), URL_Friend_user);
      //  }


        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                filterList);


        list_of_users.setAdapter(arrayAdapter);

        list_of_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**ban a user by getting the position of the user in an array list then  set ban
             * Submit text query and update changes
             * get string array list of users*/

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object listItem = list_of_users.getItemAtPosition(i);
                //id = ids.get(i);
                //query = o.toString();
                Log.d("o", listItem.toString());
                recieverUser = listItem.toString();
                textView.setText(recieverUser);

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

           @Override
           public boolean onQueryTextSubmit(String s) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               filterSearch(s);
               return false;
           }
       }
        );

    }

    /**
     * @getUsers get all users
     * @onResponse creates a full list
     * logging list for which user is in that list. if empty, [""]. If not, [user1, user2..]
     * *@onErrorResponse detect error in responses for requests*
     */
    public List<String> getUsers() {


        List<String> usernames = new ArrayList<String>();
        StringRequest req = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //usernames = response;
                Log.d("USERS", response.toString());
                // JSONObject test = response;

                //parsing request
                String str = response;
                //replacing characters
                str = str.replace("[", "");
                str = str.replace("]", "");
                str = str.replace("\"", "");
                String[] read_list = str.split(",");

                //read thru list and add to arraylist monitored by array adapter
                for (int i = 0; i < read_list.length; i++) {
                    if (read_list[i] == null) {
                        list.add("");
                    } else {

                        //a a list to store filtered objects
                        list.add(read_list[i]);
                        filterList.add(read_list[i]);
                    }
                    Log.d("LIST" + i, list.get(i));
                }

                arrayAdapter.notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("rr", error.toString());

                // Log.d("ERROR", body.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, "REQ");

        return usernames;
    }

    /**
     * @filterSearch search for user and set the data and then send notif.
     * @onClick make a boolean case statement of blocked and unblocked users; open the given link with new intent
     * When user banned, send message.
     * @onResponse sends request to ban by searching user name.
     */

    public void filterSearch(String s) {
        filterList.clear();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(s)) {
                filterList.add(list.get(i));
            }
        }

        arrayAdapter.notifyDataSetChanged();

    }

    @Override

    public void onClick(View view) {

            switch (view.getId()) {

                case R.id.btnAccept:
                    senderUser = "true";
                    String URL_Friend_username = Const.SERVER+"user/friend-requests"+user;
                    // Make the POST request to accept the friend request
                    StringRequest str = new StringRequest(Request.Method.POST, URL_Friend_accept, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("resp", response);

                            // Handle the response after accepting the friend request
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ERROR", error.toString());

                            // Handle error
                        }
                    }) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("senderUsername", GlobalUser.getUsername());
                            params.put("receiverUsername", recieverUser);
                            return params;
                        }
                    };

                    // Add the request to the request queue
                    AppController.getInstance().addToRequestQueue(str, "accept_friend_request");
                    break;

//                case R.id.btnReject:
//                    senderUser = "false";
//                    // Handle rejection logic if needed
//                    break;



                // Add cases for other buttons if needed
            }


//                case R.id.btnAccept:
//                    senderUser = "true";
//                    break;
//                case R.id.btnReject:
//                    senderUser = "false";
//                    break;
                // case R.id.btnGit:
                //     startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://git.las.iastate.edu/cs309/fall2023/sd_325/-/tree/main")));
                //     break;
              }
            }



