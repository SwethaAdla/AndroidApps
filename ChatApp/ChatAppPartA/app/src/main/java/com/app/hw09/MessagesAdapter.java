package com.app.hw09;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private ArrayList<Messages> messages;
    private Context mContext;
    String currentUid;
    ArrayList<User> users;
    Trip trip;
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();
    DatabaseReference mdata = FirebaseDatabase.getInstance().getReference();

    public MessagesAdapter(ArrayList<Messages> messages, Context mContext, String currentUid,ArrayList<User> users, Trip trip) {
        this.messages = messages;
        this.mContext = mContext;
        this.currentUid = currentUid;
        this.users = users;
        this.trip = trip;
        //this.isRemoved = isRemoved;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View day = inflater.inflate(R.layout.messages_row_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(day);
        return viewHolder;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout layout1,layout2;
        public TextView messageL1;
        public TextView senderNameTimeL1;
        public ImageView delImgL1;
        public ImageView ivL1;
        public TextView messageL2;
        public TextView senderNameTimeL2;
        public ImageView delImgL2;
        public ImageView ivL2;

        public ViewHolder( View itemView) {
            super(itemView);
            layout1 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout1);
            layout2 = (RelativeLayout) itemView.findViewById(R.id.relativeLayout2);

            messageL1 = (TextView) itemView.findViewById(R.id.textViewL1Message);
            senderNameTimeL1 = (TextView) itemView.findViewById(R.id.textViewL1sendernametime);
            ivL1 = (ImageView) itemView.findViewById(R.id.imageViewL1);
            delImgL1 = (ImageView) itemView.findViewById(R.id.imageViewL1delete);

            messageL2 = (TextView) itemView.findViewById(R.id.textViewL2message);
            senderNameTimeL2 = (TextView) itemView.findViewById(R.id.textViewL2senderNametime);
            ivL2 = (ImageView) itemView.findViewById(R.id.imageViewL2);
            delImgL2 = (ImageView) itemView.findViewById(R.id.imageViewL2delete);
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Messages message = messages.get(position);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(R.color.butterflyblue);
        gd.setCornerRadius(10);
        gd.setStroke(2, Color.WHITE);
        if(trip.isRemoved && message.getSentBy().equals(trip.getCreatedByUid()) && currentUid.equals(trip.getCreatedByUid())) {
           // return;
            holder.layout1.setVisibility(View.GONE);
            holder.layout2.setVisibility(View.GONE);
        }else{
            String name = "";
            for (User u : users) {
                if (u.getKey().equalsIgnoreCase(message.getSentBy())) {
                    name = u.getFname() + " " + u.getLname();
                }
            }
            Log.d("demo", "onBingViewHolder. position " + position + "message is " + message.toString());
            if (message.getSentBy().equalsIgnoreCase(currentUid)) {
                holder.layout1.setVisibility(View.GONE);
                holder.layout2.setBackground(gd);
                if (message.getText().equals("") || message.getText().isEmpty()) {

                    TextView msg = holder.messageL2;
                    msg.setVisibility(View.GONE);
                    ImageView picture = holder.ivL2;
                    picture.setVisibility(View.VISIBLE);
                    Picasso.with(mContext).load(message.getImageUrl()).into(picture);
                    TextView nameTime = holder.senderNameTimeL2;

                    nameTime.setText(name + ", " + message.getSentTime()); // need to fetch the sender name using the id
                    ImageView delImg = holder.delImgL2;
                    delImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("demo", "delL1 clicked");
                            mdata.child("users").child(currentUid).child("trips").child(trip.getKey()).child("messages").child(message.getKey()).removeValue();
                        }
                    });
                } else {
                    ImageView iv = holder.ivL2;
                    iv.setVisibility(View.GONE);
                    holder.layout2.getLayoutParams().height = 120;
                    holder.layout2.getLayoutParams().width = 560;
                    TextView msg = holder.messageL2;
                    msg.setText(message.getText());
                    TextView nameTime = holder.senderNameTimeL2;
                    nameTime.setText(name + ", " + message.getSentTime()); // need to fetch the sender name using the id
                    ImageView delImg = holder.delImgL2;
                    delImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mdata.child("users").child(currentUid).child("trips").child(trip.getKey()).child("messages").child(message.getKey()).removeValue();
                        }
                    });
                }
            } else {
                //others messages.. code for layout1
                holder.layout2.setVisibility(View.GONE);
                    gd.setColor(R.color.icceberg);
                    gd.setCornerRadius(10);
                    gd.setStroke(2, Color.WHITE);
                    holder.layout1.setBackground(gd);
                if (message.getText().equals("") || message.getText().isEmpty()) {
                    TextView msg = holder.messageL1;
                    msg.setVisibility(View.GONE);
                    ImageView picture = holder.ivL1;
                    picture.setVisibility(View.VISIBLE);
                    Picasso.with(mContext).load(message.getImageUrl()).into(picture);
                    TextView nameTime = holder.senderNameTimeL1;
                    nameTime.setText(name + ", " + message.getSentTime()); // need to fetch the sender name using the id
                    ImageView delImg = holder.delImgL1;
                    delImg.setEnabled(true);
                        delImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mdata.child("users").child(currentUid).child("trips").child(trip.getKey()).child("messages").child(message.getKey()).removeValue();
                            }
                        });
                } else {
                    ImageView iv = holder.ivL1;
                    iv.setVisibility(View.GONE);
                    holder.layout1.getLayoutParams().height = 120;
                    holder.layout1.getLayoutParams().width = 560;
                    TextView msg = holder.messageL1;
                    msg.setText(message.getText());
                    TextView nameTime = holder.senderNameTimeL1;
                    nameTime.setText(name + ", " + message.getSentTime()); // need to fetch the sender name using the id
                    ImageView delImg = holder.delImgL1;
                    delImg.setEnabled(true);
                        delImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mdata.child("users").child(currentUid).child("trips").child(trip.getKey()).child("messages").child(message.getKey()).removeValue();
                            }
                        });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


}
