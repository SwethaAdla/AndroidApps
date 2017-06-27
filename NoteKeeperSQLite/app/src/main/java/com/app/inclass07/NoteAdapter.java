package com.app.inclass07;

        import android.content.Context;
        import android.content.DialogInterface;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AlertDialog;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.ImageView;
        import android.widget.TextView;

        import org.ocpsoft.prettytime.PrettyTime;

        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.GregorianCalendar;

/**
 * Created by sweth on 2/27/2017.
 */

public class NoteAdapter extends ArrayAdapter<Note> {
    public static final String  YES = "Yes";
    public static final String  NO = "No";
    ArrayList<Note> mData;
    Context mContext;
    int mResource;
    DatabaseDataManager dm;

    public NoteAdapter(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
        dm = new DatabaseDataManager(mContext);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

        final Note note = mData.get(position);

        TextView noteTitle = (TextView) convertView.findViewById(R.id.noteTitle);
        noteTitle.setText(note.getText().toString());

        CheckBox chkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        if(note.getStatus().equalsIgnoreCase("completed")){
            chkBox.setChecked(true);

        }else{
            chkBox.setChecked(false);
        }
        chkBox.setVisibility(View.VISIBLE);

        TextView priority = (TextView) convertView.findViewById(R.id.priorityText);
        priority.setText(note.getPriority().toString()+" priority");

        Calendar t = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dt = null; //replace 4 with the column index
        try {
            dt = sdf.parse(note.getUpdate_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PrettyTime p = new PrettyTime();
        TextView timeLapsed = (TextView) convertView.findViewById(R.id.timeLapsed);
        timeLapsed.setText(p.format(dt));

        chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alertDailogBuilder(note, isChecked);
            }
        });
convertView.setLongClickable(true);

        return convertView;
    }

    public void alertDailogBuilder(final Note note, boolean isChecked) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if (isChecked) {
            builder.setMessage("Do you really want to mark it as Completed?")
                    .setPositiveButton(YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            note.setStatus("Completed");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();
                            note.setUpdate_time(String.valueOf(date));
                            dm.updateNote(note);

                        }
                    })
                    .setNegativeButton(NO, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        } else {
            builder.setMessage("Do you really want to mark it as Pending?")
                    .setPositiveButton(YES, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            note.setStatus("Pending");
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();
                            note.setUpdate_time(String.valueOf(date));
                            dm.updateNote(note);

                        }
                    })
                    .setNegativeButton(NO, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
    }

    }