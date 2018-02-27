package com.avadharwebworld.avadhar.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionValues;
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
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.EducationAdapter;
import com.avadharwebworld.avadhar.Adapter.JobAdapter;
import com.avadharwebworld.avadhar.Adapter.NewsFeedAdapter;
import com.avadharwebworld.avadhar.Data.CommentItem;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.ProfileViewItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Education extends AppCompatActivity {
private RecyclerView rv_education;
    Display display;
    List<String> statecodeid =  new ArrayList<String>();
    List<String> statecodename =  new ArrayList<String>();
    List<String> citycodeid =  new ArrayList<String>();
    List<String> citycodename =  new ArrayList<String>();
    List<String> countrycodid =  new ArrayList<String>();
    List<String> countrycodename =  new ArrayList<String>();
    TextView empyview;

    String search="";
    String cname="";
    String elib="";
    String insnm="";
    String affmod="";
    String affto="";
    String country_code="0";
    String state_code="0";
    String city_code="0";
   // private String[] countrycodename={"Select Country"},countrycodid={"0"},statecodeid={"0"},statecodename={"Select State"},citycodeid,citycodename;
    Point size;
    EducationAdapter adapter;
    boolean userScrolled = false;
    int getcurrentitem;
    private static RelativeLayout bottomLayout;
    JSONObject Jobjectlike;
    JSONObject jsonObject;
    FrameLayout eduLayout;
    private GridLayoutManager mLayoutmanager;
    private RecyclerView.LayoutManager manager;
    private List<EducationFeedItem> educationFeedItems;
     PopupWindow popupWindow=null;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,offset=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        setupWindowAnimations();

        setContentView(R.layout.activity_education);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        eduLayout=(FrameLayout) findViewById(R.id.layour_education);
        empyview=(TextView)findViewById(R.id.tv_education_empty_view);
        eduLayout.getForeground().setAlpha(0);
        educationFeedItems=new ArrayList<EducationFeedItem>();
        mLayoutmanager=new GridLayoutManager(getApplicationContext(),2);
        manager=new LinearLayoutManager(this);
        adapter=new EducationAdapter(this,getApplicationContext(),educationFeedItems);
        rv_education=(RecyclerView)findViewById(R.id.recycle_education);
        rv_education.setHasFixedSize(true);
        rv_education.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv_education.setItemAnimator(new DefaultItemAnimator());

        rv_education.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                visibleItemCount = mLayoutmanager.getChildCount();
                totalItemCount = mLayoutmanager.getItemCount();

                Log.e("total item count", String.valueOf(totalItemCount));
                pastVisiblesItems = ((LinearLayoutManager) rv_education.getLayoutManager()).findFirstVisibleItemPosition();
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
                             //   getEducationDetails();
//                                listAdapter.
                            //    bottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);


                    }
                }
            }


        });
        getEducationDetails();

    }



    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        setupWindowoutAnimations();
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
            case R.id.menu_education_filter_search:
                SearchBox();
                break;
//            case R.id.menu_follower_follower:
//                intent=new Intent(this,Followers.class);
//                break;
        }
//        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.education_menu, menu);
//        final EditText editText = (EditText) menu.findItem(
//                R.id.menu_search).getActionView();
//        editText.addTextChangedListener(textWatcher);

//        MenuItem menuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clearData();
                getFilter(query,cname,elib,insnm,affmod,affto,country_code,state_code,city_code);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when collapsed
//                return true; // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//               // editText.clearFocus();
//                return true; // Return true to expand action view
//            }
//        });
        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }

    public void getEducationDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.GetEducationDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                          educationFeed(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                       // dialog.hide();
                        Toast.makeText(Education.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
              //  params.put("offset", String.valueOf(offset));
//
                offset+=15;
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

    private void educationFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("educationfeed");


            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                EducationFeedItem item=new EducationFeedItem();

                item.setAddress(feedObj.isNull("address")? "null":feedObj.getString("address"));
                item.setAffiliatedto(feedObj.isNull("affiliatedto")?"null":feedObj.getString("affiliatedto"));
                item.setAffiliationmode(feedObj.isNull("affiliationmode")?"null":feedObj.getString("affiliationmode"));
                item.setCity(feedObj.isNull("city")?"null":feedObj.getString("city"));
                item.setCnumber(feedObj.isNull("cnumber")?"null":feedObj.getString("cnumber"));
                item.setCountry(feedObj.isNull("country")?"null":feedObj.getString("country"));
                item.setCourse_id(feedObj.isNull("course_id")?"null":feedObj.getString("course_id"));
                item.setCoursedetail(feedObj.isNull("coursedetail")?"null":feedObj.getString("coursedetail"));
                item.setCoursename(feedObj.isNull("coursename")?"null":feedObj.getString("coursename"));
                item.setCperson(feedObj.isNull("cperson")?"null":feedObj.getString("cperson"));
                item.setEligibility(feedObj.isNull("eligibility")?"null":feedObj.getString("eligibility"));
                item.setEmail(feedObj.isNull("email")?"null":feedObj.getString("email"));
                item.setInst_id(feedObj.isNull("inst_id")?"null":feedObj.getString("inst_id"));
                item.setInstitution_name(feedObj.isNull("institution_name")?"null":feedObj.getString("institution_name"));
                item.setState(feedObj.isNull("state")?"null":feedObj.getString("state"));
                item.setWebsite(feedObj.isNull("website")?"null":feedObj.getString("website"));
                item.setImage(feedObj.isNull("image")?"null":DatabaseInfo.EducationImagePathURL+feedObj.getString("image"));

                Log.e("image",String.valueOf(DatabaseInfo.EducationImagePathURL+feedObj.getString("image")));
                Log.e("education fetch",String.valueOf(response));
                educationFeedItems.add(item);


            }
           // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
            rv_education.scrollToPosition(totalItemCount);
            rv_education.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void SearchBox(){
        eduLayout.getForeground().setAlpha(220);
        String[] selectstate={"Select State"};
        String[] selectcity={"Select City"};



        final MaterialEditText coursename,eligibility,instname,afflimode,afflito;
        Button btnfilter;
        final android.support.v7.widget.AppCompatSpinner country,state,city;
        LayoutInflater layout = (LayoutInflater) this.getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layout.inflate(R.layout.educationsearchlayout, null);
        Display display = this.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

//        final Dialog dialog=new Dialog(this, R.style.FullHeightDialog);
//        dialog.setContentView(R.layout.educationsearchlayout);

        btnfilter=(Button)popupView.findViewById(R.id.btn_educationsearch_search);
        coursename=(MaterialEditText)popupView.findViewById(R.id.et_educationsearch_coursename);
        eligibility=(MaterialEditText)popupView.findViewById(R.id.et_educationsearch_eligibility);
        instname=(MaterialEditText)popupView.findViewById(R.id.et_educationsearch_institutionName);
        afflimode=(MaterialEditText)popupView.findViewById(R.id.et_educationsearch_affiliationMode);
        afflito=(MaterialEditText)popupView.findViewById(R.id.et_educationsearch_affiliationTo);
        country=(android.support.v7.widget.AppCompatSpinner)popupView.findViewById(R.id.sp_educationsearch_country);
        state=(android.support.v7.widget.AppCompatSpinner)popupView.findViewById(R.id.sp_educationsearch_state);
        city=(android.support.v7.widget.AppCompatSpinner)popupView.findViewById(R.id.sp_educationsearch_city);

        ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,selectstate);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);
        state.setAdapter(spinnerAdapter);

        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,selectcity);
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_iltem);
        city.setAdapter(spinnerAdapter1);
        getCountryCode(country);





        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statecodeid.clear();
                statecodename.clear();
                citycodeid.clear();
                citycodename.clear();
                Log.e("country code size",String.valueOf(countrycodid.size()));
                if(position!=0){
                    String cid= countrycodid.get(position);
                    getStateCode(state,cid);
                }

//                Log.e("log country seltd pos",String.valueOf(position));
//                Log.e("log c code seltd pos",String.valueOf(countrycodid.get(position)));
//                Log.e("log c code seltd pos",String.valueOf(cid));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citycodeid.clear();
                citycodename.clear();
                if(position!=0) {
                    String sid = statecodeid.get(position);
                    getCityCode(city, sid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 search="";
                cname=String.valueOf(coursename.getText());
                elib=String.valueOf(eligibility.getText());
                insnm=String.valueOf(instname.getText());
                 affmod=String.valueOf(afflimode.getText());
                affto=String.valueOf(afflito.getText());
                country_code="0";
                state_code="0";
                 city_code="0";
                if(country.getSelectedItemPosition()!=0){
                country_code=String.valueOf(countrycodid.get(country.getSelectedItemPosition()));
                }else{country_code="0";}
                if(state.getSelectedItemPosition()!=0){
                    state_code=String.valueOf(statecodeid.get(state.getSelectedItemPosition()));
                }else {
                    state_code="0";
                }
                if(city.getSelectedItemPosition()!=0){
                 city_code=String.valueOf(citycodeid.get(city.getSelectedItemPosition()));
                }else {city_code="0";}
                clearData();
                getFilter(search,cname,elib,insnm,affmod,affto,country_code,state_code,city_code);

              //  getFilter(search,String.valueOf(coursename.getText()),String.valueOf(eligibility.getText()),String.valueOf(instname.getText()),String.valueOf(afflimode.getText()),String.valueOf(afflito.getText()),String.valueOf(countrycodid.get(country.getSelectedItemPosition())),String.valueOf(statecodeid.get(state.getSelectedItemPosition())),String.valueOf(citycodeid.get(city.getSelectedItemPosition())));
                }
        });
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setCanceledOnTouchOutside(true);
      popupWindow.setTouchInterceptor(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                  popupWindow.dismiss();

                  return true;

              }
              eduLayout.getForeground().setAlpha(0);
              return  false;
          }
      });


        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        dialog.setTitle("Search");
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);;
    }

    public void getCountryCode(final Spinner country){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.GetCountrycodeURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONArray jArray;
                          JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("volleyJson", Jobject.toString());
                            try {
                                jArray = Jobject.getJSONArray("country");
                                Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()+1),Toast.LENGTH_SHORT).show();
                                int jarraylength=jArray.length()+1;

                                String c1[] = new String[jarraylength];
                                String c2[] = new String[jarraylength];
                                String c3[] = new String[jarraylength];
                                String sname,cid;
                                for (int i = 0; i <jarraylength+1; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);


                                    cid = json_data.getString("id");
                                    sname = json_data.getString("name");
                                    Log.e(sname, "got");
                                    Log.e("got",json_data.toString());
                                    Log.e(cid, "got");
                                    c1[i]=cid;
                                    c3[i]=sname;
                                    Log.e("list sizzeeeee",String.valueOf(countrycodename.size()+1));
                                    countrycodename.add(i,sname);
                                    countrycodid.add(i ,cid);



                                }


                            } catch (Exception e) {

                            }
                            countrycodename.add(0,"Select Country");
                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,countrycodename);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            country.setAdapter(spinnerAdapter);
                            country.setSelection(0);
                         //   Toast.makeText(getApplicationContext()," country code size "+String.valueOf(Arrays.asList(countrycodid.size())),Toast.LENGTH_SHORT).show();

                            //  Toast.makeText(getApplicationContext(),"country string array count "+countrycodename.length,Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                        }
                        pDialog.hide();
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(Education.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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
    public void getStateCode(final Spinner state, final String countrycode){
        if(!countrycode.equals("0")){
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetstatecodeURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Volleyresponse",response.toString());
                            try {
                                JSONArray jArray;
                                JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                                Log.e("volleyJson", Jobject.toString());
                                try {
                                    jArray = Jobject.getJSONArray("statelist");
                                    int jarraylength=jArray.length()+1;
                                    String c1[] = new String[jarraylength];
                                    String c2[] = new String[jarraylength];
                                    String c3[] = new String[jarraylength];
                                    Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()),Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < jarraylength; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);

                                        String cid = json_data.getString("id");
                                        String sname = json_data.getString("name");
                                        Log.e(sname, "got");
                                        Log.e("got",json_data.toString());
                                        Log.e(cid, "got");


                                            cid = json_data.getString("id");
                                            sname = json_data.getString("name");
                                            c1[i]=cid;
                                            c3[i]=sname;
                                            statecodeid.add(cid);
                                            statecodename.add(sname);
                                        }
//                                        c1[i]=cid;
//                                        c3[i]=sname;

//                                        statecodename=c3;
//                                        statecodeid=c1;


                                } catch (Exception e) {
                                }

                                statecodename.add(0,"Select State");
                                statecodeid.add(0,"0");
                                ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,statecodename);
                                spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                                state.setAdapter(spinnerAdapter);
                                state.setSelection(0);

                            } catch (Exception e) {
                            }
                            pDialog.hide();
                            // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.hide();
                            Toast.makeText(Education.this,error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cid", String.valueOf(countrycode));
                    Log.e("country code send",countrycode);
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
    public void getCityCode(final Spinner city, final String Statecode) {
        if (!Statecode.equals("0")) {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading...");
            pDialog.show();
            pDialog.setCancelable(false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetcitycodeURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Volleyresponse", response.toString());
                            try {
                                JSONArray jArray;
                                JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                                Log.e("volleyJson", Jobject.toString());
                                try {
                                    jArray = Jobject.getJSONArray("citylist");
                                    int jarraylength=jArray.length()+1;

                                    String c1[] = new String[jarraylength];
                                    String c2[] = new String[jarraylength];
                                    String c3[] = new String[jarraylength];
                                    for (int i = 0; i < jarraylength; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);

                                        String cid = json_data.getString("id");
                                        String sname = json_data.getString("name");
                                        Log.e(sname, "got");
                                        Log.e("got", json_data.toString());
                                        Log.e(cid, "got");


                                            cid = json_data.getString("id");
                                            sname = json_data.getString("name");
                                            c1[i]=cid;
                                            c3[i]=sname;
                                            citycodeid.add(cid);
                                            citycodename.add(sname);
                                        }

//

                                } catch (Exception e) {
                                }
                                citycodename.add(0,"Select City");
                                citycodeid.add(0,"0");
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_iltem, citycodename);
                                spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                                city.setAdapter(spinnerAdapter);
                                city.setSelection(0);

                            } catch (Exception e) {
                            }
                            pDialog.hide();
                            // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.hide();
                            Toast.makeText(Education.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sid", String.valueOf(Statecode));
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
    public void getFilter(final String search, final String coursename, final String eligibility, final String instname, final String afflimode, final String afflito, final String countrycode, final String statecode, final String citycode){

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetEducationSearchURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            searchFilter(Jobject);
                            pDialog.hide();
                            if(popupWindow!=null)
                            {popupWindow.dismiss();}
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        // dialog.hide();
                        Toast.makeText(Education.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("search", String.valueOf(search));
                params.put("coursename", String.valueOf(coursename));
                params.put("eligibility", String.valueOf(eligibility));
                params.put("institution_name", String.valueOf(instname));
                params.put("affiliatedto", String.valueOf(afflito));
                params.put("affiliationmode", String.valueOf(afflimode));
                params.put("countryId", String.valueOf(countrycode));
                params.put("sid", String.valueOf(statecode));
                params.put("city1", String.valueOf(citycode));


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
    private void searchFilter(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("educationsearch");


            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                EducationFeedItem item=new EducationFeedItem();

                item.setAddress(feedObj.isNull("address")? "null":feedObj.getString("address"));
                item.setAffiliatedto(feedObj.isNull("affiliatedto")?"null":feedObj.getString("affiliatedto"));
                item.setAffiliationmode(feedObj.isNull("affiliationmode")?"null":feedObj.getString("affiliationmode"));
                item.setCity(feedObj.isNull("city")?"null":feedObj.getString("city"));
                item.setCnumber(feedObj.isNull("cnumber")?"null":feedObj.getString("cnumber"));
                item.setCountry(feedObj.isNull("country")?"null":feedObj.getString("country"));
                item.setCourse_id(feedObj.isNull("course_id")?"null":feedObj.getString("course_id"));
                item.setCoursedetail(feedObj.isNull("coursedetail")?"null":feedObj.getString("coursedetail"));
                item.setCoursename(feedObj.isNull("coursename")?"null":feedObj.getString("coursename"));
                item.setCperson(feedObj.isNull("cperson")?"null":feedObj.getString("cperson"));
                item.setEligibility(feedObj.isNull("eligibility")?"null":feedObj.getString("eligibility"));
                item.setEmail(feedObj.isNull("email")?"null":feedObj.getString("email"));
                item.setInst_id(feedObj.isNull("inst_id")?"null":feedObj.getString("inst_id"));
                item.setInstitution_name(feedObj.isNull("institution_name")?"null":feedObj.getString("institution_name"));
                item.setState(feedObj.isNull("state")?"null":feedObj.getString("state"));
                item.setWebsite(feedObj.isNull("website")?"null":feedObj.getString("website"));
                item.setImage(feedObj.isNull("image")?"null":DatabaseInfo.EducationImagePathURL+feedObj.getString("image"));

//                Log.e("image",String.valueOf(DatabaseInfo.EducationImagePathURL+feedObj.getString("image")));
//                Log.e("education fetch",String.valueOf(response));
                educationFeedItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
            rv_education.scrollToPosition(totalItemCount);
            rv_education.setAdapter(adapter);
            if(adapter.getItemCount()==0)
            {
                empyview.setVisibility(View.VISIBLE);
            }
            else{
                empyview.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void clearData() {
        educationFeedItems.clear(); //clear list
        adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
    }


    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);
    }
    private void setupWindowoutAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

}
