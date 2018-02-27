package com.avadharwebworld.avadhar.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class ViewJobProfileDetails extends AppCompatActivity {
    String rid , sp_uid;
    TextView Name,id,qualification,interestedfield,jobrole,experieance,expsalary,
            dateofbirth,gender,mstatus,nationality,currentlocation,email,mobile,
            jobtype,category,jobs,joblocation,keyskill,experesummery,language,
            modeofstudy,university,yearofpass,other;
    FeedImageView image;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_profile_details);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.jobprofiledetails);
        getWindow().setExitTransition(new Explode());
        Intent i=getIntent();
        rid= i.getExtras().getString("id");
        initialize();
        TabHost host = (TabHost) findViewById(R.id.th_job_profile_details_details);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.personaldetails));
        spec.setContent(R.id.jobprofile_tab1);
        spec.setIndicator(getResources().getString(R.string.personaldetails));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getResources().getString(R.string.profiledetails));
        spec.setContent(R.id.jobprofiletab2);
        spec.setIndicator(getResources().getString(R.string.profiledetails));
        host.addTab(spec);
        //tab3
        spec = host.newTabSpec(getResources().getString(R.string.academicdetails));
        spec.setContent(R.id.jobprofiletab3);
        spec.setIndicator(getResources().getString(R.string.academicdetails));
        host.addTab(spec);
        viewDetails();
    }

    private void initialize(){
        Name=(TextView)findViewById(R.id.tv_job_profile_details_name);
        id=(TextView)findViewById(R.id.tv_job_profile_details_id);
        qualification=(TextView)findViewById(R.id.tv_job_profile_details_qualif);
        interestedfield=(TextView)findViewById(R.id.tv_job_profile_details_interested);
        jobrole=(TextView)findViewById(R.id.tv_job_profile_details_jobrole);
        experieance=(TextView)findViewById(R.id.tv_job_profile_details_experince);
        expsalary=(TextView)findViewById(R.id.tv_job_profile_details_expectedsalary);
        dateofbirth=(TextView)findViewById(R.id.tv_job_profile_details_tab1_dob);
        gender=(TextView)findViewById(R.id.tv_job_profile_details_tab1_gender);
        mstatus=(TextView)findViewById(R.id.tv_job_profile_details_tab1_maritialstatus);
        nationality=(TextView)findViewById(R.id.tv_job_profile_details_tab1_nationality);
        currentlocation=(TextView)findViewById(R.id.tv_job_profile_details_tab1_currentlocation);
        email=(TextView)findViewById(R.id.tv_job_profile_details_tab1_email);
        mobile=(TextView)findViewById(R.id.tv_job_profile_details_tab1_number);
        jobtype=(TextView)findViewById(R.id.tv_job_profile_details_jobtype);
        category=(TextView)findViewById(R.id.tv_job_profile_details_category);
        jobs=(TextView)findViewById(R.id.tv_job_profile_details_jobs);
        joblocation=(TextView)findViewById(R.id.tv_job_profile_details_joblocation);
        keyskill=(TextView)findViewById(R.id.tv_job_profile_details_keyskill);
        experesummery=(TextView)findViewById(R.id.tv_job_profile_details_expsummary);
        language=(TextView)findViewById(R.id.tv_job_profile_details_language);
        modeofstudy=(TextView)findViewById(R.id.tv_job_profile_details_modeofstudy);
        university=(TextView)findViewById(R.id.tv_job_profile_details_university);
        yearofpass=(TextView)findViewById(R.id.tv_job_profile_details_yearofpass);
        other=(TextView)findViewById(R.id.tv_job_profile_details_otherdetails);
        image=(FeedImageView)findViewById(R.id.iv_job_profile_details_feed);

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
    private void viewDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.JobProfileDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("jobfeed");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);
                                String image1=feedObj.isNull("photo")? DatabaseInfo.JObImagePathURL+"default.jpg":DatabaseInfo.JObImagePathURL+feedObj.getString("photo");
                                image.setImageUrl(image1,imageLoader);
                                Name.setText(feedObj.isNull("uname")? "":feedObj.getString("uname"));
                                id.setText(feedObj.isNull("profileid")? "":feedObj.getString("profileid"));
                                qualification.setText(feedObj.isNull("qualification")? "":feedObj.getString("qualification"));
                                interestedfield.setText(feedObj.isNull("category1")? "":feedObj.getString("category1"));
                                jobrole.setText(feedObj.isNull("jobs")? "":feedObj.getString("jobs"));
                                experieance.setText(feedObj.isNull("experience")? "":feedObj.getString("experience"));
                                expsalary.setText(feedObj.isNull("salary")? "":feedObj.getString("salary"));
                                dateofbirth.setText(feedObj.isNull("dob")? "":feedObj.getString("dob"));
                                gender.setText(feedObj.isNull("gender")? "":feedObj.getString("gender"));
                                mstatus.setText(feedObj.isNull("mstatus")? "":feedObj.getString("mstatus"));
                                nationality.setText(feedObj.isNull("nationality")? "":feedObj.getString("nationality"));
                                currentlocation.setText(feedObj.isNull("location")? "":feedObj.getString("location"));
                                email.setText(feedObj.isNull("email")? "":feedObj.getString("email"));
                                mobile.setText(feedObj.isNull("contactno")? "":feedObj.getString("contactno"));
                                jobtype.setText(feedObj.isNull("jobtype")? "":feedObj.getString("jobtype"));
                                category.setText(feedObj.isNull("category1")? "":feedObj.getString("category1"));
                                jobs.setText(feedObj.isNull("jobs")? "":feedObj.getString("jobs"));
                                joblocation.setText(feedObj.isNull("joblocation")? "":feedObj.getString("joblocation"));
                                keyskill.setText(feedObj.isNull("keyskills")? "":feedObj.getString("keyskills"));
                                experesummery.setText(feedObj.isNull("expsummary")? "":feedObj.getString("expsummary"));
                                language.setText(feedObj.isNull("language")? "":feedObj.getString("language"));
                                modeofstudy.setText(feedObj.isNull("studymode")? "":feedObj.getString("studymode"));
                                university.setText(feedObj.isNull("university")? "":feedObj.getString("university"));
                                yearofpass.setText(feedObj.isNull("passyear")? "":feedObj.getString("passyear"));
                                other.setText(feedObj.isNull("otherdet")? "":feedObj.getString("otherdet"));


                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(ViewJobProfileDetails.this, error.toString(), Toast.LENGTH_LONG).show();
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
