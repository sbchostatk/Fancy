package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button b1 = (Button)findViewById(R.id.button1);
        Button b2 = (Button)findViewById(R.id.button2);
        Button b3 = (Button)findViewById(R.id.button3);
        Button b4 = (Button)findViewById(R.id.button4);
        TextView textView = (TextView) findViewById(R.id.res);
        Intent intent = getIntent();

        SpannableString content = new SpannableString("Your reservation");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        b1.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(Main_Page.this, Booking.class);
                startActivity(intent);
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent=new Intent(Main_Page.this, Profile.class);
                startActivity(intent);
            }
        });
        b3.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent= new Intent(Main_Page.this, Info.class);
                startActivity(intent);
            }
        });
        b4.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent(Main_Page.this, Menu.class);
                startActivity(intent);
            }
        });

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent=new Intent(Main_Page.this, Reservations.class);
                startActivity(intent);
                return false;
            }
        });
    }

}
