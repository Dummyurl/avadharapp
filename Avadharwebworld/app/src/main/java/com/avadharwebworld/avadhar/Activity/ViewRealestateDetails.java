package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class ViewRealestateDetails extends AppCompatActivity {
    private TextView title,propertymain,propertytab1,propertyid,pricemain,pricetab,
            name,contactnumber,email,typeofproperty,typeofactivity,typeofuser,prpretydetails,
            country,state,locationtab1,locationtab2,propertytab2,moredetails,availableproperty,totalarea;
    private FeedImageView image;
    private SharedPreferences sp;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String rid , sp_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_realestate_details);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setExitTransition(new Explode());
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.nav_realestate);
        Intent i=getIntent();
        rid= i.getExtras().getString("id");
        initialize();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        TabHost host = (TabHost) findViewById(R.id.th_realestate_details_details);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.basicprpertyinformation));
        spec.setContent(R.id.realestate_tab1);
        spec.setIndicator(getResources().getString(R.string.basicprpertyinformation));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getResources().getString(R.string.propertyfeature));
        spec.setContent(R.id.realestate_tab2);
        spec.setIndicator(getResources().getString(R.string.propertyfeature));
        host.addTab(spec);
        viewDetails();
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
//        final EditText editText = (EditText) menu.findItem(
//                R.id.menu_search).getActionView();
//        editText.addTextChangedListener(textWatcher);

//        MenuItem menuItem = menu.findItem(R.id.menu_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        final EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getApplicationContext(),query+"/ edit text value : "+searchPlate.getText().toString(),Toast.LENGTH_LONG ).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
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
    private void initialize(){

        title=(TextView)findViewById(R.id.tv_realestate_details_title);
        propertymain=(TextView)findViewById(R.id.tv_realestate_details_property);
        propertytab1=(TextView)findViewById(R.id.tv_realestate_details_propertytab);
        propertytab2=(TextView)findViewById(R.id.tv_realestate_details_property_tab2);
        propertyid=(TextView)findViewById(R.id.tv_realestate_details_pid);
        pricemain=(TextView)findViewById(R.id.tv_realestate_details_price);
        pricetab=(TextView)findViewById(R.id.tv_realestate_details_pricetab);
        name=(TextView)findViewById(R.id.tv_realestate_details_name);
        contactnumber=(TextView)findViewById(R.id.tv_realestate_details_contactnumber);
        email=(TextView)findViewById(R.id.tv_realestate_details_email);
        typeofproperty=(TextView)findViewById(R.id.tv_realestate_details_typeofproperty);
        typeofactivity=(TextView)findViewById(R.id.tv_realestate_details_typeofactivity);
        typeofuser=(TextView)findViewById(R.id.tv_realestate_details_typeofuser);
        prpretydetails=(TextView)findViewById(R.id.tv_realestate_details_preperty_details);
        country=(TextView)findViewById(R.id.tv_realestate_details_country);

        state=(TextView)findViewById(R.id.tv_realestate_details_state);
        locationtab1=(TextView)findViewById(R.id.tv_realestate_details_location);
        locationtab2=(TextView)findViewById(R.id.tv_realestate_details_location_tab2);
        moredetails=(TextView)findViewById(R.id.tv_realestate_details_moredetails);
        availableproperty=(TextView)findViewById(R.id.tv_realestate_details_avilableproperty);
        totalarea=(TextView)findViewById(R.id.tv_realestate_details_totalarea);
        image=(FeedImageView)findViewById(R.id.iv_realestate_details_feed);

    }
    private void viewDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.ViewRealestateDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("realestate");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);
//
                                        String image1=feedObj.isNull("image")? DatabaseInfo.RealestateImagePathURL+"default.jpg":DatabaseInfo.RealestateImagePathURL+feedObj.getString("image");
                                        image.setImageUrl(image1,imageLoader);
                                        title.setText(feedObj.isNull("ownername")? "":feedObj.getString("ownername"));
                                        propertymain.setText(feedObj.isNull("rtype")? "":feedObj.getString("rtype"));
                                        propertytab1.setText(feedObj.isNull("rtype")? "":feedObj.getString("rtype"));
                                        propertytab2.setText(feedObj.isNull("rtype")? "":feedObj.getString("rtype"));
                                        propertyid.setText(feedObj.isNull("memberid")? "":feedObj.getString("memberid"));
                                        pricemain.setText(feedObj.isNull("price")? "":feedObj.getString("price"));
                                        pricetab.setText(feedObj.isNull("price")? "":feedObj.getString("price"));
                                        name.setText(feedObj.isNull("titlename")? "":feedObj.getString("titlename"));
                                        contactnumber.setText(feedObj.isNull("mobileno")? "":feedObj.getString("mobileno"));
                                        email.setText(feedObj.isNull("email")? "":feedObj.getString("email"));
                                        typeofproperty.setText(feedObj.isNull("subcategory")? "":feedObj.getString("subcategory"));
                                        typeofactivity.setText(feedObj.isNull("type")? "":feedObj.getString("type"));
                                        typeofuser.setText(feedObj.isNull("towner")? "":feedObj.getString("towner"));
                                        prpretydetails.setText(feedObj.isNull("adddes")? "":feedObj.getString("adddes"));

                                country.setText(feedObj.isNull("country1")? "":feedObj.getString("country1"));
                                state.setText(feedObj.isNull("state1")? "":feedObj.getString("state1"));
                                locationtab1.setText(feedObj.isNull("city1")? "":feedObj.getString("city1"));
                                locationtab2.setText(feedObj.isNull("city1")? "":feedObj.getString("city1"));

                                moredetails.setText(feedObj.isNull("adddes")? "":feedObj.getString("adddes"));
                                availableproperty.setText(feedObj.isNull("aproperty")? "":feedObj.getString("aproperty"));
                                totalarea.setText(feedObj.isNull("total")? "":feedObj.getString("total"));
                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(ViewRealestateDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rid", String.valueOf(rid));



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
