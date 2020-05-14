package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Edit_profile extends AppCompatActivity {

    SharedPreferences s;
    EditText text1;
    EditText text2;
    EditText text3;
    EditText text4;
    EditText text5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        text1 = (EditText) findViewById(R.id.editText3);
        text2 = (EditText) findViewById(R.id.editText4);
        text3 = (EditText) findViewById(R.id.editText5);
        text4 = (EditText) findViewById(R.id.editText6);
        text5 = (EditText) findViewById(R.id.editText7);
        Button button = (Button) findViewById(R.id.button);
        final RadioButton radioButton1 = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton radioButton2 = (RadioButton) findViewById(R.id.radioButton2);

        s = getSharedPreferences("customer", MODE_PRIVATE);
        final int id = s.getInt("id", 0);

        showUserInfo(id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = text1.getText().toString();
                String l_name = text2.getText().toString();
                String mail = text3.getText().toString();
                String pass = text4.getText().toString();
                String phone = text5.getText().toString();
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
                else if (!isValidData("[a-zA-Z]+", name)) {
                    Toast.makeText(getApplicationContext(), "Wrong first name", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("[a-zA-Z]+", l_name)) {
                    Toast.makeText(getApplicationContext(), "Wrong last name", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", mail)) {
                    Toast.makeText(getApplicationContext(), "Wrong e-mail", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("\\+7\\(\\d{3}\\)\\d{7}", phone) && phone != "" && phone != null) {
                    Toast.makeText(getApplicationContext(), "Wrong phone number", Toast.LENGTH_LONG).show();
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
                    if (phone.equals(""))
                        data.put("phone", "");
                    else
                        data.put("phone", phone);
                    data.put("email", mail);
                    data.put("id", id);
                    data.put("password", pass);
                    Database db = new Database(view.getContext());
                    db.getWritableDatabase().update("sign_in", data, "id = ?",
                            new String[] {Integer.toString(id)});
                    Intent intent = new Intent(Edit_profile.this, Profile.class);
                    startActivity(intent);
                }
            }
        });
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

    void showUserInfo(int id)
    {
        Database db = new Database(this);
        Cursor cursor = db.getReadableDatabase().query("sign_in", null, null, null, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex("first_name");
        int lnameColumnIndex = cursor.getColumnIndex("last_name");
        int phoneColumnIndex = cursor.getColumnIndex("phone");
        int mailColumnIndex = cursor.getColumnIndex("email");
        int passwordColumnIndex = cursor.getColumnIndex("password");
        int idColumnIndex = cursor.getColumnIndex("id");

        if (cursor.moveToFirst()) {
            do {
                String cur_name = cursor.getString(nameColumnIndex);
                String cur_lname = cursor.getString(lnameColumnIndex);
                String cur_phone = cursor.getString(phoneColumnIndex);
                String cur_mail = cursor.getString(mailColumnIndex);
                String cur_password = cursor.getString(passwordColumnIndex);
                int cur_id = cursor.getInt(idColumnIndex);
                if (id == cur_id) {
                    text1.setText(cur_name);
                    text2.setText(cur_lname);
                    text3.setText(cur_mail);
                    text4.setText(cur_password);
                    if (cur_phone != null && cur_phone.equals(""))
                        text5.setText(cur_phone);
                }
            } while (cursor.moveToNext());
        }
    }

}
