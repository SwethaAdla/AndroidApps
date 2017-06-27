package com.app.inclass07;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class NotesTable {
    static final String TABLE_NAME = "notes_keeper";
    static final String COLUMN_ID ="id";
    static final String COLUMN_TEXT ="text";
    static final String COLUMN_STATUS ="status";
    static final String COLUMN_PRIORITY ="priority";
    static final String COLUMN_UPDATE_TIME ="update_time";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+TABLE_NAME+" (");
        sb.append(COLUMN_ID +" integer primary key autoincrement, ");
        sb.append(COLUMN_TEXT +" text not null, ");
        sb.append(COLUMN_STATUS +" text not null, ");
        sb.append(COLUMN_PRIORITY +" text not null, ");
        sb.append(COLUMN_UPDATE_TIME +" text not null);");
        try {
            db.execSQL(sb.toString());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    static  public void onUpgrade(SQLiteDatabase db, int oldveraion, int newversion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME+";");
        NotesTable.onCreate(db);
    }

}
