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

