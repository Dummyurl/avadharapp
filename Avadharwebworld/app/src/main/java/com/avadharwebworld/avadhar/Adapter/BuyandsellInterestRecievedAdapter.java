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
import com.avadharwebworld.avadhar.Data.BuyandSellInterestItem;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.MatrimonyItem;
import com.avadharwebworld.avadhar.Data.RealestateItem;
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

public class BuyandsellInterestRecievedAdapter extends RecyclerView.Adapter<BuyandsellInterestRecievedAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid,profileid;
    int screenWidth,screenHeight;
    Display display;
    Point size;

    private List<BuyandSellInterestItem> buyandSellInterestItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public BuyandsellInterestRecievedAdapter(Activity activity,Context context,List<BuyandSellInterestItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.buyandSellInterestItems=item;
    }
    @Override
    public BuyandsellInterestRecievedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buyandsellinterestrecievedlayout, parent, false);
        return new BuyandsellInterestRecievedAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BuyandsellInterestRecievedAdapter.ViewHolder holder, int position) {
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        profileid=sp.getString(Constants.PROFILEID,"");
        BuyandSellInterestItem bind=buyandSellInterestItems.get(position);
        holder.Name.setText(bind.getName());
        holder.viewProfileid.setText(bind.getMprofileid());
        holder.mobile.setText(bind.getMobile());
        holder.email.setText(bind.getEmail());
        holder.viewcontact.setVisibility(View.GONE);

//        holder.religion.setText(bind.getReligion());

        Log.e("At Matrimony adapter","fetch");
    }

    @Override
    public int getItemCount() {
        return buyandSellInterestItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name,type,price,location1,viewProfileid,mobile,email;
        public FeedImageView image;
        public ImageView favorite,thump,delete;
        public LinearLayout viewcontact;
        public boolean findclick=false;

        public ViewHolder(final View itemView) {
            super(itemView);


            Name=(TextView)itemView.findViewById(R.id.tv_buyandsell_interest_name);
            viewProfileid=(TextView)itemView.findViewById(R.id.tv_buyandsell_interest_id);
            mobile=(TextView)itemView.findViewById(R.id.tv_buyandsell_interest_contactnumber);
            email=(TextView)itemView.findViewById(R.id.tv_buyandsell_interest_email);
            viewcontact=(LinearLayout)itemView.findViewById(R.id.ll_buyandsell_interestrecieved );

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
                    if (!findclick) {
                        viewcontact.setVisibility(View.VISIBLE);
                        findclick=true;
                    }else {
                        viewcontact.setVisibility(View.GONE);
                        findclick=false;
                    }
                }
            });



        }

    }

}
