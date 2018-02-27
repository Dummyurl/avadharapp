package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.MatrimonyFavoriteAdapter;
import com.avadharwebworld.avadhar.Adapter.MatrimonyInterestAdapter;
import com.avadharwebworld.avadhar.Adapter.MatrimonyInterestRecievedAdapter;
import com.avadharwebworld.avadhar.Data.MatrimonyFavoriteItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestRecievedItem;
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

public class MatrimonyInterestedProfile extends AppCompatActivity {
    ProgressDialog pDialog;
    private RecyclerView rv_matrimony,rv_matrimony_recieved;
    boolean userScrolled = false;
    private List<MatrimonyInterestItem> MatrimonyFavoriteItems;
    private List<MatrimonyInterestRecievedItem> MatrimonyInterestedItems;
    MatrimonyInterestAdapter  adapter;
    MatrimonyInterestRecievedAdapter adapter_recieved;
    private RecyclerView.LayoutManager manager_recieved,manager;
    int pastVisiblesItems_recievd,pastVisiblesItems, visibleItemCount_recieved, totalItemCount_receieved, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,page=1;
    TextView empryview_recieved,empryview;
    private SharedPreferences sp;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony_interested_profile);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.matrimonyinterest);
        TabHost host = (TabHost) findViewById(R.id.th_matrimony_interest);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.send));
        spec.setContent(R.id.matri_interest_tab1);
        spec.setIndicator(getResources().getString(R.string.send));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getResources().getString(R.string.recieved));
        spec.setContent(R.id.matri_interest_tab2);
        spec.setIndicator(getResources().getString(R.string.recieved));
        host.addTab(spec);
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        sendInterest();
        recievedInterest();



    }
    private void getMatriDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyInterestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            MatriFeed(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        // dialog.hide();
                        Toast.makeText(MatrimonyInterestedProfile.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(uid));
//                page+=1;
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
    private void MatriFeed(JSONObject response) {
        Log.e("inside jobfeed method",String.valueOf(response));
        try {
            JSONArray feedArray = response.getJSONArray("matrimonial");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                MatrimonyInterestItem item=new MatrimonyInterestItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setImage(feedObj.isNull("image")? DatabaseInfo.MatrimonialImagePath+"default.jpg":DatabaseInfo.MatrimonialImagePath+feedObj.getString("image"));
                item.setName(feedObj.isNull("mat_name")?"null":feedObj.getString("mat_name"));
                item.setReligion(feedObj.isNull("mat_religion")? "null":feedObj.getString("mat_religion"));
                item.setMprofileid(feedObj.isNull("mprofileid")?"null":feedObj.getString("mprofileid"));

                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                MatrimonyFavoriteItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
//            rv_jobs.scrollToPosition(totalItemCount);
            if(adapter.getItemCount()==0){
                empryview.setVisibility(View.VISIBLE);
            }else {empryview.setVisibility(View.GONE);}
            rv_matrimony.setAdapter(adapter);
            pDialog.hide();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void getMatriDetailsRecieved() {
        Log.e("new malayalam","getMatrirecieved()");


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyInterestRecievedURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            MatriFeedRecieved(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // dialog.hide();
                        Toast.makeText(MatrimonyInterestedProfile.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(uid));
//                page+=1;
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
    private void MatriFeedRecieved(JSONObject response) {
        Log.e("inside recieved",String.valueOf(response));
        try {
            JSONArray feedArray = response.getJSONArray("matrimonial");

            Log.e("malayalam",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                MatrimonyInterestRecievedItem item=new MatrimonyInterestRecievedItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                Log.e("id",String.valueOf(item.getId()));
                item.setImage(feedObj.isNull("image")? DatabaseInfo.MatrimonialImagePath+"default.jpg":DatabaseInfo.MatrimonialImagePath+feedObj.getString("image"));
                Log.e("image",String.valueOf(item.getImage()));
                item.setName(feedObj.isNull("mat_name")?"null":feedObj.getString("mat_name"));
                Log.e("name",String.valueOf(item.getName()));
//                item.setReligion(feedObj.isNull("mat_religion")? "null":feedObj.getString("mat_religion"));
                item.setMprofileid(feedObj.isNull("mprofileid")?"null":feedObj.getString("mprofileid"));
                 item.setInteresteduid(feedObj.isNull("interstuid")?"null":feedObj.getString("interstuid"));
                Log.e("profile id",String.valueOf(item.getMprofileid()));
                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                MatrimonyInterestedItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter_recieved.notifyDataSetChanged();
            if(adapter_recieved.getItemCount()==0){
                empryview_recieved.setVisibility(View.VISIBLE);
            }else {empryview_recieved.setVisibility(View.GONE);}
            rv_matrimony_recieved.setAdapter(adapter_recieved);
//            pDialog.hide();

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    private void sendInterest(){
        MatrimonyFavoriteItems=new ArrayList<MatrimonyInterestItem>();
        manager=new LinearLayoutManager(this);
        rv_matrimony=(RecyclerView)findViewById(R.id.recycle_matrimony_interest_send);
        empryview=(TextView)findViewById(R.id.tv_matrimony_interest_send_empty_view);
        adapter=new MatrimonyInterestAdapter(this,getApplicationContext(),MatrimonyFavoriteItems);
        rv_matrimony.setHasFixedSize(true);
        rv_matrimony.setLayoutManager(manager);
        rv_matrimony.setItemAnimator(new DefaultItemAnimator());

        rv_matrimony.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                visibleItemCount = manager.getChildCount();
                totalItemCount = manager.getItemCount();

                Log.e("total item count", String.valueOf(totalItemCount));
                pastVisiblesItems = ((LinearLayoutManager) rv_matrimony.getLayoutManager()).findFirstVisibleItemPosition();
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
//                                getMatriDetails();
//                                listAdapter.
                                //    bottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);


                    }
                }
            }


        });
        getMatriDetails();
    }
    private void recievedInterest(){
        MatrimonyInterestedItems=new ArrayList<MatrimonyInterestRecievedItem>();
        manager_recieved=new LinearLayoutManager(this);
        rv_matrimony_recieved=(RecyclerView)findViewById(R.id.recycle_matrimony_interest_recieved);
        empryview_recieved=(TextView)findViewById(R.id.tv_matrimony_interest_recieved_empty_view);
        adapter_recieved=new MatrimonyInterestRecievedAdapter(this,getApplicationContext(),MatrimonyInterestedItems);
        rv_matrimony_recieved.setHasFixedSize(true);
        rv_matrimony_recieved.setLayoutManager(manager_recieved);
        rv_matrimony_recieved.setItemAnimator(new DefaultItemAnimator());
        rv_matrimony_recieved.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                visibleItemCount_recieved = manager.getChildCount();
                totalItemCount_receieved = manager.getItemCount();

                Log.e("total item count", String.valueOf(totalItemCount_receieved));
                pastVisiblesItems_recievd = ((LinearLayoutManager) rv_matrimony_recieved.getLayoutManager()).findFirstVisibleItemPosition();
                Log.e("past Visible item", String.valueOf(pastVisiblesItems_recievd));
                Log.e("visible item count", String.valueOf(visibleItemCount_recieved));

                if (dy > 0) {

//                    if(userScrolled){
//                        if (totalItemCount > previousTotal) {
//                            userScrolled = true;
//                            previousTotal = totalItemCount;
//                        }

                    if (userScrolled && (visibleItemCount_recieved + pastVisiblesItems_recievd) == totalItemCount_receieved) {
                        userScrolled = false;
                        //     bottomLayout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                getMatriDetails();
//                                listAdapter.
                                //    bottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);


                    }
                }
            }


        });
        Log.e("new ","getMatrirecieved()");
        getMatriDetailsRecieved();
    }

}
