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


public class add_account extends AppCompatActivity {
    private static final String EXTRA_MESSAGE = "com.example.passwordmanager.extra.MESSAGE";
    private static final int TEXT_REQUEST = 1;
    public static final String LOG_TAG = add_account.class.getSimpleName();

    private EditText mTitle, mUrl, mUsername, mPassword, mNotes;
    private Button mShowPassword, mCancel, mAdd;
    DatabaseHelper DBhelper;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_acount);

        context = add_account.this;
        DBhelper = new DatabaseHelper(this);

        mTitle = (EditText) findViewById(R.id.title_edit);
        mUrl = (EditText) findViewById(R.id.url_edit);
        mUsername = (EditText) findViewById(R.id.user_edit);
        mPassword = (EditText) findViewById(R.id.password_edit);
        mNotes = (EditText) findViewById(R.id.note_edit);

        mShowPassword = (Button) findViewById(R.id.show);
        mCancel = (Button) findViewById(R.id.cancel);
        mAdd = (Button) findViewById(R.id.add);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Back to Account list
                Intent intent = new Intent(add_account.this, account_list.class);
                startActivity(intent);
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Title, password and username must be filled
                if (mTitle.getText().toString().length() != 0 && mUsername.getText().toString().length() != 0 && mPassword.getText().toString().length() != 0) {
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
        System.out.println(mTitle.getText().toString());
        ContentValues values = new ContentValues();
        values.put("ID", id + "");
        values.put("TITLE", mTitle.getText().toString());
        values.put("URL", mUrl.getText().toString());
        values.put("USERNAME", mUsername.getText().toString());
        values.put("PASSWORD", mPassword.getText().toString());
        values.put("NOTES", mNotes.getText().toString());
        DBhelper.addData(values);
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
}
