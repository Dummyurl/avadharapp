package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.transition.Explode;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.avadharwebworld.avadhar.Adapter.HisstoryAdapter;
import com.avadharwebworld.avadhar.Adapter.HisstoryAdapter2;
import com.avadharwebworld.avadhar.Adapter.HisstoryAdapter3;
import com.avadharwebworld.avadhar.Adapter.MatrimonyInterestAdapter;
import com.avadharwebworld.avadhar.Adapter.MatrimonyInterestRecievedAdapter;
import com.avadharwebworld.avadhar.Data.HisstoryItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestRecievedItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class HisStory extends AppCompatActivity {
    private FloatingActionButton create;
    LayoutInflater inflater;
    private RecyclerView rv_hisstorypost,rv_hisstorytop,rv_hisstory_latest;
    PopupWindow popupWindow=null;
    int offset=0,offset1=0,offset2=0;
    LinearLayout Ltab1,Ltab2,Ltab3,Lhistorytab;
    boolean userScrolled = false;
    private List<HisstoryItem> hisstoryItems1,hisstoryItems2,hisstoryItems3;
    private List<MatrimonyInterestRecievedItem> MatrimonyInterestedItems;
    HisstoryAdapter adapter1,adpater21,adapter31;
    HisstoryAdapter2 adpater2;
    HisstoryAdapter3 adapter3;
    private RecyclerView.LayoutManager manager_recieved,manager,manager2,manager1;
    int pastVisiblesItems_recievd,pastVisiblesItems, visibleItemCount_recieved, totalItemCount_receieved, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,page=1;
    TextView empryview_top,empry_latest,empryview;
    int pastVisiblesItems1,visibleItemCount1, totalItemCount1,previousTotal1,screenWidth1,screenHeight1,pastVisiblesItems2,visibleItemCount2, totalItemCount2,previousTotal2,screenWidth2,screenHeight2;
    private SharedPreferences sp;
    private String uid;
    TabHost host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_his_story);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.nav_hisstory);
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        create=(FloatingActionButton)findViewById(R.id.history_fab);
        Ltab1=(LinearLayout)findViewById(R.id.hisstory_tab1);
        Ltab2=(LinearLayout)findViewById(R.id.hisstory_tab2);
        Ltab3=(LinearLayout)findViewById(R.id.hisstory_tab3);
        Lhistorytab=(LinearLayout)findViewById(R.id.ll_hisstory_tab);
//        Lhistorytab.bringToFront();
         host = (TabHost) findViewById(R.id.th_hisstory);
        host.setup();
        Ltab1.bringToFront();
        Ltab2.bringToFront();
//        Ltab3.bringToFront();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.hisstorypost));
        spec.setContent(R.id.hisstory_tab1);
        spec.setIndicator(getResources().getString(R.string.hisstorypost));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getResources().getString(R.string.toprating));
        spec.setContent(R.id.hisstory_tab2);
        spec.setIndicator(getResources().getString(R.string.toprating));
        host.addTab(spec);
//        Tab 3
        spec = host.newTabSpec(getResources().getString(R.string.latest));
        spec.setContent(R.id.hisstory_tab3);
        spec.setIndicator(getResources().getString(R.string.latest));
        host.addTab(spec);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();

            }
        });
        setLatest();
        setPost();
        setTop();

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
        if(item.getItemId()==R.id.hisstory_action_search){

        }else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    super.onBackPressed();

            }
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.hisstory, menu);
//        final EditText editText = (EditText) menu.findItem(
//                R.id.menu_search).getActionView();
//        editText.addTextChangedListener(textWatcher);
//
//        MenuItem menuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.hisstory_action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                TextView title = (TextView) host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
                title.setText(getResources().getString(R.string.searchresult));
                hisstoryItems1.clear();
                adapter1.notifyDataSetChanged();
                setSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//
        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }
    private void setPost(){
        hisstoryItems1=new ArrayList<HisstoryItem>();
        manager=new LinearLayoutManager(this);
        rv_hisstorypost=(RecyclerView)findViewById(R.id.recycle_hisstory_post);
        empryview=(TextView)findViewById(R.id.tv_hisstory_post_empty_view);
        adapter1=new HisstoryAdapter(this,getApplicationContext(),hisstoryItems1);
        rv_hisstorypost.setHasFixedSize(true);
        rv_hisstorypost.setLayoutManager(manager);
        rv_hisstorypost.setItemAnimator(new DefaultItemAnimator());

        rv_hisstorypost.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                pastVisiblesItems = ((LinearLayoutManager) rv_hisstorypost.getLayoutManager()).findFirstVisibleItemPosition();
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
                                getMatriDetails();
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
    private void showPopup() {
        try {
            final Button create;
            final EditText title,description;
//            android.view.Display display = ((android.view.WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//            int height = displaymetrics.heightPixels;
//            int width = displaymetrics.widthPixels;
// We need to get the instance of the LayoutInflater

            LayoutInflater layout = (LayoutInflater) this.getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layout.inflate(R.layout.history_create, null);
            Display display = this.getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int width = display.getWidth();

            create=(Button)popupView.findViewById(R.id.btn_hisstory_save);
            title=(EditText)popupView.findViewById(R.id.et_hisstory_title);
            description=(EditText)popupView.findViewById(R.id.et_hisstory_content);

                    popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!title.getText().toString().equals("")&&!description.getText().toString().equals("")){
                        HisstoryItem item=new HisstoryItem();
                        createPost(description.getText().toString(), title.getText().toString(), uid);
                        adapter3.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(),"Please fill all field..!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                        popupWindow.dismiss();

                        return true;

                    }
//                    eduLayout.getForeground().setAlpha(0);
                    return  false;
                }
            });


            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
//        dialog.setTitle("Search");
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);;

//            popup_close.setOnClickListener(cancel_button);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getMatriDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.HisStoryPostURL+"?offset=1",
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

                        // dialog.hide();
                        Toast.makeText(HisStory.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", String.valueOf(offset));
                offset+=10;
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
            JSONArray feedArray = response.getJSONArray("hisstory");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                HisstoryItem item=new HisstoryItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setImage(feedObj.isNull("profile_pic")? DatabaseInfo.ProfilepicURL+"default.jpg":DatabaseInfo.ProfilepicURL+feedObj.getString("profile_pic"));
//
               String titile=feedObj.isNull("name")?"":feedObj.getString("name");
                byte[] utf8Bytes = titile.getBytes("UTF8");
                String test=new String(titile.getBytes(),"UTF8");
                item.setName(feedObj.isNull("username")?"null":feedObj.getString("username"));
//                item.setReligion(feedObj.isNull("mat_religion")? "null":feedObj.getString("mat_religion"));
//                item.setMprofileid(feedObj.isNull("mprofileid")?"null":feedObj.getString("mprofileid"));
                item.setDescription(feedObj.getString("address"));
                item.setTitle(feedObj.isNull("name")?"":feedObj.getString("name"));
                item.setDate(feedObj.isNull("hisdate")?"":feedObj.getString("hisdate"));
//                item.setDb(feedObj.isNull("db_backup_stat")?"":feedObj.getString("db_backup_stat"));
                item.setUid(feedObj.isNull("uid")?"":feedObj.getString("uid"));
                 Log.e("image profilepic",String.valueOf(DatabaseInfo.ProfilepicURL+feedObj.getString("profile_pic")));
                Log.e("image desc",String.valueOf(feedObj.getString("address")));
                Log.e("image name",String.valueOf(feedObj.getString("username")));
                Log.e("image uid",String.valueOf(feedObj.getString("uid")));
//                Log.e("image db",String.valueOf(feedObj.getString("db_backup_stat")));

                hisstoryItems1.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter1.notifyDataSetChanged();
            rv_hisstorypost.scrollToPosition(totalItemCount);
            if(adapter1.getItemCount()==0){
                empryview.setVisibility(View.VISIBLE);
            }else {empryview.setVisibility(View.GONE);}
            rv_hisstorypost.setAdapter(adapter1);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    private void setLatest(){
        hisstoryItems3=new ArrayList<HisstoryItem>();
        manager1=new LinearLayoutManager(this);
        rv_hisstory_latest=(RecyclerView)findViewById(R.id.recycle_hisstory_latest);
        empry_latest=(TextView)findViewById(R.id.tv_hisstory_latest_empty_view);
        adapter3=new HisstoryAdapter3(this,getApplicationContext(),hisstoryItems3);
        rv_hisstory_latest.setHasFixedSize(true);
        rv_hisstory_latest.setLayoutManager(manager1);
        rv_hisstory_latest.setItemAnimator(new DefaultItemAnimator());

        rv_hisstory_latest.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                visibleItemCount2 = manager1.getChildCount();
                totalItemCount2 = manager1.getItemCount();

                Log.e("total item count", String.valueOf(totalItemCount2));
                pastVisiblesItems2 = ((LinearLayoutManager) rv_hisstory_latest.getLayoutManager()).findFirstVisibleItemPosition();
                Log.e("past Visible item", String.valueOf(pastVisiblesItems2));
                Log.e("visible item count", String.valueOf(visibleItemCount2));

                if (dy > 0) {

//                    if(userScrolled){
//                        if (totalItemCount > previousTotal) {
//                            userScrolled = true;
//                            previousTotal = totalItemCount;
//                        }

                    if (userScrolled && (visibleItemCount2 + pastVisiblesItems2) == totalItemCount2) {
                        userScrolled = false;
                        //     bottomLayout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getHistoryLatest();
//                                listAdapter.
                                //    bottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);


                    }
                }
            }


        });
        getHistoryLatest();
    }
    private void setTop(){
        hisstoryItems2=new ArrayList<HisstoryItem>();
        manager2=new LinearLayoutManager(this);
        rv_hisstorytop=(RecyclerView)findViewById(R.id.recycle_hisstory_top);
        empryview_top=(TextView)findViewById(R.id.tv_hisstory_top_empty_view);
        adpater2=new HisstoryAdapter2(this,getApplicationContext(),hisstoryItems2);
        rv_hisstorytop.setHasFixedSize(true);
        rv_hisstorytop.setLayoutManager(manager2);
        rv_hisstorytop.setItemAnimator(new DefaultItemAnimator());

        rv_hisstorytop.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                visibleItemCount1 = manager2.getChildCount();
                totalItemCount1= manager2.getItemCount();

                Log.e("total item count", String.valueOf(totalItemCount1));
                pastVisiblesItems1 = ((LinearLayoutManager) rv_hisstorytop.getLayoutManager()).findFirstVisibleItemPosition();
                Log.e("past Visible item", String.valueOf(pastVisiblesItems1));
                Log.e("visible item count", String.valueOf(visibleItemCount1));

                if (dy > 0) {

//                    if(userScrolled){
//                        if (totalItemCount > previousTotal) {
//                            userScrolled = true;
//                            previousTotal = totalItemCount;
//                        }

                    if (userScrolled && (visibleItemCount1 + pastVisiblesItems1) == totalItemCount1) {
                        userScrolled = false;
                        //     bottomLayout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getHistoryTop();
//                                listAdapter.
                                //    bottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);


                    }
                }
            }


        });
        getHistoryTop();
    }
    private void getHistoryTop() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.HisStoryTopURL+"offset=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            getFeedTop(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // dialog.hide();
                        Toast.makeText(HisStory.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", String.valueOf(offset1));
                offset1+=10;
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
    private void getFeedTop(JSONObject response) {
        Log.e("inside jobfeed method",String.valueOf(response));
        try {
            JSONArray feedArray = response.getJSONArray("hisstory");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                HisstoryItem item=new HisstoryItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setImage(feedObj.isNull("profile_pic")? DatabaseInfo.ProfilepicURL+"default.jpg":DatabaseInfo.ProfilepicURL+feedObj.getString("profile_pic"));
//
                String titile=feedObj.isNull("name")?"":feedObj.getString("name");
                byte[] utf8Bytes = titile.getBytes("UTF8");
                String test=new String(titile.getBytes(),"UTF8");
                item.setName(feedObj.isNull("username")?"null":feedObj.getString("username"));
//                item.setReligion(feedObj.isNull("mat_religion")? "null":feedObj.getString("mat_religion"));
//                item.setMprofileid(feedObj.isNull("mprofileid")?"null":feedObj.getString("mprofileid"));
                item.setDescription(feedObj.getString("address"));
                item.setTitle(feedObj.isNull("name")?"":feedObj.getString("name"));
                item.setDate(feedObj.isNull("hisdate")?"":feedObj.getString("hisdate"));
//                item.setDb(feedObj.isNull("db_backup_stat")?"":feedObj.getString("db_backup_stat"));
                item.setUid(feedObj.isNull("uid")?"":feedObj.getString("uid"));
                Log.e("image profilepic",String.valueOf(DatabaseInfo.ProfilepicURL+feedObj.getString("profile_pic")));
                Log.e("image desc",String.valueOf(feedObj.getString("address")));
                Log.e("image name",String.valueOf(feedObj.getString("username")));
                Log.e("image uid",String.valueOf(feedObj.getString("uid")));
//                Log.e("image db",String.valueOf(feedObj.getString("db_backup_stat")));

                hisstoryItems2.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adpater2.notifyDataSetChanged();
            rv_hisstorytop.scrollToPosition(totalItemCount1);
            if(adpater2.getItemCount()==0){
                empryview_top.setVisibility(View.VISIBLE);
            }else {empryview_top.setVisibility(View.GONE);}
            rv_hisstorytop.setAdapter(adpater2);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    private void getHistoryLatest() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.HisStoryLatestURL+"?offset=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                           getFeedLatest(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // dialog.hide();
                        Toast.makeText(HisStory.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("offset", String.valueOf(offset2));
                offset2+=10;
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
    private void getFeedLatest(JSONObject response) {
        Log.e("inside jobfeed method",String.valueOf(response));
        try {
            JSONArray feedArray = response.getJSONArray("hisstory");

            Log.e("feedlength",String.valueOf(feedArray.length()));

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                HisstoryItem item=new HisstoryItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setImage(feedObj.isNull("profile_pic")? DatabaseInfo.ProfilepicURL+"default.jpg":DatabaseInfo.ProfilepicURL+feedObj.getString("profile_pic"));
//
                String titile=feedObj.isNull("name")?"":feedObj.getString("name");
                byte[] utf8Bytes = titile.getBytes("UTF8");
                String test=new String(titile.getBytes(),"UTF8");
                item.setName(feedObj.isNull("username")?"null":feedObj.getString("username"));
//                item.setReligion(feedObj.isNull("mat_religion")? "null":feedObj.getString("mat_religion"));
//                item.setMprofileid(feedObj.isNull("mprofileid")?"null":feedObj.getString("mprofileid"));
                item.setDescription(feedObj.getString("address"));
                item.setTitle(feedObj.isNull("name")?"":feedObj.getString("name"));
                item.setDate(feedObj.isNull("hisdate")?"":feedObj.getString("hisdate"));
//                item.setDb(feedObj.isNull("db_backup_stat")?"":feedObj.getString("db_backup_stat"));
                item.setUid(feedObj.isNull("uid")?"":feedObj.getString("uid"));
                Log.e("image profilepic",String.valueOf(DatabaseInfo.ProfilepicURL+feedObj.getString("profile_pic")));
                Log.e("image desc",String.valueOf(feedObj.getString("address")));
                Log.e("image name",String.valueOf(feedObj.getString("username")));
                Log.e("image uid",String.valueOf(feedObj.getString("uid")));
//                Log.e("image db",String.valueOf(feedObj.getString("db_backup_stat")));

                hisstoryItems3.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter3.notifyDataSetChanged();
            rv_hisstory_latest.scrollToPosition(totalItemCount2);
            if(adapter3.getItemCount()==0){
                empry_latest.setVisibility(View.VISIBLE);
            }else {empry_latest.setVisibility(View.GONE);}
            rv_hisstory_latest.setAdapter(adapter3);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    public void createPost(final String description, final String title,final String uid){
        final String[] res = new String[1];
        String pa="?uid="+uid+"&address="+description+"&title="+title;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.HisstoryCreateURL+pa,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        String   favoriteResult =response.toString();

                        Toast.makeText(getApplicationContext(),"Post Created....",Toast.LENGTH_SHORT).show();
//
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HisStory.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(uid));
                params.put("address",String.valueOf(description));
                params.put("name",String.valueOf(title));

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }
    private void setSearch(final String search){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.HisstorySearchURL+"?search="+search,
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
                        // dialog.hide();
                        Toast.makeText(HisStory.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("search", String.valueOf(search));
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


}
