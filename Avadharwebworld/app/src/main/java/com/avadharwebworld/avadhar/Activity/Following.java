package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.FollowingAdapter;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.FollowingItem;
import com.avadharwebworld.avadhar.Data.ProfileViewItem;
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

public class Following extends AppCompatActivity {
    private RecyclerView rv_following;
    private FollowingAdapter adapter;
    private ProgressDialog dialog;
    private List<FollowingItem> followingItems;
    private List<ProfileViewItem>profileViewItems;
    JSONArray Jsonarraymsgid;
    JSONObject Jsonobjectmsgid;
    private RecyclerView.LayoutManager mLayoutManager;
    JSONObject jsonObject;
    boolean userScrolled = false;
    public String testString="http://google.com";
    private int[] mdataType=null;
    public  String [] msgIDFULL,messageid={"",""};
    Constants consts;
    private String sp_uid,sp_name,sp_profileimage,sp_profileimgpath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_following);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");

        final SharedPreferences sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        followingItems =new ArrayList<FollowingItem>();
        profileViewItems=new ArrayList<ProfileViewItem>();
        rv_following=(RecyclerView)findViewById(R.id.recycle_following);
        adapter= new FollowingAdapter(this,getApplicationContext(), followingItems);
        rv_following.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rv_following.setLayoutManager(mLayoutManager);
        rv_following.setItemAnimator(new DefaultItemAnimator());
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        dialog.show();
        followdetails();
    }

    private void followdetails(){
        Log.e("enter into","follow details");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.FollowingDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            parseJsonFollowing(Jobject);

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        dialog.hide();

                        Toast.makeText(Following.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(sp_uid));
//                params.put("offset", String.valueOf(OFFSET));
//                OFFSET+=+15;
                Log.e("Uid send", String.valueOf(sp_uid));
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

    private void parseJsonFollowing(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("friendslist");
            String flw[]=new String[feedArray.length()];
            Log.e("friends list",String.valueOf(response));
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FollowingItem item=new FollowingItem();
                String coverpic=feedObj.isNull("coverpic")?DatabaseInfo.CoverPicUploadURl:feedObj.getString("coverpic");
                item.setCoverpic(DatabaseInfo.CoverPicUploadURl+coverpic);
                item.setUid(feedObj.getString("uid"));
                flw[i]=feedObj.getString("uid");
                item.setUsername(feedObj.getString("username"));
                String name=feedObj.isNull("name")?"null":feedObj.getString("name");
                item.setUname(name);
                item.setProfilepic(feedObj.isNull("profilepic")?DatabaseInfo.ProfilepicURL:DatabaseInfo.ProfilepicURL+feedObj.getString("profilepic"));


                followingItems.add(item);


            }
            Constants.Followid=flw;
            adapter.notifyDataSetChanged();
            rv_following.setAdapter(adapter);
            dialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
            dialog.hide();
        }


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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=null;
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }else {
            switch (item.getItemId()) {
                case R.id.menu_following_profile:
                    intent = new Intent(this, Profile.class);
                    intent.putExtra("id",  sp_uid);
                    break;
                case R.id.menu_following_follower:
                    intent = new Intent(this, Followers.class);
                    break;
            }
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.followin_menu, menu);
//
        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }
}
