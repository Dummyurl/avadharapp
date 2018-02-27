package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.BuyandsellViewAddAdapter;
import com.avadharwebworld.avadhar.Adapter.JobAdapter;
import com.avadharwebworld.avadhar.Adapter.JobMyVacancyAdapter;
import com.avadharwebworld.avadhar.Data.BuyandSellItem;
import com.avadharwebworld.avadhar.Data.JobItem;
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

public class JobMyVacancyPost extends AppCompatActivity {
    boolean userScrolled = false;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<JobItem> jobItems;
    PopupWindow popupWindow=null;
    JobMyVacancyAdapter adapter;
    ProgressDialog pDialog;

    List<String>qualification=new ArrayList<String>();
    List<String>experience=new ArrayList<String>();
    List<String>interestedfield=new ArrayList<String>();
    List<String>jobrole=new ArrayList<String>();
    int parentid=0;
    TextView emptyview;
    private SharedPreferences sp;
    private String uid,profileid;
    RecyclerView rv_jobs;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,offset=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_job_my_vacancy_post);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.viewmyvacancy);
        jobItems=new ArrayList<JobItem>();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        profileid=sp.getString(Constants.PROFILEID,"");
        emptyview=(TextView)findViewById(R.id.tv_job_myvacancy_empty_view);
        mLayoutManager=new LinearLayoutManager(this);
        adapter = new JobMyVacancyAdapter(this, getApplicationContext(), jobItems);
        rv_jobs=(RecyclerView)findViewById(R.id.recycle_job_myvacancy);
        rv_jobs.setHasFixedSize(true);
        rv_jobs.setLayoutManager(mLayoutManager);
        rv_jobs.setItemAnimator(new DefaultItemAnimator());
        getJobDetails();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            super.onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=null;
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.temp, menu);
//
        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }

    public void getJobDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.JobMyVacancyURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
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
                        Toast.makeText(JobMyVacancyPost.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                  params.put("profileid", String.valueOf(profileid));
//


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
                JobItem item=new JobItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setJobtitle(feedObj.isNull("jobtitle")? "null":feedObj.getString("jobtitle"));
                item.setProfilid(feedObj.isNull("vac_id")? "null":feedObj.getString("vac_id"));

                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                jobItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
            if(adapter.getItemCount()==0){
                emptyview.setVisibility(View.VISIBLE);
            }else {emptyview.setVisibility(View.GONE);}

            rv_jobs.scrollToPosition(totalItemCount);
            rv_jobs.setAdapter(adapter);
            pDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
