package com.example.drupp_driver.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME="pool_riders.db";
    private static final String TABLE_NAME="riderstable";

   // public static final String QUESTION="question";

    public static final String KEY_SUB1="rnum";
    public static final String KEY_SUB2="rname";
    public static final String KEY_SUB3="scity";
    public static final String KEY_SUB4="dcity";
    public static final String KEY_SUB5="status";


    // contructor for the class
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("create table "+TABLE_NAME+" (rider_num integer,rider_name text,scity text,dcity text,ride_status text)");
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                KEY_SUB1 + " VARCHAR, " +
                KEY_SUB2 + " VARCHAR, " +
                KEY_SUB3 + " VARCHAR, " +
                KEY_SUB4 + " VARCHAR, " +
                KEY_SUB5 + " VARCHAR);"
        );
        Log.d(TAG, "onCreate: Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+TABLE_NAME);
        onCreate(db);
    }

    // to insert data in table
    public void insertData(String rider_num,String rider_name,String scity,String dcity,String ride_status)
    {
        SQLiteDatabase db=this.getWritableDatabase();


        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_SUB1,rider_num);
        contentValues.put(KEY_SUB2,rider_name);
        contentValues.put(KEY_SUB3,scity);
        contentValues.put(KEY_SUB4,dcity);
        contentValues.put(KEY_SUB5,ride_status);
        db.insert(TABLE_NAME,null,contentValues);
        Log.d(TAG, "insertData: DATA INSERTED");
      //  db.execSQL("INSERT INTO riderstable VALUES( "+  rider_num +", " + rider_name +", "+ scity + ", " + dcity + ", " + ride_status +");");

    }

    public void reCreateTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(" CREATE TABLE    " + TABLE_NAME + " (" +
                KEY_SUB1 + " VARCHAR, " +
                KEY_SUB2 + " VARCHAR, " +
                KEY_SUB3 + " VARCHAR, " +
                KEY_SUB4 + " VARCHAR, " +
                KEY_SUB5 + " VARCHAR );"
        );
        Log.d(TAG, "onCreate: Database Created");

    }

    // to get data stored in table
    public Cursor getAllData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);
        Log.d(TAG, "getAllData: "+res.toString());
        return res;
    }

    public String getRiderName(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select "+KEY_SUB2+" from "+TABLE_NAME+" where "+KEY_SUB1+" ="+id,null);
        Log.d(TAG, "getRiderName: id= "+id+" res= "+res.toString());
        return res.toString();
    }

    public String getRiderSCity(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select "+KEY_SUB3+" from "+TABLE_NAME+" where "+KEY_SUB1+"  ="+id,null);
        Log.d(TAG, "getRiderName: id= "+id+" res= "+res.toString());

        return res.toString();
    }

    public String getRiderDCity(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select "+KEY_SUB4+" from "+TABLE_NAME+" where "+KEY_SUB1+"  ="+id,null);
        Log.d(TAG, "getRiderName: id= "+id+" res= "+res.toString());

        return res.toString();
    }

    public String getRiderStatus(String id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select "+KEY_SUB5+" from "+TABLE_NAME+" where "+KEY_SUB1+"  ="+id,null);
        return res.toString();
    }
    public void deleteTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public void dropTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }
}
