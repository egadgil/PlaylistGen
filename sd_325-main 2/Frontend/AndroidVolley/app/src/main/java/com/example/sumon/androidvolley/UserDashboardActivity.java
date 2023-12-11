package com.example.sumon.androidvolley;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * UserDashboardActivity class that is the homepage of the application once the user is logged in, basically has
 * buttons to direct the user to whereever they want to go in the application
 * @author Zach
 */
public class UserDashboardActivity extends AppCompatActivity {

    private TextView welcomeText;
    private Button btnProfilePage, btnLeaderboardPage, btnPlaylistGeneratorPage,
            btnSavedPlaylists, btnPaymentPage, btnDirectMessage, spotifyLogin, requestpend;
            btnSavedPlaylists, btnPaymentPage, btnDirectMessage, discoverPage;

    private ProgressDialog progressDialog;
    /**
     * Protected onCreate method that sets the visuals to user_dashboard.xml, a homepage for the app with various buttons
     * that are set to java variables for functionality. Allows user to navigate the application easier.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(GlobalUser.getMembershipType() == "null" || GlobalUser.getMembershipType() == "Unpaid Member")
        {
            super.onCreate(savedInstanceState);
            themeUtils.onActivityCreateSetTheme(this);
            setContentView(R.layout.unpaid_user_dashboard);
            getSupportActionBar().setTitle("User Dashboard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            welcomeText = (TextView) findViewById(R.id.userDashboardTxt);
            btnProfilePage = (Button) findViewById(R.id.profilePageBtn);
            btnPlaylistGeneratorPage = (Button) findViewById(R.id.playlistGeneratorPageBtn);
            btnPaymentPage = (Button) findViewById(R.id.paymentPageBtn);
            Log.d("first first conditional", "wee");
        }
        //else if(GlobalUser.getMembershipType() == Const.PAID_MEMBER){
        else {
            super.onCreate(savedInstanceState);
            themeUtils.onActivityCreateSetTheme(this);
            setContentView(R.layout.user_dashboard);
            getSupportActionBar().setTitle("User Dashboard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //Set frontend variables to java variables
            welcomeText = (TextView) findViewById(R.id.userDashboardTxt);
            btnProfilePage = (Button) findViewById(R.id.profilePageBtn);
            btnLeaderboardPage = (Button) findViewById(R.id.leaderboardPageBtn);
            btnPlaylistGeneratorPage = (Button) findViewById(R.id.playlistGeneratorPageBtn);
            btnSavedPlaylists = (Button) findViewById(R.id.savedPlaylistBtn);
            btnPaymentPage = (Button) findViewById(R.id.paymentPageBtn);
            btnDirectMessage = (Button) findViewById(R.id.dmBtn);
            discoverPage = (Button) findViewById(R.id.discoverPage);
            Log.d("First conditional", "waa");
        }








<<<<<<< Frontend/AndroidVolley/app/src/main/java/com/example/sumon/androidvolley/UserDashboardActivity.java
        //Set frontend variables to java variables
        welcomeText = (TextView) findViewById(R.id.userDashboardTxt);
        btnProfilePage = (Button) findViewById(R.id.profilePageBtn);
        btnLeaderboardPage = (Button) findViewById(R.id.leaderboardPageBtn);
        btnPlaylistGeneratorPage = (Button) findViewById(R.id.playlistGeneratorPageBtn);
        btnSavedPlaylists = (Button) findViewById(R.id.savedPlaylistBtn);
        btnPaymentPage = (Button) findViewById(R.id.paymentPageBtn);
        btnDirectMessage = (Button) findViewById(R.id.dmBtn);
        spotifyLogin = (Button) findViewById(R.id.spotifyLogin);


=======
>>>>>>> Frontend/AndroidVolley/app/src/main/java/com/example/sumon/androidvolley/UserDashboardActivity.java
        //Large chunk of code to display the loading progressdialog at the
        //start of activity
        Handler handle = new Handler()
        {
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(4);
            }
        };








        progressDialog = new ProgressDialog(UserDashboardActivity.this);

        if(GlobalUser.getUITheme() != null) {
            progressDialog.cancel();
            if (!GlobalUser.getUITheme().equals(themeUtils.getcTheme)) {
                // themeUtils.restartWithTheme = false;
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                themeUtils.changeToTheme(UserDashboardActivity.this, GlobalUser.getUITheme());

            }
        } else {
            progressDialog.cancel();
            //if(progressDialog.isShowing()){
            //  progressDialog.dismiss();
            //}
            themeUtils.changeToTheme(UserDashboardActivity.this, GlobalUser.getUITheme());

        }

        progressDialog.setMax(100);
        progressDialog.setMessage("Taking you to our userdashboard now");
        progressDialog.setTitle("You've successfully logged in");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);





        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(progressDialog.getProgress() <= progressDialog.getMax()) {
                        Thread.sleep(200);
                        handle.sendMessage(handle.obtainMessage());
                        if(progressDialog.getProgress() == progressDialog.getMax()) {

                            progressDialog.dismiss();


                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();



        //Display username
        String username = GlobalUser.getUsername();
        String txt = "Welcome " + username + " to our user dashboard\n Take a look around, generate some playlists!";
        Log.d("text", txt);
        welcomeText.setText(txt);
        //Functionality for on click listeners


        if(GlobalUser.getMembershipType() != "null" && GlobalUser.getMembershipType() != "Unpaid Member") {
            btnSavedPlaylists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDashboardActivity.this,
                            SavedPlaylist.class));
                }
            });
            discoverPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserDashboardActivity.this,
                            DiscoverPage.class));
                }
            });
            btnLeaderboardPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Head to leadboard
                    startActivity(new Intent(UserDashboardActivity.this,
                            LeaderboardActivity.class));
                }
            });
        }

            btnPlaylistGeneratorPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Head to playlist generator
                    startActivity(new Intent(UserDashboardActivity.this,
                            PlaylistGenerator.class));
                }
            });

        btnProfilePage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Head to profile page
                startActivity(new Intent(UserDashboardActivity.this,
                        ProfilePageActivity.class));
            }
        });
        btnPaymentPage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Head to payment page
                startActivity(new Intent(UserDashboardActivity.this,
                        PaymentStartActivity.class));
            }
        });

    }
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * Protected Overriden onDestroy method that gets rid of the progressDialog, needed for bugs between
     * Themes and the ProgressDialog
     */
    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    /**
     * public Overriden onOptionsItemSelected method that is used for theme functionality
     * @param item The menu item that was selected.
     *
     * @return super.onOptionsItemSelected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

