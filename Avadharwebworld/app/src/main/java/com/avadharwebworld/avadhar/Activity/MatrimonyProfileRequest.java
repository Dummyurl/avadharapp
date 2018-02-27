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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.MatrimonyEditAdapter;
import com.avadharwebworld.avadhar.Adapter.MatrimonyRequestAdapter;
import com.avadharwebworld.avadhar.Data.MatrimonyEditItem;
import com.avadharwebworld.avadhar.Data.MatrimonyRequestItem;
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

public class MatrimonyProfileRequest extends AppCompatActivity {
    ProgressDialog pDialog;
    private RecyclerView rv_matrimony;
    boolean userScrolled = false;
    private List<MatrimonyRequestItem> matrimonyItems;
    MatrimonyRequestAdapter adapter;
    private RecyclerView.LayoutManager manager;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,page=1;
    TextView empryview;
    private SharedPreferences sp;
    private String uid,profileid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony_profile_request);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.profilerequest);
        matrimonyItems=new ArrayList<MatrimonyRequestItem>();
        manager=new LinearLayoutManager(this);
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        profileid=sp.getString(Constants.PROFILEID,"");
        uid=sp.getString(Constants.UID,"");
        rv_matrimony=(RecyclerView)findViewById(R.id.recycle_matrimony_profilerequest);
        empryview=(TextView)findViewById(R.id.tv_matrimony_profilerequest_empty_view);
        adapter=new MatrimonyRequestAdapter(this,getApplicationContext(),matrimonyItems);
        rv_matrimony.setHasFixedSize(true);
        rv_matrimony.setLayoutManager(manager);
        rv_matrimony.setItemAnimator(new DefaultItemAnimator());
        getMatriDetails();
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
    public void getMatriDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyEditviewURL,
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
                        Toast.makeText(MatrimonyProfileRequest.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("profileid", String.valueOf(profileid));
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
                MatrimonyRequestItem item=new MatrimonyRequestItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setStatus(feedObj.isNull("mat_martial")?"null":feedObj.getString("mat_martial"));
                item.setName(feedObj.isNull("mat_name")? "null":feedObj.getString("mat_name"));
                item.setMprofileid(feedObj.isNull("mprofileid")?"null":feedObj.getString("mprofileid"));

                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                matrimonyItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
//            rv_jobs.scrollToPosition(totalItemCount);
            rv_matrimony.setAdapter(adapter);
            pDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
