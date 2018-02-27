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
import android.widget.Button;
import android.widget.ImageButton;
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
import com.avadharwebworld.avadhar.Data.MyPackageItem;
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

public class JobMyPackageAdapter extends RecyclerView.Adapter<JobMyPackageAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    boolean isClicked=false;
    private SharedPreferences sp;
    private String sp_uid;
    int screenWidth,screenHeight;
    Display display;
    Point size;
    private String favoriteResult="";
    private List<MyPackageItem> jobItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public JobMyPackageAdapter(Activity activity,Context context,List<MyPackageItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.jobItems=item;
    }
    @Override
    public JobMyPackageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobmypackagelayout, parent, false);
        return new JobMyPackageAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobMyPackageAdapter.ViewHolder holder, int position) {
        MyPackageItem bind=jobItems.get(position);
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
//        if(bind.getJobtitle()!=null){holder.jobtitle.setText(bind.getJobtitle());}

//        if(bind.getCompanyname()!=null){holder.companyName.setText(bind.getCompanyname());}
//        else{
//            holder.lvcomname.setVisibility(View.GONE);
//        }
        holder.edit.setVisibility(View.GONE);
        if(!bind.getExperience().equals("null")){holder.experience.setText(bind.getExperience());}
        else{
            holder.lvexperience.setVisibility(View.GONE);
        }
//        if(!bind.getProfilid().equals("null")){holder.profileid.setText(bind.getProfilid());}else{
//            holder.lvproid.setVisibility(View.GONE);
//        }
//        if(!bind.getSalary().equals("null")){holder.salary.setText(bind.getSalary());}
//        else{
//            holder.lvsalary.setVisibility(View.GONE);
//        }
        if(!bind.getLocation().equals("null")){holder.location.setText(bind.getLocation());}
        else{
            holder.lvlocatioon.setVisibility(View.GONE);
        }
        if(!bind.getJobtype().equals("null")){holder.jobtype.setText(bind.getJobtype());}
        else{
            holder.lvjobtype.setVisibility(View.GONE);
        }
        if(!bind.getQualification().equals("null")){holder.qualification.setText(bind.getJobtype());}
        else{
            holder.lvjobtype.setVisibility(View.GONE);
        }
//        if(!bind.getImage().equals("null")){holder.jobImage.setImageUrl(bind.getImage(),imageLoader);}
//        else holder.jobImage.setVisibility(View.GONE);
            holder.view.setText(mContext.getResources().getString(R.string.matchedprofile)+" ("+bind.getCount()+")");





    }

    @Override
    public int getItemCount() {
        return jobItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView salary,jobtype,experience,location,jobtitle,qualification;
        public FeedImageView jobImage;
        public RecyclerView rv_job;
        public Button view;
        public ImageButton edit;
        public LinearLayout lvcomname,lvproid,lvsalary,lvjobtype,lvexperience,lvlocatioon,lvqualification;
        public RelativeLayout rvfavoritemessage;
        public com.github.ivbaranov.mfb.MaterialFavoriteButton favorite;


        public ViewHolder(final View itemView) {
            super(itemView);

            rv_job=(RecyclerView)itemView.findViewById(R.id.recycle_job);

            lvproid=(LinearLayout)itemView.findViewById(R.id.lv_jobmypackage_id);

            lvjobtype=(LinearLayout)itemView.findViewById(R.id.lv_jobmypackage_id);
            lvexperience=(LinearLayout)itemView.findViewById(R.id.lv_jobmypackage_exp);
            lvlocatioon=(LinearLayout)itemView.findViewById(R.id.lv_jobmypackage_location);
            lvqualification=(LinearLayout)itemView.findViewById(R.id.lv_jobmypackage_qualification);


            jobtype=(TextView)itemView.findViewById(R.id.tv_jobmypackage_jobtype);
            experience=(TextView)itemView.findViewById(R.id.tv_jobmypackage_experience);
            location=(TextView)itemView.findViewById(R.id.tv_jobmypackage_location);
            qualification=(TextView)itemView.findViewById(R.id.tv_jobmypackage_qualification);
            edit=(ImageButton)itemView.findViewById(R.id.btn_jobmypackage_edit);
            view=(Button)itemView.findViewById(R.id.btn_jobmypackage_matchprofile);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
//                    MyPackageItem eduItem=jobItems.get(getLayoutPosition());
//
//                    Intent intent=new Intent(mContext, ViewJobDetails.class);
//                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
//                    intent.putExtra("id",  eduItem.getId());
//                    // intent.putExtra("coursename",eduItem.getCoursename());
//                    mContext.startActivity(intent);
//                }
//            });

//            rv_education.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext,"clicked edit",Toast.LENGTH_SHORT).show();
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext,"clicked view",Toast.LENGTH_SHORT).show();
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
