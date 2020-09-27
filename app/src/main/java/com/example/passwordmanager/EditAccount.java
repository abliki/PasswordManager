package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;


public class EditAccount extends AppCompatActivity {
    private EditText title,url,username,password,notes;
    private Button generate_password, show_password, cancel, save;
    DatabaseHelper DBhelper;
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Intent mIntent = getIntent();
        final long id = mIntent.getIntExtra("ID", '0');

        context = EditAccount.this;
        DBhelper = new DatabaseHelper(this);

        title = (EditText) findViewById(R.id.title_edit);
        url = (EditText) findViewById(R.id.url_edit);
        username = (EditText) findViewById(R.id.user_edit);
        password = (EditText) findViewById(R.id.password_edit);
        notes = (EditText) findViewById(R.id.note_edit);

        Cursor data = DBhelper.getData();
        while (data.getLong(0)!=id){
            data.moveToNext();
        }
        title.setText(data.getString(1));
        url.setText(data.getString(2));
        username.setText(data.getString(3));
        password.setText(data.getString(4));
        notes.setText(data.getString(5));




        generate_password = (Button) findViewById(R.id.generate);
        show_password = (Button) findViewById(R.id.show);
        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Back to Account list
                Intent intent = new Intent(EditAccount.this, AccountList.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Title, password and username must be filled
                if (title.getText().toString().length() != 0
                        && username.getText().toString().length() != 0
                        && password.getText().toString().length() != 0){
                    update_data(id);
                    Intent intent = new Intent(EditAccount.this, AccountList.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Title, Username and Password must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void update_data(long id){
        //Create a contentvalue to insert to database

        System.out.println(title.getText().toString());
        ContentValues values = new ContentValues();
        values.put("ID",id+"");
        values.put("TITLE", title.getText().toString());
        values.put("URL", url.getText().toString());
        values.put("USERNAME", username.getText().toString());
        values.put("PASSWORD", password.getText().toString());
        values.put("NOTES", notes.getText().toString());
        DBhelper.updateData(values);
        DBhelper.closeDB();

    }

}
