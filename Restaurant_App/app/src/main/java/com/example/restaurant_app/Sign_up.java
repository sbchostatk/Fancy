package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        final EditText text1 = (EditText) findViewById(R.id.editText3);
        final EditText text2 = (EditText) findViewById(R.id.editText4);
        final EditText text3 = (EditText) findViewById(R.id.editText5);
        final EditText text4 = (EditText) findViewById(R.id.editText6);
        Button button = (Button) findViewById(R.id.button);
        final RadioButton radioButton1 = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        final Random rand = new Random();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = text1.getText().toString();
                String l_name = text2.getText().toString();
                String mail = text3.getText().toString();
                String pass = text4.getText().toString();
                String gen = "";

                if (name.matches("")) {
                    text1.setError("First name is required");
                }
                else if (l_name.matches("")) {
                    text2.setError("Last name is required");
                }
                else if (mail.matches("")) {
                    text3.setError("E-mail is required");
                }
                else if (!radioButton1.isChecked() && !radioButton2.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please choose gender", Toast.LENGTH_LONG).show();
                }
                else if (pass.matches("")) {
                    text4.setError("Password is required");
                }
                else if (!checkBox.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "You didn't agree to terms and conditions :(", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("[a-zA-Z]+", name)) {
                    Toast.makeText(getApplicationContext(), "Wrong first name", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("[a-zA-Z]+", l_name)) {
                    Toast.makeText(getApplicationContext(), "Wrong last name", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", mail)) {
                    Toast.makeText(getApplicationContext(), "Wrong e-mail", Toast.LENGTH_LONG).show();
                }
                else if (pass.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                }
                else {
                    name = up_first_letter(name);
                    l_name = up_first_letter(l_name);
                    if (radioButton1.isChecked())
                        gen = "Male";
                    else
                        gen = "Female";
                    ContentValues data = new ContentValues();
                    data.put("first_name", name);
                    data.put("last_name", l_name);
                    data.put("gender", gen);
                    data.put("email", mail);
                    data.put("id", rand.nextInt(9999) + 1000);
                    data.put("password", pass);

                    Database db = new Database(view.getContext());
                    Cursor cursor = db.getReadableDatabase().query("sign_in", null, null, null, null, null, null);

                    int mailColumnIndex = cursor.getColumnIndex("email");
                    int passwordColumnIndex = cursor.getColumnIndex("password");

                    if (cursor.moveToFirst()) {
                        do {
                            String cur_mail = cursor.getString(mailColumnIndex);
                            String cur_password = cursor.getString(passwordColumnIndex);
                            if (cur_mail.equals(mail)) {
                                if (cur_password.equals(pass)) {
                                    Toast.makeText(getApplicationContext(), "Such user already exists", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Toast.makeText(getApplicationContext(), "User with such e-mail already exists", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } while (cursor.moveToNext());
                        addData(db, data);
                    }
                    else {
                        addData(db, data);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Sign_up.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    boolean isValidData(String pattern, String data)
    {
        Pattern pa = Pattern.compile(pattern);
        final Matcher mat = pa.matcher(data);
        if(!mat.matches())
            return false;
        else
            return true;
    }

    String up_first_letter(String name)
    {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    void addData(Database db, ContentValues data)
    {
        db.getWritableDatabase().insert("sign_in", null, data);
        Intent intent = new Intent(Sign_up.this, MainActivity.class);
        startActivity(intent);
    }

}
