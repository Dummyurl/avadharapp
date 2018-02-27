package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
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
import com.avadharwebworld.avadhar.Adapter.JobAdapter;
import com.avadharwebworld.avadhar.Adapter.JobMyPackageAdapter;
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.Data.MyPackageItem;
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

public class ViewMyJobPackage extends AppCompatActivity {
    boolean userScrolled = false;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MyPackageItem> jobItems;
    PopupWindow popupWindow=null;
    JobMyPackageAdapter adapter;
    ProgressDialog pDialog;
    private SharedPreferences sp;
    private String uid;
    List<String>qualification=new ArrayList<String>();
    List<String>experience=new ArrayList<String>();
    List<String>interestedfield=new ArrayList<String>();
    List<String>jobrole=new ArrayList<String>();
    int parentid=0;
    TextView emptyview;
    RecyclerView rv_jobs;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,offset=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_job_package);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.viewjobpackage);
        getWindow().setExitTransition(new Explode());
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        jobItems=new ArrayList<MyPackageItem>();
        emptyview=(TextView)findViewById(R.id.tv_jobmypackage_empty_view);
        mLayoutManager=new LinearLayoutManager(this);
        adapter = new JobMyPackageAdapter(this, getApplicationContext(), jobItems);
        rv_jobs=(RecyclerView)findViewById(R.id.recycle_jobmypackages);
        rv_jobs.setHasFixedSize(true);
        rv_jobs.setLayoutManager(mLayoutManager);
        rv_jobs.setItemAnimator(new DefaultItemAnimator());
        getJobDetails();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetMyJobPakcage,
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
                        Toast.makeText(ViewMyJobPackage.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                  params.put("uid", String.valueOf(uid));
//
//                offset+=15;
//                Log.e("offset", String.valueOf(offset));
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
            JSONArray feedArray = response.getJSONArray("mypackage");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                MyPackageItem item=new MyPackageItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setResumetitle(feedObj.isNull("resumetitle")? "null":feedObj.getString("resumetitle"));
                item.setJobtype(feedObj.isNull("jobtype")?"null":feedObj.getString("jobtype"));
                item.setLocation(feedObj.isNull("joblocation")? "null":feedObj.getString("joblocation"));
                item.setExperience(feedObj.isNull("experience")? "null":feedObj.getString("experience"));
                item.setQualification(feedObj.isNull("qualification")? "null":feedObj.getString("qualification"));
                item.setInterstedfield(feedObj.isNull("interestedfield")? "null":feedObj.getString("interestedfield"));
                item.setCount(feedObj.isNull("pcount")? "null":feedObj.getString("pcount"));
                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                jobItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
//            rv_jobs.scrollToPosition(totalItemCount);
            rv_jobs.setAdapter(adapter);
            pDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
