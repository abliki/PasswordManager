package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {

    EditText editText;
    Button button;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");

        editText = (EditText) findViewById(R.id.editTextTextPassword);
        button = (Button) findViewById(R.id.buttonLoginPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();

                if(text.equals(password)) {
                    Intent intent = new Intent(getApplicationContext(), PasswordList.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginPage.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void toNext(View view) {
        Intent intent = new Intent(this, PasswordList.class);
        startActivity(intent);
    }
}