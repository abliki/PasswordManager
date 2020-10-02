package com.example.passwordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class CreatePasswordActivity extends AppCompatActivity {

    EditText editText1, editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        editText1 = (EditText) findViewById(R.id.editTextTextPassword1);
        editText2 = (EditText) findViewById(R.id.editTextTextPassword2);
        button = (Button) findViewById(R.id.buttonPasswordEnter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text1 = editText1.getText().toString();
                String text2 = editText2.getText().toString();
                if(text1.equals("") || text2.equals("")) {
                    Toast.makeText(CreatePasswordActivity.this, "No password entered!", Toast.LENGTH_SHORT).show();
                } else if (text1.length() < 8) {
                    Toast.makeText(CreatePasswordActivity.this, "Minimum 8 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    if (text1.equals(text2)) {
                        new AlertDialog.Builder(CreatePasswordActivity.this)
                                .setTitle("Confirm Password")
                                .setMessage("This password cannot be changed later. Do you want to continue?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        MasterKey masterKey = null;
                                        try {
                                            masterKey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                                                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                                                    .build();
                                        } catch (GeneralSecurityException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        SharedPreferences sharedPreferences = null;
                                        try {
                                            sharedPreferences = EncryptedSharedPreferences.create(
                                                    getApplicationContext(),
                                                    "secret_shared_prefs",
                                                    masterKey,
                                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                                        } catch (GeneralSecurityException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
//                        SharedPreferences sharedPreferences = getSharedPreferences("secret_shared_prefs", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        String text1 = editText1.getText().toString();
                                        editor.putString("password", text1);
                                        editor.apply();

                                        Intent intent = new Intent(getApplicationContext(), AccountList.class);
                                        startActivity(intent);
                                        finish();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();

                    } else {
                        Toast.makeText(CreatePasswordActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}