In Android, there are several ways to store data depending on your requirements and the type of data you want to store. Here are some commonly used methods:

1. Shared Preferences: Shared Preferences allow you to store small amounts of primitive data (such as key-value pairs) in key-value pairs. This is ideal for storing simple user preferences or application settings. You can use the `SharedPreferences` class to read from and write to shared preferences.

2. Internal Storage: Android provides a private internal storage directory for each app, where you can store private data files. You can use the `Context` class's `openFileOutput()` and `openFileInput()` methods to create and access files in internal storage, respectively.

3. External Storage: If you need to store larger files or files that can be accessed by other apps or the user, you can use external storage. You can check if external storage is available and then use the `Environment.getExternalStorageDirectory()` method to get the root directory of the external storage. However, starting from Android 10 (API level 29), accessing external storage requires the `READ_EXTERNAL_STORAGE` or `WRITE_EXTERNAL_STORAGE` permission.

4. SQLite Database: If you have structured data or need to perform complex queries, you can use an SQLite database. Android provides the `SQLiteOpenHelper` class to manage database creation and version management. You can create tables, insert, update, delete, and query data using SQL commands.

5. Content Providers: Content Providers allow you to share data between different apps in a controlled manner. They provide a standard interface for accessing and manipulating data. Content Providers are useful when you want to expose your app's data to other apps or access data from other apps.

6. Network Storage: If your data needs to be stored remotely, you can use network storage solutions such as cloud storage, databases hosted on servers, or APIs to store and retrieve data.

Choose the storage method that best suits your needs based on the size and type of data, accessibility requirements, and security considerations.

---

To use an SQLite database in Android, you can follow these steps:

1. Define a Contract Class: Create a contract class that defines the structure of your database, including table names, column names, and SQL queries. This class will act as a contract between your code and the database. Here's an example:

```java
public final class MyContract {
    private MyContract() {} // Private constructor to prevent instantiation
    
    public static class MyEntry implements BaseColumns {
        public static final String TABLE_NAME = "my_table";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
    }
}
```

2. Create a Database Helper Class: Create a class that extends `SQLiteOpenHelper`. This class will handle the creation, upgrading, and opening of the database. Override the `onCreate()` and `onUpgrade()` methods. Here's an example:

```java
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + MyContract.MyEntry.TABLE_NAME + " (" +
                MyContract.MyEntry._ID + " INTEGER PRIMARY KEY," +
                MyContract.MyEntry.COLUMN_NAME_TITLE + " TEXT," +
                MyContract.MyEntry.COLUMN_NAME_DESCRIPTION + " TEXT)";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}
```

3. Access the Database: To access the database, create an instance of the `MyDatabaseHelper` class and use `getReadableDatabase()` or `getWritableDatabase()` to get an instance of `SQLiteDatabase`. You can then perform various operations such as inserting, querying, updating, and deleting data. Here's an example:

```java
MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
SQLiteDatabase db = dbHelper.getWritableDatabase();

// Inserting data
ContentValues values = new ContentValues();
values.put(MyContract.MyEntry.COLUMN_NAME_TITLE, "Title");
values.put(MyContract.MyEntry.COLUMN_NAME_DESCRIPTION, "Description");
long newRowId = db.insert(MyContract.MyEntry.TABLE_NAME, null, values);

// Querying data
String[] projection = {
    MyContract.MyEntry._ID,
    MyContract.MyEntry.COLUMN_NAME_TITLE,
    MyContract.MyEntry.COLUMN_NAME_DESCRIPTION
};

Cursor cursor = db.query(
    MyContract.MyEntry.TABLE_NAME,
    projection,
    null,
    null,
    null,
    null,
    null
);

// Iterating over the cursor and accessing data
while (cursor.moveToNext()) {
    long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(MyContract.MyEntry._ID));
    String title = cursor.getString(cursor.getColumnIndexOrThrow(MyContract.MyEntry.COLUMN_NAME_TITLE));
    String description = cursor.getString(cursor.getColumnIndexOrThrow(MyContract.MyEntry.COLUMN_NAME_DESCRIPTION));
    
    // Do something with the data
}

// Closing the cursor and database
cursor.close();
db.close();
```

Remember to close the cursor and database when you are done using them.

These are the basic steps to use an SQLite database in Android. You can perform more advanced operations like updating or deleting data by using appropriate methods provided by the `SQLiteDatabase` class.

---

In Android, the SQLite database files are stored in the app's private internal storage by default. The exact location of the database files depends on the context in which you create the database.

When you create an SQLite database using `SQLiteOpenHelper` or `SQLiteDatabase` class, the database file is stored in the following directory:

`/data/data/<package_name>/databases/`

Here, `<package_name>` represents the package name of your Android application. The `/data/data/` directory is the private internal storage directory specific to your app.

Note that this directory is not directly accessible to users or other apps unless your app has root access or you explicitly provide access through a Content Provider.

Additionally, if you use the `Context` class's `openOrCreateDatabase()` method to create the database, it will be stored in the app's default database directory:

`/data/data/<package_name>/`

In this case, the database file will be directly stored in the app's private internal storage directory without the `databases/` subdirectory.

Remember that the internal storage is specific to your app and is not accessible by other apps or the user unless you explicitly share the data using Content Providers or other means.

---

## [Save data using SQLite](https://developer.android.com/training/data-storage/sqlite)

MainActivity.java:

```java
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ClickDbHelper;

public class MainActivity extends AppCompatActivity {
    private ClickDbHelper dbHelper;
    private EditText textBox;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database helper and UI elements
        dbHelper = new ClickDbHelper(this);
        textBox = findViewById(R.id.editText);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userText = textBox.getText().toString();
                long res = insertRow(userText);
                if (res == -1) {
                    Toast.makeText(MainActivity.this, "Insertion Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Insertion Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
```

ClickDbHelper.java:

```java
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.content.Context;

public class ClickDbHelper extends SQLiteOpenHelper {

    public static final class MyContract {
        // To prevent someone from accidentally instantiating the contract class,
        // make the constructor private.
        private MyContract() {}

        /* Inner class that defines the table contents */
        public static class Click implements BaseColumns {
            public static final String TABLE_NAME = "Click";
            public static final String COLUMN_1 = "Time";
            public static final String COLUMN_2 = "Description";
        }
    }

    //https://www.sqlitetutorial.net/sqlite-date/
    //https://alvinalexander.com/android/sqlite-default-datetime-field-current-time-now/
    //https://www.sqlite.org/datatype3.html

    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + MyContract.Click.TABLE_NAME + " (" +
        MyContract.Click._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        MyContract.Click.COLUMN_1 + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
        MyContract.Click.COLUMN_2 + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + MyContract.Click.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public ClickDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertRow(String description){
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MyContract.Click.COLUMN_2, description);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MyContract.Click.TABLE_NAME, null, values);

        db.close();

        return newRowId;
    }
}
```

---

## Read data from an SQLite database in Android

1. Create an instance of your `ClickDbHelper` class and get a readable database:
   ```java
   ClickDbHelper dbHelper = new ClickDbHelper(context);
   SQLiteDatabase db = dbHelper.getReadableDatabase();
   ```

2. Define a projection, which specifies the columns you want to retrieve from the table:
   ```java
   String[] projection = {
       ClickDbHelper.MyContract.Click._ID,
       ClickDbHelper.MyContract.Click.COLUMN_1,
       ClickDbHelper.MyContract.Click.COLUMN_2
   };
   ```

3. Perform a query on the database using the `query` method:
   ```java
   Cursor cursor = db.query(
       ClickDbHelper.MyContract.Click.TABLE_NAME,
       projection,
       null,
       null,
       null,
       null,
       null
   );
   ```

   This query retrieves all columns and rows from the specified table. You can customize the parameters of the `query` method based on your specific requirements (e.g., adding selection, selection arguments, sorting order).

4. Iterate over the `Cursor` to access the retrieved data:
   ```java
   while (cursor.moveToNext()) {
       long id = cursor.getLong(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click._ID));
       String column1 = cursor.getString(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click.COLUMN_1));
       String column2 = cursor.getString(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click.COLUMN_2));
       
       // Process the retrieved data as needed
   }
   ```

   In the above code, `getColumnIndexOrThrow` retrieves the index of the column based on the column name.

5. Close the `Cursor` and the database connection when you're done:
   ```java
   cursor.close();
   db.close();
   ```

Here's an example that demonstrates the complete process:

```java
ClickDbHelper dbHelper = new ClickDbHelper(context);
SQLiteDatabase db = dbHelper.getReadableDatabase();

String[] projection = {
    ClickDbHelper.MyContract.Click._ID,
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

while (cursor.moveToNext()) {
    long id = cursor.getLong(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click._ID));
    String column1 = cursor.getString(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click.COLUMN_1));
    String column2 = cursor.getString(cursor.getColumnIndexOrThrow(ClickDbHelper.MyContract.Click.COLUMN_2));
    
    // Process the retrieved data as needed
}

cursor.close();
db.close();
```

Make sure to adjust the code based on your specific table structure, column names, and query requirements.

---

To display SQLite database data in a table format in Android, you can use a `ListView` along with a custom adapter. Here's an example of how you can achieve this:

1. Create a new layout file called `list_item_layout.xml` for each row in the table:
   ```xml
   <!-- list_item_layout.xml -->
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:orientation="horizontal">

       <TextView
           android:id="@+id/textViewColumn1"
           android:layout_width="0dp"
           android:layout_height="wrap_content"
           android:layout_weight="1"
           android:textAppearance="@android:style/TextAppearance.Medium"/>

       <TextView
           android:id="@+id/textViewColumn2"
           android:layout_width="0dp"
           android:layout_height="wrap_content"
           android:layout_weight="1"
           android:textAppearance="@android:style/TextAppearance.Medium"/>
   </LinearLayout>
   ```

2. Modify your `MainActivity` to include a `ListView` and set up the adapter to display the data:
   ```java
   import android.support.v7.app.AppCompatActivity;
   import android.os.Bundle;
   import android.view.View;
   import android.widget.AdapterView;
   import android.widget.ArrayAdapter;
   import android.widget.ListView;
   import android.database.Cursor;
   import com.yourpackage.ClickDbHelper;

   public class MainActivity extends AppCompatActivity {
       private ClickDbHelper dbHelper;
       private ListView listView;

       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);

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
           ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, data);

           // Set the adapter for the ListView
           listView.setAdapter(adapter);
       }
   }
   ```

Make sure to adjust the code based on your specific table structure, column names, and layout requirements. The example assumes you have a `ListView` with the ID `listView` in your `activity_main.xml` layout file.

---

## Starting with the app

### Creating the activity_main.xml file

It has an App Bar with the text, "What are you feeling".
A Relative Layout consisting of EditText and an OK Button.
Another button "Show Stats".
All these components are bounded by a Relative Layout.

[activity_main.xml](activity_main.xml)

### Creating the ClickDbHelper Class

`ClickDbHelper` should extend `SQLiteOpenHelper`.
Create `MyContract` class to define Schema.
Using [official documentation](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper) override necessary functions

[ClickDbHelper.java](ClickDbHelper.java)
