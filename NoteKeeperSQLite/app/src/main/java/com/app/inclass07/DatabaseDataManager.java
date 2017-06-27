package com.app.inclass07;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Gopinath N on 2/26/2017.
 */

public class DatabaseDataManager {

    private Context mContext;
    private SQLiteDatabase db;
    private DatabaseOpenHelper databaseOpenHelper;
    private NoteDAO noteDAO;

    public DatabaseDataManager(Context context){
        this.mContext =context;
        databaseOpenHelper = new DatabaseOpenHelper(this.mContext);
        db =databaseOpenHelper.getWritableDatabase();
        noteDAO = new NoteDAO(db);
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public NoteDAO getNoteDAO(){
        return this.noteDAO;
    }

    public long saveNote(Note note){
        return  this.noteDAO.save(note);
    }

    public boolean updateNote(Note note){
        return this.noteDAO.update(note);
    }

    public boolean deleteNote(Note note){
        return this.noteDAO.delete(note);
    }

    public Note getNote(long id){
        return this.noteDAO.get(id);
    }

    public List<Note> getAllNotes(){
        return this.noteDAO.getAll();
    }


}
