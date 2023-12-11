package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class CounterActivity extends AppCompatActivity {

    Button increaseBtn;
    Button backBtn;
    TextView numberTxt;

    Button decreaseBtn;

    Button colorChange;

    Button loadBtn;

    Button surpriseButton;
     int counter = 0;

     boolean loaded = false;
     ImageView grayBap;
     ImageView gaytiste;
     ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        grayBap = findViewById(R.id.holoBap);
        grayBap.setVisibility(View.GONE);
        gaytiste = findViewById(R.id.gayBap);
        gaytiste.setVisibility(View.GONE);
        increaseBtn = findViewById(R.id.increaseBtn);
        backBtn = findViewById(R.id.backBtn);
        numberTxt = findViewById(R.id.number);
        decreaseBtn = findViewById(R.id.decreaseBtn);
        loadBtn = findViewById(R.id.load);
        surpriseButton = findViewById(R.id.surpriseBtn);
        surpriseButton.setVisibility(View.GONE);
        colorChange = findViewById(R.id.changeColor);
        Random rand = new Random(101);

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(loaded) {
                    if (counter == 100) {
                        makeToastMax();
                    } else {
                        numberTxt.setText(String.valueOf(++counter));
                    }
                }
            }
        });
        decreaseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(loaded) {
                    if (counter == 0) {
                        makeToastMin();
                    } else {
                        numberTxt.setText(String.valueOf(--counter));
                    }
                }
            }
        });

        surpriseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                increaseBtn.setVisibility(View.GONE);
                backBtn.setVisibility(View.GONE);
                numberTxt.setVisibility(View.GONE);
                decreaseBtn.setVisibility(View.GONE);
                colorChange.setVisibility(View.GONE);
                loadBtn.setVisibility(View.GONE);
                surpriseButton.setVisibility(View.GONE);
                gaytiste.setVisibility(View.VISIBLE);
                grayBap.setVisibility(View.VISIBLE);
            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(loaded) {

                    Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });


        colorChange.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(loaded) {
                    int rand1 = rand.nextInt(255);
                    int rand2 = rand.nextInt(255);
                    int rand3 = rand.nextInt(255);
                    int rand4 = rand.nextInt(255);
                    v.setBackgroundColor(Color.argb(rand1, rand2, rand3, rand4));
                }
            }
        });
        loadBtn.setOnClickListener(new View.OnClickListener()
        {

            Handler handle = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    super.handleMessage(msg);
                    progressDialog.incrementProgressBy(4);
                }
            };
            @Override
            public void onClick(View v)
            {
                surpriseButton.setVisibility(View.VISIBLE);
                progressDialog = new ProgressDialog(CounterActivity.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Loading!!!");
                progressDialog.setTitle("Progress Bar");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setCancelable(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while(progressDialog.getProgress() <= progressDialog.getMax()) {
                                Thread.sleep(200);
                                handle.sendMessage(handle.obtainMessage());
                                if(progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                    loaded = true;

                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    public void makeToastMax()
    {
        Toast.makeText(getApplicationContext(), "Max number reached", Toast.LENGTH_LONG).show();
    }
    public void makeToastMin()
    {
        Toast.makeText(getApplicationContext(), "Min number reached", Toast.LENGTH_LONG).show();
    }

   // public static int sendCounter()
  //  {
      //  return counter;
   // }

}