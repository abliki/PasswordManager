package com.example.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccoundList extends AppCompatActivity {

    private ExpandableListView mListView;
    private FloatingActionButton add_button;
    DatabaseHelper DBhelper;

    private AccountListAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list);
        context = AccoundList.this;
        DBhelper = new DatabaseHelper(this);
        mListView = (ExpandableListView) findViewById(R.id.acclist);
        add_button = (FloatingActionButton) findViewById(R.id.toadd);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccoundList.this, add_account.class);
                startActivity(intent);
            }
        });

        populateList();
    }

    private void populateList() {
        //get the data and append to a list
        Cursor data = DBhelper.getData();
        ArrayList<List<String>> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column TITLE
            //then add it to the ArrayList
            listData.add(Arrays.asList(data.getString(0), data.getString(1), data.getString(2),
                    data.getString(3), data.getString(4), data.getString(5)));
        }
        data.close();
        //create the list adapter and set the adapter
        adapter = new AccountListAdapter(this.context,listData);
        mListView.setAdapter(adapter);
    }
}
