package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Reservations extends AppCompatActivity {


    SharedPreferences s;
    TextView name;
    TextView date;
    TextView time;
    TextView phone;
    TextView email;
    TextView persons;
    TextView table;

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        name = (TextView) findViewById(R.id.n);
        date = (TextView) findViewById(R.id.d);
        time = (TextView) findViewById(R.id.t);
        phone = (TextView) findViewById(R.id.ph);
        email = (TextView) findViewById(R.id.e);
        persons = (TextView) findViewById(R.id.pe);
        table = (TextView) findViewById(R.id.tab);

        button = (Button) findViewById(R.id.button);

        s = getSharedPreferences("customer", MODE_PRIVATE);
        final int id = s.getInt("id", 0);

        Calendar date_today = Calendar.getInstance();
        Calendar res_date = new GregorianCalendar(3000, 0, 1);
        Calendar r = res_date;
        Calendar temp = new GregorianCalendar();

        final Database db = new Database(this);
        Cursor cursor = db.getReadableDatabase().query("booking", null, null, null, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex("name");
        int dateColumnIndex = cursor.getColumnIndex("date");
        int timeColumnIndex = cursor.getColumnIndex("time");
        int phoneColumnIndex = cursor.getColumnIndex("phone");
        int mailColumnIndex = cursor.getColumnIndex("email");
        int personsColumnIndex = cursor.getColumnIndex("persons");
        int tableColumnIndex = cursor.getColumnIndex("table_num");
        int idColumnIndex = cursor.getColumnIndex("id");

        if (cursor.moveToFirst()) {
            do {
                String cur_date = cursor.getString(dateColumnIndex);
                String cur_time = cursor.getString(timeColumnIndex);
                int cur_id = cursor.getInt(idColumnIndex);
                if (id == cur_id) {
                    String[] all_date = cur_date.split("\\.");
                    String[] all_time = cur_time.split(":");

                    temp.set(Calendar.YEAR, Integer.parseInt(all_date[2]));
                    temp.set(Calendar.MONTH, Integer.parseInt(all_date[1]) - 1);
                    temp.set(Calendar.DAY_OF_MONTH, Integer.parseInt(all_date[0]));
                    temp.set(Calendar.HOUR_OF_DAY, Integer.parseInt(all_time[0]));
                    temp.set(Calendar.MINUTE, Integer.parseInt(all_time[1]));

                    if(temp.before(res_date) && temp.after(date_today))
                        res_date = temp;
                }
            } while (cursor.moveToNext());

            if (res_date == r)
            {
                setLines();
                return;
            }
        }
        else {
            setLines();
            return;
        }
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("kk:mm");
        String dmy = format1.format(res_date.getTime());
        String hm = format2.format(res_date.getTime());

        if (cursor.moveToFirst()) {
            do {
                String cur_name = cursor.getString(nameColumnIndex);
                String cur_date = cursor.getString(dateColumnIndex);
                String cur_time = cursor.getString(timeColumnIndex);
                String cur_phone = cursor.getString(phoneColumnIndex);
                String cur_mail = cursor.getString(mailColumnIndex);
                String cur_persons = cursor.getString(personsColumnIndex);
                String cur_table = cursor.getString(tableColumnIndex);
                int cur_id = cursor.getInt(idColumnIndex);
                if (id == cur_id && dmy.equals(cur_date) && hm.equals(cur_time)) {
                    name.setText(cur_name);
                    date.setText(cur_date);
                    time.setText(cur_time);
                    email.setText(cur_mail);
                    persons.setText(cur_persons);
                    phone.setText(cur_phone);
                    table.setText(cur_table);
                }
            } while (cursor.moveToNext());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (name.getText().toString().equals("-"))
                    Toast.makeText(getApplicationContext(), "There's nothing to cancel", Toast.LENGTH_LONG).show();
                else {
                    db.getWritableDatabase().delete("booking", "date = '" + date.getText().toString() + "' and time = '" + time.getText().toString() + "'", null);
                    Toast.makeText(getApplicationContext(), "The reservation is canceled", Toast.LENGTH_LONG).show();
                    setLines();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Reservations.this, Main_Page.class);
        startActivity(intent);
        finish();
    }

    void setLines()
    {
        name.setText("-");
        date.setText("-");
        time.setText("-");
        email.setText("-");
        persons.setText("-");
        phone.setText("-");
        table.setText("-");
    }
}
