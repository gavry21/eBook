package com.example.android.darktheme.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.darktheme.InfoObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DB_NAME ="DataBase.db";

    private static final String TABLE_NAME = "activity";
    private static final String COL1 = "ID";
    private static final String Pages = "Pages";
    private static final String Dat = "Dat";
    private static final String Sp = "Speed";
    Double speed;

    private static final String CREATE_DATE_TABLE = "create table " +TABLE_NAME+" ("+
            COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Pages + " integer, " +
            Sp + " double, " +
            Dat + " VARCHAR(25) "+ ")";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                Pages +"INTERGER,"+
//        Date +"TEXT" +")";
        db.execSQL(CREATE_DATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(Integer item,  Double speed, String dat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Pages, item);
        contentValues.put(Sp, speed);
        contentValues.put(Dat, dat);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + dat + " to " + TABLE_NAME);
        Log.d(TAG, "addData: Adding " + speed + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor viewDat(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public ArrayList<InfoObject> getAllData(){
        ArrayList<InfoObject> infoObjects = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        while(res.moveToNext()) {
            String id = res.getString(0);   //0 is the number of id column in your database table
            Integer pages = res.getInt(1);
            String dat = res.getString(3);
            Double speed = res.getDouble(2);
            Log.e(TAG, dat);
            InfoObject infoObject1 = new InfoObject(pages, dat ,speed);
            infoObjects.add(infoObject1);
        }
        return infoObjects;

    }

  /*  public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }*/


  public Double getLast(){
      SQLiteDatabase db = this.getWritableDatabase();
      String query = "select * from " + TABLE_NAME + " ORDER BY " + Sp + " DESC LIMIT 1";
      Cursor res = db.rawQuery(query,null);
      res.moveToLast();
      //if(res.moveToFirst())
        speed = res.getDouble(res.getColumnIndex(Sp));
      res.close();
      return speed;
  }

    public Cursor getItemID(Integer read) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + Pages + " = '" + read + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(int newName, int id, int oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + Pages +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + Pages + " = '" + oldName + "'";
        Log.d(TAG, "updateRead: query: " + query);
        Log.d(TAG, "updateRead: Setting read to " + newName);
        db.execSQL(query);
    }


    public void deleteName(int id, int name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + Pages + " = '" + name + "'";
        Log.d(TAG, "deleteWant: query: " + query);
        Log.d(TAG, "deleteWant: Deleting " + name + " from database.");
        db.execSQL(query);
    }

}
