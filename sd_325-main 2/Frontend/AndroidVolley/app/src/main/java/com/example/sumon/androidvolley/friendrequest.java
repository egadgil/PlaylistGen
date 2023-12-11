package com.example.sumon.androidvolley;

import static com.example.sumon.androidvolley.GlobalUser.user;
import static com.example.sumon.androidvolley.R.id.latest;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.PingFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.handshake.ServerHandshakeBuilder;
import org.json.JSONObject;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//make screen for pending and sending


public class friendrequest extends AppCompatActivity implements View.OnClickListener {

    private Button btnNo, btnAdd, PendBtn;
    private SearchView searchView;
    private String receiverUser;


    //users = users();

    //  searchView = (SearchView) findViewById(R.id.searchView);
    private ListView list_of_users; // = (ListView) findViewById(R.id.list);
    private TextView textView; // = (TextView) findViewById(R.id.user);
    public static TextView latest;
    private String senderUsername;
    private String URL_Friend_send = Const.SERVER + "users/send-friend-request";
    private String URL_Friend_user = Const.SERVER + "users/friend-requests";
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
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.friendrequest);
        getUsers();

        activity = this;
        list_of_users = (ListView) findViewById(R.id.list);
        searchView = (SearchView) findViewById(R.id.searchView);
        //latest = (TextView) findViewById(R.id.latest);
       // btnNo = (Button) findViewById(R.id.NoBtn);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        //PendBtn = (Button) findViewById(R.id.PendBtn);
        list_of_users = (ListView) findViewById(R.id.list);
       textView = (TextView) findViewById(R.id.user);

        //??
       // btnGit = (Button) findViewById(R.id.btnGit);

        btnNo.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
       // PendBtn.setOnClickListener(this);


       // btnGit.setOnClickListener(this);

        //Intent inn1=getIntent();
       // inn1=new Intent(friendrequest.this, pending.class);
      //  startActivity(inn1);
      //  arrayAdapter = new ArrayAdapter<String>(
       //         this,
        //        android.R.layout.simple_list_item_1,
        //        filterList);

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
                senderUsername = listItem.toString();
                textView.setText(senderUsername);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

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
       // String sendurl = "/send-friend-request";
        // String userurl = "http://friend-requests/{username}";
        //String accepturl = "http://accept-friend-request";

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, sendurl, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        textView.setText("Sent to: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("rerorr", error.toString());
//
//
//                    }
//                });
//
//        String userurl = "http://friend-requests/{username}";
//        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
//                (Request.Method.GET,  userurl,null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        textView.setText("Request from: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("rerorr", error.toString());
//
//
//                    }
//                });
        //search view is not initialized
        /**
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
*/


    /**
     * @getUsers get all users
     * @onResponse creates a full list
     * logging list for which user is in that list. if empty, [""]. If not, [user1, user2..]
     * *@onErrorResponse detect error in responses for requests*
     */



    public List<String> getUsers() {


        List<String> usernames = new ArrayList<String>();
        StringRequest req = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

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


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 Log.d("ERR", error.getMessage());
//                Log.d("rr", error.toString());
//                Log.d("Request URL", URL_Friend_accept);
//                if (error.networkResponse != null) {
//                    int statusCode = error.networkResponse.statusCode;
//                    Log.d("Error.Response", "Unexpected response code " + statusCode);
//                } else {
//                    Log.d("Error.Response", "Unexpected error with no response code");
//                }

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



//    StringRequest str = new StringRequest(Request.Method.POST, URL_Friend_send, new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            Log.d("resp",response);
//            Log.d("adding", senderUsername);
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            Log.d("ERROR", error.toString());
//
//        }
//    }){
//        //put sender and reciever username
//        @Override
//        public Map<String, String> getParams() throws AuthFailureError {
//            Map<String, String> params = new HashMap<>();
//            params.put("senderUsername", GlobalUser.getUsername());
//            params.put("receiverUsername", recieverUser);
//            return params;
//        }
//    };

       //  AppController.getInstance().addToRequestQueue(str, "search resp");


    @Override

    public void onClick(View view) {
      //  add = "false";
        switch (view.getId()) {
            case R.id.btnFriendRequest:
            startActivity(new Intent(friendrequest.this,
                    ProfilePageActivity.class));
            break;
//            case R.id.btnAdd:
//                senderUser = "true";
//                break;
//            case R.id.NoBtn:
//                senderUser = "false";
//                break;
            case R.id.btnAdd:
                senderUsername = "true";

                // Make the POST request to accept the friend request
                StringRequest str = new StringRequest(Request.Method.POST, URL_Friend_send, new Response.Listener<String>() {
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
                        params.put("receiverUsername", senderUsername);
                        return params;
                    }
                };

                // Add the request to the request queue
                AppController.getInstance().addToRequestQueue(str, "send_friend_request");
                break;



            // Add cases for other buttons if needed
        }
    }
           // case R.id.btnGit:
           //     startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://git.las.iastate.edu/cs309/fall2023/sd_325/-/tree/main")));
           //     break;
        }
      //  try {
      //      WebSocketManager.getInstance().sendMessage(addUser + ":" + add);
      //      Log.d("socket", "block sent");
      //  } catch (Exception e) {
      //      Log.d("Exception message", e.getMessage().toString());



        //connectWS();


        // StringRequest str = new StringRequest(Request.Method.POST, URL_BAN, new Response.Listener<String>() {
          /*  @Override
            public void onResponse(String response) {
                Log.d("resp", response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());

            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", banUser);
                params.put("ban", block);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(str, "search resp");
    }
    }
*/


