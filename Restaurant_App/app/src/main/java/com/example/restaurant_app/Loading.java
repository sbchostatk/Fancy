package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Thread background =
                new Thread() {
                    public void run() {

                        try {
                            sleep(4*1000);

                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(i);

                            finish();

                        } catch (Exception e) {

                        }
                    }
                };
        background.start();
    }
}
