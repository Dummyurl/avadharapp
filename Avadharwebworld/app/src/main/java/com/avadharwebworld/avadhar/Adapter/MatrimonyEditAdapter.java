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
import com.avadharwebworld.avadhar.Activity.MatrimonyRegistrationEdit;
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Activity.ViewMatrimonyDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.MatrimonyEditItem;
import com.avadharwebworld.avadhar.Data.MatrimonyItem;
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

public class MatrimonyEditAdapter extends RecyclerView.Adapter<MatrimonyEditAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid,profileid;
    int screenWidth,screenHeight;
    Display display;
    Point size;

    private List<MatrimonyEditItem> matrimonyItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public MatrimonyEditAdapter(Activity activity,Context context,List<MatrimonyEditItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.matrimonyItems=item;
    }
    @Override
    public MatrimonyEditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matrimonyeditviewlayout, parent, false);
        return new MatrimonyEditAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MatrimonyEditAdapter.ViewHolder holder, int position) {
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        profileid=sp.getString(Constants.PROFILEID,"");
        MatrimonyEditItem bind=matrimonyItems.get(position);
//        if(!bind.getImage().equals("null")) holder.image.setImageUrl(bind.getImage(),imageLoader);
        holder.Name.setText(bind.getName());
        holder.status.setText(bind.getStatus());
//        holder.religion.setText(bind.getReligion());

        Log.e("At Matrimony adapter","fetch");
    }

    @Override
    public int getItemCount() {
        return matrimonyItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name,status;
        public FeedImageView image;
        public RecyclerView rv_education;
        public ImageView favorite,thump;
        public Button edit,delete;

        public ViewHolder(final View itemView) {
            super(itemView);


            thump=(ImageView)itemView.findViewById(R.id.iv_matrimony_thump);
            Name=(TextView)itemView.findViewById(R.id.tv_matrimony_editview_name);
            status=(TextView)itemView.findViewById(R.id.tv_matrimony_editview_status);
            edit=(Button) itemView.findViewById(R.id.btn_matrimony_editview_edit);
            delete=(Button)itemView.findViewById(R.id.btn_matrimony_editview_delete);
            image=(FeedImageView)itemView.findViewById(R.id.iv_matrimony_feed);
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

                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                    MatrimonyEditItem eduItem=matrimonyItems.get(getLayoutPosition());
//                    eduItem.getId();
                    Intent intent=new Intent(mContext, MatrimonyRegistrationEdit.class);
                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra("id",  eduItem.getId());
                    intent.putExtra("profileid",eduItem.getMprofileid());
                    //  intent.putExtra("coursename",eduItem.getCoursename());
//                    Toast.makeText(mContext,"id is "+eduItem.getId(),Toast.LENGTH_SHORT).show();
                    mContext.startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MatrimonyEditItem eduItem=matrimonyItems.get(getLayoutPosition());

                    deleteProfile(eduItem.getId(),profileid);
                    matrimonyItems.remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                    notifyItemRangeChanged(getAdapterPosition(),matrimonyItems.size());
//                    Toast.makeText(mContext,"deleted  " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();

                }
            });



        }
    }
    public void  setFavorite(final String uid, final String pid){
        final String[] res = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.AddtoMatrimonyFavoriteURL,
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
                params.put("pid", String.valueOf(pid));
                params.put("uid",String.valueOf(uid));
                Log.e("profile id",pid);
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
    public void  deleteProfile(final String uid, final String pid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyDeleteProfileURL,
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
                params.put("avadharid", String.valueOf(pid));
                params.put("id",String.valueOf(uid));
                Log.e("profile id",pid);
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
