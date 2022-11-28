package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ToDoDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "ToDos";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "description";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+
                "(" +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_TITLE+" TEXT, "+KEY_DESC+" TEXT, "+
                KEY_DATE+" TEXT, "+KEY_TIME+" TEXT"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);

    }


    public void addToDos(String title,String desc,String date,String time){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE,title);
        values.put(KEY_DESC,desc);
        values.put(KEY_DATE,date);
        values.put(KEY_TIME,time);

        db.insert(TABLE_NAME,null,values);
    }


    //To Fetch Data
    public ArrayList<ToDoModel> fetchToDos(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);

        ArrayList<ToDoModel> arrToDos = new ArrayList<>();



        int i=1;
        while (cursor.moveToNext()){

            ToDoModel model = new ToDoModel();
            model.sno = String.valueOf(i);
            model.id=cursor.getInt(0);
            model.title = cursor.getString(1);
            model.desc = cursor.getString(2);
            model.date = cursor.getString(3);
            model.time = cursor.getString(4);

            arrToDos.add(model);
            i++;

        }
        return arrToDos;
    }


    //To Update Data
    public void updateToDos(ToDoModel model){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE,model.title);
        values.put(KEY_DESC,model.desc);
        values.put(KEY_DATE,model.date);
        values.put(KEY_TIME,model.time);


        db.update(TABLE_NAME,values,KEY_ID+" = "+model.id,null);

    }


    //To delete Data
    public void deleteToDos(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,KEY_ID+" = "+id,null);
    }
}
