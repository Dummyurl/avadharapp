package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.avadharwebworld.avadhar.Adapter.MatrimonyAdapter;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.MatrimonyItem;
import com.avadharwebworld.avadhar.Data.MyPackageItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.MultiSelectSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matrimonial extends AppCompatActivity {
    List<String> qualification2=new ArrayList<String>();
    List<String> university2=new ArrayList<String>();
    List<String>experience2=new ArrayList<String>();
    List<String>jobrole2=new ArrayList<String>();
    List<String> statecodeid =  new ArrayList<String>();
    List<String> statecodename =  new ArrayList<String>();
    List<String> citycodeid =  new ArrayList<String>();
    List<String> citycodename =  new ArrayList<String>();
    List<String> countrycodid =  new ArrayList<String>();
    List<String> countrycodename =  new ArrayList<String>();
    List<String> religioncode1 =  new ArrayList<String>();
    List<String> religionname1 =  new ArrayList<String>();
    List<String> religionname2 =  new ArrayList<String>();
    List<String> religioncode2 =  new ArrayList<String>();
    ProgressDialog pDialog;
    private int day, month, year,parentid=0;
    private RecyclerView rv_matrimony;
    boolean userScrolled = false,search=true;
    private List<MatrimonyItem> matrimonyItems;
    MatrimonyAdapter adapter;
    private RecyclerView.LayoutManager manager;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight,page=1;
    TextView empryview;
    private SharedPreferences sp;
    private String uid;
    PopupWindow popupWindow=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_matrimonial);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        search=true;
        matrimonyItems=new ArrayList<MatrimonyItem>();
        manager=new LinearLayoutManager(this);
        rv_matrimony=(RecyclerView)findViewById(R.id.recycle_matrimony);
        empryview=(TextView)findViewById(R.id.tv_matrimony_empty_view);
        adapter=new MatrimonyAdapter(this,getApplicationContext(),matrimonyItems);
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
                                if(search) getMatriDetails();
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
        if(item.getItemId()==R.id.matrimony_action_search){

        }else if(item.getItemId()==R.id.menu_matimonial_filter_search) {
                SearchBox();
        }else if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        else  {
            switch (item.getItemId()){

                case R.id.menu_matimonial_registration:
                    intent = new Intent(this, MatrimonyRegistration.class);
                    break;
                case R.id.menu_matrimonial_myfavorites:
                    intent = new Intent(this, MatrimonyMyFavorite.class);
                    break;
                case R.id.menu_matrimonial_interestedprofile:
                    intent = new Intent(this, MatrimonyInterestedProfile.class);
                    break;
                case R.id.menu_matrimonial_editprofile:
                    intent = new Intent(this, MatrimonyEditProfile.class);
                    break;
                case R.id.menu_matrimonial_buypackage:
                    intent = new Intent(this, MatrimonyBuyPackage.class);
                    break;
                case R.id.menu_matrimonial_mypackage:
                    intent = new Intent(this, MatrimonyMyPackage.class);
                    break;

            }
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.matrimonial_menu, menu);
//        final EditText editText = (EditText) menu.findItem(
//                R.id.menu_search).getActionView();
//        editText.addTextChangedListener(textWatcher);

//        MenuItem menuItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.matrimony_action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        final EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                matrimonyItems.clear();
                adapter.notifyDataSetChanged();
                setSearch(query,"","","","","","","","","","","","");
                search=false;
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
    public void getMatriDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.GetMatrimonyURL+"?page=1",
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
                        Toast.makeText(Matrimonial.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("page", String.valueOf(page));
                page+=1;
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
                MatrimonyItem item=new MatrimonyItem();

                item.setId(feedObj.isNull("id")? "null":feedObj.getString("id"));
                item.setImage(feedObj.isNull("image")? DatabaseInfo. MatrimonialImagePath+"default.jpg":DatabaseInfo.MatrimonialImagePath+feedObj.getString("image"));
                item.setName(feedObj.isNull("mat_name")?"null":feedObj.getString("mat_name"));
                item.setProftype(feedObj.isNull("mat_profile")? "null":feedObj.getString("mat_profile"));
                item.setReligion(feedObj.isNull("mat_religion")? "null":feedObj.getString("mat_religion"));
                item.setMprofileid(feedObj.isNull("mprofileid")?"null":feedObj.getString("mprofileid"));

                // Log.e("image",String.valueOf(DatabaseInfo.JObImagePathURL+feedObj.getString("image")));

                matrimonyItems.add(item);


            }
            // Log.e("feed result",String.valueOf(Arrays.asList(educationFeedItems)));
            adapter.notifyDataSetChanged();
//            rv_jobs.scrollToPosition(totalItemCount);
            if(adapter.getItemCount()==0){
                empryview.setVisibility(View.VISIBLE);
            }else {empryview.setVisibility(View.GONE);}

            rv_matrimony.setAdapter(adapter);
            rv_matrimony.scrollToPosition(totalItemCount);
            pDialog.hide();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void SearchBox(){

        String[] selectstate={"Select State"};
        String[] selectcity={"Select City"};



        final MaterialEditText profileid;
        Button btnfilter;
        final RadioGroup type;

        final Spinner country1,state,city,religion,caste,physical,family,maritial,jathakam,star;
        LayoutInflater layout = (LayoutInflater) this.getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layout.inflate(R.layout.matrimonysearchlayout, null);
        Display display = this.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

        country1=(Spinner)popupView.findViewById(R.id.sp_matrimony_search_postadd_country);
        state=(Spinner)popupView.findViewById(R.id.sp_matrimony_search_postadd_state);
        city=(Spinner)popupView.findViewById(R.id.sp_matrimony_search_postadd_city);
        religion=(Spinner)popupView.findViewById(R.id.sp_matrimony_searchfilter_religion);
        caste=(Spinner)popupView.findViewById(R.id.sp_matrimony_searchfilter_caste);
        physical=(Spinner)popupView.findViewById(R.id.sp_matrimony_searchfilter_physicalstatus);
        family=(Spinner)popupView.findViewById(R.id.sp_matrimony_searchfilter_familytype);
        maritial=(Spinner)popupView.findViewById(R.id.sp_matrimony_searchfilter_maritialstatus);
        jathakam=(Spinner)popupView.findViewById(R.id.sp_matrimony_searchfilter_maritialstatus);
        star=(Spinner)popupView.findViewById(R.id.sp_matrimony_searchfilter_star);
        type=(RadioGroup)popupView.findViewById(R.id.rg_matrimony_searchfilter_modeofmatri);
        profileid=(MaterialEditText)popupView.findViewById(R.id.et_matrimony_searchfilter_profileid);
        btnfilter=(Button)popupView.findViewById(R.id.btn_matrimony_searchlayput_filter) ;

        getCountryCode(country1);
        country1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statecodeid.clear();
                statecodename.clear();
                citycodeid.clear();
                citycodename.clear();
                if(position!=0){
                    String cid= countrycodid.get(position);
                    getStateCode(state,cid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"country code position ",Toast.LENGTH_SHORT).show();

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

        String cid="0";
        getReligion(religion,cid);
        religion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                religioncode1.clear();
                religionname1.clear();
                if(position!=0){
                    String cid=religioncode2.get(position);
                    getReligion(caste,cid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rel,cast,countr,stat,cit,gender,gender1,family1,star1,physical1,maritia,profid,jathak;
                if (religion.getSelectedItemPosition()==0){rel="";}else rel=religioncode2.get(religion.getSelectedItemPosition());
                if (caste.getSelectedItemPosition()==0||caste.getSelectedItemPosition()==-1){cast="";}else cast=religioncode1.get(caste.getSelectedItemPosition());
                if (country1.getSelectedItemPosition()==0){countr="";}else countr=countrycodid.get(country1.getSelectedItemPosition());
                if (state.getSelectedItemPosition()==0||state.getSelectedItemPosition()==-1){stat="";}else stat=statecodeid.get(state.getSelectedItemPosition());
                if (city.getSelectedItemPosition()==0||city.getSelectedItemPosition()==-1){cit="";}else cit=citycodeid.get(city.getSelectedItemPosition());

                if(type.getCheckedRadioButtonId()==R.id.rb_matrimony_searchfilter_bride||
                        type.getCheckedRadioButtonId()==R.id.rb_matrimony_searchfilter_groom){
                    gender =((RadioButton)popupView.findViewById(type.getCheckedRadioButtonId())).getText().toString();
                    if(gender=="Bride"){
                        gender1="Female";
                    }else gender1="Male";
                }else {gender1="";}
                if (family.getSelectedItemPosition()==0||family.getSelectedItemPosition()==-1){family1="";}else family1=family.getSelectedItem().toString();
                if (star.getSelectedItemPosition()==0||star.getSelectedItemPosition()==-1){star1="";}else star1=star.getSelectedItem().toString();
                if (physical.getSelectedItemPosition()==0||physical.getSelectedItemPosition()==-1){physical1="";}else physical1=physical.getSelectedItem().toString();
                if (maritial.getSelectedItemPosition()==0||maritial.getSelectedItemPosition()==-1){maritia="";}else maritia=maritial.getSelectedItem().toString();
                if (jathakam.getSelectedItemPosition()==0||jathakam.getSelectedItemPosition()==-1){jathak="";}else jathak=jathakam.getSelectedItem().toString();
                if (star.getSelectedItemPosition()==0||star.getSelectedItemPosition()==-1){star1="";}else star1=star.getSelectedItem().toString();

                profid=profileid.getText().toString();
                matrimonyItems.clear();
                adapter.notifyDataSetChanged();
                setSearch("",rel,gender1,cast,maritia,profid,jathak,physical1,star1,family1,countr,stat,cit);
                search=false;
                popupWindow.dismiss();
            }
        });

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

    public void getCountryCode(final Spinner country){
//        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetCountrycodeURL,
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
//                        pDialog.hide();
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        Toast.makeText(Matrimonial.this,error.toString(), Toast.LENGTH_LONG).show();
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
//            final ProgressDialog pDialog = new ProgressDialog(this);
//            pDialog.setMessage("Loading...");
//            pDialog.show();
//            pDialog.setCancelable(false);

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
//                            pDialog.hide();
                            // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            pDialog.hide();
                            Toast.makeText(Matrimonial.this,error.toString(), Toast.LENGTH_LONG).show();
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
//            final ProgressDialog pDialog = new ProgressDialog(this);
//            pDialog.setMessage("Loading...");
//            pDialog.show();
//            pDialog.setCancelable(false);

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
//                            pDialog.hide();
                            // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            pDialog.hide();
                            Toast.makeText(Matrimonial.this, error.toString(), Toast.LENGTH_LONG).show();
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
    public void getparnerprefercencejob(final MultiSelectSpinner ifield){
        final List<String> interestedfield2=new ArrayList<String>();
//        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetJobInterestedURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONArray jArray;
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
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
                                    interestedfield2.add(i,sname);




                                }


                            } catch (Exception e) {
                            }
//                            interestedfield2.add(0,"Select Interested Filed");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,interestedfield2);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

//                            ifield.setAdapter(spinnerAdapter);
                            ifield.setItems(interestedfield2);
                            //   Toast.makeText(getApplicationContext()," country code size "+String.valueOf(Arrays.asList(countrycodid.size())),Toast.LENGTH_SHORT).show();

                            //  Toast.makeText(getApplicationContext(),"country string array count "+countrycodename.length,Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                        }
//                        pDialog.hide();
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        Toast.makeText(Matrimonial.this,error.toString(), Toast.LENGTH_LONG).show();
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
    public void getReligion(final Spinner st, final String Rcode){
//        final List<String> religioncode =  new ArrayList<String>();
        Toast.makeText(getApplicationContext(),"input code "+Rcode,Toast.LENGTH_SHORT).show();
        if(!Rcode.equals("0")){
//
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetReligionListURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Volleyresponse",response.toString());
                            try {
                                JSONArray jArray;
                                JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                                Log.e("volleyJson", Jobject.toString());
                                try {
                                    jArray = Jobject.getJSONArray("religion");
                                    int jarraylength=jArray.length()+1;
                                    String c1[] = new String[jarraylength];
                                    String c2[] = new String[jarraylength];
                                    String c3[] = new String[jarraylength];
                                    Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()),Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < jarraylength; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);

                                        String cid = json_data.getString("id");
                                        String sname = json_data.getString("caste");
                                        Log.e(sname, "got");
                                        Log.e("got",json_data.toString());
                                        Log.e(cid, "got");


                                        cid = json_data.getString("id");
                                        sname = json_data.getString("caste");
                                        c1[i]=cid;
                                        c3[i]=sname;


                                        religioncode1.add(cid);
                                        religionname1.add(sname);
                                    }
//                                        c1[i]=cid;
//                                        c3[i]=sname;

//                                        statecodename=c3;
//                                        statecodeid=c1;


                                } catch (Exception e) {
                                }

                                religionname1.add(0,"Select Caste");
                                religioncode1.add(0,"0");
                                ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,religionname1);
                                spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                                st.setAdapter(spinnerAdapter);
                                st.setSelection(0);

                            } catch (Exception e) {
                            }
//                            pDialog.hide();
                            // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            pDialog.hide();
                            Toast.makeText(Matrimonial.this,error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cid", String.valueOf(Rcode));
//                    Log.e("country code send",countrycode);
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
        else{
//
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetReligionListURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Volleyresponse",response.toString());
                            try {
                                JSONArray jArray;
                                JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                                Log.e("volleyJson", Jobject.toString());
                                try {
                                    jArray = Jobject.getJSONArray("religion");
                                    int jarraylength=jArray.length()+1;
                                    String c1[] = new String[jarraylength];
                                    String c2[] = new String[jarraylength];
                                    String c3[] = new String[jarraylength];
                                    Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()),Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < jarraylength; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);

                                        String cid = json_data.getString("id");
                                        String sname = json_data.getString("caste");
                                        Log.e(sname, "religion");
                                        Log.e("got",json_data.toString());
                                        Log.e(cid, "relition code");


                                        cid = json_data.getString("id");
                                        sname = json_data.getString("caste");
                                        c1[i]=cid;
                                        c3[i]=sname;


                                        religioncode2.add(cid);
                                        religionname2.add(sname);
                                    }
//                                        c1[i]=cid;
//                                        c3[i]=sname;

//                                        statecodename=c3;
//                                        statecodeid=c1;


                                } catch (Exception e) {
                                }
                                Toast.makeText(getApplicationContext(),String.valueOf(Arrays.asList(religionname2)),Toast.LENGTH_LONG).show();
                                religionname2.add(0,"Select Religion");
                                religioncode2.add(0,"0");
                                ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,religionname2);
                                spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                                st.setAdapter(spinnerAdapter);
                                st.setSelection(0);

                            } catch (Exception e) {
                            }
//                            pDialog.hide();
                            // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            pDialog.hide();
                            Toast.makeText(Matrimonial.this,error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cid", String.valueOf(Rcode));
//                    Log.e("country code send",countrycode);
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
    private void setSearch(final String search, final String religion, final String gender,final String caste,final String bride, final String profileid,final String jathakam,final String physical,  final String star, final String family,  final String country, final String state, final String city){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonySearchURL,
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
                        Toast.makeText(Matrimonial.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();



                params.put("search", String.valueOf(search));
                params.put("religion", String.valueOf(religion));
                params.put("countryId", String.valueOf(country));
                params.put("sid", String.valueOf(state));
                params.put("city1", String.valueOf(city));
                params.put("martl", String.valueOf(bride));
                params.put("gender", String.valueOf(gender));
                Log.e("gender",gender);
                params.put("castesel", String.valueOf(caste));
                params.put("profid", String.valueOf(profileid));
                params.put("physical", String.valueOf(physical));
                params.put("jathakm", String.valueOf(jathakam));
                params.put("star", String.valueOf(star));
                params.put("familytype", String.valueOf(family));

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
