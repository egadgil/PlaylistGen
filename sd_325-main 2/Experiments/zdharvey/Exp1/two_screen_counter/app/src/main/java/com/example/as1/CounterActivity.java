package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class CounterActivity extends AppCompatActivity {

    Button increaseBtn;
    Button backBtn;
    TextView numberTxt;

    Button decreaseBtn;

    Button randBtn;

    Button multiBtn;

    Button divideBtn;

    Button resetBtn;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        increaseBtn = findViewById(R.id.increaseBtn);
        backBtn = findViewById(R.id.backBtn);
        numberTxt = findViewById(R.id.number);
        decreaseBtn = findViewById(R.id.decreaseBtn);
        randBtn = findViewById(R.id.randBtn);
        multiBtn = findViewById(R.id.multiBtn);
        divideBtn = findViewById(R.id.divideBtn);
        resetBtn = findViewById(R.id.resetBtn);
        Random rand = new Random(100);

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(counter == 100)
                {
                    makeToastMax();
                }
                else {
                    numberTxt.setText(String.valueOf(++counter));
                }
            }
        });
        decreaseBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                if(counter == 0)
                {
                    makeToastMin();
                }
                else {
                    numberTxt.setText(String.valueOf(--counter));
                }
            }
        });
        randBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                counter = rand.nextInt(100);
                numberTxt.setText(String.valueOf(counter));
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        multiBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                counter = counter * 5;
                if(counter > 100)
                {
                    counter = 100;
                    makeToastMax();
                }
                    numberTxt.setText(String.valueOf(counter));

            }
        });
        divideBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                counter = counter / 5;
                if(counter == 0)
                {
                    makeToastMin();
                }
                numberTxt.setText(String.valueOf(counter));
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                counter = 0;
                numberTxt.setText(String.valueOf(counter));
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
}