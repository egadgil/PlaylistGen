package com.example.as1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class intentTesting extends AppCompatActivity {

    Button returner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_testing);

        returner = findViewById(R.id.returnBtn);

        returner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(intentTesting.this, MainActivity.class);
                startActivity(i);
            }
        });

    }


}
