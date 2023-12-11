package com.example.sumon.androidvolley;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sumon.androidvolley.app.AppController;
import com.example.sumon.androidvolley.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/** @author: gigi harrabi */
public class GlobalUser {

   public static GlobalUser user = new GlobalUser();

   public static boolean SPOTIFY_LOGGED_IN = false;

    private String username;
    //string to store UITheme
    private String UITheme;
    //string full name
    private String name;
    //string to store email
    private String email;
    //string to store membership type
    private String membershipType;
    //sting to store avatar
    private String Avatar;
    //array list to store saved playlists
    private ArrayList<String> savedPlaylists;


    public static void  setUserName(String name){
        user.username = name;
    }
    public static void setUITheme(String uitheme){
       user.UITheme = uitheme;
    }
    public static void setMembershipType(String type) { user.membershipType = type;}

    public static void setName(String name){
       user.name = name;
    }

    public static void setEmail(String email){
        user.email = email;
    }

    public static void setAvatar(String avatar){
        user.Avatar = avatar;
    }
    public static void setSavedPlaylists(ArrayList<String> playlists){
        user.savedPlaylists = playlists;
    }
    /**get username, ui theme, name, email, avatar */
    public static String getUsername(){
        return user.username;
    }

    public static String getUITheme(){
        return user.UITheme;
    }
    public static String getName(){
        return user.name;
    }
    public static String getEmail(){
        return user.email;
    }
    public static String getAvatar(){
        return user.Avatar;
    }
    /**what is their membership status*/
    public static String getMembershipType(){return user.membershipType;}
    /**get saved palylist string*/
    public static ArrayList<String> getSavedPlaylists(){
        return user.savedPlaylists;
    }

    /**log out and make all info null*/
    public static void logout(){
        user.username = null;
        user.UITheme = null;
        user.name = null;
        user.email = null;
        user.membershipType = null;
        user.Avatar = null;
        user.savedPlaylists = null;
        themeUtils.cTheme = null;
        themeUtils.restartWithTheme = true;
    }
    /** set url and log into server. make a request and use listener for response
     * change if err on local host check IPv4 address*/
    public static void refreshUser(String username) throws JSONException{
        //change
        //if err on local host check IPv4 address
        String url = Const.SERVER + "users/set_user_info";

        Log.d("HEREHREH",username);
        StringRequest user = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {

            @Override
            /** use json obj to set user name, name, avatar, ui themem, membership type
             *
             *
             * and make ui theme plain when null*/
            public void onResponse(String response) {
                Log.d("HERE","HERE");
                Log.d("test", response);

                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("resp",obj.toString());
                    String username = obj.getString("username");
                  //  Log.d("USER",username);
                    setUserName(username);
                    setUserName(obj.getString("username")); // cant be null
                    setName(obj.getString("fullName"));
                    setAvatar(obj.getString("profilePicture"));
                    String uiTheme = obj.getString("uiThemeColor");

                    setUITheme(uiTheme);
                    setMembershipType(obj.getString("membershipType"));

                    /** Log.d("AHH", getUITheme());*/

                    if(getUITheme() == null || obj.getString("uiThemeColor") == "null"){
                        setUITheme("plain");
                        Log.d("SET", "PLANE");
                    }

                } catch (JSONException e) {

                    Log.d("MYAPP", "unexpected JSON exception in global user", e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Log.d("ERR",error.toString());
            }
        }){
            /** map user name and log it*/
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",  username);
                Log.d("USEr", username);

                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(user, "REQ");

        Log.d("END","ED");


    }
}
