package com.app.inclass09;


        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.res.Resources;
        import android.graphics.Color;
        import android.support.annotation.NonNull;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.TextView;


        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;
        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

        import okhttp3.FormBody;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;


public class AppAdapter extends ArrayAdapter<App> {
    ArrayList<App> mData;
    Context mContext;
    int mResource;
ChitChatActivity activity;
    public AppAdapter(Context context, int resource, ArrayList<App> objects, ChitChatActivity activity) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }

       final App app = mData.get(position);
       final Button btnView = (Button) convertView.findViewById(R.id.buttonView);
        final Button btnJoin = (Button) convertView.findViewById(R.id.buttonJoin);
        TextView appDetails = (TextView) convertView.findViewById(R.id.textViewChannelName);
        appDetails.setText(app.getName());
        if(!app.isSubscribed){
            btnView.setVisibility(View.INVISIBLE);
            btnJoin.setVisibility(View.VISIBLE);
        }
        convertView.findViewById(R.id.buttonJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.setSubscribed(true);
                activity.subscripList.add(app);

                OkHttpClient client = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("channel_id", app.getId())
                        .build();
                Request request = new Request.Builder()
                        .url("http://52.90.79.130:8080/Groups/api/subscribe/channel")
                        .header("Authorization","BEARER "+activity.token )
                        .post(formBody)
                        .build();

                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

                    String data = response.body().string();
                    JSONObject root = new JSONObject(data);
                    String status = root.getString("status");


                    if(status.equals("1")){
                       // activity.token = root.getString("data");
                        btnView.setVisibility(View.VISIBLE);
                        btnJoin.setVisibility(View.INVISIBLE);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        convertView.findViewById(R.id.buttonView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(mContext, LastActivity.class);
                i.putExtra(MainActivity.TOKEN_KEY, activity.token);
                i.putExtra("APP_KEY", (Serializable) app);
                i.putExtra(MainActivity.EMAIL_KEY,activity.userEmail);
                mContext.startActivity(i);
            }
        });
        return convertView;
    }
}
