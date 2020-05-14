package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        final Intent intent= getIntent();
        String s = intent.getStringExtra("t");
        TextView textView = (TextView) findViewById(R.id.main_t);
        textView.append(s);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent1 = new Intent(Table.this, Main_Page.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Table.this, Main_Page.class);
        startActivity(intent);
        finish();
    }
}
