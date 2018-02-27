package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.JobFavoriteAdapter;
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.Data.JobItemFavorites;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobMyFavorites extends AppCompatActivity {
    RecyclerView rvMyfavorites;
    private int totalItemCount=0;
    private SharedPreferences sp;
    private String uid,profileid;
    boolean userScrolled = false;
    public RecyclerView.LayoutManager mLayoutManager;
    private List<JobItemFavorites> jobItems;
    public JobFavoriteAdapter adapter;
     public TextView emptyview;
    ProgressDialog pDialog;
    RecyclerView rv_jobsfavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_my_favorites);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.myfavorites);
        jobItems=new ArrayList<JobItemFavorites>();
        rv_jobsfavorite=(RecyclerView)findViewById(R.id.recycle_jobmyfavorites);
        emptyview=(TextView)findViewById(R.id.tv_jobfavorite_empty_view);
        emptyview.setVisibility(View.GONE);
        mLayoutManager=new LinearLayoutManager(this);
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        profileid=sp.getString(Constants.PROFILEID,"");
        uid=sp.getString(Constants.UID,"");
        adapter=new JobFavoriteAdapter(this,getApplicationContext(),jobItems);
        rvMyfavorites=(RecyclerView)findViewById(R.id.recycle_jobmyfavorites);
        rvMyfavorites.setHasFixedSize(true);
        rvMyfavorites.setLayoutManager(mLayoutManager);
        rvMyfavorites.setItemAnimator(new DefaultItemAnimator());
        getJobDetails();
        adapter.setOnDataChangeListener(new JobFavoriteAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(int size) {
                if(size==0){
                    emptyview.setVisibility(View.VISIBLE);
                }else {emptyview.setVisibility(View.GONE);}
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            super.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void getJobDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.ViewJobMyFavoritesURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
//                            Log.e("jobnect", String.valueOf(Jobject));
                            jobFeed(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        // dialog.hide();
                        Toast.makeText(JobMyFavorites.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                  params.put("uid", String.valueOf(uid));
//
//                offset+=15;
                Log.e("uid send", String.valueOf(uid));
                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void jobFeed(JSONObject response) {
        Log.e("inside jobfeed method",String.valueOf(response));
        try {
            JSONArray feedArray = response.getJSONArray("jobfeed");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                JobItemFavorites item=new JobItemFavorites();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setJobtitle(feedObj.isNull("jobtitle")? "null":feedObj.getString("jobtitle"));
                item.setJobtype(feedObj.isNull("jobtype")? "null":feedObj.getString("jobtype"));
                item.setSalary(feedObj.isNull("salary")?"null":feedObj.getString("salary"));

                item.setCompanyname(feedObj.isNull("cname")? "null":feedObj.getString("cname"));
                item.setImage(feedObj.isNull("vacancyimg")? "null":DatabaseInfo.JObImagePathURL+feedObj.getString("vacancyimg"));//image
                item.setProfilid(feedObj.isNull("vac_id")? "null":feedObj.getString("vac_id"));
                item.setLocation(feedObj.isNull("jobloc")? "null":feedObj.getString("jobloc"));
                item.setExperience(feedObj.isNull("experience")? "null":feedObj.getString("experience"));

                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                jobItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));

            adapter.notifyDataSetChanged();
            totalItemCount = mLayoutManager.getItemCount();
            if(adapter.getItemCount()==0){
                emptyview.setVisibility(View.VISIBLE);
            }else {emptyview.setVisibility(View.GONE);}
//            rv_jobsfavorite.scrollToPosition(totalItemCount);
            rv_jobsfavorite.setAdapter(adapter);
            pDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void clearData() {
        jobItems.clear(); //clear list
        adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
    }

}
