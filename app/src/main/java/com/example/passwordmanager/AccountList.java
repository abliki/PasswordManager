package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountList extends AppCompatActivity {

    private ExpandableListView mListView;
    private FloatingActionButton add_button;
    DatabaseHelper DBhelper;

    private AccountListAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list);
        context = AccountList.this;
        DBhelper = new DatabaseHelper(this);
        mListView = (ExpandableListView) findViewById(R.id.acclist);
        add_button = (FloatingActionButton) findViewById(R.id.toadd);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountList.this, AddAccount.class);
                startActivity(intent);
            }
        });

        populateList();



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.acclist){
            String[] menuItems = getResources().getStringArray(R.array.edit_meun);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Cursor data = DBhelper.getData();
        final ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        data.moveToPosition(ExpandableListView.getPackedPositionGroup(info.packedPosition));

        int menuItemId = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.edit_meun);
        final long id = data.getLong(0);
        Log.d("IDinAcc", Long.toString(id));

        if(menuItems[menuItemId].equals("Edit")) {

            Intent intent = new Intent(AccountList.this, EditAccount.class);
            intent.putExtra("ID", id);
            intent.putExtra("Title", data.getString(1));
            intent.putExtra("URL", data.getString(2));
            intent.putExtra("Username", data.getString(3));
            intent.putExtra("Password", data.getString(4));
            intent.putExtra("Notes", data.getString(5));
            startActivity(intent);
            populateList();
            return true;
        }
        else if(menuItems[menuItemId].equals("Delete")) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DBhelper.deleteData(id);
                    populateList();
                }
            };
            new AlertDialog.Builder(this).setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        }
        return true;
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
        DBhelper.closeDB();
        //create the list adapter and set the adapter
        adapter = new AccountListAdapter(this.context,listData);
        mListView.setIndicatorBounds(0, 20);
        mListView.setAdapter(adapter);
        registerForContextMenu(mListView);
    }
}
