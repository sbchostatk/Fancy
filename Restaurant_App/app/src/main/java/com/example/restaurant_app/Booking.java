package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Booking extends AppCompatActivity {

    SharedPreferences s;
    int y = 1;
    ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        final EditText name = (EditText) findViewById(R.id.editText3);
        final EditText mail = (EditText) findViewById(R.id.editText4);
        final EditText phone = (EditText) findViewById(R.id.editText5);
        final EditText date = (EditText) findViewById(R.id.editText7);
        final Spinner time = (Spinner) findViewById(R.id.spinner);
        final EditText per = (EditText) findViewById(R.id.editText6);

        data = new ArrayList<>();

        timesList();

        s = getSharedPreferences("customer", MODE_PRIVATE);
        final int id = s.getInt("id", 0);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter);

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                y = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Database db = new Database(this);
        Cursor cursor = db.getReadableDatabase().query("sign_in", null, null, null, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex("first_name");
        int lnameColumnIndex = cursor.getColumnIndex("last_name");
        int phoneColumnIndex = cursor.getColumnIndex("phone");
        int mailColumnIndex = cursor.getColumnIndex("email");
        int idColumnIndex = cursor.getColumnIndex("id");

        if (cursor.moveToFirst()) {
            do {
                String cur_name = cursor.getString(nameColumnIndex);
                String cur_lname = cursor.getString(lnameColumnIndex);
                String cur_phone = cursor.getString(phoneColumnIndex);
                String cur_mail = cursor.getString(mailColumnIndex);
                int cur_id = cursor.getInt(idColumnIndex);
                if (id == cur_id) {
                    name.setText(cur_name + " " + cur_lname);
                    mail.setText(cur_mail);
                    if (cur_phone != null && cur_phone != "")
                        phone.setText(cur_phone);
                }
            } while (cursor.moveToNext());
        }

        Button booking = (Button) findViewById(R.id.button);
        booking.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String n = name.getText().toString();
                String m = mail.getText().toString();
                String p = phone.getText().toString();
                String d = date.getText().toString();
                String t = data.get(y);
                String pe = per.getText().toString();

                if (n.equals(""))
                {
                    name.setError("Name is required");
                }
                else if (m.equals(""))
                {
                    mail.setError("E-mail is required");
                }
                else if (p.equals(""))
                {
                    phone.setError("Phone number is required");
                }
                else if (d.equals(""))
                {
                    date.setError("Date is required");
                }
                else if (t.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "You didn't select time", Toast.LENGTH_LONG).show();
                }
                else if (pe.equals(""))
                {
                    per.setError("Number of persons is required");
                }
                else if (!isValidData("[a-zA-Z ]+", n)) {
                    Toast.makeText(getApplicationContext(), "Wrong name", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", m)) {
                    Toast.makeText(getApplicationContext(), "Wrong e-mail", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("\\+7\\(\\d{3}\\)\\d{7}", p)) {
                    Toast.makeText(getApplicationContext(), "Wrong phone number", Toast.LENGTH_LONG).show();
                }
                else if (!isDateValid(date.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Wrong date", Toast.LENGTH_LONG).show();
                }
                else if (!isValidData("^\\d+$", pe)) {
                    Toast.makeText(getApplicationContext(), "Wrong number of persons", Toast.LENGTH_LONG).show();
                }
                else if (Integer.parseInt(per.getText().toString()) < 1 || Integer.parseInt(per.getText().toString()) > 10) {
                    Toast.makeText(getApplicationContext(), "Too many people", Toast.LENGTH_LONG).show();
                }
                else {
                    ContentValues data = new ContentValues();

                    data.put("id", id);
                    data.put("name", n);
                    data.put("email", m);
                    data.put("phone", p);
                    data.put("date", d);
                    data.put("time", t);
                    data.put("persons", pe);

                    int x = 1;
                    Database db = new Database(view.getContext());

                    SQLiteDatabase d1 = db.getReadableDatabase();
                    Cursor cursor = d1.query("booking", null, null, null, null, null, null);

                    int numColumnIndex = cursor.getColumnIndex("table_num");
                    int dateColumnIndex = cursor.getColumnIndex("date");
                    int timeColumnIndex = cursor.getColumnIndex("time");

                    if (cursor.moveToFirst()) {
                        do {
                            int cur_table = cursor.getInt(numColumnIndex);
                            String cur_date = cursor.getString(dateColumnIndex);
                            String cur_time = cursor.getString(timeColumnIndex);
                            if (cur_date.equals(d))
                            {
                                String[] c_t = cur_time.split(":");
                                int c_hour = Integer.parseInt(c_t[0]);
                                String[] n_t = t.split(":");
                                int n_hour = Integer.parseInt(n_t[0]);

                                if(Math.abs(c_hour - n_hour) <= 2)
                                {
                                    x = ++cur_table;
                                    if (x > 20) {
                                        Toast.makeText(getApplicationContext(), "Sorry, no tables available. Please, change time", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                        } while (cursor.moveToNext());
                        putData(x, d, t, db, data);
                    }
                    else {
                        putData(1, d, t, db, data);
                    }
                    cursor.close();
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

    public boolean isDateValid(String dateToValidate){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false);

        try {

            Date date = sdf.parse(dateToValidate);
            Date today = new Date();

            if (date.getTime() < today.getTime())
                return false;

        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    void putData(int x, String d, String t, Database db, ContentValues data)
    {
        data.put("table_num", x);
        data.put("date", d);
        data.put("time", t);
        db.getWritableDatabase().insert("booking", null, data);
        Intent intent1 = new Intent(Booking.this, Table.class);
        intent1.putExtra("t", Integer.toString(x));
        startActivity(intent1);
    }

    void timesList()
    {
        for (int i = 10; i < 22; i++)
        {
            String t = Integer.toString(i) + ":00";
            data.add(t);
            t = Integer.toString(i) + ":30";
            data.add(t);
        }
    }
}
