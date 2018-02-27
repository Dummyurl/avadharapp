package com.avadharwebworld.avadhar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Activity.ViewMatrimonyDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestRecievedItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by user-03 on 12/24/2016.
 */

public class MatrimonyInterestRecievedAdapter extends RecyclerView.Adapter<MatrimonyInterestRecievedAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid;
    int screenWidth,screenHeight;
    Display display;
    Point size;

    private List<MatrimonyInterestRecievedItem> MatrimonyInterestItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public MatrimonyInterestRecievedAdapter(Activity activity,Context context,List<MatrimonyInterestRecievedItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.MatrimonyInterestItems=item;
    }
    @Override
    public MatrimonyInterestRecievedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matrimonyinterestrecieved, parent, false);
        return new MatrimonyInterestRecievedAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MatrimonyInterestRecievedAdapter.ViewHolder holder, int position) {
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        MatrimonyInterestRecievedItem bind=MatrimonyInterestItems.get(position);
        if(!bind.getImage().equals("null")) holder.image.setImageUrl(bind.getImage(),imageLoader);
        holder.Name.setText(bind.getName());
//        holder.religion.setText(bind.getReligion());
        holder.thump.setVisibility(View.GONE);
        holder.favorite.setVisibility(View.GONE);

        Log.e("At Matrimony adapter","fetch");
    }

    @Override
    public int getItemCount() {
        return MatrimonyInterestItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name;
        public FeedImageView image;
        public RecyclerView rv_education;
        public ImageView favorite,thump;
        public Button accept,reject;

        public ViewHolder(final View itemView) {
            super(itemView);

            rv_education=(RecyclerView)itemView.findViewById(R.id.recycle_education);

            accept=(Button)itemView.findViewById(R.id.btn_matrimony_recieved_accept);
            reject=(Button)itemView.findViewById(R.id.btn_matrimony_recieved_reject);
            favorite=(ImageView)itemView.findViewById(R.id.iv_matrimony_recieved_favorite);
            thump=(ImageView)itemView.findViewById(R.id.iv_matrimony_recieved_thump);
            Name=(TextView)itemView.findViewById(R.id.tv_matrimony_recieved_name);
            image=(FeedImageView)itemView.findViewById(R.id.iv_matrimony_recieved_feed);
            WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            display=wm.getDefaultDisplay();
            size=new Point();
            display.getSize(size);
            screenHeight=size.y;
            screenWidth=size.x;
            LinearLayout.LayoutParams parmsIMG = new LinearLayout.LayoutParams(screenWidth/2,150);
//            InstImage.setLayoutParams(parmsIMG);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                    MatrimonyInterestRecievedItem eduItem=MatrimonyInterestItems.get(getLayoutPosition());
//                    eduItem.getId();
                    Intent intent=new Intent(mContext, ViewMatrimonyDetails.class);
                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra("id",  eduItem.getId());
                    //  intent.putExtra("coursename",eduItem.getCoursename());
//                    Toast.makeText(mContext,"id is "+eduItem.getId(),Toast.LENGTH_SHORT).show();
                    mContext.startActivity(intent);
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MatrimonyInterestRecievedItem eduItem=MatrimonyInterestItems.get(getLayoutPosition());
                    setInterestAccept(sp_uid,eduItem.getInteresteduid());
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MatrimonyInterestRecievedItem eduItem=MatrimonyInterestItems.get(getLayoutPosition());

                    setInterestReject(sp_uid,eduItem.getInteresteduid());
                }
            });
//            favorite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext,"favorite clicked",Toast.LENGTH_SHORT).show();
//
//                    MatrimonyInterestRecievedItem eduItem=MatrimonyInterestItems.get(getLayoutPosition());
//                    setFavorite(sp_uid,eduItem.getMprofileid());
//                }
//            });
//
//            thump.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext,"thump clicked",Toast.LENGTH_SHORT).show();
//
//                    MatrimonyInterestRecievedItem eduItem=MatrimonyInterestItems.get(getLayoutPosition());
//                    setInterestSend(sp_uid,eduItem.getMprofileid());
//                }
//            });
//


        }
    }
    public void  setInterestAccept(final String uid, final String pid){
        final String[] res = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyAcceptInterestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        String   favoriteResult =response.toString();


                        Toast.makeText(mContext,favoriteResult,Toast.LENGTH_SHORT).show();

//                        setAnimationForFavoriteText(response,layout);
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", String.valueOf(pid));
                params.put("uid",String.valueOf(uid));

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    public void  setInterestReject(final String uid, final String pid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyRejectInterestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        String   favoriteResult =response.toString();


                        Toast.makeText(mContext,favoriteResult,Toast.LENGTH_SHORT).show();

//                        setAnimationForFavoriteText(response,layout);
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", String.valueOf(pid));
                params.put("uid",String.valueOf(uid));

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
