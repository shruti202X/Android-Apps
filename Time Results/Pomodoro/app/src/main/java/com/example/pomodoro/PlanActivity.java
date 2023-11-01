package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

public class PlanActivity extends AppCompatActivity {

    private EditText titleEditText;
    private NumberPicker workTimePicker;
    private NumberPicker breakTimePicker;
    private Spinner prioritySpinner;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;

    private AutoCompleteTextView tagsAutoCompleteTextView;
    private Button okButton;
    private LinearLayout tagsLinearLayout;

    private List<Long> tags;
    private DbHelper dbHelper;

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "prefferedData";
    private static final String WORK_TIME_KEY = "preferred_work_time";
    private static final String BREAK_TIME_KEY = "preferred_break_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        titleEditText = findViewById(R.id.editText_title);
        workTimePicker = findViewById(R.id.numberPicker_work_time);
        breakTimePicker = findViewById(R.id.numberPicker_break_time);
        prioritySpinner = findViewById(R.id.spinner_priority);
        hourPicker = findViewById(R.id.numberPicker_hour);
        minutePicker = findViewById(R.id.numberPicker_minute);
        tagsAutoCompleteTextView = findViewById(R.id.autoCompleteTextView_tags);
        okButton = findViewById(R.id.button_ok);
        tagsLinearLayout = findViewById(R.id.layout_tags);

        titleEditText.setText("Untitled");

        workTimePicker.setMaxValue(120);
        workTimePicker.setMinValue(1);

        breakTimePicker.setMaxValue(60);
        breakTimePicker.setMinValue(1);

        hourPicker.setMaxValue(48);
        hourPicker.setMinValue(0);

        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(1);

        // Retrieve saved values from SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        workTimePicker.setValue(sharedPreferences.getInt(WORK_TIME_KEY, 25));
        breakTimePicker.setValue(sharedPreferences.getInt(BREAK_TIME_KEY, 5));

        // Set spinner initial value
        prioritySpinner.setSelection(1); // MEDIUM

        // Set the hour and minute values
        hourPicker.setValue(2);
        minutePicker.setValue(1);

        tags = new ArrayList<Long>();
        dbHelper = new DbHelper(this);

        tagsAutoCompleteTextView.setThreshold(1);
        List<String> tagSuggestions = getAllTags();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tagSuggestions);
        tagsAutoCompleteTextView.setAdapter(arrayAdapter);
        tagsAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTag = (String) parent.getItemAtPosition(position);
                tagsLinearLayout.addView(createTagTextView(selectedTag));
                long tagId = getTagId(selectedTag); // Check if tag exists and get its ID
                if (tagId == -1) {
                    // Tag doesn't exist, insert it
                    tagId = insertTag(selectedTag);
                }
                tags.add((Long) tagId);
                //System.out.println("tags = "+tags);
            }
        });
        tagsAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Unused
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Unused
            }

            //TO DO
            //make your own arrayAdapter I guess to deal with having allsuggestions, m, ma, mat, math, maths when typing maths
            //or delete prevInput
            //or let it be like this
            //or do something like arrayAdappter.clear(); arrayAdapter.addAll(tagSuggestions); arrayAdapter.add(currentInput);
            @Override
            public void afterTextChanged(Editable editable) {
                String currentInput = editable.toString().trim();
                //System.out.println("currentInput = " + currentInput);
                if (!currentInput.isEmpty()) {
                    // Add the current input as a suggestion
                    if (!tagSuggestions.contains(currentInput)) {
                        arrayAdapter.add(currentInput);
                        //tagSuggestions.add(currentInput);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the entered values to SharedPreferences
                saveValuesToSharedPreferences();

                //Save to DataBase
                long planId = saveToDataBase();

                //In Bundle pass the planId
                Bundle bundle = new Bundle();
                bundle.putLong("planId", planId);

                // Start MainActivity and clear the activity stack
                Intent intent = new Intent(PlanActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });
    }

    private void saveValuesToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WORK_TIME_KEY, workTimePicker.getValue());
        editor.putInt(BREAK_TIME_KEY, breakTimePicker.getValue());
        editor.apply();
    }

    private long saveToDataBase(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Inserting data
        ContentValues values = new ContentValues();
        values.put(DbHelper.MyContract.Plan.COLUMN_TITLE, titleEditText.getText().toString());
        values.put(DbHelper.MyContract.Plan.COLUMN_WORK_TIME, workTimePicker.getValue());
        values.put(DbHelper.MyContract.Plan.COLUMN_BREAK_TIME, breakTimePicker.getValue());
        values.put(DbHelper.MyContract.Plan.COLUMN_PRIORITY, prioritySpinner.getSelectedItem().toString());
        values.put(DbHelper.MyContract.Plan.COLUMN_TIME, (hourPicker.getValue() * 60) + minutePicker.getValue());
        values.put(DbHelper.MyContract.Plan.COLUMN_TAGS, tags.toString());
        //System.out.println("tags added are " + tags.toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DbHelper.MyContract.Plan.TABLE_NAME, null, values);

        // Check if the insertion was successful
        if (newRowId != -1) {
            Toast.makeText(this, "Plan inserted at "+newRowId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error inserting plan", Toast.LENGTH_SHORT).show();
        }

        // Don't forget to close the database connection
        db.close();

        return newRowId;
    }
    private TextView createTagTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);

        // Set the background to the default editbox_background
        Drawable editBoxBackground = ContextCompat.getDrawable(this, android.R.drawable.editbox_background);
        textView.setBackground(editBoxBackground);

        // Set text style to bold
        textView.setTypeface(null, Typeface.BOLD);

        // Set other properties as needed
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        return textView;
    }
    private long getTagId(String tag) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {DbHelper.MyContract.Tag._ID};
        String selection = DbHelper.MyContract.Tag.COLUMN_TAG + "=?";
        String[] selectionArgs = {tag};

        Cursor cursor = db.query(
                DbHelper.MyContract.Tag.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        long tagId = -1;

        if (cursor != null && cursor.moveToFirst()) {
            tagId = cursor.getLong(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Tag._ID));
            cursor.close();
        }

        db.close();
        return tagId;
    }
    private long insertTag(String tag) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbHelper.MyContract.Tag.COLUMN_TAG, tag);

        long tagId = db.insert(DbHelper.MyContract.Tag.TABLE_NAME, null, values);

        db.close();
        return tagId;
    }
    public List<String> getAllTags() {
        List<String> tagList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {DbHelper.MyContract.Tag.COLUMN_TAG};
        Cursor cursor = db.query(
                DbHelper.MyContract.Tag.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String tag = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.MyContract.Tag.COLUMN_TAG));
                tagList.add(tag);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return tagList;
    }
}