package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.avadharwebworld.avadhar.Data.JobItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ViewJobDetails extends AppCompatActivity {
    ProgressDialog pDialog;
    String id;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    FeedImageView jobimage;
    LinearLayout job;
    TextView jobname,jobnodata,companyname,jobtype,jobrole,jobid,qualification,location,
            experience,salary,jobtypetab,jobdescription,contactname,contactnumber,contactemail
            ,joblocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getWindow().setExitTransition(new Explode());

        Intent i=getIntent();
        id= i.getExtras().getString("id");
        variabledeclarition();
        getJobDetails();
        TabHost host = (TabHost) findViewById(R.id.th_jobdetails_details);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("JOB REQUIRMENTS");
        spec.setContent(R.id.tab1);
        spec.setIndicator("JOB REQUIRMENTS");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("JOB DESCRIPTION");
        spec.setContent(R.id.tab2);
        spec.setIndicator("JOB DESCRIPTION");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("CONTACT DETAILS");
        spec.setContent(R.id.tab3);
        spec.setIndicator("CONTACT DETAILS");
        host.addTab(spec);

    }

    public void getJobDetails() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.ViewJobProfileURL,
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
                        Toast.makeText(ViewJobDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                  params.put("vacancyid", String.valueOf(id));
//
//                Log.e("offset", String.valueOf(offset));
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
        Log.e("inside jobfeed method", String.valueOf(response));
        try {
            JSONArray feedArray = response.getJSONArray("jobfeed");
            String jobname1 = null,companyname1 = null,jobtype1 = null,jobrole1=null,jobid1,qualification1=null,experience1 = null,salary1 = null,location=null,jobtypetab1 = null,jobdescription1 = null,contactname1 = null,contactnumber1 = null,contactemail1 = null,joblocation1=null,jobimage1 = null;
            Log.e("feedlength", String.valueOf(feedArray.length()));
                if(feedArray.length()==0){
                    job.setVisibility(View.GONE);
                    jobnodata.setVisibility(View.VISIBLE);
                }else {
                    for (int i = 0; i < feedArray.length(); i++) {
                        JSONObject feedObj = (JSONObject) feedArray.get(i);


                        jobname1 = feedObj.isNull("jobtitle") ? "null" : feedObj.getString("jobtitle");
                        companyname1 = feedObj.isNull("cname") ? "null" : feedObj.getString("cname");
                        jobtype1 = feedObj.isNull("jobtype") ? "null" : feedObj.getString("jobtype");
                        jobtypetab1 = feedObj.isNull("jobtype") ? "null" : feedObj.getString("jobtype");
                        jobrole1 = "";
                        jobid1 = feedObj.isNull("vac_id") ? "null" : feedObj.getString("vac_id");
                        experience1 = feedObj.isNull("experience") ? "null" : feedObj.getString("experience");
                        salary1 = feedObj.isNull("salary") ? "null" : feedObj.getString("salary");
                        jobdescription1 = feedObj.isNull("jobdesc") ? "null" : feedObj.getString("jobdesc");
                        contactemail1 = feedObj.isNull("cmail") ? "null" : feedObj.getString("cmail");
                        contactname1 = feedObj.isNull("cname") ? "null" : feedObj.getString("cname");
                        contactnumber1 = feedObj.isNull("cno") ? "null" : feedObj.getString("cno");
                        contactnumber1 = contactnumber1.replace("-", "");
                        jobimage1 = feedObj.isNull("vacancyimg") ? "null" : DatabaseInfo.JObImagePathURL + feedObj.getString("vacancyimg");
                        qualification1 = feedObj.isNull("qualification") ? "null" : feedObj.getString("qualification");
                        joblocation1 = feedObj.isNull("jobloc") ? "null" : feedObj.getString("jobloc");


                        Log.e("details", String.valueOf(response));

                    }

            if(!jobimage1.equals("null")){
                jobimage.setImageUrl(jobimage1,imageLoader);
            }else{
                jobimage.setVisibility(View.GONE);
            }

            if(!jobdescription1.equals("null")){
                jobdescription.setText(jobdescription1);
            }else{jobdescription.setText("");}

            if(!companyname1.equals("null")){
                companyname.setText(companyname1);
            }else{companyname.setText("");}

            if(!jobname1.equals("null")){
                jobname.setText(jobname1);
            }else{jobname.setText("");}

            if(!jobtype1.equals("null")){
                jobtype.setText(jobtype1);
            }else{jobtype.setText("");}

            if(!jobtypetab1.equals("null")){
                jobtypetab.setText(jobtypetab1);
            }else{jobtypetab.setText("");}

            if(!experience1.equals("null")){
                experience.setText(experience1);
            }else{experience.setText("");}

            if(!salary1.equals("null")){
                salary.setText(salary1);
            }else{salary.setText("");}


            if(!contactemail1.equals("null")){
                contactemail.setText(contactemail1);
            }else{contactemail.setText("");}

            if(!contactname1.equals("null")){
                contactname.setText(contactname1);
            }else{contactnumber.setText("");}

            if(!contactnumber1.equals("null")){
                contactnumber.setText(contactnumber1);
            }else{contactnumber.setText("");}

            if(!qualification1.equals("null")){
                qualification.setText(qualification1);
            }else{qualification.setText("");}

            if(!joblocation1.equals("null")){
                joblocation.setText(joblocation1);
            }else{joblocation.setText("");}


            if(!jobrole1.equals("null")){
                jobrole.setText(jobrole1);
            }else{jobrole.setText("");}
                }
            pDialog.hide();

            final String finalContactnumber = contactnumber1;
            contactnumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + finalContactnumber));
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void variabledeclarition(){
        jobnodata=(TextView)findViewById(R.id.tv_jobdetails_empty_view);
        job=(LinearLayout)findViewById(R.id.lv_jobdetails);
        jobname=(TextView)findViewById(R.id.tv_jobdetails_name);
        companyname=(TextView)findViewById(R.id.tv_jobdetails_companyname);
        jobtype=(TextView)findViewById(R.id.tv_jobdetails_jobtype);
        jobrole=(TextView)findViewById(R.id.tv_jobdetails_tab1_jobrole);
        experience=(TextView)findViewById(R.id.tv_jobdetails_experince);
        salary=(TextView)findViewById(R.id.tv_jobdetails_salary);
        jobtypetab=(TextView)findViewById(R.id.tv_jobdetails_tab1_jobtype);
        jobdescription=(TextView)findViewById(R.id.tv_jobdetails_tab2_details);
        joblocation=(TextView)findViewById(R.id.tv_jobdetails_tab1_joblocation);
        contactemail=(TextView)findViewById(R.id.tv_jobdetails_tab3_contactemail);
        contactnumber=(TextView)findViewById(R.id.tv_jobdetails_tab3_contactnumber);
        contactname=(TextView)findViewById(R.id.tv_jobdetails_tab3_contactname);
        jobimage=(FeedImageView)findViewById(R.id.iv_jobdetails_feed);
        qualification=(TextView)findViewById(R.id.tv_jobdetails_tab1_qualification);
        location=(TextView)findViewById(R.id.tv_jobdetails_tab1_joblocation);

    }
}
