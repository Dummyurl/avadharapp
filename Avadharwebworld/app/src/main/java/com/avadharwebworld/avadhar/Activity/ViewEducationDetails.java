package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class ViewEducationDetails extends AppCompatActivity {
    private String courseid,getcoursename,temp1,temp2,temp4,temp5,temp6,temp3;
    private TextView instname,affto,affmode,location,coursename,coursedetails,instaddress,cperson,cnumber,website,email;
    private FeedImageView instimage;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ProgressDialog dialog;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_view_education_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        courseid= i.getExtras().getString("id");
        getcoursename=i.getExtras().getString("coursename");
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        layout=(LinearLayout)findViewById(R.id.activity_view_education_details);

        instimage=(FeedImageView)findViewById(R.id.iv_education_details_image);
        instname=(TextView)findViewById(R.id.tv_education_details_institutename);
        affto=(TextView)findViewById(R.id.tv_education_details_affiliatedto);
        affmode=(TextView)findViewById(R.id.tv_education_details_affiliationmode);
        location=(TextView)findViewById(R.id.tv_education_details_location);
        coursename=(TextView)findViewById(R.id.tv_education_details_coursename);
        coursedetails=(TextView)findViewById(R.id.tv_education_details_coursedetails);
        instaddress=(TextView)findViewById(R.id.tv_education_details_address);
        cperson=(TextView)findViewById(R.id.tv_education_details_contactperson);
        cnumber=(TextView)findViewById(R.id.tv_education_details_contactnumber);
        website=(TextView)findViewById(R.id.tv_education_details_website);
        email=(TextView)findViewById(R.id.tv_education_details_email);
        layout.setVisibility(View.GONE);

        viewDetails();
        Toast.makeText(getApplicationContext(),"passed id is "+courseid,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog

        super.onBackPressed();


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
//            case R.id.menu_follower_profile:
//                intent=new Intent(this,Profile.class);
//                break;
//            case R.id.menu_follower_follower:
//                intent=new Intent(this,Followers.class);
//                break;
        }
      //  startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.followin_menu, menu);
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
                Toast.makeText(getApplicationContext(),query+"/ edit text value : "+searchPlate.getText().toString(),Toast.LENGTH_LONG ).show();
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

    private void viewDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.ViewEducationDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("educationfeed");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);

                                c1=feedObj.isNull("address")? " ":feedObj.getString("address");
                                c2=feedObj.isNull("affiliatedto")?"null":feedObj.getString("affiliatedto");
                                c3=feedObj.isNull("affiliationmode")?"null":feedObj.getString("affiliationmode");
                              //temp1=  feedObj.isNull("city")?"null":feedObj.getString("city");
                                c4=feedObj.isNull("cnumber")?"null":feedObj.getString("cnumber");
                           //  temp2=  feedObj.isNull("country")?"null":feedObj.getString("country");
                            //   temp3= feedObj.isNull("course_id")?"null":feedObj.getString("course_id");
                                c5= feedObj.isNull("coursedetail")?"null":feedObj.getString("coursedetail");
                                c6= feedObj.isNull("coursename")?"null":feedObj.getString("coursename");
                                c7=feedObj.isNull("cperson")?"null":feedObj.getString("cperson");

                            //  temp4=  feedObj.isNull("eligibility")?"null":feedObj.getString("eligibility");
                                c8=feedObj.isNull("email")?"null":feedObj.getString("email");
                              //  temp5=feedObj.isNull("inst_id")?"null":feedObj.getString("inst_id");
                                c9=feedObj.isNull("institution_name")?"null":feedObj.getString("institution_name");
                              // temp6=feedObj.isNull("state")?"null":feedObj.getString("state");
                                c10=feedObj.isNull("website")?"null":feedObj.getString("website");
                                c11=feedObj.isNull("image")?DatabaseInfo.EducationImagePathURL:DatabaseInfo.EducationImagePathURL+feedObj.getString("image");
                                c12="";
                                dialog.hide();
                                layout.setVisibility(View.VISIBLE);
                                instaddress.setText(c1);
                                affto.setText(c2);
                                affmode.setText(c3);
                                cnumber.setText(c4 );
                                coursedetails.setText(c5);
                                coursename.setText(c6);
                                cperson.setText(c7);
                                email.setText(c8);
                                instname.setText(c9);
                                website.setText(c10);
                                instimage.setImageUrl(c11,imageLoader);
                                location.setText(c12);

                                Log.e("website",String.valueOf(feedObj.getString("website")));
                                Log.e("address",String.valueOf(feedObj.getString("address")));
                                Log.e("affiliatedto",String.valueOf(feedObj.getString("affiliatedto")));
                                Log.e("affiliationmode",String.valueOf(feedObj.getString("affiliationmode")));
                                Log.e("cnumber",String.valueOf(feedObj.getString("cnumber")));
                                Log.e("cperson",String.valueOf(feedObj.getString("cperson")));
                                Log.e("image",String.valueOf(feedObj.getString("image")));
                                Log.e("coursedetail",String.valueOf(feedObj.getString("coursedetail")));
                                Log.e("institution_name",String.valueOf(feedObj.getString("institution_name")));
                                Log.e("email",String.valueOf(feedObj.getString("email")));
                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(ViewEducationDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("courseid", String.valueOf(courseid));
//                params.put("offset", String.valueOf(OFFSET));
//                OFFSET+=+15;

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
