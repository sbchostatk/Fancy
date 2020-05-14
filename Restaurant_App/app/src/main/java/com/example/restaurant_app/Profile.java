package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    SharedPreferences s;
    TextView name;
    TextView l_name;
    TextView gender;
    TextView phone;
    TextView email;
    TextView password;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.n);
        l_name = (TextView) findViewById(R.id.l);
        gender = (TextView) findViewById(R.id.g);
        phone = (TextView) findViewById(R.id.ph);
        email = (TextView) findViewById(R.id.e);
        password = (TextView) findViewById(R.id.pa);

        button = (Button) findViewById(R.id.button);

        setData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Edit_profile.class);
                startActivity(intent);
            }
        });

    }

    void setData()
    {
        s = getSharedPreferences("customer", MODE_PRIVATE);
        int id = s.getInt("id", 0);

        Database db = new Database(this);
        Cursor cursor = db.getReadableDatabase().query("sign_in", null, null, null, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex("first_name");
        int lnameColumnIndex = cursor.getColumnIndex("last_name");
        int genderColumnIndex = cursor.getColumnIndex("gender");
        int phoneColumnIndex = cursor.getColumnIndex("phone");
        int mailColumnIndex = cursor.getColumnIndex("email");
        int passwordColumnIndex = cursor.getColumnIndex("password");
        int idColumnIndex = cursor.getColumnIndex("id");

        if (cursor.moveToFirst()) {
            do {
                String cur_name = cursor.getString(nameColumnIndex);
                String cur_lname = cursor.getString(lnameColumnIndex);
                String cur_gender = cursor.getString(genderColumnIndex);
                String cur_phone = cursor.getString(phoneColumnIndex);
                String cur_mail = cursor.getString(mailColumnIndex);
                String cur_password = cursor.getString(passwordColumnIndex);
                int cur_id = cursor.getInt(idColumnIndex);
                if (id == cur_id) {
                    name.setText(cur_name);
                    l_name.setText(cur_lname);
                    gender.setText(cur_gender);
                    email.setText(cur_mail);
                    password.setText(cur_password);
                    if (cur_phone == null || cur_phone.equals(""))
                        phone.setText("");
                    else
                        phone.setText(cur_phone);
                }
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Profile.this, Main_Page.class);
        startActivity(intent);
        finish();
    }
}
