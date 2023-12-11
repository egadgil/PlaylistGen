package com.example.sumon.androidvolley;

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

import org.java_websocket.handshake.ServerHandshake;
/** @author: gigi harrabi */
public class AdminDashActivity extends AppCompatActivity implements View.OnClickListener, WebSocketListener, org.java_websocket.WebSocketListener {

    private Button btnBlock, btnUnblock, btnGit, NoBtn, addBtn;
    private SearchView searchView;
    private String block;
    private ListView list_of_users;
    private TextView textView;
    public static TextView latest;
    private String banUser;
    private String URL_BAN = Const.SERVER + "users/set_ban";
    private String WEB_S = Const.SERVER + "notif/admin";

    public static String message;

    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> filterList = new ArrayList<>();

    private ArrayAdapter<String> arrayAdapter;
   private final String URL = Const.SERVER + "users/get_usernames";
    private List<String> users;
   public static Activity activity;
    /** creates and sets up activity
     * search for users in the listed text. Use block or unblock button using a listener
     * make an array list of users stored and set
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.admin_dash);
        getUsers();

        activity = this;

        connectWS();
        //users = users();

        searchView = (SearchView) findViewById(R.id.searchView);
        list_of_users = (ListView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.user);
        latest = (TextView) findViewById(R.id.latest);
        btnBlock = (Button) findViewById(R.id.blockBtn);
        btnUnblock = (Button) findViewById(R.id.btnUnblock);
        //??
        btnGit = (Button) findViewById(R.id.btnGit);

        btnBlock.setOnClickListener(this);
        btnUnblock.setOnClickListener(this);
        btnGit.setOnClickListener(this);



        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                filterList);


        list_of_users.setAdapter(arrayAdapter);

        list_of_users.setOnItemClickListener(new AdapterView.OnItemClickListener(){
/**ban a user by getting the position of the user in an array list then  set ban
 * Submit text query and update changes
 * get string array list of users*/

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object listItem = list_of_users.getItemAtPosition(i);
                //id = ids.get(i);
                //query = o.toString();
                Log.d("o", listItem.toString());
                banUser = listItem.toString();
                textView.setText(banUser);

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

    /** @getUsers get all users
     *
     *@onResponse creates a full list
     * logging list for which user is in that list. if empty, [""]. If not, [user1, user2..]
     * *@onErrorResponse detect error in responses for requests*
     * */
    public List<String> getUsers() {


        List<String> usernames = new ArrayList<String>();
        StringRequest req = new StringRequest(Request.Method.GET,URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //usernames = response;
                Log.d("USERS", response.toString());
               // JSONObject test = response;

                //parsing request
                String str = response;
                //replacing characters
                str = str.replace("[","");
                str = str.replace("]", "");
                str = str.replace("\"","");
                String[] read_list = str.split(",");

                //read thru list and add to arraylist monitored by array adapter
                for(int i = 0; i < read_list.length; i ++){
                    if(read_list[i] == null){
                        list.add("");
                    }else {

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
                // Log.d("ERR", error.getMessage());
                Log.d("rr", error.toString());

                // Log.d("ERROR", body.toString());
            }
        });

        AppController.getInstance().addToRequestQueue(req, "REQ");

        return usernames;
    }
    /** @filterSearch search for user and set the data and then send notif.
     * @onClick make a boolean case statement of blocked and unblocked users; open the given link with new intent
     * When user banned, send message.
     * @onResponse sends request to ban by searching user name.
     *
     */

    public void filterSearch(String s){
    filterList.clear();

        for(int i = 0; i < list.size(); i++){
            if(list.get(i).contains(s))
            {
                filterList.add(list.get(i));
            }
        }

        arrayAdapter.notifyDataSetChanged();

    }

    @Override

    public void onClick(View view) {
        block = "false";
        switch (view.getId()) {
            case R.id.blockBtn:
                block = "true";
            break;
            case R.id.btnUnblock:
                block = "false";
                break;
            case R.id.btnGit:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://git.las.iastate.edu/cs309/fall2023/sd_325/-/tree/main")));
                break;
        }
        try{
            WebSocketManager.getInstance().sendMessage(banUser + ":" + block);
            Log.d("socket","block sent");
        }catch(Exception e){
            Log.d("Exception message", e.getMessage().toString());

        }

        //connectWS();


        StringRequest str = new StringRequest(Request.Method.POST, URL_BAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resp",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("username", banUser);
                params.put("ban", block);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(str, "search resp");


    }
/** @makeToast toast is a specific android studio element and was used for testing here*/
    public static void makeToast(String mess) {
        Log.d("taot", "toast");
        Log.d("DDDD",mess);
        latest.setText("");
        latest.setText(message);
    }
    private void connectWS(){
        String serverURL = WEB_S;

        //establish websocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverURL);
        WebSocketManager.getInstance().setWebSocketListener(AdminDashActivity.this);

    }
/** open websocket, make a handshake which gets both local addresses and sends a signal,ping, if the network is 'awake'; if not awake then
 * close WS. If awake then send pong which is a reply saying 'yes, i am awake'*/
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = message;
            Log.d("MESS", s);
        });
            Log.d("THis", message);
            Toast.makeText(AdminDashActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = "---\nconnection closed by " + closedBy + "\nreason: " + reason;
        });

    }

    @Override
    public void onWebSocketError(Exception ex) {
        Log.d("ERRR", ex.getMessage());
    }

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
        return null;
    }

    @Override
    public void onWebsocketHandshakeReceivedAsClient(WebSocket conn, ClientHandshake request, ServerHandshake response) throws InvalidDataException {

    }

    @Override
    public void onWebsocketHandshakeSentAsClient(WebSocket conn, ClientHandshake request) throws InvalidDataException {

    }

    @Override
    public void onWebsocketMessage(WebSocket conn, String message) {

    }

    @Override
    public void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {

    }

    @Override
    public void onWebsocketOpen(WebSocket conn, Handshakedata d) {

    }

    @Override
    public void onWebsocketClose(WebSocket ws, int code, String reason, boolean remote) {

    }

    @Override
    public void onWebsocketClosing(WebSocket ws, int code, String reason, boolean remote) {

    }

    @Override
    public void onWebsocketCloseInitiated(WebSocket ws, int code, String reason) {

    }

    @Override
    public void onWebsocketError(WebSocket conn, Exception ex) {

    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {

    }

    @Override
    public PingFrame onPreparePing(WebSocket conn) {
        return null;
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {

    }

    @Override
    public void onWriteDemand(WebSocket conn) {

    }

    @Override
    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
        return null;
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
        return null;
    }
}
