package com.avadharwebworld.avadhar.Activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class SettingsAcademic extends AppCompatActivity {
    EditText schoolname,collegename,workingcomplany,workingas,field,companylocation;
    Spinner syearfrom,syearto,cyearfrom,cyearto,workingon;
    RadioGroup rgemplye;
    Button update;
    String uid;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_settings_academic);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(R.string.academic1);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initialize();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        setYear(syearfrom);
        setYear(syearto);
        setYear(cyearfrom);
        setYear(cyearto);
        setYear(workingon);


        viewDetails();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdate();
            }
        });

    }
    private void initialize(){

        schoolname=(EditText)findViewById(R.id.et_academic_settings_schoolname);
        collegename=(EditText)findViewById(R.id.et_academic_settings_collagename);
        workingcomplany=(EditText)findViewById(R.id.et_academic_settings_currentworkingcompany);
        workingas=(EditText)findViewById(R.id.et_academic_settings_currentlyworkingas);
        field=(EditText)findViewById(R.id.et_academic_settings_field);
        companylocation=(EditText)findViewById(R.id.et_academic_settings_companylocation);
        syearfrom=(Spinner)findViewById(R.id.sp_academic_settings_schoolyearfrom);
        syearto=(Spinner)findViewById(R.id.sp_academic_settings_schollyearto);
        cyearfrom=(Spinner)findViewById(R.id.sp_academic_settings_collegeyearfrom);
        cyearto=(Spinner)findViewById(R.id.sp_academic_settings_collegeyearto);
        workingon=(Spinner)findViewById(R.id.sp_academic_settings_startedworking);
        update=(Button)findViewById(R.id.btn_academic_settings_update);
        rgemplye=(RadioGroup)findViewById(R.id.rg_academic_settings_employ);

    }

    @Override
    public void onBackPressed()
    {
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

    private void viewDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetUserDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("user");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);

                                schoolname.setText(feedObj.isNull("school")? "":feedObj.getString("school"));


                                collegename.setText(feedObj.isNull("clgname")? "":feedObj.getString("clgname"));
                                String sfrom=feedObj.isNull("usfrom")? "":feedObj.getString("usfrom");
                                String cfrom=feedObj.isNull("ucfrom")? "":feedObj.getString("ucfrom");
                                String sto=feedObj.isNull("usto")? "":feedObj.getString("usto");
                                String cto=feedObj.isNull("ucto")? "":feedObj.getString("ucto");
                                Log.e("sfrom",sfrom);
                                String job=feedObj.isNull("job")? "":feedObj.getString("job");
                                if(job.equals("Employee")){
                                    rgemplye.check(R.id.rb_academic_settings_employee);
                                }else if(job.equals("Others"))  rgemplye.check(R.id.rb_academic_settings_other);
                                workingcomplany.setText(feedObj.isNull("cmp_name")? "":feedObj.getString("cmp_name"));
                                workingas.setText(feedObj.isNull("det_employ")? "":feedObj.getString("det_employ"));
                                field.setText(feedObj.isNull("uwfield")? "":feedObj.getString("uwfield"));
                                companylocation.setText(feedObj.isNull("cmp_location")? "":feedObj.getString("cmp_location"));
                             List year=setdata();
                                if(year.contains(sfrom)){
                                    syearfrom.setSelection(year.indexOf(sfrom));
                                }
                                if(year.contains(cfrom)){
                                    cyearfrom.setSelection(year.indexOf(cfrom));
                                }
                                if(year.contains(sto)){
                                    syearto.setSelection(year.indexOf(sto));
                                }
                                if(year.contains(cto)){
                                    cyearto.setSelection(year.indexOf(cto));
                                }





                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(SettingsAcademic.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(uid));



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
    private void setYear(Spinner spinner) {
        String selectwght="Select";

        List<String> height1=new ArrayList<String>();
        height1.add(selectwght);
        for (int i = 1980; i <= 2017; i++) {

            height1.add(String.valueOf(i));

        }
        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,height1);

        spinner.setAdapter(spinnerAdapter1);
    }
    private List setdata(){
        String selectwght="Select";

        List<String> height1=new ArrayList<String>();
        height1.add(selectwght);
        for (int i = 1980; i <= 2017; i++) {

            height1.add(String.valueOf(i));

        }
        return height1;

    }
    private void setUpdate(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.UpdateAcademicSettingsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(SettingsAcademic.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String rgGender1="";
                if(rgemplye.getCheckedRadioButtonId()==R.id.rb_academic_settings_employee||
                        rgemplye.getCheckedRadioButtonId()==R.id.rb_academic_settings_other){
                    rgGender1 =((RadioButton)findViewById(rgemplye.getCheckedRadioButtonId())).getText().toString();

                }else {rgGender1="";}
                params.put("uid",uid);
                params.put("school",schoolname.getText().toString());
                params.put("sfrom",validateSp(syearfrom));
                params.put("sto", validateSp(syearto));
                params.put("clgname", collegename.getText().toString());
                params.put("cfrom", validateSp(cyearfrom));
                params.put("cto",validateSp(cyearto));
                params.put("employ",workingas.getText().toString());
                params.put("cmpname", workingcomplany.getText().toString());
                params.put("job",rgGender1);
                params.put("cmploc", companylocation.getText().toString());
                params.put("wyear", validateSp(workingon));
                params.put("field", field.getText().toString());

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
    private String validateSp(Spinner spinner){
        String returncheck="";
        if(spinner.getSelectedItemPosition()!=0){
//            returncheck="";
             returncheck= spinner.getSelectedItem().toString();

        }else {
            returncheck="";
        }
        return returncheck;
    }
}
