package com.example.sep_r.notekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private NoteListAdapter adapter;
    private ArrayList<Note> noteList = new ArrayList<Note>();
    private EditText subject;
    String childName = "note";
    static int i=0;
    Button add;
    RecyclerView rv;
    DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mChild = mRootref.child("Notes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner sp = (Spinner) findViewById(R.id.spinner);

        rv = (RecyclerView) findViewById(R.id.recyclerView1);
        subject = (EditText) findViewById(R.id.editText);
        add = (Button) findViewById(R.id.button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=mChild.push().getKey();
                Note note = new Note(subject.getText().toString(),sp.getSelectedItem().toString(), String.valueOf(new Date()),"Pending");
               note.setId(id);
                //Map<String,Note> map = new HashMap<String, Note>();
                //map.put("ABC",new Note(subject.getText().toString(),sp.getSelectedItem().toString(), String.valueOf(new Date()),"Pending"));
                mChild.child(id).setValue(note);
                //mChild.child(childName + i).setValue(note);
                //i++;

            }
        });

        ArrayAdapter<CharSequence> genereSpinner = ArrayAdapter.createFromResource(MainActivity.this, R.array.priorities, android.R.layout.simple_spinner_item);
        genereSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(genereSpinner);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalChild = (int) dataSnapshot.getChildrenCount();

                for(int i =0; i< totalChild;i++)
                {
                    Note note = dataSnapshot.getValue(Note.class);
                    Toast.makeText(MainActivity.this, note.getNote(), Toast.LENGTH_SHORT).show();

                    if (note != null) {
                        noteList.add(note);
                    }

                }

                //Log.d("Demo", "onDataChange: " + sub);
                //Toast.makeText(MainActivity.this, totalChild + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(noteList != null) {
            adapter = new NoteListAdapter(MainActivity.this, R.layout.list_item, noteList);
            adapter.notifyDataSetChanged();
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
            case R.id.show_all:
                adapter = new NoteListAdapter(MainActivity.this, R.layout.list_item, noteList);
               adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
                // TODO put your code here to respond to the button tap

                return true;
            case R.id.show_complete:
                // TODO put your code here to respond to the button tap
                ArrayList<Note> compList = new ArrayList<Note>();
                for(Note n: noteList){
                    if(n.getPriority().equalsIgnoreCase("completed")){
                        compList.add(n);
                    }
                }
                adapter = new NoteListAdapter(MainActivity.this, R.layout.list_item, compList);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
                return true;
            case R.id.show_pending:
                ArrayList<Note> pendList = new ArrayList<Note>();
                for(Note n: noteList){
                    if(n.getPriority().equalsIgnoreCase("pending")){
                        pendList.add(n);
                    }
                }

                // TODO put your code here to respond to the button tap
                adapter = new NoteListAdapter(MainActivity.this, R.layout.list_item, pendList);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
                return true;
            case R.id.sort_time:
                // TODO put your code here to respond to the button tap

                Collections.sort(noteList, new Comparator<Note>() {
                    @Override
                    public int compare(Note o1, Note o2) {
                        Date timeOfNote1 = new Date(o1.getUpdate_time());
                        Date timeOfNote2 = new Date(o2.getUpdate_time());
                        return timeOfNote1.compareTo(timeOfNote2);
                    }
                });
                adapter = new NoteListAdapter(MainActivity.this, R.layout.list_item, noteList);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
                return true;
            case R.id.sort_priority:
                adapter = new NoteListAdapter(MainActivity.this, R.layout.list_item, noteList);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
