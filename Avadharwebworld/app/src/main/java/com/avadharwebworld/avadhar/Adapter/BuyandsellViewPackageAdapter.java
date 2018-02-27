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
import com.avadharwebworld.avadhar.Activity.ViewRealestateDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.BuyandsellMypackageItem;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.MatrimonyItem;
import com.avadharwebworld.avadhar.Data.RealestateItem;
import com.avadharwebworld.avadhar.Data.RealestateMypackageItem;
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

public class BuyandsellViewPackageAdapter extends RecyclerView.Adapter<BuyandsellViewPackageAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid,profileid;
    int screenWidth,screenHeight;
    Display display;
    Point size;

    private List<BuyandsellMypackageItem> buyandsellMypackageItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public BuyandsellViewPackageAdapter(Activity activity,Context context,List<BuyandsellMypackageItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.buyandsellMypackageItems=item;
    }
    @Override
    public BuyandsellViewPackageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buyandsellmypackagelayout, parent, false);
        return new BuyandsellViewPackageAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BuyandsellViewPackageAdapter.ViewHolder holder, int position) {
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        profileid=sp.getString(Constants.PROFILEID,"");
        BuyandsellMypackageItem bind=buyandsellMypackageItems.get(position);
        holder.type.setText(bind.getActivitytype());
        holder.category1.setText(bind.getProftype());
        holder.pricefrom.setText(bind.getPricefrom());
        holder.priceto.setText(bind.getPriceto());
        holder.edit.setVisibility(View.GONE);
//        holder.religion.setText(bind.getReligion());


    }

    @Override
    public int getItemCount() {
        return buyandsellMypackageItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView type,pricefrom,priceto,category1;
        public FeedImageView image;
        public ImageView favorite,thump;
        public Button edit,delete;

        public ViewHolder(final View itemView) {
            super(itemView);
            type=(TextView)itemView.findViewById(R.id.tv_buyandsell_viewpackage_r_buyorsell);
            category1=(TextView)itemView.findViewById(R.id.tv_buyandsell_viewpackage_r_category);
            pricefrom=(TextView)itemView.findViewById(R.id.tv_buyandsell_viewpackage_r_pricefrom);
            priceto=(TextView)itemView.findViewById(R.id.tv_buyandsell_viewpackage_r_priceto);
            edit=(Button)itemView.findViewById(R.id.btn_buyandsell_viewpackage_r_edit);
            delete=(Button)itemView.findViewById(R.id.btn_buyandsell_viewpackage_r_delete);

            WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            display=wm.getDefaultDisplay();
            size=new Point();
            display.getSize(size);
            screenHeight=size.y;
            screenWidth=size.x;
            LinearLayout.LayoutParams parmsIMG = new LinearLayout.LayoutParams(screenWidth/2,150);
//            InstImage.setLayoutParams(parmsIMG);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
//                    RealestateItem eduItem=realestateItems.get(getLayoutPosition());
////                    eduItem.getId();
//                    Intent intent=new Intent(mContext, ViewRealestateDetails.class);
//                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
//                    intent.putExtra("id",  eduItem.getId());
//                    //  intent.putExtra("coursename",eduItem.getCoursename());
////                    Toast.makeText(mContext,"id is "+eduItem.getId(),Toast.LENGTH_SHORT).show();
//                    mContext.startActivity(intent);
//                }
//            });
//

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BuyandsellMypackageItem eduItem=buyandsellMypackageItems.get(getLayoutPosition());
                    setDelete(sp_uid,eduItem.getId());
                    buyandsellMypackageItems.remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                    notifyItemRangeChanged(getAdapterPosition(),buyandsellMypackageItems.size());
                }
            });
        }
    }
    public void  setDelete(final String uid, final String pid){
        final String[] res = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.BuyandsellMypackageDeleteURL,
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
    public void  setInterestSend(final String uid, final String pid,final String avadharid,final String title,final String rid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.RealestateAddInterestURL,
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
                params.put("aid", String.valueOf(avadharid));
                params.put("loginid",String.valueOf(uid));
                params.put("rid", String.valueOf(rid));
                params.put("title",String.valueOf(title));
                params.put("pid", String.valueOf(pid));
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
