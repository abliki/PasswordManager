package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AccountList extends AppCompatActivity {
    public static final String LOG_TAG = AccountList.class.getSimpleName();

    private ExpandableListView mListView;
    private FloatingActionButton add_button;
    DatabaseHelper DBhelper;
    private Crypter crypter;

    private AccountListAdapter adapter;
    private Context context;
    private String masterKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list);

        context = AccountList.this;
        DBhelper = new DatabaseHelper(this);
        crypter = new Crypter();

        Intent intent = getIntent();
        masterKey = intent.getStringExtra(LoginPage.PASSWORD_KEY);

        DBhelper.setMasterKey(masterKey);

        net.sqlcipher.database.SQLiteDatabase.loadLibs(this);

        // init views
        mListView = (ExpandableListView) findViewById(R.id.password_list);
        add_button = (FloatingActionButton) findViewById(R.id.float_button_toadd);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountList.this, AddAccount.class);
                intent.putExtra(LoginPage.PASSWORD_KEY, masterKey);
                startActivity(intent);
            }
        });

        try {
            populateList();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }




    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.password_list){
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

        if(menuItems[menuItemId].equals("Edit")) {

            Intent intent = new Intent(AccountList.this, AddAccount.class);
            intent.putExtra("ID", id);
            intent.putExtra("Title", data.getString(1));
            intent.putExtra("URL", data.getString(1));
            intent.putExtra("Username", data.getString(1));
            intent.putExtra("Password", data.getString(1));
            intent.putExtra("Notes", data.getString(1));
            intent.putExtra(LoginPage.PASSWORD_KEY, masterKey);
            startActivity(intent);

//            try {
//                populateList();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            }
            return true;
        }
        else if(menuItems[menuItemId].equals("Delete")) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DBhelper.deleteData(id);
                    DBhelper.closeDB();

                    try {
                        populateList();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
            };
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // cancel the dialog by do nothing
                        }
                    })
                    .show();
        }
        return true;
    }

    /**
     * Get data from sqlite database and reflect the information on a list to show
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    private void populateList() throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        //get the data and append to a list
        Cursor data = DBhelper.getData();
        ArrayList<List<String>> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column TITLE
            //then add it to the ArrayList
            listData.add(Arrays.asList(
                    data.getString(0),
                    crypter.decrypt(data.getString(1), data.getString(0)),
                    crypter.decrypt(data.getString(2), data.getString(0)),
                    crypter.decrypt(data.getString(3), data.getString(0)),
                    crypter.decrypt(data.getString(4), data.getString(0)),
                    crypter.decrypt(data.getString(5), data.getString(0))
                    ));
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
