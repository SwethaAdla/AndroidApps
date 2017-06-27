package com.app.inclass07;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseDataManager dm;
    EditText noteText;
    Spinner spinner;
    public static final String STATUS_PENDING ="Pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dm= new DatabaseDataManager(this);

        spinner = (Spinner) findViewById(R.id.PrioritySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        List<Note> notesList = dm.getAllNotes();
        loadNotesList(notesList);

        findViewById(R.id.Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add a row to table
                noteText = (EditText)findViewById(R.id.NoteTitle);
                String noteTitle = noteText.getText().toString();
                Log.d("demo", "Note text "+noteTitle);
                String priority = spinner.getSelectedItem().toString();
                Log.d("demo","Priority Selected "+priority);

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();

                dm.saveNote(new Note(noteTitle, priority,STATUS_PENDING, dateFormat.format(date)));
                noteText.setText("");
                loadNotesList(dm.getAllNotes());
            }
        });

    }

    public  void loadNotesList(final List<Note> notesList){
        ArrayList<Note> arrayList = new ArrayList<Note>(notesList);
        Collections.sort(arrayList, new NoteComparator()) ;

        ListView listView = (ListView) findViewById(R.id.notesListView);
        NoteAdapter adapter = new NoteAdapter(this, R.layout.row_view, arrayList);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = notesList.get(position);
                alertDailogBuilder(note);
                loadNotesList(dm.getAllNotes());
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_showall) {
            List<Note> notesL = dm.getAllNotes();
            Collections.reverse(notesL);
            loadNotesList(notesL);
        } else if (id == R.id.nav_showCompleted) {
            List<Note> completedNotes = new ArrayList<Note>();
            for(Note n: dm.getAllNotes()){
                if(n.getStatus().equalsIgnoreCase("completed")){
                    completedNotes.add(n);
                }
            }

            loadNotesList(completedNotes);
        } else if (id == R.id.nav_showPending) {
            List<Note> pendingNotes = new ArrayList<Note>();
            for(Note n: dm.getAllNotes()){
                if(n.getStatus().equalsIgnoreCase("pending")){
                    pendingNotes.add(n);
                }
            }

           loadNotesList(pendingNotes);

        } else if (id == R.id.nav_sortbyTime) {
            ArrayList<Note> notesList = new ArrayList<Note>(dm.getAllNotes());
            Collections.sort(notesList, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    return o1.getUpdate_time().compareTo(o2.getUpdate_time());
                }
            });

            loadNotesList(notesList);

        }else if (id == R.id.nav_sortByPriority) {
            ArrayList<Note> notesList = new ArrayList<Note>(dm.getAllNotes());
            Collections.sort(notesList, new Comparator<Note>() {
                @Override
                public int compare(Note o1, Note o2) {
                    int val =-1;
                    if(o1.getPriority().equalsIgnoreCase(o2.getPriority())){
                        if(o2.getPriority().equalsIgnoreCase("High")){
                            return val =0;
                        }else if(o2.getPriority().equalsIgnoreCase("Medium")){
                            return val=1;
                        }else if(o2.getPriority().equalsIgnoreCase("Low")){
                            return val=2;
                        }
                    }
                    return val;
                }
            });

            loadNotesList(notesList);
        }

        return super.onOptionsItemSelected(item);
    }
    public void alertDailogBuilder(final Note note) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you really want to delete the task?")
                    .setPositiveButton(NoteAdapter.YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dm.deleteNote(note);
                            loadNotesList(dm.getAllNotes());
                        }
                    })
                    .setNegativeButton(NoteAdapter.NO, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

    }
    @Override
    protected void onDestroy() {
        dm.close();
        super.onDestroy();
    }



}
