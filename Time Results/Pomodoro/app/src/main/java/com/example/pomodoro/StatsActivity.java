package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.pomodoro.DbHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
public class StatsActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DbHelper.MyContract.Memo.TABLE_NAME,
                null, // Passing null for the projection retrieves all columns
                null,
                null,
                null,
                null,
                null
        );

        int[][] intData = new int[cursor.getCount()][5];
        String[][] strData = new String[cursor.getCount()][2];

        for(int i=0; cursor.moveToNext(); i++) {
            int memo_id = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Memo._ID));
            String column1 = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Memo.COLUMN_1));
            String column2 = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Memo.COLUMN_2));
            int column3 = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Memo.COLUMN_3));
            int column4 = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Memo.COLUMN_4));
            int column5 = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Memo.COLUMN_5)) / 1000;
            int column6 = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Memo.COLUMN_6)) / 1000;

            int[] intDataArr = intData[i];
            intDataArr[0] = memo_id;
            intDataArr[1] = column3;
            intDataArr[2] = column4;
            intDataArr[3] = column5;
            intDataArr[4] = column6;
            strData[i][0] = column1;
            strData[i][1] = column2;
        }

        cursor.close();
        db.close();

        tableLayout = findViewById(R.id.tableLayout);

        for (int i = 0; i < intData.length; i++) {
            // Create a new TableRow
            TableRow tableRow = new TableRow(this);
            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(layoutParams);

            int[] intDataArr = intData[i];

            // Create TextViews for each column and set the data
            TextView column0TextView = createTextView(intDataArr[0], 0.2f);
            TextView column1TextView = createTextView(strData[i][0], 1f);
            TextView column2TextView = createTextView(strData[i][1], 2f);
            TextView column3TextView = createTextView(intDataArr[1], 0.4f);
            TextView column5TextView = createTextView(intDataArr[3], 0.6f);
            TextView column6TextView = createTextView(intDataArr[4], 0.6f);

            // Add the TextViews to the TableRow
            tableRow.addView(column0TextView);
            tableRow.addView(column1TextView);
            tableRow.addView(column2TextView);
            tableRow.addView(column3TextView);
            tableRow.addView(column5TextView);
            tableRow.addView(column6TextView);

            // Add the TableRow to the TableLayout
            tableLayout.addView(tableRow);
        }
    }
    private TextView createTextView(String text, float weight) {
        TextView textView = new TextView(this);

        textView.setText(text);

        Drawable table_outline = ContextCompat.getDrawable(this, R.drawable.table_outline);
        textView.setBackground(table_outline);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = weight;
        textView.setLayoutParams(layoutParams);

        return textView;
    }
    private TextView createTextView(int num, float weight) {
        return createTextView(Integer.toString(num), weight);
    }
}