package com.example.sumon.androidvolley;

import android.app.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

/** @author: gigi harrabi */
public class themeUtils

{

    public static String cTheme;
    public static boolean restartWithTheme;
    public static String getcTheme = "null";
    /** @changeToTheme Simple elements to change the theme with button.
     * If there is no choice or theme then the theme is plain.
     * Changing the theme will make a new activity*/
    public static void changeToTheme(Activity activity, String theme)

    {
        cTheme = theme;

        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));

    }
/** @onActivityCreateSetTheme Changing the theme will make a new activity
 *  Make switch cases for each theme  */
    public static void onActivityCreateSetTheme(Activity activity) {

        if(cTheme == null){

            if(GlobalUser.getUsername() != null){
                try {
                    GlobalUser.refreshUser(GlobalUser.getUsername());

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else if(GlobalUser.getUsername() == null){
                cTheme = "plain";
                Log.d("PLAIN", "PLAIN");

            }

            if(GlobalUser.getUITheme() != null)
            {
                cTheme = GlobalUser.getUITheme();
                Log.d("UserTheme", GlobalUser.getUITheme());
            }
            else
            {
                cTheme = "plain";
                Log.d("NO THEME SELECTED","PLAIN");
            }



            //Log.d("USER", GlobalUser.getUITheme());


        }

        switch (cTheme)
        {
            default:
            case "moss":
                Log.d("SET THEME", "moss");
                getcTheme = "moss";
                activity.setTheme(R.style.moss);
                break;
            case "skyblue":
                Log.d("SET THEME", "skyblue");
                getcTheme = "skyblue";
                activity.setTheme(R.style.skyblye);
                break;
            case "lilac":
                Log.d("SET THEME", "lilac");
                getcTheme = "lilac";
                activity.setTheme(R.style.lilac);
                break;
            case "plain":
                Log.d("SET THEME", "plain");
                getcTheme = "plain";
                activity.setTheme(R.style.plain);
                break;


        }

    }

}
