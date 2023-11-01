package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pomodoro.DbHelper;

public class MemoActivity extends AppCompatActivity {

    private Button okButton;
    private Button cancelButton;
    private TextView multiLineText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        okButton = findViewById(R.id.okButton);
        cancelButton = findViewById(R.id.cancelButton);
        multiLineText = findViewById(R.id.multiLineText);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMemo();
                endMemoActivity();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endMemoActivity();
            }
        });
    }
    public void endMemoActivity(){
        // Set the result as OK
        setResult(RESULT_OK);

        // Finish the MemoActivity and go back to MainActivity
        finish();
    }

    private void saveMemo(){
        String memoText = multiLineText.getText().toString();
        Intent intent = getIntent();
        if(intent==null) return;

        int sessionNumber = intent.getIntExtra("SESSION_NUMBER", 1);
        int workType = intent.getBooleanExtra("WORK_TYPE", true)?1:0;
        long pomodoroTime = intent.getLongExtra("SESSION_POMODORO_TIME", 0);
        long breakTime = intent.getLongExtra("SESSION_BREAK_TIME", 0);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Inserting data
        ContentValues values = new ContentValues();
        values.put(DbHelper.MyContract.Memo.COLUMN_2, memoText);
        values.put(DbHelper.MyContract.Memo.COLUMN_3, sessionNumber);
        values.put(DbHelper.MyContract.Memo.COLUMN_4, workType);
        values.put(DbHelper.MyContract.Memo.COLUMN_5, pomodoroTime);
        values.put(DbHelper.MyContract.Memo.COLUMN_6, breakTime);

        long newRowId = db.insert(DbHelper.MyContract.Memo.TABLE_NAME, null, values);

        Toast.makeText(this, "Added Memo "+newRowId, Toast.LENGTH_SHORT).show();
    }
}
