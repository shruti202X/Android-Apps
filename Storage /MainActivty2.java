package com.example.random_number_inbetween;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.database.Cursor;
import com.example.random_number_inbetween.ClickDbHelper;

public class MainActivity2 extends AppCompatActivity {
    private ClickDbHelper dbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize database helper and UI elements
        dbHelper = new ClickDbHelper(this);
        listView = findViewById(R.id.listView);

        displayData();
    }

    private void displayData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                ClickDbHelper.MyContract.Click.COLUMN_1,
                ClickDbHelper.MyContract.Click.COLUMN_2
        };

        Cursor cursor = db.query(
                ClickDbHelper.MyContract.Click.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Create an array to store the data for the adapter
        String[] data = new String[cursor.getCount()];
        int index = 0;

        while (cursor.moveToNext()) {
            String column1 = cursor.getString(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click.COLUMN_1));
            String column2 = cursor.getString(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click.COLUMN_2));

            // Combine the columns into a single string
            String rowData = column1 + " - " + column2;
            data[index] = rowData;
            index++;
        }

        cursor.close();
        db.close();

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }
}
