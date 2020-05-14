package com.example.restaurant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.login);
        final EditText editText = (EditText) findViewById(R.id.email);
        final EditText editText2 = (EditText) findViewById(R.id.password);
        final TextView textView=(TextView)findViewById(R.id.link_signup);

        SpannableString content = new SpannableString("No account yet? Create new one");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = editText.getText().toString();
                String password = editText2.getText().toString();

                Pattern em = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                final Matcher mat_em = em.matcher(email);

                if (email.equals("")) {
                    editText.setError("E-mail is required");
                }
                else if (password.equals("")) {
                    editText2.setError("Password is required");
                }
                else if (!mat_em.matches()) {
                    Toast.makeText(getApplicationContext(), "Wrong e-mail", Toast.LENGTH_LONG).show();
                }
                else if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                }
                else {
                    Database db = new Database(view.getContext());
                    Cursor cursor = db.getReadableDatabase().query("sign_in", null, null, null, null, null, null);

                    int mailColumnIndex = cursor.getColumnIndex("email");
                    int passwordColumnIndex = cursor.getColumnIndex("password");
                    int idColumnIndex = cursor.getColumnIndex("id");

                    if (cursor.moveToFirst()) {
                        do {
                            String cur_mail = cursor.getString(mailColumnIndex);
                            String cur_password = cursor.getString(passwordColumnIndex);
                            int cur_id = cursor.getInt(idColumnIndex);
                            if (cur_mail.equals(editText.getText().toString())) {
                                if (cur_password.equals(editText2.getText().toString())) {
                                    setID(cur_id);
                                    Intent intent = new Intent(MainActivity.this, Main_Page.class);
                                    startActivity(intent);
                                    return;
                                } else {
                                    editText2.setError("Wrong password");
                                    return;
                                }
                            }
                        } while (cursor.moveToNext());
                        editText.setError("Wrong e-mail");
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong e-mail or password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent=new Intent(MainActivity.this, Sign_up.class);
                startActivity(intent);
                return false;
            }
        });
    }

    void setID(int id)
    {
        sPref = getSharedPreferences("customer", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("id", id);
        ed.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
