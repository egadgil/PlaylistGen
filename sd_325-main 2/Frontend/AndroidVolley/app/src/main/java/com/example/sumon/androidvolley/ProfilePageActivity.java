package com.example.sumon.androidvolley;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
/** @author: gigi harrabi */
public class ProfilePageActivity extends AppCompatActivity implements OnClickListener {
    View.OnClickListener myClickListener;
    private String username, email, membershipType = "";
    private ArrayList<String> savedPlaylist = new ArrayList<String>();
    private Button savedPlaylists;
    private Button UITheme;
    private Button changeAvatar;
    private Button logout;
    private Button btnFriendRequest;
    private Button PendBtn;
    private ImageView profile_pic;
    private TextView usernameTxt;
    private TextView memberShipTxt;


    @Override
    /** if no user name, ask if user is logged in.
     * When logged in, go to dash board to view your profile page and display
     * lets you view profile pic choices
     * make buttons for saved playlist, ui theme, change avatar, logout and set username text
     * set saved palylist, ui theme, change avatar, logout
     * */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.profile_page);
        usernameTxt = (TextView) findViewById(R.id.username);
        PendBtn = (Button) findViewById(R.id.PendBtn);
        PendBtn.setOnClickListener(this);
        memberShipTxt = (TextView) findViewById(R.id.membershipType);

            if(GlobalUser.getUsername() == null){
                 Snackbar snackbar = Snackbar
                       .make(findViewById(android.R.id.content), "hmm... are you logged in?", Snackbar.LENGTH_INDEFINITE)
                     .setAction("Dashboard", new View.OnClickListener(){
                         @Override
                         public void onClick(View v){
                                       startActivity(new Intent(ProfilePageActivity.this,
                                                     MainActivity.class));
                         }
                     });
                 snackbar.show();
        }
            else {

                if(GlobalUser.getUITheme() == null){
                    finish();
                    startActivity(getIntent());
                }

                if(GlobalUser.getMembershipType() == null ||
                GlobalUser.getMembershipType() == "null")
                {
                    GlobalUser.setMembershipType("Unpaid Member");
                }
                usernameTxt.setText(GlobalUser.getUsername());
                memberShipTxt.setText(GlobalUser.getMembershipType());
                savedPlaylists = (Button) findViewById(R.id.btnSavedPlay);
                UITheme = (Button) findViewById(R.id.btnTheme);
                changeAvatar = (Button) findViewById(R.id.btnAvatar);
                logout = (Button) findViewById(R.id.btnLogout);
                btnFriendRequest = (Button) findViewById(R.id.btnFriendRequest);

                profile_pic = (ImageView) findViewById(R.id.profile_pic);
                if(GlobalUser.getAvatar() != null){
                    switch(GlobalUser.getAvatar()){
                        case "clouds":
                            profile_pic.setImageResource(R.drawable.profile_clouds);
                            break;
                        case "headphone":
                            profile_pic.setImageResource(R.drawable.profile_headphone);
                            break;
                        case "moon":
                            profile_pic.setImageResource(R.drawable.profile_moon);
                            break;
                        case "swirl":
                            profile_pic.setImageResource(R.drawable.profile_swirl);
                            break;
                    }
                }

                savedPlaylists.setOnClickListener(this);
                UITheme.setOnClickListener(this);
                changeAvatar.setOnClickListener(this);
                logout.setOnClickListener(this);
                btnFriendRequest.setOnClickListener(this);
            }

    }

    @Override
    /** set cases for buttons mentione and start new intent to start new activities
     * log out and show that you are logged out*/
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSavedPlay:
                startActivity(new Intent(ProfilePageActivity.this,
                        PlaylistGenerator.class));
                break;
            case R.id.btnTheme:
                startActivity(new Intent(ProfilePageActivity.this,
                        UIThemeActivity.class));
                break;
            case R.id.btnAvatar:
                startActivity(new Intent(ProfilePageActivity.this,
                        AvatarActivity.class));
                break;
            case R.id.btnFriendRequest:
                startActivity(new Intent(ProfilePageActivity.this,
                        friendrequest.class));
               // btnFriendRequest.setOnClickListener((view) -> { friendrequest(view); });
                break;

            case R.id.PendBtn:
                startActivity(new Intent(ProfilePageActivity.this,
                        pending.class));
                // btnFriendRequest.setOnClickListener((view) -> { friendrequest(view); });
                break;
            case R.id.btnLogout:

                GlobalUser.logout();
                Snackbar snackbar_unimplemented_feature = Snackbar
                        .make(findViewById(android.R.id.content), "User logged out", Snackbar.LENGTH_LONG);
                snackbar_unimplemented_feature.show();

                startActivity(new Intent(ProfilePageActivity.this,
                        MainActivity.class));
                break;

        }

    }


}
