package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.StringWriter;

public class PasswordRandomGeneration extends AppCompatActivity {
    private boolean includeLower = true, includeNumber = true, includeUpper = false, includeSpecial = false;
    TextView passwordGenText, passwordLengthHint;
    Button buttonGenerate, buttonConfirm;
    Switch lowerSwitch, upperSwitch, numberSwitch, specialSwitch;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_random_generation);

        passwordGenText = findViewById(R.id.password_random);
        passwordLengthHint = findViewById(R.id.password_length_hint);
        buttonGenerate = findViewById(R.id.generate_new_pw_button);
        buttonConfirm = findViewById(R.id.confirm_pw_button);
        lowerSwitch = findViewById(R.id.lower_case_label);
        upperSwitch = findViewById(R.id.upper_case_label);
        numberSwitch = findViewById(R.id.number_label);
        specialSwitch = findViewById(R.id.special_character_label);
        seekBar = findViewById(R.id.password_gen_length_seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                passwordLengthHint.setText("Password Length: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        generatePw();
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
    public String passwordGen(boolean lower, boolean upper, boolean numbers, boolean symbols, int passwordLength) {
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
        for (int i = 0; i < passwordLength; i++) {
            int rand = (int) (Math.random() * chars.length());
            password.append(chars.charAt(rand));
        }
        return password.toString();
    }

    private void generatePw() {
        // change the parameter status based on user input
        includeLower = lowerSwitch.isChecked();
        includeUpper = upperSwitch.isChecked();
        includeNumber = numberSwitch.isChecked();
        includeSpecial = specialSwitch.isChecked();
        int progress = seekBar.getProgress();

        String pw = passwordGen(includeLower, includeUpper, includeNumber, includeSpecial, progress);
        passwordGenText.setText(pw);

        // set the text size if the string is too long or too short
        if (pw.length() > 12)
            passwordGenText.setTextSize(36);
        else
            passwordGenText.setTextSize(48);
    }

    // onClick handler of the "generate" button
    public void generateRandomPassword(View view) {
        generatePw();
    }
}