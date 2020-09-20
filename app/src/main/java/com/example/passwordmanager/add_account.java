package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class add_account extends AppCompatActivity {
    private EditText title,url,username,password,notes;
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
                if (title.getText().toString().length() != 0 && username.getText().toString().length() != 0 && password.getText().toString().length() != 0){
                    add_data();
                    Intent intent = new Intent(add_account.this, account_list.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Title, Username and Password must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void add_data(){
            //Create a contentvalue to insert to database
            long id = System.currentTimeMillis()/1000;
            System.out.println(title.getText().toString());
            ContentValues values = new ContentValues();
            values.put("ID",id+"");
            values.put("TITLE", title.getText().toString());
            values.put("URL", url.getText().toString());
            values.put("USERNAME", username.getText().toString());
            values.put("PASSWORD", password.getText().toString());
            values.put("NOTES", notes.getText().toString());
            DBhelper.addData(values);
            DBhelper.closeDB();
    }

}
