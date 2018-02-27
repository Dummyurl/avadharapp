package com.avadharwebworld.avadhar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.FollowingItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.ProfileImageView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user-03 on 12/15/2016.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private List<FollowingItem> followingItem;
    public FollowingAdapter(Activity activity, Context context, List<FollowingItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.followingItem =item;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FollowingItem bind= followingItem.get(position);
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        if(!bind.getUname().equals("null")){
            holder.name.setText(bind.getUname());
        }else {
            holder.name.setText(bind.getUsername());
        }
        holder.profilepic.setImageUrl(bind.getProfilepic(),imageLoader);

        holder.coverphoto.setImageUrl(bind.getCoverpic(),imageLoader);


    }

    @Override
    public int getItemCount() {
        return followingItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ProfileImageView coverphoto,profilepic;
        public Button btnFollow;
        public ViewHolder(final View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.followers_name);
            coverphoto=(ProfileImageView)itemView.findViewById(R.id.followers_coverimage);
            btnFollow=(Button)itemView.findViewById(R.id.followers_btn_follow);
            profilepic=(ProfileImageView)itemView.findViewById(R.id.followers_profileimage);

            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(btnFollow.getText().equals(mContext.getResources().getString(R.string.following))){
                        removeFollow(getAdapterPosition());
                        btnFollow.setText(mContext.getResources().getString(R.string.follow));

                    }else{
                        addFollow(getAdapterPosition());
                        btnFollow.setText(mContext.getResources().getString(R.string.following));

                    }
                }
            });
        }
    }
    private void addFollow(int position){
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.AddFollowURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
//                        Toast.makeText(SocialMedia.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//                Log.e("message id",Jsonobjectmsgid.toString());
//                String[]msg=objsocial.messagefull();
//                Log.e("messagefull", String.valueOf(Arrays.asList(objsocial.msgIDFULL)));
                String[]flw= Constants.Followid;
//                Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(msg)),Toast.LENGTH_LONG).show();
                Log.e("messageid",String.valueOf(Arrays.asList(flw)));
                params.put("fid",String.valueOf(flw[itemposition]));
                params.put("uid", String.valueOf(sp_uid ));
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void removeFollow(int position){
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.RemoveFollowURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//
                String[]flw= Constants.Followid;
                Log.e("fid",String.valueOf(Arrays.asList(flw)));
                params.put("fid",String.valueOf(flw[itemposition]));

                params.put("uid", String.valueOf(sp_uid ));
//

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}

