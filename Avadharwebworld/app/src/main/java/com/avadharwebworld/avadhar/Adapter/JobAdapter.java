package com.avadharwebworld.avadhar.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Handler;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.avadharwebworld.avadhar.Activity.Jobs;
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Activity.ViewJobDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by user-03 on 12/24/2016.
 */

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    boolean isClicked=false;
    private SharedPreferences sp;
    private String sp_uid;
    int screenWidth,screenHeight;
    Display display;
    Point size;
    private String favoriteResult="";
    private List<JobItem> jobItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public JobAdapter(Activity activity,Context context,List<JobItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.jobItems=item;
    }
    @Override
    public JobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_layout, parent, false);
        return new JobAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobAdapter.ViewHolder holder, int position) {
        JobItem bind=jobItems.get(position);
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        if(bind.getJobtitle()!=null){holder.jobtitle.setText(bind.getJobtitle());}

        if(bind.getCompanyname()!=null){holder.companyName.setText(bind.getCompanyname());}
        else{
            holder.lvcomname.setVisibility(View.GONE);
        }
        if(!bind.getExperience().equals("null")){holder.experience.setText(bind.getExperience());}
        else{
            holder.lvexperience.setVisibility(View.GONE);
        }
        if(!bind.getProfilid().equals("null")){holder.profileid.setText(bind.getProfilid());}else{
            holder.lvproid.setVisibility(View.GONE);
        }
        if(!bind.getSalary().equals("null")){holder.salary.setText(bind.getSalary());}
        else{
            holder.lvsalary.setVisibility(View.GONE);
        }
        if(!bind.getLocation().equals("null")){holder.location.setText(bind.getLocation());}
        else{
            holder.lvlocatioon.setVisibility(View.GONE);
        }
        if(!bind.getJobtype().equals("null")){holder.jobtype.setText(bind.getJobtype());}
        else{
            holder.lvjobtype.setVisibility(View.GONE);
        }
        if(!bind.getImage().equals("null")){holder.jobImage.setImageUrl(bind.getImage(),imageLoader);}
        else holder.jobImage.setVisibility(View.GONE);




    }

    @Override
    public int getItemCount() {
        return jobItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView companyName,profileid,salary,jobtype,experience,location,jobtitle;
        public FeedImageView jobImage;
        public RecyclerView rv_job;
        public LinearLayout lvcomname,lvproid,lvsalary,lvjobtype,lvexperience,lvlocatioon,lvjobtitle;
        public RelativeLayout rvfavoritemessage;
        public com.github.ivbaranov.mfb.MaterialFavoriteButton favorite;


        public ViewHolder(final View itemView) {
            super(itemView);

            rv_job=(RecyclerView)itemView.findViewById(R.id.recycle_job);

            lvproid=(LinearLayout)itemView.findViewById(R.id.lv_job_id);
            lvsalary=(LinearLayout)itemView.findViewById(R.id.lv_job_salary);
            lvjobtype=(LinearLayout)itemView.findViewById(R.id.lv_job_jobetype);
            lvexperience=(LinearLayout)itemView.findViewById(R.id.lv_job_experience);
            lvlocatioon=(LinearLayout)itemView.findViewById(R.id.lv_job_location);
            lvcomname=(LinearLayout)itemView.findViewById(R.id.lv_job_comapany);
            rvfavoritemessage=(RelativeLayout)itemView.findViewById(R.id.rv_job_addedfavorite);

            jobImage=(FeedImageView)itemView.findViewById(R.id.iv_job_feed);
            companyName=(TextView)itemView.findViewById(R.id.tv_job_companyname);
            profileid=(TextView)itemView.findViewById(R.id.tv_job_id);
            salary=(TextView)itemView.findViewById(R.id.tv_job_salary);
            jobtype=(TextView)itemView.findViewById(R.id.tv_job_jobtype);
            experience=(TextView)itemView.findViewById(R.id.tv_job_experince);
            location=(TextView)itemView.findViewById(R.id.tv_job_location);
            jobtitle=(TextView)itemView.findViewById(R.id.tv_job_name);
            favorite=(com.github.ivbaranov.mfb.MaterialFavoriteButton)itemView.findViewById(R.id.iv_job_favorite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                    JobItem eduItem=jobItems.get(getLayoutPosition());

                    Intent intent=new Intent(mContext, ViewJobDetails.class);
                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra("id",  eduItem.getId());
                   // intent.putExtra("coursename",eduItem.getCoursename());
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
            favorite.setOnFavoriteAnimationEndListener(new MaterialFavoriteButton.OnFavoriteAnimationEndListener() {
                @Override
                public void onAnimationEnd(MaterialFavoriteButton buttonView, boolean favorite) {

                }
            });
            favorite.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            //
                            JobItem eduItem=jobItems.get(getLayoutPosition());
                         setFavorite(sp_uid,eduItem.getProfilid());

//                            Handler mHandler = new Handler();
//                            mHandler.postDelayed(new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    Log.e("favorite result",favoriteResult);
//                                    if(favoriteResult.equals("1")) {
////                                        rvfavoritemessage.setVisibility(View.VISIBLE);
////                                        AnimationSet set = new AnimationSet(true);
////                                        Animation trAnimation = new TranslateAnimation(0, 1000, 0, 0);
////                                        trAnimation.setDuration(3000);
////                                        trAnimation.setRepeatMode(Animation.REVERSE);
////
////                                        set.addAnimation(trAnimation);
////                                        Animation anim = new AlphaAnimation(1.0f, 0.0f);
////                                        anim.setDuration(2000);
////                                        set.addAnimation(anim);
////                                        rvfavoritemessage.startAnimation(set);
////
////                                        set.setAnimationListener(new Animation.AnimationListener() {
////                                            @Override
////                                            public void onAnimationStart(Animation animation) {
////
////                                            }
////
////                                            @Override
////                                            public void onAnimationEnd(Animation animation) {
////                                                rvfavoritemessage.setVisibility(View.GONE);
////                                            }
////
////                                            @Override
////                                            public void onAnimationRepeat(Animation animation) {
////
////                                            }
////                                        });
//
//                                    }   else {
//                                        Toast.makeText(mContext,"Already Added To Favorite",Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//
//                            }, 2000L);
//

                }
            });



        }
    }
    private void   setFavorite(final String uid, final String jobid){
        final String[] res = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.AddToJobFavoritesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                     favoriteResult =response.toString();


                            Toast.makeText(mContext,favoriteResult,Toast.LENGTH_SHORT).show();

//                        setAnimationForFavoriteText(response,layout);
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mActivity,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid", String.valueOf(jobid));
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
