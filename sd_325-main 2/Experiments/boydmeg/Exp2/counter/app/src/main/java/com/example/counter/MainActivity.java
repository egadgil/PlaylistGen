package com.example.counter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    Button button;
    Button buttonLoad;
    ToggleButton toggleButton;
    ProgressDialog progressDialog;
    Button buttonWarm;
    Button buttonCool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button1, button2;


        button = findViewById(R.id.toCounterBtn);
        buttonLoad = findViewById(R.id.toLoadingBtn);
        toggleButton = findViewById(R.id.ToggleButton);





        toggleButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v){
               if(toggleButton.isChecked())
               {
                    button.setEnabled(true);
                    buttonLoad.setEnabled(true);

               }
               else {
                   button.setEnabled(false);
                   buttonLoad.setEnabled(false);
               }
           }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }

        });

        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v)
            {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setTitle("Progress Dialog");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();
            }

        });


    }


}