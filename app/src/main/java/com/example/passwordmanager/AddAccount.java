package com.example.passwordmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class AddAccount extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "com.example.passwordmanager.extra.MESSAGE";
    private static final int TEXT_REQUEST = 1;
    public static final String LOG_TAG = AddAccount.class.getSimpleName();


    private EditText mTitle, mUrl, mUsername, mPassword, mNotes;
    private Button mShowPassword, mCancel, mAdd;
    DatabaseHelper DBhelper;
    private Context context;
    private Crypter crypter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Intent mIntent = this.getIntent();
        final long id = mIntent.getLongExtra("ID", 0);

        context = AddAccount.this;
        DBhelper = new DatabaseHelper(this);
        crypter = new Crypter();

        mTitle = (EditText) findViewById(R.id.title_edit);
        mUrl = (EditText) findViewById(R.id.url_edit);
        mUsername = (EditText) findViewById(R.id.user_edit);
        mPassword = (EditText) findViewById(R.id.password_edit);
        mNotes = (EditText) findViewById(R.id.note_edit);

        mShowPassword = (Button) findViewById(R.id.show);
        mCancel = (Button) findViewById(R.id.cancel);
        mAdd = (Button) findViewById(R.id.add);

        if (id != 0){
            try {
                setOriginalText(mIntent, id);
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

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Back to Account list
                Intent intent = new Intent(AddAccount.this, AccountList.class);
                startActivity(intent);
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Title, password and username must be filled
                if (mTitle.getText().toString().length() != 0
                        && mUsername.getText().toString().length() != 0
                        && mPassword.getText().toString().length() != 0) {

                    try {
                        if (id == 0){
                            add_data();
                        }
                        else {
                            update_data(id);
                        }
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
                    Intent intent = new Intent(AddAccount.this, AccountList.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(context, "Title, Username and Password must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void add_data() throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        //Create a contentvalue to insert to database
        System.out.println(mTitle.getText().toString());
        ContentValues values = new ContentValues();
        long id = System.currentTimeMillis()/1000;

        values.put("ID", id+"");
        values.put("TITLE", crypter.encrypt(context, mTitle.getText().toString(), id+""));
        values.put("URL", crypter.encrypt(context, mUrl.getText().toString(), id+""));
        values.put("USERNAME", crypter.encrypt(context, mUsername.getText().toString(), id+""));
        values.put("PASSWORD", crypter.encrypt(context, mPassword.getText().toString(), id+""));
        values.put("NOTES", crypter.encrypt(context, mNotes.getText().toString(), id+""));

        DBhelper.addData(values);
        DBhelper.closeDB();

    }

    private void update_data(long id) throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        //Create a contentvalue to insert to database
        System.out.println(mTitle.getText().toString());
        ContentValues values = new ContentValues();
        values.put("TITLE", crypter.encrypt(context, mTitle.getText().toString(), id+""));
        values.put("URL", crypter.encrypt(context, mUrl.getText().toString(), id+""));
        values.put("USERNAME", crypter.encrypt(context, mUsername.getText().toString(), id+""));
        values.put("PASSWORD", crypter.encrypt(context, mPassword.getText().toString(), id+""));
        values.put("NOTES", crypter.encrypt(context, mNotes.getText().toString(), id+""));

        DBhelper.updateData(values, id);
        DBhelper.closeDB();

    }

    /**
     * onClick listener to the PasswordRandomGeneration activity
     *
     * @param view
     */
    public void toRandomGenerateActivity(View view) {
        Intent intent = new Intent(this, PasswordRandomGeneration.class);
        String message = mPassword.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    /**
     * overrides the on activity result to put the generated password to the edittext block
     *
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
                mPassword.setText(pwGen);
            }
        }
    }

    /**
     * onClick handler of the SHOW button that simply displays the password
     *
     * @param view
     */
    public void switchPasswordVisibility(View view) {
        String mode = mShowPassword.getText().toString();

        if (mode.equals(getString(R.string.button_label_show))) {
            Log.d(LOG_TAG, "switchPasswordVisibility: show");
            mShowPassword.setText(R.string.button_label_hide);
//            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else if (mode.equals(getString(R.string.button_label_hide))) {
            Log.d(LOG_TAG, "switchPasswordVisibility: hide");
            mShowPassword.setText(R.string.button_label_show);
//            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            Log.d(LOG_TAG, "the show/hide button is neither hide nor show, something wrong");
        }
    }

    private void setOriginalText(Intent mIntent, long id) throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        mTitle.setText(crypter.decrypt(mIntent.getStringExtra("Title"), Long.toString(id)));
        mUrl.setText(crypter.decrypt(mIntent.getStringExtra("URL"), Long.toString(id)));
        mUsername.setText(crypter.decrypt(mIntent.getStringExtra("Username"), Long.toString(id)));
        mPassword.setText(crypter.decrypt(mIntent.getStringExtra("Password"), Long.toString(id)));
        mNotes.setText(crypter.decrypt(mIntent.getStringExtra("Notes"), Long.toString(id)));
    }

}
