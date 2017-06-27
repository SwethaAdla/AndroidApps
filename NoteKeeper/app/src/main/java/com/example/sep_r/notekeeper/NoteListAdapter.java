package com.example.sep_r.notekeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sep_r on 03-04-2017.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
        private static ArrayList<Note> mProdArList;
        private static Context mContext;
        private int mResource;


        public NoteListAdapter(Context mContext, int mResource, ArrayList<Note> prods) {
            this.mContext = mContext;
            this.mResource = mResource;
            this.mProdArList = prods;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Note prod = mProdArList.get(position);
            TextView title = holder.title;
            TextView priority = holder.priority;
            TextView time = holder.time;
            final CheckBox chkBox = holder.chkBox;

            title.setText(prod.getNote());
            priority.setText(prod.getPriority() + " Priority");
            time.setText(prod.getUpdate_time());



            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Do you really want to mark it as complete?").setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prod.setStatus("Completed");
                                prod.setUpdate_time(new Date().toString());
                                chkBox.setChecked(true);
                                Toast.makeText(mContext, "Completed!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "Okay!", Toast.LENGTH_SHORT).show();
                                chkBox.setChecked(false);
                            }
                        });
                        builder.show();

                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Do you really want to mark it as pending?").setCancelable(false);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                prod.setStatus("Pending");
                                prod.setUpdate_time(new Date().toString());
                                chkBox.setChecked(false);
                                Toast.makeText(mContext, "Done!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext, "Okay!", Toast.LENGTH_SHORT).show();
                                chkBox.setChecked(true);
                            }
                        });
                        builder.show();

                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mProdArList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView title;
            public TextView priority;
            public TextView time;
            public CheckBox chkBox;


            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(final View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

               title = (TextView) itemView.findViewById(R.id.textViewSubject);
                priority = (TextView) itemView.findViewById(R.id.textViewPriority);
                time = (TextView) itemView.findViewById(R.id.textViewTime);
                chkBox = (CheckBox) itemView.findViewById(R.id.checkBoxCompleted);

            }

        }
    }

