//DataBase connection is open for extended period

package com.example.random_number_inbetween;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import com.example.random_number_inbetween.ClickDbHelper;

public class MainActivity extends AppCompatActivity {
    private ClickDbHelper dbHelper;
    private SQLiteDatabase writableDb;
    private EditText userTextBox;
    private Button okButton;
    private Button statsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper and UI elements
        dbHelper = new ClickDbHelper(this);
        writableDb = dbHelper.getWritableDatabase();
        userTextBox = (EditText) findViewById(R.id.editTextFeeling);
        okButton = (Button) findViewById(R.id.buttonOK);
        statsButton = (Button) findViewById(R.id.buttonShowStats);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userText = userTextBox.getText().toString();
                long rowId = insertRow(userText);
                if (rowId == -1) {
                    Toast.makeText(MainActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Insertion Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        writableDb.close();
    }

    public long insertRow(String description){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ClickDbHelper.MyContract.Click.COLUMN_2, description);

        // Insert the new row, returning the primary key value of the new row
        return writableDb.insert(ClickDbHelper.MyContract.Click.TABLE_NAME, null, values);
    }
}
