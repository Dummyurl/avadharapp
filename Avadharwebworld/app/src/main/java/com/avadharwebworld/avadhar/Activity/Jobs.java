package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.MultiSelectSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jobs extends AppCompatActivity {
    boolean userScrolled = false;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<JobItem> jobItems;
    PopupWindow popupWindow=null;
    JobAdapter adapter;
    ProgressDialog pDialog;

    List<String>qualification=new ArrayList<String>();
    List<String>experience=new ArrayList<String>();
    List<String>interestedfield=new ArrayList<String>();
    List<String>jobrole=new ArrayList<String>();
    int parentid=0;
    TextView emptyview;
    RecyclerView rv_jobs;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,offset=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_jobs);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getWindow().setExitTransition(new Explode());
        jobItems=new ArrayList<JobItem>();
        emptyview=(TextView)findViewById(R.id.tv_job_empty_view);
        mLayoutManager=new LinearLayoutManager(this);
        adapter = new JobAdapter(this, getApplicationContext(), jobItems);
        rv_jobs=(RecyclerView)findViewById(R.id.recycle_job);
        rv_jobs.setHasFixedSize(true);
        rv_jobs.setLayoutManager(mLayoutManager);
        rv_jobs.setItemAnimator(new DefaultItemAnimator());
        rv_jobs.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                pastVisiblesItems = ((LinearLayoutManager) rv_jobs.getLayoutManager()).findFirstVisibleItemPosition();
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
        getJobDetails();
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
            case R.id.menu_job_profile:
                intent=new Intent(this,ViewJobProfileList.class);
                break;
            case R.id.menu_job_filter_search:
                SearchBox();
                intent=new Intent();
                break;
            case R.id.menu_job_postresume:
                intent=new Intent(this, JobPostResume.class);
                break;
            case R.id.menu_job_postvacancy:
                intent=new Intent(this,JobPostVacancy.class);
                break;
            case R.id.menu_job_myfavorites:
                intent=new Intent(this,JobMyFavorites.class);
                break;
            case R.id.menu_job_buypackage:
                intent=new Intent(this,JobBuyPackage.class);
                break;
            case R.id.menu_job_myvacancy:
                intent=new Intent(this,JobMyVacancyPost.class);
                break;
            case R.id.menu_job_mypackges:
                intent=new Intent(this,ViewMyJobPackage.class);
                break;
        }try{
            startActivity(intent);
        }catch (Exception e){

        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.job_menu, menu);
//        final EditText editText = (EditText) menu.findItem(
//                R.id.menu_search).getActionView();
//        editText.addTextChangedListener(textWatcher);

//        MenuItem menuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.job_action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clearData();
                getFilter(query,"","","","","","","","");
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

    public void getJobDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.ViewJobDetailsURl+"?pagelimit=1",
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
                        Toast.makeText(Jobs.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void SearchBox(){
      //  eduLayout.getForeground().setAlpha(220);
        String[] selectqualification={"Qualification"};
        String[] selectexperience={"Experience"};
        String[] selectjobrole={"Job Role"};
        String[] selectinterestedfield={"Interested Field"};
        String[] selectexpectedsalary={"Expected Salary"};

        final RadioGroup jobtype;
        final RadioButton fulltime,parttime;
        final com.avadharwebworld.avadhar.Support.MultiSelectSpinner jobrole;
        final MaterialEditText resumetitle,joblocation;
        Button btnfilter;
        final android.support.v7.widget.AppCompatSpinner qualification,experience,interestedfield,expectedsalary;

        LayoutInflater layout = (LayoutInflater) this.getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layout.inflate(R.layout.jobsearchlayout, null);
        Display display = this.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

//        final Dialog dialog=new Dialog(this, R.style.FullHeightDialog);
//        dialog.setContentView(R.layout.educationsearchlayout);

        btnfilter=(Button)popupView.findViewById(R.id.btn_jobsearch_search);
        resumetitle=(MaterialEditText)popupView.findViewById(R.id.et_jobsearch_resumetitle);
        joblocation=(MaterialEditText)popupView.findViewById(R.id.et_jobsearch_joblocation);

        fulltime=(RadioButton)popupView.findViewById(R.id.rb_jobsearch_fulltime);
        parttime=(RadioButton)popupView.findViewById(R.id.rb_jobsearch_parttime);

        qualification=(android.support.v7.widget.AppCompatSpinner)popupView.findViewById(R.id.sp_jobsearch_qualification);
        experience=(android.support.v7.widget.AppCompatSpinner)popupView.findViewById(R.id.sp_jobsearch_experience);
        interestedfield=(android.support.v7.widget.AppCompatSpinner)popupView.findViewById(R.id.sp_jobsearch_interestedfield);
        expectedsalary=(android.support.v7.widget.AppCompatSpinner)popupView.findViewById(R.id.sp_jobsearch_expectedsalary);

        jobrole=(MultiSelectSpinner)popupView.findViewById(R.id.sp_jobsearch_jobrole);

        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,selectjobrole);
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_iltem);
        qualification.setAdapter(spinnerAdapter1);

        ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,selectqualification);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);
        qualification.setAdapter(spinnerAdapter);

        getQualificationCode(qualification);
        getinterestedfieldCode(interestedfield);
        MultiSelectSpinner.OnMultipleItemsSelectedListener listener;
//        listener=(MultiSelectSpinner.OnMultipleItemsSelectedListener)this;
//        jobrole.setListener(listener);
       // jobrole.setItems(selectjobrole);



//        city.setAdapter(spinnerAdapter1);
      //  getCountryCode(country);

        interestedfield.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id!=0) {
                    getjobroleCode(jobrole, String.valueOf(id));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                statecodeid.clear();
////                statecodename.clear();
////                citycodeid.clear();
////                citycodename.clear();
////                Log.e("country code size",String.valueOf(countrycodid.size()));
////                if(position!=0){
////                    String cid= countrycodid.get(position);
////                    getStateCode(state,cid);
////                }
//
////                Log.e("log country seltd pos",String.valueOf(position));
////                Log.e("log c code seltd pos",String.valueOf(countrycodid.get(position)));
////                Log.e("log c code seltd pos",String.valueOf(cid));
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                citycodeid.clear();
////                citycodename.clear();
//                if(position!=0) {
////                    String sid = statecodeid.get(position);
////                    getCityCode(city, sid);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        btnfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search="",jobtype="",location="",qualification1="",exp="",resumetitle1="",category="",salary="",job="";
                if(fulltime.isChecked()){jobtype="Full-Time";}
                else if(parttime.isChecked()){jobtype="Part-Time";}
                if(!interestedfield.getSelectedItem().equals("Select Interested Field")){
                    category=String.valueOf(interestedfield.getSelectedItemId());
                }
                if(!expectedsalary.getSelectedItem().equals("Select Expected Salary")){
                    salary=String.valueOf(expectedsalary.getSelectedItem());
                }
                if(!experience.getSelectedItem().equals("Select Experience")){
                    exp=String.valueOf(experience.getSelectedItem());
                }
                if(!qualification.getSelectedItem().equals("Select Qualification")){
                    qualification1=String.valueOf(qualification.getSelectedItem());
                }
                if(jobrole.getSelectedItem()!=null&&jobrole!=null)
                {
                    job=String.valueOf(jobrole.getSelectedItem());
                }else {job="";}

                location=String.valueOf(joblocation.getText());

                resumetitle1=String.valueOf(resumetitle.getText());



                clearData();
                getFilter(search,jobtype,location,qualification1,exp,resumetitle1,category,salary,job);
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
//                eduLayout.getForeground().setAlpha(0);
                return  false;
            }
        });


        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        dialog.setTitle("Search");
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);;
    }
    public void getQualificationCode(final Spinner qualifi){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.GetJobQualificationURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONArray jArray;
                            JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("volleyJson", Jobject.toString());
                            try {
                                jArray = Jobject.getJSONArray("qualif");
                                Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()+1),Toast.LENGTH_SHORT).show();
                                int jarraylength=jArray.length()+1;

                                String c1[] = new String[jarraylength];
                                String c2[] = new String[jarraylength];
                                String c3[] = new String[jarraylength];
                                String sname,cid;
                                for (int i = 0; i <jarraylength+1; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);


                                    cid = json_data.getString("id");
                                    sname = json_data.getString("course");
                                    Log.e(sname, "got");
                                    Log.e("got",json_data.toString());
                                    Log.e(cid, "got");
                                    c1[i]=cid;
                                    c3[i]=sname;
                                  //  Log.e("list sizzeeeee",String.valueOf(countrycodename.size()+1));
                                    qualification.add(i,sname);




                                }


                            } catch (Exception e) {
                            }
                            qualification.add(0,"Select Qualification");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,qualification);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            qualifi.setAdapter(spinnerAdapter);
                            qualifi.setSelection(0);
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
                        Toast.makeText(Jobs.this,error.toString(), Toast.LENGTH_LONG).show();
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
    public void getinterestedfieldCode(final Spinner qualifi){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.GetJobInterestedURL+"parentid="+String.valueOf(parentid),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONArray jArray;
                            JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("volleyJson", Jobject.toString());
                            try {
                                jArray = Jobject.getJSONArray("interested");
                                Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()+1),Toast.LENGTH_SHORT).show();
                                int jarraylength=jArray.length()+1;

                                String c1[] = new String[jarraylength];
                                String c2[] = new String[jarraylength];
                                String c3[] = new String[jarraylength];
                                String sname,cid;
                                for (int i = 0; i <jarraylength+1; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);


                                    cid = json_data.getString("id");
                                    sname = json_data.getString("category");
                                    Log.e(sname, "got");
                                    Log.e("got",json_data.toString());
                                    Log.e(cid, "got");
                                    c1[i]=cid;
                                    c3[i]=sname;
                                    //  Log.e("list sizzeeeee",String.valueOf(countrycodename.size()+1));
                                    interestedfield.add(i,sname);




                                }


                            } catch (Exception e) {
                            }
                            interestedfield.add(0,"Select Interested Field");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,interestedfield);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            qualifi.setAdapter(spinnerAdapter);
                            qualifi.setSelection(0);
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
                        Toast.makeText(Jobs.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parentid", String.valueOf(parentid));
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
    public void getjobroleCode(final MultiSelectSpinner qualifi, final String id){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.GetJobInterestedURL+"parentid="+String.valueOf(parentid),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONArray jArray;
                            JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("volleyJson", Jobject.toString());
                            try {
                                jArray = Jobject.getJSONArray("interested");
                                Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()+1),Toast.LENGTH_SHORT).show();
                                int jarraylength=jArray.length()+1;

                                String c1[] = new String[jarraylength];
                                String c2[] = new String[jarraylength];
                                String c3[] = new String[jarraylength];
                                String sname,cid;
                                for (int i = 0; i <jarraylength+1; i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);


                                    cid = json_data.getString("id");
                                    sname = json_data.getString("category");
                                    Log.e(sname, "got");
                                    Log.e("got",json_data.toString());
                                    Log.e(cid, "got");
                                    c1[i]=cid;
                                    c3[i]=sname;
                                    //  Log.e("list sizzeeeee",String.valueOf(countrycodename.size()+1));
                                    jobrole.add(i,sname);




                                }


                            } catch (Exception e) {
                            }
                          //  jobrole.add(0,"Select Job Role");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,interestedfield);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                           // qualifi.setAdapter(spinnerAdapter);
                            qualifi.setItems(jobrole);

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
                        Toast.makeText(Jobs.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parentid", String.valueOf(id));
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
    public void getFilter(final String search, final String jobtype, final String location, final String qualification1, final String exp, final String resumetitle, final String category, final String salary, final String job){
        String paramsPassed="?search="+search+"&jobtype="+jobtype+"&location="+location+"&qualification="+qualification1+"&exp="+exp+"&resumetitle="+resumetitle+"&category="+category+"&salary="+salary+"&job="+job;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.GetJobSearchURL+paramsPassed,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            jobFeed(Jobject);
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
                        Toast.makeText(Jobs.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("search", String.valueOf(search));
                Log.e("search",search);
                params.put("jobtype", String.valueOf(jobtype));
                Log.e("jobtype",jobtype);
                params.put("location", String.valueOf(location));
                Log.e("location",location);
                params.put("qualification", String.valueOf(qualification1));
                Log.e("qualification",qualification1);
                params.put("exp", String.valueOf(exp));
                Log.e("exp",exp);
                params.put("resumetitle", String.valueOf(resumetitle));
                Log.e("resumetitle",resumetitle);
                params.put("category", String.valueOf(category));
                Log.e("category",category);
                params.put("salary", String.valueOf(salary));
                Log.e("salary",salary);
                params.put("job", String.valueOf(job));
                Log.e("job",job);

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


    public void clearData() {
        jobItems.clear(); //clear list
        adapter.notifyDataSetChanged(); //let your adapter know about the changes and reload view.
    }
}
