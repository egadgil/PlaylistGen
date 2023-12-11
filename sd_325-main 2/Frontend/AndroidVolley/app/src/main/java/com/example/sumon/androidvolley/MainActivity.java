package com.example.sumon.androidvolley;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Main method that is the startup of our application, has three buttons. Signup and Login which the user can
 * interact with, and Admin which admins can use to get to our admin dashboard.
 * @author Zach
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {
    private Button btnJson, btnString, btnImage, btnLeaderboard, btnSongArtist, btnSignup, btnLogin, btnProfile,btnAdmin, btnUpload;

    /**
     * protected onCreate method that sets the visuals to activity_main.xml and connects the buttons to java variables for
     * functionality. Also sets onClickListeners.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*btnString = (Button) findViewById(R.id.btnStringRequest);
        btnJson = (Button) findViewById(R.id.btnJsonRequest);
        btnImage = (Button) findViewById(R.id.btnImageRequest);
        btnLeaderboard = (Button) findViewById(R.id.btnLeaderboard);
        btnSongArtist = (Button) findViewById(R.id.btnSongArtist);*/
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnAdmin = (Button) findViewById(R.id.btnAdmin);
        btnUpload = (Button) findViewById(R.id.btnUpload);

        //btnProfile = (Button) findViewById(R.id.btnProfile);

        // button click listeners
       /* btnString.setOnClickListener(this);
        btnJson.setOnClickListener(this);
        btnImage.setOnClickListener(this);
        btnLeaderboard.setOnClickListener(this);
        btnSongArtist.setOnClickListener(this);*/
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnAdmin.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        //btnProfile.setOnClickListener(this);
    }

    /**
     * onClick method that has switch cases for every button that is clickable and will take the user/admin to whichever
     * button they click.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnLogin:
                startActivity(new Intent(MainActivity.this,
                        LoginActivity.class));
                break;
            case R.id.btnSignup:
                startActivity(new Intent(MainActivity.this,
                        SignupActivity.class));
                break;
            case R.id.btnAdmin:
                startActivity(new Intent(MainActivity.this,
                        AdminDashActivity.class));
                break;

            case R.id.btnUpload:
                startActivity(new Intent(MainActivity.this,
                        picupload.class));
                break;
            /*case R.id.btnSongArtist:
                startActivity(new Intent(MainActivity.this,
                        SongArtistActivity.class));
                break;
            case R.id.btnLeaderboard:
                startActivity(new Intent(MainActivity.this,
                        LeaderboardActivity.class));
                break;
            case R.id.btnStringRequest:
                startActivity(new Intent(MainActivity.this,
                        StringRequestActivity.class));
                break;
            case R.id.btnJsonRequest:
                startActivity(new Intent(MainActivity.this,
                        JsonRequestActivity.class));
                break;
            case R.id.btnImageRequest:
                startActivity(new Intent(MainActivity.this,
                        ImageRequestActivity.class));
                break;*/
        }
    }

}
