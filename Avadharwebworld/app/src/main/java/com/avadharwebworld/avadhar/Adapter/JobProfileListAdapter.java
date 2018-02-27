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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Activity.ViewJobDetails;
import com.avadharwebworld.avadhar.Activity.ViewJobProfileDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.Data.JobProfileLIstItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.FeedImageView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by user-03 on 12/24/2016.
 */

public class JobProfileListAdapter extends RecyclerView.Adapter<JobProfileListAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    boolean isClicked=false;
    private SharedPreferences sp;
    private String sp_uid;
    int screenWidth,screenHeight;
    Display display;
    Point size;

    private List<JobProfileLIstItem> jobItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public JobProfileListAdapter(Activity activity,Context context,List<JobProfileLIstItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.jobItems=item;
    }
    @Override
    public JobProfileListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_profile_list_layout, parent, false);
        return new JobProfileListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobProfileListAdapter.ViewHolder holder, int position) {
        JobProfileLIstItem bind=jobItems.get(position);

        if(!bind.getImage().equals("null")){
            holder.jobImage.setImageUrl(bind.getImage(),imageLoader);
        }
        if(!bind.getProfilid().equals("null")){
            holder.profileid.setText(bind.getProfilid());
        }
        if(!bind.getSalary().equals("null")){holder.salary.setText(bind.getSalary());}
        else { holder.salary.setVisibility(View.GONE);}
        if(!bind.getResumetitle().equals("null")){
            holder.jobdesc.setText(bind.getResumetitle());
        }else holder.jobdesc.setVisibility(View.GONE);

        if(!bind.getName().equals("null")){holder.Name.setText(bind.getName());}

        if(!bind.getQualification().equals("null")){holder.qualification.setText(bind.getQualification());}

    }

    @Override
    public int getItemCount() {
        return jobItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name,profileid,salary,qualification,location,jobdesc;
        public FeedImageView jobImage;
        public RecyclerView rv_job;



        public ViewHolder(final View itemView) {
            super(itemView);

            rv_job=(RecyclerView)itemView.findViewById(R.id.recycle_job_profile);

            jobImage=(FeedImageView)itemView.findViewById(R.id.iv_job_profile_list_feed);
            Name=(TextView)itemView.findViewById(R.id.tv_job_profile_list_name);
            profileid=(TextView)itemView.findViewById(R.id.tv_job_profile_list_id);
            salary=(TextView)itemView.findViewById(R.id.tv_job_profile_list_salary);
            qualification=(TextView)itemView.findViewById(R.id.tv_job_profile_list_qualification);
            jobdesc=(TextView)itemView.findViewById(R.id.tv_job_profile_list_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                    JobProfileLIstItem eduItem=jobItems.get(getLayoutPosition());

                    Intent intent=new Intent(mContext, ViewJobProfileDetails.class);
                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra("id",  eduItem.getId());
                    Toast.makeText(mContext.getApplicationContext(),eduItem.getId(),Toast.LENGTH_SHORT).show();
                    // intent.putExtra("coursename",eduItem.getCoursename());
                    mContext.startActivity(intent);
                }
            });




        }
    }

}
