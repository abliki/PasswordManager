package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PasswordList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);
    }

    public void toPasswordGenerate(View view) {
        Intent intent = new Intent(this, PasswordGenerateActivity.class);
        startActivity(intent);
    }
}