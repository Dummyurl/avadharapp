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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.FeedImageView;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by user-03 on 12/24/2016.
 */

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid;
    int screenWidth,screenHeight;
    Display display;
    Point size;

    private List<EducationFeedItem> educationFeedItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public EducationAdapter(Activity activity,Context context,List<EducationFeedItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.educationFeedItems=item;
    }
    @Override
    public EducationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.education_layout, parent, false);
        return new EducationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EducationAdapter.ViewHolder holder, int position) {
        EducationFeedItem bind=educationFeedItems.get(position);
        if(!bind.getImage().equals("null")) holder.InstImage.setImageUrl(bind.getImage(),imageLoader);
        holder.InstName.setText(bind.getInstitution_name());
        holder.InstProgram.setText(bind.getCoursename());
        Log.e("At Education adapter","fetch");
    }

    @Override
    public int getItemCount() {
        return educationFeedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView InstName,InstProgram;
        public FeedImageView InstImage;
        public RecyclerView rv_education;

        public ViewHolder(final View itemView) {
            super(itemView);

            rv_education=(RecyclerView)itemView.findViewById(R.id.recycle_education);
            InstProgram=(TextView)itemView.findViewById(R.id.tv_education_program);
            InstName=(TextView)itemView.findViewById(R.id.tv_education_institute);
            InstImage=(FeedImageView)itemView.findViewById(R.id.iv_education_feed);
            WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            display=wm.getDefaultDisplay();
            size=new Point();
            display.getSize(size);
            screenHeight=size.y;
            screenWidth=size.x;
            LinearLayout.LayoutParams parmsIMG = new LinearLayout.LayoutParams(screenWidth/2,150);
            InstImage.setLayoutParams(parmsIMG);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                    EducationFeedItem eduItem=educationFeedItems.get(getLayoutPosition());
                    eduItem.getInst_id();
                    Intent intent=new Intent(mContext, ViewEducationDetails.class);
                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
                            intent.putExtra("id",  eduItem.getCourse_id());
                            intent.putExtra("coursename",eduItem.getCoursename());
                    mContext.startActivity(intent);
                }
            });

//            rv_education.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });
        }
    }

}
