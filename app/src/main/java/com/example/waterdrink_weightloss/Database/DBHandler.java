package com.example.waterdrink_weightloss.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.

    Context context ;
    private static final String DB_NAME = "water_drink_target";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "target";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our day column
    private static final String DAY = "day";

    // below variable id for our month column.
    private static final String MONTH = "month";

    // below variable for our year column.
    private static final String YEAR = "year";

    // below variable is for our achievement column.
    private static final String ACHIEVEMENT = "achievement";

    //below variable for our water add record column.
    private static final String GLASS_ADD_RECORD = "glass_add_record";


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DAY + " INTEGER,"
                + MONTH + " INTEGER,"
                + YEAR + " INTEGER,"
                + ACHIEVEMENT + " INTEGER,"
                + GLASS_ADD_RECORD + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new day to our sqlite database.
    public void addNewDayRecord(int day, int month, int year, int achieve, String glass_add) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(DAY, day);
        values.put(MONTH, month);
        values.put(YEAR, year);
        values.put(ACHIEVEMENT, achieve);
        values.put(GLASS_ADD_RECORD,glass_add);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);
        Log.d("add",day + "--"  + month + "--" + year);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Log.d("Drop","Drop Table");
        onCreate(db);
    }

    // below is the method for updating water achievement data
    public void updateData(int id,int day, int month, int year, int update_achievement, String glass_add) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ID_COL,id);
        values.put(DAY, day);
        values.put(MONTH, month);
        values.put(YEAR, year);
        values.put(ACHIEVEMENT, update_achievement);
        values.put(GLASS_ADD_RECORD,glass_add);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(TABLE_NAME, values, "id=?", new String[]{String.valueOf(id)});

        Log.d("update",id + "**" + day +"--" + "--" + month + "--" + year + "--"  + update_achievement);
        db.close();
    }

    // below is the method for deleting our day record.
    public void deleteCourse(int id) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // we have created a new method for reading all the courses.
    public ArrayList<DataModel> readData() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<DataModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new DataModel(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5)));
                //Toast.makeText(context, cursor.getInt(4)+"", Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        //Log.d("read",courseModalArrayList.size()+"");
        return courseModalArrayList;
    }

    // we have created a new method for reading the courses of Specific Date .
    public ArrayList<DataModel> readDataDateWise(int day , int month , int year) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE day=" + day +" AND month=" + month + " AND year=" + year, null);

        // on below line we are creating a new array list.
        ArrayList<DataModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new DataModel(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5)));
                //Toast.makeText(context, cursor.getInt(4)+"", Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        //Log.d("read",courseModalArrayList.size()+"");
        return courseModalArrayList;
    }

    // we have created a new method for reading the courses Week Wise .
    public ArrayList<DataModel> readDataWeekWise(int day1 ,int month1 , int year1 ,
                                                 int day7, int month7 , int year7) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        // on below line we are creating a cursor with query to read data from database.

        //This Condition for No data found write in graph ,if data not in specific date range
        if(month1==month7)
        {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE (day>=" + day1
                    + " AND month=" + month1 + " AND year=" + year1 +")"
                    + " AND (day<=" + day7 + " AND month=" + month7
                    + " AND year=" + year7 + ")", null);
        }
        else{
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE (day>=" + day1
                    + " AND month=" + month1 + " AND year=" + year1 +")"
                    + " OR (day<=" + day7 + " AND month=" + month7
                    + " AND year=" + year7 + ")", null);
        }

        // on below line we are creating a new array list.
        ArrayList<DataModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new DataModel(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5)));
                //Toast.makeText(context, cursor.getInt(1)+"", Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        //Log.d("read",courseModalArrayList.size()+"");
        return courseModalArrayList;
    }

    // we have created a new method for reading all the courses month and year wise.
    public ArrayList<DataModel> readDataMonthWise(int month,int year) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE month=+" + month + " AND year=+" + year, null);

        // on below line we are creating a new array list.
        ArrayList<DataModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new DataModel(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5)));
                //Toast.makeText(context, cursor.getInt(4)+"", Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        //Log.d("read",courseModalArrayList.size()+"");
        return courseModalArrayList;
    }

    // we have created a new method for reading all the courses year wise.
    public ArrayList<DataModel> readDataYearWise(int year) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE year=" + year, null);

        // on below line we are creating a new array list.
        ArrayList<DataModel> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new DataModel(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5)));
                //Toast.makeText(context, cursor.getInt(4)+"", Toast.LENGTH_SHORT).show();
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        //Log.d("read",courseModalArrayList.size()+"");
        return courseModalArrayList;
    }

}
