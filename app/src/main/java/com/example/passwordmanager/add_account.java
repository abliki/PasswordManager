package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class add_account extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "com.example.passwordmanager.extra.MESSAGE";
    private static final int TEXT_REQUEST = 1;
    public static final String LOG_TAG = add_account.class.getSimpleName();

    private EditText title, url, username, password, notes;
    private Button generate_password, show_password, cancel, add;
    DatabaseHelper DBhelper;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_acount);

        context = add_account.this;
        DBhelper = new DatabaseHelper(this);

        title = (EditText) findViewById(R.id.title_edit);
        url = (EditText) findViewById(R.id.url_edit);
        username = (EditText) findViewById(R.id.user_edit);
        password = (EditText) findViewById(R.id.password_edit);
        notes = (EditText) findViewById(R.id.note_edit);

        generate_password = (Button) findViewById(R.id.generate);
        show_password = (Button) findViewById(R.id.show);
        cancel = (Button) findViewById(R.id.cancel);
        add = (Button) findViewById(R.id.add);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Back to Account list
                Intent intent = new Intent(add_account.this, account_list.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Title, password and username must be filled
                if (title.getText().toString().length() != 0 && username.getText().toString().length() != 0 && password.getText().toString().length() != 0) {
                    add_data();
                    Intent intent = new Intent(add_account.this, account_list.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Title, Username and Password must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void add_data() {
        //Create a contentvalue to insert to database
        long id = System.currentTimeMillis() / 1000;
        System.out.println(title.getText().toString());
        ContentValues values = new ContentValues();
        values.put("ID", id + "");
        values.put("TITLE", title.getText().toString());
        values.put("URL", url.getText().toString());
        values.put("USERNAME", username.getText().toString());
        values.put("PASSWORD", password.getText().toString());
        values.put("NOTES", notes.getText().toString());
        DBhelper.addData(values);
        DBhelper.closeDB();
    }

    /**
     * onClick listener to the PasswordRandomGeneration activity
     * @param view
     */
    public void toRandomGenerateActivity(View view) {
        Intent intent = new Intent(this, PasswordRandomGeneration.class);
        String message = password.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    /**
     * overrides the on activity result to put the generated password to the edittext block
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String pwGen = data.getStringExtra(PasswordRandomGeneration.EXTRA_REPLY);
                password.setText(pwGen);
            }
        }
    }

    /**
     * onClick handler of the SHOW button that simply displays the password
     * @param view
     */
    public void switchPasswordVisibility(View view) {
        String mode = show_password.getText().toString();

        if (mode.equals("Show")) {
            Log.d(LOG_TAG, "switchPasswordVisibility: show");
            show_password.setText(R.string.button_label_hide);
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        }

        else if (mode.equals("Hide")) {
            Log.d(LOG_TAG, "switchPasswordVisibility: hide");
            show_password.setText(R.string.button_label_show);
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        else {
            Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
