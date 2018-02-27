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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrimonyBuyPackage extends AppCompatActivity {
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
    List<String> JobpositionList1;
    List<String> agebetween=new ArrayList<String>();
    List<String> heightbetween=new ArrayList<String>();
    List<String>  matchingstar=new ArrayList<String>();
    private int day, month, year,parentid=0;
    private String profileid,day1,month1,year1,resumepath="",photopath,uid,jobpostiondata;
    String country_code="0",state_code="0",city_code="0";
    private SharedPreferences sp;

    String[] jobposdata,matchingstr;
    Spinner maritialstatus,country,state,religion,caste,city,
          physicalstatus,
            qualification,famiytype,star,typeofjathakam;
    MaterialEditText email,
            name;
    RadioGroup lookingfor,plan;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_matrimony_buy_package);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.buypackage);
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        profileid=sp.getString(Constants.PROFILEID,"");
        uid=sp.getString(Constants.UID,"");
        initialize();
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(famiytype.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select One Family Type",Toast.LENGTH_SHORT).show();
                }
                else if(maritialstatus.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Maritial Status",Toast.LENGTH_SHORT).show();
                }
                else if(physicalstatus.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Physical Status",Toast.LENGTH_SHORT).show();
                }
                else if(religion.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Religion",Toast.LENGTH_SHORT).show();
                }
                else if(caste.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Caste",Toast.LENGTH_SHORT).show();
                }
                else if(typeofjathakam.getSelectedItemPosition()==0){
                     Toast.makeText(getApplicationContext(),"Please Select Type of Jathakam",Toast.LENGTH_SHORT).show();
                }
                else if(star.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Star",Toast.LENGTH_SHORT).show();
                }
                else if(plan.getCheckedRadioButtonId()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Type of Plan",Toast.LENGTH_SHORT).show();
                }
                else if(lookingfor.getCheckedRadioButtonId()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Type of user (Bride or Groom) ",Toast.LENGTH_SHORT).show();
                }

                else {
                    setRegistration();
                }
            }
        });
    }

    private void initialize(){
        lookingfor=(RadioGroup)findViewById(R.id.rg_matrymony_buypackage_modeofmatri);
        plan=(RadioGroup)findViewById(R.id.rg_myatrimony_buypackage_buypackage_plan);
        religion=(Spinner)findViewById(R.id.sp_matrymony_buypackage_religion);
        caste=(Spinner)findViewById(R.id.sp_matrymony_buypackage_caste);
        star=(Spinner)findViewById(R.id.sp_matrymony_buypackage_star);
        famiytype=(Spinner)findViewById(R.id.sp_matrymony_buypackage_familytype);
        maritialstatus=(Spinner)findViewById(R.id.sp_matrymony_buypackage_maritialstatus);
        physicalstatus=(Spinner)findViewById(R.id.sp_matrymony_buypackage_physicalstatus);
        save=(Button) findViewById(R.id.btn_matrimony_buypackage_save);
        typeofjathakam=(Spinner)findViewById(R.id.sp_matrymony_buypackage_typeofjathakam);
    }

    public void setRegistration(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyBuyPakcageURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

//                        pDialog.hide();
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        Toast.makeText(MatrimonyBuyPackage.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String type1="";
                if(lookingfor.getCheckedRadioButtonId()==R.id.rb_matrymony_buypackage_bride||
                        lookingfor.getCheckedRadioButtonId()==R.id.rb_matrymony_buypackage_bride){
                    type1 =((RadioButton)findViewById(lookingfor.getCheckedRadioButtonId())).getText().toString();
                }else {type1="";}

                params.put("brideorgroom",type1 );
                params.put("martl",maritialstatus.getSelectedItem().toString());

                String planid="0";
                if(plan.getCheckedRadioButtonId()==R.id.rb_myatrimony_buypackage_buypackage_gold){planid="1";}
                else if(plan.getCheckedRadioButtonId()==R.id.rb_myatrimony_buypackage_buypackage_silver){planid="2";}
                else if(plan.getCheckedRadioButtonId()==R.id.rb_myatrimony_buypackage_buypackage_diamond){planid="3";}

                params.put("matriplan",planid);
                params.put("castesel",String.valueOf(religioncode1.get(validateSpCode(caste))) );
                params.put("religion",String.valueOf(religioncode2.get(validateSpCode(religion))));
                params.put("physical",String.valueOf(physicalstatus.getSelectedItem().toString()) );
                params.put("jathakm",String.valueOf(typeofjathakam.getSelectedItem().toString()) );
                params.put("star",String.valueOf(star.getSelectedItem().toString()));
                params.put("familytype",String.valueOf(famiytype.getSelectedItem().toString()));
                params.put("uid",uid);


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
                                JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
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
                            Toast.makeText(MatrimonyBuyPackage.this,error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MatrimonyBuyPackage.this,error.toString(), Toast.LENGTH_LONG).show();
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
    private int validateSpCode(Spinner spinner){
        int returncheck=0;
        if(spinner.getSelectedItemPosition()!=0){
            returncheck=spinner.getSelectedItemPosition();

        }else {
            returncheck=0;
        }
        return returncheck;
    }
}
