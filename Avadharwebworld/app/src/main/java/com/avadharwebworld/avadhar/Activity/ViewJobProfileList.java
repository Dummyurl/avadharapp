package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.JobAdapter;
import com.avadharwebworld.avadhar.Adapter.JobProfileListAdapter;
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.Data.JobProfileLIstItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewJobProfileList extends AppCompatActivity {
    boolean userScrolled = false;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<JobProfileLIstItem> jobItems;
    PopupWindow popupWindow=null;
    JobProfileListAdapter adapter;
    ProgressDialog pDialog;
    RecyclerView rv_job_profile;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,offset=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_profile_list);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.jobprofiles);
        getWindow().setExitTransition(new Explode());
        jobItems=new ArrayList<JobProfileLIstItem>();
        mLayoutManager=new LinearLayoutManager(this);
        adapter = new JobProfileListAdapter(this, getApplicationContext(), jobItems);
        rv_job_profile=(RecyclerView)findViewById(R.id.recycle_job_profile);
        rv_job_profile.setHasFixedSize(true);
        rv_job_profile.setLayoutManager(mLayoutManager);
        rv_job_profile.setItemAnimator(new DefaultItemAnimator());
        rv_job_profile.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) ;
                {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();

                Log.e("total item count", String.valueOf(totalItemCount));
                pastVisiblesItems = ((LinearLayoutManager) rv_job_profile.getLayoutManager()).findFirstVisibleItemPosition();
                Log.e("past Visible item", String.valueOf(pastVisiblesItems));
                Log.e("visible item count", String.valueOf(visibleItemCount));

                if (dy > 0) {

//                    if(userScrolled){
//                        if (totalItemCount > previousTotal) {
//                            userScrolled = true;
//                            previousTotal = totalItemCount;
//                        }

                    if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                        userScrolled = false;
                        //     bottomLayout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getJobProfileList();
//                                listAdapter.
                                //    bottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);


                    }
                }
            }


        });
        getJobProfileList();
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog

        super.onBackPressed();

//        Intent intent = new Intent(Following.this, MainActivity.class);
//        startActivity(intent);

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
//            case R.id.menu_follower_profile:
//                intent=new Intent(this,Profile.class);
//                break;
//            case R.id.menu_follower_follower:
//                intent=new Intent(this,Followers.class);
//                break;
        }
        //startActivity(intent);
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

    public void getJobProfileList() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.ViewJobProfileListURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            jobProfilefeed(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        // dialog.hide();
                        Toast.makeText(ViewJobProfileList.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                  params.put("pagelimit", String.valueOf(offset));
//
                offset+=1;
                Log.e("offset", String.valueOf(offset));
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
    private void jobProfilefeed(JSONObject response) {
        Log.e("inside jobfeed method",String.valueOf(response));
        try {
            JSONArray feedArray = response.getJSONArray("jobprofile");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                JobProfileLIstItem item=new JobProfileLIstItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setResumetitle(feedObj.isNull("resumetitle")? "null":feedObj.getString("resumetitle"));
               // item.setJobtype(feedObj.isNull("jobtype")? "null":feedObj.getString("jobtype"));
                item.setSalary(feedObj.isNull("salary")?"null":feedObj.getString("salary"));

                item.setName(feedObj.isNull("uname")? "null":feedObj.getString("uname"));
                item.setImage(feedObj.isNull("photo")? DatabaseInfo.JObImagePathURL+"default.jpg":DatabaseInfo.JObImagePathURL+feedObj.getString("photo"));//image
                item.setProfilid(feedObj.isNull("profileid")? "null":feedObj.getString("profileid"));
                item.setQualification(feedObj.isNull("qualification")?"null":feedObj.getString("qualification"));


                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                jobItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
            rv_job_profile.scrollToPosition(totalItemCount);
            rv_job_profile.setAdapter(adapter);
            pDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
