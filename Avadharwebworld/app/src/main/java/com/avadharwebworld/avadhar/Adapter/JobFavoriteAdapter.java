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
import com.avadharwebworld.avadhar.Activity.JobMyFavorites;
import com.avadharwebworld.avadhar.Activity.Jobs;
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Activity.ViewJobDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.Data.JobItemFavorites;
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

public class JobFavoriteAdapter extends RecyclerView.Adapter<JobFavoriteAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    boolean isClicked=false;
    private SharedPreferences sp;
    private String sp_uid;
    int screenWidth,screenHeight;
    Display display;
    Point size;
    private String favoriteResult="";
    private List<JobItemFavorites> jobItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public JobFavoriteAdapter(Activity activity,Context context,List<JobItemFavorites> item){
        this.mActivity=activity;
        this.mContext=context;
        this.jobItems=item;
    }
    @Override
    public JobFavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jobfavoritelayout, parent, false);
        return new JobFavoriteAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobFavoriteAdapter.ViewHolder holder, int position) {
        JobItemFavorites bind=jobItems.get(position);
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
        public TextView companyName, profileid, salary, jobtype, experience, location, jobtitle,emptyview;
        public FeedImageView jobImage;
        public RecyclerView rv_job;
        public ImageButton removefavorite;
        public LinearLayout lvcomname, lvproid, lvsalary, lvjobtype, lvexperience, lvlocatioon, lvjobtitle;
        public RelativeLayout rvfavoritemessage;
        public com.github.ivbaranov.mfb.MaterialFavoriteButton favorite;


        public ViewHolder(final View itemView) {
            super(itemView);

            rv_job = (RecyclerView) itemView.findViewById(R.id.recycle_jobmyfavorites);

            lvproid = (LinearLayout) itemView.findViewById(R.id.lv_jobfavorite_id);
            lvsalary = (LinearLayout) itemView.findViewById(R.id.lv_jobfavorite_salary);
            lvjobtype = (LinearLayout) itemView.findViewById(R.id.lv_jobfavorite_jobetype);
            lvexperience = (LinearLayout) itemView.findViewById(R.id.lv_jobfavorite_experience);
            lvlocatioon = (LinearLayout) itemView.findViewById(R.id.lv_jobfavorite_location);
            lvcomname = (LinearLayout) itemView.findViewById(R.id.lv_jobfavorite_comapany);

//            rvfavoritemessage=(RelativeLayout)itemView.findViewById(R.id.rv_job_addedfavorite);

//            emptyview=(TextView)itemView.findViewById(R.id.tv_jobfavorite_empty_view);
            jobImage = (FeedImageView) itemView.findViewById(R.id.iv_jobfavorite_feed);
            removefavorite=(ImageButton)itemView.findViewById(R.id.iv_jobfavorite_delete) ;
            companyName = (TextView) itemView.findViewById(R.id.tv_jobfavorite_companyname);
            profileid = (TextView) itemView.findViewById(R.id.tv_jobfavorite_id);
            salary = (TextView) itemView.findViewById(R.id.tv_jobfavorite_salary);
            jobtype = (TextView) itemView.findViewById(R.id.tv_jobfavorite_jobtype);
            experience = (TextView) itemView.findViewById(R.id.tv_jobfavorite_experince);
            location = (TextView) itemView.findViewById(R.id.tv_jobfavorite_location);
            jobtitle = (TextView) itemView.findViewById(R.id.tv_jobfavorite_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                    JobItemFavorites Item = jobItems.get(getLayoutPosition());

                    Intent intent = new Intent(mContext, ViewJobDetails.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id", Item.getId());
                    // intent.putExtra("coursename",eduItem.getCoursename());
                    mContext.startActivity(intent);
                }
            });
            removefavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobMyFavorites jobMyFavorites=new JobMyFavorites();

                    JobItemFavorites Item = jobItems.get(getLayoutPosition());
                    jobItems.remove(getLayoutPosition());
                    setRemovefavorite(sp_uid,Item.getProfilid());
//                    setRemovefavorite(sp_uid,Item.getProfilid());
                    notifyItemRemoved(getLayoutPosition());
                    emptyView();
                    notifyItemRangeChanged(getLayoutPosition(), jobItems.size());



                }
            });



        }

        private void setRemovefavorite(final String uid, final String jobid) {
            final String[] res = new String[1];
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.RemovejobFavoriteURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Volleyresponse", response.toString());

//                            favoriteResult = response.toString();
//
//                            if (favoriteResult.equals("1")) {
                                Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(mContext, "Already Added To Favorite", Toast.LENGTH_SHORT).show();
//
//                            }
//                        setAnimationForFavoriteText(response,layout);
                            // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(mActivity, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("pid", String.valueOf(jobid));
                    params.put("uid", String.valueOf(uid));
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
    public interface OnDataChangeListener{
        public void onDataChanged(int size);
    }
    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }
    private void emptyView() {

        if(mOnDataChangeListener != null){
            mOnDataChangeListener.onDataChanged(jobItems.size());
        }
    }
}
