package com.app.inclass07;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class NoteDAO {

    private SQLiteDatabase db;

    public NoteDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(Note note){
        ContentValues values =new ContentValues();
        values.put("text", note.getText());
        values.put("status", note.getStatus());
        values.put("priority", note.getPriority());
        values.put("update_time", String.valueOf(note.getUpdate_time()));
        return db.insert(NotesTable.TABLE_NAME,null,values);
    }

    public boolean update(Note note){
        ContentValues values =new ContentValues();
        values.put("text", note.getText());
        values.put("status", note.getStatus());
        values.put("priority", note.getPriority());
        values.put("update_time", String.valueOf(note.getUpdate_time()));
        return db.update(NotesTable.TABLE_NAME,values,NotesTable.COLUMN_ID+"=?", new String[]{note.getId()+""})>0;
    }

    public boolean delete(Note note){
        return db.delete(NotesTable.TABLE_NAME,NotesTable.COLUMN_ID+"=?", new String[]{note.getId()+""})>0;
    }

    public Note get(long id){
        Note note = null;
        Cursor c = db.query(true, NotesTable.TABLE_NAME,new String[]{
                    NotesTable.COLUMN_ID, NotesTable.COLUMN_TEXT,NotesTable.COLUMN_STATUS, NotesTable.COLUMN_PRIORITY, NotesTable.COLUMN_UPDATE_TIME},
                    NotesTable.COLUMN_ID+"=?",new String[]{id+""},null,null,null,null);
        if(c!= null &&c.moveToFirst()){
            note = buildNoteFromCursor(c);
            if(!c.isClosed()){
                c.close();
            }
        }

        return note;
    }

    public List<Note> getAll(){
        List<Note> noteList = new ArrayList<Note>();
        Cursor c = db.query(true, NotesTable.TABLE_NAME,new String[]{
                        NotesTable.COLUMN_ID, NotesTable.COLUMN_TEXT,NotesTable.COLUMN_STATUS, NotesTable.COLUMN_PRIORITY, NotesTable.COLUMN_UPDATE_TIME},
                    null,null,null,null,null,null);
        if(c!= null &&c.moveToFirst()){
            do{
                Note note = buildNoteFromCursor(c);
                if(note != null){
                    noteList.add(note);
                }
            }while (c.moveToNext());
            if(!c.isClosed()){
                c.close();
            }
        }

        return noteList;

    }

    public Note buildNoteFromCursor(Cursor c){
        Note note = null;
        if(c!= null ){
            note = new Note();
            note.setId(c.getLong(0));
            note.setText(c.getString(1));
            note.setStatus(c.getString(2));
            note.setPriority(c.getString(3));
            note.setUpdate_time(c.getString(4));

        }
        return  note;
    }
}
