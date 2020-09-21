package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.StringWriter;

public class PasswordRandomGeneration extends AppCompatActivity {
    private boolean includeLower = true, includeNumber = true, includeUpper = false, includeSpecial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_random_generation);

        TextView passwordGenText = findViewById(R.id.password_random_text_hint);
        Button buttonGenerate = findViewById(R.id.generate_new_pw_button);
        Button buttonConfirm = findViewById(R.id.confirm_pw_button);
        Switch lowerSwitch = findViewById(R.id.lower_case_label);
        Switch upperSwitch = findViewById(R.id.upper_case_label);
        Switch numberSwitch = findViewById(R.id.number_label);
        Switch specialSwitch = findViewById(R.id.special_character_label);
    }

    /**
     * helper function to generate a random password based on the given parameters
     * @param lower boolean: if true, include all lowercase letters
     * @param upper boolean: if true, include all uppercase letters
     * @param numbers boolean: if true, include all numbers
     * @param symbols boolean: if true, include all special characters
     * @param passwordLength String: a string representation of the number of digits in the password
     * @return a randomized password based on the given requirements
     */
    public String passwordGenerator(boolean lower, boolean upper, boolean numbers, boolean symbols, String passwordLength) {
        StringBuilder password = new StringBuilder();
        String chars = "";
        if (lower)
            chars += "abcdefghijklmnopqrstuvwxyz";
        if (upper)
            chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (numbers)
            chars += "0123456789";
        if (symbols)
            chars += "#@?;:/*-+_#$%&!";
        int length = Integer.parseInt(passwordLength);
        for (int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * chars.length());
            password.append(chars.charAt(rand));
        }
        return password.toString();
    }

    public void generateRandomPassword(View view) {
    }
}