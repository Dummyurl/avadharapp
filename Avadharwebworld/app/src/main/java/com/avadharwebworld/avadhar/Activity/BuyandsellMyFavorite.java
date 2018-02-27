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
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.BuyandSellAdapter;
import com.avadharwebworld.avadhar.Adapter.BuyandSellFavoriteAdapter;
import com.avadharwebworld.avadhar.Data.BuyandSellItem;
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

public class BuyandsellMyFavorite extends AppCompatActivity {
    ProgressDialog pDialog;
    private RecyclerView rv_buyandsell;
    boolean userScrolled = false;
    private List<BuyandSellItem> buyandSellItems;
    BuyandSellFavoriteAdapter adapter;
    private RecyclerView.LayoutManager manager;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,page=1;
    TextView empryview;
    private SharedPreferences sp;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
         setContentView(R.layout.activity_buyandsell_my_favorite);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.myfavorites);
        buyandSellItems=new ArrayList<BuyandSellItem>();
        sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        manager=new LinearLayoutManager(this);
        rv_buyandsell=(RecyclerView)findViewById(R.id.recycle_buyandsell_favorite);
        empryview=(TextView)findViewById(R.id.tv_buyandsell_favorite_empty_view);
        adapter=new BuyandSellFavoriteAdapter(this,getApplicationContext(),buyandSellItems);
        rv_buyandsell.setHasFixedSize(true);
        rv_buyandsell.setLayoutManager(manager);
        rv_buyandsell.setItemAnimator(new DefaultItemAnimator());

        rv_buyandsell.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                pastVisiblesItems = ((LinearLayoutManager) rv_buyandsell.getLayoutManager()).findFirstVisibleItemPosition();
                Log.e("past Visible item", String.valueOf(pastVisiblesItems));
                Log.e("visible item count", String.valueOf(visibleItemCount));

                if (dy > 0) {

//
                    if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                        userScrolled = false;
                        //     bottomLayout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
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
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.temp, menu);

        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }

    public void getMatriDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.BuyandsellFavoriteURL,
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
                        Toast.makeText(BuyandsellMyFavorite.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("logid", String.valueOf(uid));
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
            JSONArray feedArray = response.getJSONArray("buyandsell");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                BuyandSellItem item=new BuyandSellItem();

                item.setId(feedObj.isNull("id")? "":feedObj.getString("id"));
                item.setImage(feedObj.isNull("image")? DatabaseInfo.BuyansellImagePathURL+"default.jpg":DatabaseInfo.BuyansellImagePathURL+feedObj.getString("image"));
                item.setName(feedObj.isNull("addtitle")? "":feedObj.getString("addtitle"));
                item.setPrice(feedObj.isNull("price")? "":feedObj.getString("price"));
                item.setMprofileid(feedObj.isNull("profilid")?" ":feedObj.getString("profilid"));
//
                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                buyandSellItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
            if(adapter.getItemCount()==0){
                empryview.setVisibility(View.VISIBLE);
            }else {empryview.setVisibility(View.GONE);}

            rv_buyandsell.setAdapter(adapter);
            rv_buyandsell.scrollToPosition(totalItemCount);
            pDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
