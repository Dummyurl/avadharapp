package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class ViewMatrimonyDetails extends AppCompatActivity {
    String matriid , sp_uid;
    private SharedPreferences sp;
    Button sendrequest;
    LinearLayout personaldetails;
    RelativeLayout RLPersonal;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ProgressDialog dialog;
    TextView  name,id,age,religion,maritialstatus,email,mobile,permAddress,communiAddress,
            weight,height,complexion,physicalstatus,bloodgroup,
            bodytype,eatinghabit,drinking,smoking,employedin,jobcategory,
            jobposition,joblocation,companyname,monthlyincome,qualification,
            fieldofstudy,additionalqualification,familytype,familymembers,
            familystatus,fathername,fatherjob,mothername,motherjob,noofsis,
            noofbros,othermember,star,jathakam,rasi,birthplace,dobinmal,
            birthtime,prefage,prefheight,prefjob,prefjoblocation,
            matchingstar,preflocation,preffamilystatus,concept;
    FeedImageView profimage,graganila;
    ImageView favorite,interest,lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matrimony_details);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.matrimonydetails);
        initialize();
        Intent i=getIntent();
        matriid= i.getExtras().getString("id");
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        TabHost host = (TabHost) findViewById(R.id.th_matrimony_details_details);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.physicaldetails));
        spec.setContent(R.id.matri_tab1);
        spec.setIndicator(getResources().getString(R.string.physicaldetails));
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec(getResources().getString(R.string.jobandeducation));
        spec.setContent(R.id.matri_tab2);
        spec.setIndicator(getResources().getString(R.string.jobandeducation));
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec(getResources().getString(R.string.family));
        spec.setContent(R.id.matri_tab3);
        spec.setIndicator(getResources().getString(R.string.family));
        host.addTab(spec);
        //Tab 4
        spec = host.newTabSpec(getResources().getString(R.string.horoscope));
        spec.setContent(R.id.matri_tab4);
        spec.setIndicator(getResources().getString(R.string.horoscope));
        host.addTab(spec);
        //Tab 5
        spec = host.newTabSpec(getResources().getString(R.string.partnerdetails));
        spec.setContent(R.id.matri_tab5);
        spec.setIndicator(getResources().getString(R.string.partnerdetails));
        host.addTab(spec);
        viewDetails();



        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavorite(sp_uid,id.getText().toString());
//                Toast.makeText(getApplicationContext(),"favorite clicked",Toast.LENGTH_SHORT).show();
            }
        });
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInterestSend(sp_uid,id.getText().toString());
//                Toast.makeText(getApplicationContext(),"interest clicked",Toast.LENGTH_SHORT).show();
            }
        });
        sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"send clicked",Toast.LENGTH_SHORT).show();
            }
        });

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
//
        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }
    private void initialize(){
        name=(TextView)findViewById(R.id.tv_matrimony_details_name);
        id=(TextView)findViewById(R.id.tv_matrimony_details_id);
        age=(TextView)findViewById(R.id.tv_matrimony_details_age);
        religion=(TextView)findViewById(R.id.tv_matrimony_details_religion);
        maritialstatus=(TextView)findViewById(R.id.tv_matrimony_details_maritialstatus);
        email=(TextView)findViewById(R.id.tv_matrimony_details_email);
        mobile=(TextView)findViewById(R.id.tv_matrimony_details_mobile);
        permAddress=(TextView)findViewById(R.id.tv_matrimony_details_permenataddress);
        communiAddress=(TextView)findViewById(R.id.tv_matrimony_details_communicationaddress);
        weight=(TextView)findViewById(R.id.tv_matrimony_details_weight);
        height=(TextView)findViewById(R.id.tv_matrimony_details_height);
        complexion=(TextView)findViewById(R.id.tv_matrimony_details_complexion);
        physicalstatus=(TextView)findViewById(R.id.tv_matrimony_details_physicalstatus);
        bloodgroup=(TextView)findViewById(R.id.tv_matrimony_details_bloodgroup);
        bodytype=(TextView)findViewById(R.id.tv_matrimony_details_bodytype);
        eatinghabit=(TextView)findViewById(R.id.tv_matrimony_details_habit);
        drinking=(TextView)findViewById(R.id.tv_matrimony_details_drinking);
        smoking=(TextView)findViewById(R.id.tv_matrimony_details_smoking);
        employedin=(TextView)findViewById(R.id.tv_matrimony_details_employedin);
        jobcategory=(TextView)findViewById(R.id.tv_matrimony_details_jobcategory);
        jobposition=(TextView)findViewById(R.id.tv_matrimony_details_jobposition);
        joblocation=(TextView)findViewById(R.id.tv_matrimony_details_joblocation);
        companyname=(TextView)findViewById(R.id.tv_matrimony_details_companyname);
        monthlyincome=(TextView)findViewById(R.id.tv_matrimony_details_monthlyincome);
        qualification=(TextView)findViewById(R.id.tv_matrimony_details_qualification);
        fieldofstudy=(TextView)findViewById(R.id.tv_matrimony_details_fieldofstudy);
        additionalqualification=(TextView)findViewById(R.id.tv_matrimony_details_additionalqualification);
        familytype=(TextView)findViewById(R.id.tv_matrimony_details_familytype);
        familymembers=(TextView)findViewById(R.id.tv_matrimony_details_familymembers);
        familystatus=(TextView)findViewById(R.id.tv_matrimony_details_familystatus);
        fathername=(TextView)findViewById(R.id.tv_matrimony_details_fathername);
        fatherjob=(TextView)findViewById(R.id.tv_matrimony_details_fatherjob);
        mothername=(TextView)findViewById(R.id.tv_matrimony_details_mothername);
        motherjob=(TextView)findViewById(R.id.tv_matrimony_details_motherjob);
        noofsis=(TextView)findViewById(R.id.tv_matrimony_details_noofsis);
        noofbros=(TextView)findViewById(R.id.tv_matrimony_details_noofbros);
        othermember=(TextView)findViewById(R.id.tv_matrimony_details_othermembers);
        star=(TextView)findViewById(R.id.tv_matrimony_details_star);
        jathakam=(TextView)findViewById(R.id.tv_matrimony_details_jathakam);
        rasi=(TextView)findViewById(R.id.tv_matrimony_details_rasi);
        birthplace=(TextView)findViewById(R.id.tv_matrimony_details_birthplace);
        dobinmal=(TextView)findViewById(R.id.tv_matrimony_details_dobinmalayam);
        birthtime=(TextView)findViewById(R.id.tv_matrimony_details_birthtime);
        prefage=(TextView)findViewById(R.id.tv_matrimony_details_prefage);
        prefheight=(TextView)findViewById(R.id.tv_matrimony_details_prefheight);
        prefjob=(TextView)findViewById(R.id.tv_matrimony_details_prefjob);
        prefjoblocation=(TextView)findViewById(R.id.tv_matrimony_details_prefjoblocation);
        matchingstar=(TextView)findViewById(R.id.tv_matrimony_details_matchingstar);
        preflocation=(TextView)findViewById(R.id.tv_matrimony_details_preflocation);
        preffamilystatus=(TextView)findViewById(R.id.tv_matrimony_details_preffamilystatus);
        concept=(TextView)findViewById(R.id.tv_matrimony_details_concept);
        profimage=(FeedImageView)findViewById(R.id.iv_matrimony_details_feed);
        graganila=(FeedImageView)findViewById(R.id.iv_matrimony_details_grahanila);
        favorite=(ImageView) findViewById(R.id.iv_matrimony_details_favorite);
        interest=(ImageView) findViewById(R.id.iv_matrimony_details_thump);
        personaldetails=(LinearLayout)findViewById(R.id.ll_matrimony_details_personaldetails);
        sendrequest=(Button)findViewById(R.id.btn_matrimony_detials_sendinterest);
        RLPersonal=(RelativeLayout)findViewById(R.id.rl_matrimony_details_privay);
        lock=(ImageView)findViewById(R.id.iv_matrimony_details_lock);

    }

    private void viewDetails(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetMatrimonyProfileDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("matrimonial");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);
                                name.setText(feedObj.isNull("mat_name")? "-":feedObj.getString("mat_name"));
                                id.setText(feedObj.isNull("mat_profileid")? "-":feedObj.getString("mat_profileid"));
                                age.setText(feedObj.isNull("dob")?"-":feedObj.getString("dob"));
                                religion.setText(feedObj.isNull("religion")?"-":feedObj.getString("religion")+String.valueOf(feedObj.isNull("mat_caste")?"":"-"+feedObj.getString("mat_caste")));
                                maritialstatus.setText(feedObj.isNull("mat_martialstat")?"-":feedObj.getString("mat_martialstat"));
                                email.setText( feedObj.isNull("mat_email")?"-":feedObj.getString("mat_email"));
                                mobile.setText( feedObj.isNull("mat_mobile")?"-":feedObj.getString("mat_mobile"));
                                permAddress.setText(feedObj.isNull("mat_paddr")? "-":feedObj.getString("mat_paddr"));
                                communiAddress.setText(feedObj.isNull("mat_caddr")?"-":feedObj.getString("mat_caddr"));
                                weight.setText(feedObj.isNull("mat_weight")?"-":feedObj.getString("mat_weight"));
                                height.setText(feedObj.isNull("mat_height")?"-":feedObj.getString("mat_height"));
                                complexion.setText( feedObj.isNull("mat_complexion")?"-":feedObj.getString("mat_complexion"));
                                physicalstatus.setText( feedObj.isNull("mat_physical")?"-":feedObj.getString("mat_physical"));
                                bloodgroup.setText(feedObj.isNull("mat_blood")? "-":feedObj.getString("mat_blood"));
                                bodytype.setText(feedObj.isNull("mat_bodytype")?"-":feedObj.getString("mat_bodytype"));
                                eatinghabit.setText(feedObj.isNull("mat_eat")?"-":feedObj.getString("mat_eat"));
                                drinking.setText(feedObj.isNull("mat_drink")?"-":feedObj.getString("mat_drink"));
                                smoking.setText( feedObj.isNull("mat_smoke")?"-":feedObj.getString("mat_smoke"));
                                employedin.setText( feedObj.isNull("mat_employedin")?"-":feedObj.getString("mat_employedin"));
                                jobcategory.setText(feedObj.isNull("mat_jobp")? "-":feedObj.getString("mat_jobp"));
                                jobposition.setText(feedObj.isNull("mat_designation")?"-":feedObj.getString("mat_designation"));
                                joblocation.setText(feedObj.isNull("mat_workloc")?"-":feedObj.getString("mat_workloc"));
                                companyname.setText(feedObj.isNull("mat_company")?"-":feedObj.getString("mat_company"));
                                monthlyincome.setText( feedObj.isNull("mat_mincome")?"-":feedObj.getString("mat_mincome"));
                                qualification.setText( feedObj.isNull("mat_degree")?"-":feedObj.getString("mat_degree"));
                                fieldofstudy.setText(feedObj.isNull("mat_edu")? "-":feedObj.getString("mat_edu"));
                                additionalqualification.setText(feedObj.isNull("mat_addqual")?"-":feedObj.getString("mat_addqual"));
                                familytype.setText(feedObj.isNull("mat_familytype")?"-":feedObj.getString("mat_familytype"));
                                familymembers.setText(feedObj.isNull("mat_family")?"-":feedObj.getString("mat_family"));
                                familystatus.setText( feedObj.isNull("mat_familystatus")?"-":feedObj.getString("mat_familystatus"));
                                fathername.setText( feedObj.isNull("mat_fname")?"-":feedObj.getString("mat_fname"));
                                fatherjob.setText(feedObj.isNull("mat_fjob")? "-":feedObj.getString("mat_fjob"));
                                mothername.setText(feedObj.isNull("mat_mname")?"-":feedObj.getString("mat_mname"));
                                motherjob.setText(feedObj.isNull("mat_mjob")?"-":feedObj.getString("mat_mjob"));
                                noofsis.setText(feedObj.isNull("mat_nsis")?"-":feedObj.getString("mat_nsis"));
                                noofbros.setText( feedObj.isNull("mat_nbro")?"-":feedObj.getString("mat_nbro"));
                                othermember.setText( feedObj.isNull("mat_othermem")?"-":feedObj.getString("mat_othermem"));
                                star.setText(feedObj.isNull("mat_star")? "-":feedObj.getString("mat_star"));
                                jathakam.setText(feedObj.isNull("mat_jathakam")?"-":feedObj.getString("mat_jathakam"));
                                birthplace.setText(feedObj.isNull("mat_rasi")?"-":feedObj.getString("mat_rasi"));
                                dobinmal.setText(feedObj.isNull("mat_dobmal")?"-":feedObj.getString("mat_dobmal"));
                                birthtime.setText( feedObj.isNull("mat_btime")?"-":feedObj.getString("mat_btime"));
                                prefage.setText( feedObj.isNull("mat_agebw")?"-":feedObj.getString("mat_agebw"));
                                prefheight.setText(feedObj.isNull("mat_heightbw")? "-":feedObj.getString("mat_heightbw"));
                                prefjob.setText(feedObj.isNull("mat_prejob")?"-":feedObj.getString("mat_prejob"));
                                prefjoblocation.setText(feedObj.isNull("mat_preworkl")?"-":feedObj.getString("mat_preworkl"));
                                matchingstar.setText(feedObj.isNull("mat_mstar")?"-":feedObj.getString("mat_mstar"));
                                preflocation.setText( feedObj.isNull("mat_prelocation")?"-":feedObj.getString("mat_prelocation"));
                                preffamilystatus.setText( feedObj.isNull("mat_prefstatus")?"-":feedObj.getString("mat_prefstatus"));
                                concept.setText(feedObj.isNull("mat_exconcept")? "-":feedObj.getString("mat_exconcept"));
                                profimage.setImageUrl(feedObj.isNull("image")?DatabaseInfo.MatrimonialImagePath+"default.jpg":DatabaseInfo.MatrimonialImagePath+feedObj.getString("image"),imageLoader);
                                graganila.setImageUrl(feedObj.isNull("mat_grahanila")?DatabaseInfo.GrahanilaImagePath:DatabaseInfo.GrahanilaImagePath+feedObj.getString("mat_grahanila"),imageLoader);
                                String privacy=feedObj.isNull("privacy")? "":feedObj.getString("privacy");
                                if(privacy.equals("")||privacy.equals("open")){

                                    personaldetails.setVisibility(View.VISIBLE);
                                    sendrequest.setVisibility(View.GONE);
                                    lock.setVisibility(View.GONE);
                                }else {
                                    personaldetails.setVisibility(View.INVISIBLE);
                                    sendrequest.setVisibility(View.VISIBLE);
                                    lock.setVisibility(View.VISIBLE);
                                }

                                dialog.hide();



                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(ViewMatrimonyDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", String.valueOf(matriid));
                params.put("uid", String.valueOf(sp_uid));

            Log.e("matri id",matriid);

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
    public void  setFavorite(final String uid, final String pid){
        final String[] res = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.AddtoMatrimonyFavoriteURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                     String   favoriteResult =response.toString();


                        Toast.makeText(getApplicationContext(),favoriteResult,Toast.LENGTH_SHORT).show();

//                        setAnimationForFavoriteText(response,layout);
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid", String.valueOf(pid));
                params.put("uid",String.valueOf(uid));
                Log.e("profile id",pid);
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
    public void  setInterestSend(final String uid, final String pid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.AddMatrimonyExpressInterestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        String   favoriteResult =response.toString();


                        Toast.makeText(getApplicationContext(),favoriteResult,Toast.LENGTH_SHORT).show();

//                        setAnimationForFavoriteText(response,layout);
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pid", String.valueOf(pid));
                params.put("uid",String.valueOf(uid));
                Log.e("profile id",pid);
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
