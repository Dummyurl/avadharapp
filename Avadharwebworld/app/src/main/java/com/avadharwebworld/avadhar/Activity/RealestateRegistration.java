package com.avadharwebworld.avadhar.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
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
import android.widget.DatePicker;
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
import com.avadharwebworld.avadhar.Support.MultiSelectSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealestateRegistration extends AppCompatActivity {
    private Spinner  property,typeofproperty,country,state,city,typeofactivity,furnished,typeofuesr;
    private MaterialEditText price,totalarea,propertydescription,tokenamount,landarea,builtarea,ageofproperty,ownername,mobileno,title,email,availablefromproperty;
    private RadioGroup brocker,transactiontype;
    private Button submit;
    Calendar cal;
    private int day, month, year,parentid=0;
    private String profileid,day1,month1,year1,resumepath="",photopath,uid,jobpostiondata;
    String country_code="0",state_code="0",city_code="0";
    private SharedPreferences sp;

    List<String> category=new ArrayList<String>();
    List<String> category1=new ArrayList<String>();
    List<String> subcategory=new ArrayList<String>();
    List<String> subcategory1=new ArrayList<String>();
    List<String>experience2=new ArrayList<String>();
    List<String>jobrole2=new ArrayList<String>();
    List<String> statecodeid =  new ArrayList<String>();
    List<String> statecodename =  new ArrayList<String>();
    List<String> citycodeid =  new ArrayList<String>();
    List<String> citycodename =  new ArrayList<String>();
    List<String> countrycodid =  new ArrayList<String>();
    List<String> countrycodename =  new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_realestate_registration);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.registration);
        initialize();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        profileid=sp.getString(Constants.PROFILEID,"");

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        availablefromproperty.setText(R.string.avaliablefromproperty);
        availablefromproperty.setFocusableInTouchMode(false);
        availablefromproperty.setFocusable(false);
        availablefromproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        getCountryCode(country);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Please fill this field!!!");
                    email.requestFocus();
                }
                else if((mobileno.getText().toString().length() !=10)) {
                    mobileno.setError("Mobile number must have 10 digits");
                    mobileno.requestFocus();
                }
                else if((ownername.getText().toString().length() < 1)) {
                    ownername.setError("Please fill this field!!!");
                    ownername.requestFocus();
                }
                else if(property.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select One Property Type",Toast.LENGTH_SHORT).show();
                }
                else if(typeofproperty.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select One Type of Property",Toast.LENGTH_SHORT).show();
                }
                else if(country.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Country",Toast.LENGTH_SHORT).show();
                }
                else if(state.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select State",Toast.LENGTH_SHORT).show();
                }
                else if(city.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select City",Toast.LENGTH_SHORT).show();
                }
                else if(typeofactivity.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Type of Activity",Toast.LENGTH_SHORT).show();
                }
                else if(typeofuesr.getSelectedItemPosition()==0){
                    Toast.makeText(getApplicationContext(),"Please Select Type of User",Toast.LENGTH_SHORT).show();
                }
                else if((price.getText().toString().length() < 1)) {
                    price.setError("Please fill this field!!!");
                    price.requestFocus();
                }
                else if((totalarea.getText().toString().length() < 1)) {
                    totalarea.setError("Please fill this field!!!");
                    totalarea.requestFocus();
                }
                else if((propertydescription.getText().toString().length() < 1)) {
                    propertydescription.setError("Please fill this field!!!");
                    propertydescription.requestFocus();
                }
                else {
                    setRegistration();
                }


            }
        });
        price.clearFocus();
        String cid="0";
        getSubcategory(property,cid);
        property.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                category.clear();
                category1.clear();
                if(position!=0){
                    String cid=subcategory.get(position);
                    getSubcategory(typeofproperty,cid);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void initialize(){
        property=(Spinner)findViewById(R.id.sp_realestate_registratiom_property);
        typeofproperty=(Spinner)findViewById(R.id.sp_realestate_registratiom_typeofproperty);
        country=(Spinner)findViewById(R.id.sp_realestate_registratiom_country);
        state=(Spinner)findViewById(R.id.sp_realestate_registratiom_state);
        city=(Spinner)findViewById(R.id.sp_realestate_registratiom_city);
        typeofactivity=(Spinner)findViewById(R.id.sp_realestate_registratiom_typeofactivity);
        furnished=(Spinner)findViewById(R.id.sp_realestate_registratiom_furnished);
        typeofuesr=(Spinner)findViewById(R.id.sp_realestate_registratiom_typeofuser);

        price=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_price);
        totalarea=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_totalarea);
        propertydescription=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_propertydescription);
        tokenamount=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_tokenamount);
        landarea=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_landarea);
        builtarea=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_buildarea);
        ageofproperty=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_ageofproperty);
        ownername=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_ownername);
        mobileno=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_mobile);
        email=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_email);
        availablefromproperty=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_availabletime);
        title=(MaterialEditText)findViewById(R.id.et_realestate_registratiom_titile);
        brocker=(RadioGroup)findViewById(R.id.rg_realestate_registratiom_brocker);
        transactiontype=(RadioGroup)findViewById(R.id.rg_realestate_registratiom_transaction);
        submit=(Button)findViewById(R.id.btn_realestate_registration_save);

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
    private void setDate() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                if(monthOfYear < 10){

                    month1 = "0" + String.valueOf(monthOfYear);
                }
                if(dayOfMonth < 10){

                    day1  = "0" + String.valueOf(dayOfMonth) ;
                }
                Time chosenDate = new Time();
                chosenDate.set(dayOfMonth,monthOfYear,year);
                long dtDob = chosenDate.toMillis(true);
                availablefromproperty.setText( DateFormat.format("yyyy-MM-dd", dtDob));
//                availablefromproperty.setText( year+"-"+month1 + "-" + day1  );

                day1=String.valueOf(dayOfMonth);
                month1=String.valueOf(monthOfYear);
                year1=String.valueOf(year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
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
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
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
                        Toast.makeText(RealestateRegistration.this,error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(RealestateRegistration.this,error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(RealestateRegistration.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private String validateEtMamdatory(MaterialEditText editText){
        String returncheck="";
        if(editText.getText().toString().length()<1){
            returncheck = "";
        }else {
            returncheck = editText.getText().toString();
        }
        return returncheck;
    }
    private String validateSp(Spinner spinner){
        String returncheck="";
        if(spinner.getSelectedItemPosition()!=0){
            returncheck="";
             returncheck= spinner.getSelectedItem().toString();

        }else {
            returncheck="";
        }
        return returncheck;
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
    public void setRegistration(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.RealestateInsertURL,
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
                        Toast.makeText(RealestateRegistration.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                String rgPrivacy1="";
                String date="";
                String type="";
                String type1="";
                if(typeofactivity.getSelectedItemPosition()!=0){
                    type1=typeofactivity.getSelectedItem().toString();
                }else type1="";
                params.put("category", String.valueOf(subcategory.get(validateSpCode(property))));
                params.put("type", type1);
                params.put("price", validateEtMamdatory(price));
                params.put("furnished", validateSp(furnished));
                params.put("total", validateEtMamdatory(totalarea));
                params.put("adddes", validateEtMamdatory(propertydescription));
                params.put("towner", validateSp(typeofuesr));
                params.put("floor", "");// not listed in website
                params.put("width", "");//not listed in website
                params.put("no", "");//not listed in website
                params.put("eprice", "");

                if(brocker.getCheckedRadioButtonId()==R.id.rb_realestate_registratiom_no||
                        brocker.getCheckedRadioButtonId()==R.id.rb_realestate_registratiom_yes){
                    rgPrivacy1 =((RadioButton)findViewById(brocker.getCheckedRadioButtonId())).getText().toString();
                }else {rgPrivacy1="";}
                params.put("brocker",rgPrivacy1 );
                params.put("bcharge", "");//not listed in website
                params.put("tamt", validateEtMamdatory(tokenamount));
                params.put("plot", validateEtMamdatory(landarea));
                params.put("covered", validateEtMamdatory(builtarea));
                params.put("pcity", "0");//not listed in website
                params.put("address", "");//not listed in website
                params.put("fstatus", "");//not listed in website
                params.put("florno", "");//not listed in website

                params.put("ad", "");//not listed in website
                params.put("ad1", "");//not listed in website
                params.put("city", validateSp(city));
                params.put("locality", validateSp(city));
                params.put("raddress", "");//not listed in website
                params.put("erent", "");//not listed in website
                params.put("mcharge", "");//not listed in website

                params.put("pfloor", "");//not listed in website
                params.put("aproperty", validateEtMamdatory(ageofproperty));

                params.put("afrom", availablefromproperty.getText().toString());
                params.put("ownername", validateEtMamdatory(ownername));
                params.put("titlename", validateEtMamdatory(title));
                params.put("mobileno", validateEtMamdatory(mobileno));
                params.put("email", validateEtMamdatory(email));
                if(transactiontype.getCheckedRadioButtonId()==R.id.rb_realestate_registratiom_new||
                        transactiontype.getCheckedRadioButtonId()==R.id.rb_realestate_registratiom_resale){
                    type =((RadioButton)findViewById(brocker.getCheckedRadioButtonId())).getText().toString();
                }else {type="";}
                params.put("ttype",type );
                params.put("country",  String.valueOf(countrycodid.get(validateSpCode(country))));
                params.put("state", String.valueOf(statecodeid.get(validateSpCode(state))));
                params.put("ctype", String.valueOf(category.get(validateSpCode(typeofproperty))) );
                params.put("location", String.valueOf(citycodeid.get(validateSpCode(city))) );
                params.put("avadhar_id", profileid);
            Log.e("location", String.valueOf(citycodeid.get(validateSpCode(city))));
                Log.e("date",date);
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
    public void getSubcategory(final Spinner st, final String Rcode){
//        final List<String> religioncode =  new ArrayList<String>();
        if(!Rcode.equals("0")){
//
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetSubCategoryURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Volleyresponse",response.toString());
                            try {
                                JSONArray jArray;
                                JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                                Log.e("volleyJson", Jobject.toString());
                                try {
                                    jArray = Jobject.getJSONArray("subcategory");
                                    int jarraylength=jArray.length()+1;
                                    String c1[] = new String[jarraylength];
                                    String c2[] = new String[jarraylength];
                                    String c3[] = new String[jarraylength];
                                    Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()),Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < jarraylength; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);

                                        String cid = json_data.getString("id");
                                        String sname = json_data.getString("category");


                                        cid = json_data.getString("id");
                                        sname = json_data.getString("category");
                                        c1[i]=cid;
                                        c3[i]=sname;


                                        category.add(cid);
                                        category1.add(sname);
                                    }
//                                        c1[i]=cid;
//                                        c3[i]=sname;

//                                        statecodename=c3;
//                                        statecodeid=c1;


                                } catch (Exception e) {
                                }

                                category1.add(0,"Select");
                                category.add(0,"0");
                                Toast.makeText(getApplicationContext(),String.valueOf(Arrays.asList(category)),Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,category1);
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
                            Toast.makeText(RealestateRegistration.this,error.toString(), Toast.LENGTH_LONG).show();
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
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetSubCategoryURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Volleyresponse",response.toString());
                            try {
                                JSONArray jArray;
                                JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                                Log.e("volleyJson", Jobject.toString());
                                try {
                                    jArray = Jobject.getJSONArray("subcategory");
                                    int jarraylength=jArray.length()+1;
                                    String c1[] = new String[jarraylength];
                                    String c2[] = new String[jarraylength];
                                    String c3[] = new String[jarraylength];
                                    Toast.makeText(getApplicationContext(),String.valueOf(jArray.length()),Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < jarraylength; i++) {
                                        JSONObject json_data = jArray.getJSONObject(i);

                                        String cid = json_data.getString("id");
                                        String sname = json_data.getString("category");



                                        cid = json_data.getString("id");
                                        sname = json_data.getString("category");
                                        c1[i]=cid;
                                        c3[i]=sname;


                                        subcategory.add(cid);
                                        subcategory1.add(sname);
                                    }
//                                        c1[i]=cid;
//                                        c3[i]=sname;

//                                        statecodename=c3;
//                                        statecodeid=c1;


                                } catch (Exception e) {
                                }
                                subcategory1.add(0,"Select");
                                subcategory.add(0,"0");


                                ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,subcategory1);
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
                            Toast.makeText(RealestateRegistration.this,error.toString(), Toast.LENGTH_LONG).show();
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
    public String validateDateFormat(String dateToValdate) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HHmmss");
        //To make strict date format validation
        formatter.setLenient(false);
        Date parsedDate = null;
        try {
            parsedDate = formatter.parse(dateToValdate);
            System.out.println("++validated DATE TIME ++"+formatter.format(parsedDate));

        } catch (ParseException e) {
            //Handle exception
        }
        return String.valueOf(parsedDate);
    }
}
