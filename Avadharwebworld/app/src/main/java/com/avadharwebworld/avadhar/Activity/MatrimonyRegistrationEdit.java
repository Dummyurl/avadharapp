package com.avadharwebworld.avadhar.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.avadharwebworld.avadhar.Support.FilePath;
import com.avadharwebworld.avadhar.Support.MultiSelectSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrimonyRegistrationEdit extends AppCompatActivity {
    List<String> qualification2=new ArrayList<String>();
    List<String> university2=new ArrayList<String>();
    List<String>experience2=new ArrayList<String>();
    List<String>jobrole2=new ArrayList<String>();
    List<String> statecodeid =  new ArrayList<String>();
    List<String> weightlist =  new ArrayList<String>();
    List<String> heightllist =  new ArrayList<String>();
    List<String> statecodename =  new ArrayList<String>();
    List<String> citycodeid =  new ArrayList<String>();
    List<String> citycodename =  new ArrayList<String>();
    List<String> countrycodid =  new ArrayList<String>();
    List<String> countrycodename =  new ArrayList<String>();
    List<String> religioncode1 =  new ArrayList<String>();
    List<String> religionname1 =  new ArrayList<String>();
    List<String> religionname2 =  new ArrayList<String>();
    List<String> religioncode2 =  new ArrayList<String>();
    List<String> JobpositionList,JobpositionList1;
    List<String> agebetween=new ArrayList<String>();
    List<String> heightbetween=new ArrayList<String>();
    List<String>  matchingstar=new ArrayList<String>();
    private int day, month, year,parentid=0;
    private String profileid,day1,month1,year1,resumepath="",photopath,uid,jobpostiondata,matriid,mProfileid;
    String country_code="0",state_code="0",city_code="0";
    private SharedPreferences sp;

    String[] jobposdata,matchingstr;
    Spinner profilefor,maritialstatus,country,state,religion,caste,city,
            bodytype,physicalstatus,complexion,bloodgroup,weight,height,
            Ehabit,Dhabit,Shabit,employedin,jobcategory,monthyincome,
            qualification,famiytype,familystatus,star,typeofjathakam,rasi,
            agebetweento,agebetweenfrom,heightfrom,heightto,Pmaritialstatus,Pfamilystatus;
    MaterialEditText email,presentaddress,mobile,communicationaddress,
            name,landline,dob,designation,worklocation,companyname,
            fieldofstudy,addiqualification,birthplace,dobinmalayalam,
            birthtime,pWorklocation,pNativelocation,concept;
    RadioGroup rgGender,rgPrivacy;
    CheckBox cbFather,cbMother,cbBrother,cbSister,cbOther;
    Button save,uploadGrahanila;
    MultiSelectSpinner pJobPosition,pMatchingStar;
    Calendar cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrimony_registration_edit);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.matrimonyregistrationedit);
        variableInitialize();
        Intent i=getIntent();
        matriid= i.getExtras().getString("id");
        mProfileid=i.getExtras().getString("profileid");
        Toast.makeText(getApplicationContext(),"mprofileid : "+mProfileid+"  matriid : "+matriid,Toast.LENGTH_SHORT).show();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        profileid=sp.getString(Constants.PROFILEID,"");
        uid=sp.getString(Constants.UID,"");
        jobpostiondata=getResources().getString(R.string.jobpositiondata);
        jobposdata=jobpostiondata.split(",");
        JobpositionList=new ArrayList<String>(Arrays.asList(jobposdata));
        JobpositionList1=new ArrayList<String>(Arrays.asList(jobposdata));

        getCountryCode(country);
//        getinterestedfieldCode(jobcategory);
        getQualificationCode(qualification);
//        getparnerprefercencejob(pJobPosition);
        String cid="0";
        getReligion(religion,cid);
        JobpositionList.add(0,"Select Job Category");
//        JobpositionList1.add(0,"Select Partner Job Position");
//        JobpositionList1.add(0,"0");
        ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,JobpositionList);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);
        jobcategory.setAdapter(spinnerAdapter);
        jobcategory.setSelection(0);


        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,JobpositionList1);
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_iltem);
        pJobPosition.setItems(JobpositionList1);

        matchingstr=getResources().getStringArray(R.array.matchingstar);
        matchingstar=Arrays.asList(matchingstr);
        ArrayAdapter<String> spinnerAdapter2 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,matchingstar);
        spinnerAdapter2.setDropDownViewResource(R.layout.spinner_iltem);
        pMatchingStar.setItems(matchingstar);
        pMatchingStar.setPrompt("Select Matching Star");
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
        setAgebetween();
        setHeight();
        setHeightbetween();
        setWeight();


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

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dob.setText(R.string.dateofbirth);
        dob.setFocusableInTouchMode(false);
        dob.setFocusable(false);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateofbirth();
            }
        });
        viewDetails();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdate();
            }
        });
        uploadGrahanila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(i, 2);
            }
        });

    }
    private void viewDetails(){

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
//                                .setText(feedObj.isNull("mat_profileid")? "-":feedObj.getString("mat_profileid"));
                                dob.setText(feedObj.isNull("dob")?"-":feedObj.getString("dob"));
                                String rjion= feedObj.isNull("religion")?"":feedObj.getString("religion");
                                String caste1= feedObj.isNull("mat_caste")?"":feedObj.getString("mat_caste");
                                String gent= feedObj.isNull("mat_gender")?"":feedObj.getString("mat_gender");
                                if(gent=="Male"){
                                    rgGender.check(R.id.rb_matrimonyregistration_edit_male);
                                }else {
                                    rgPrivacy.check(R.id.rb_matrimonyregistration_edit_female);
                                }
                                if (religioncode2.contains(rjion)) {
                                    religion.setSelection(religioncode2.indexOf(rjion));
                                }
                                if (religioncode1.contains(caste1)) {
                                    caste.setSelection(religioncode1.indexOf(caste1));
                                }

                                String mstatus= feedObj.isNull("mat_martialstat")?"":feedObj.getString("mat_martialstat");
                                String[] androidStrings = getResources().getStringArray(R.array.maritialstatus);

                                if (Arrays.asList(androidStrings).contains(mstatus)) {
                                    maritialstatus.setSelection(Arrays.asList(androidStrings).indexOf(mstatus));
                                }

                                email.setText( feedObj.isNull("mat_email")?"":feedObj.getString("mat_email"));
                                mobile.setText( feedObj.isNull("mat_mobile")?"":feedObj.getString("mat_mobile"));
                                presentaddress.setText(feedObj.isNull("mat_paddr")? "":feedObj.getString("mat_paddr"));
                                communicationaddress.setText(feedObj.isNull("mat_caddr")?"":feedObj.getString("mat_caddr"));

                                String weight1= feedObj.isNull("mat_weight")?"":feedObj.getString("mat_weight");

                                if (weightlist.contains(weight1)) {
                                    weight.setSelection(weightlist.indexOf(weight1));
                                }
                               String height1= feedObj.isNull("mat_height")?"":feedObj.getString("mat_height");
                                if (heightllist.contains(height1)) {
                                    height.setSelection(heightllist.indexOf(height1));
                                }

                                String complexion1 = feedObj.isNull("mat_complexion")?"-":feedObj.getString("mat_complexion");
                                if (Arrays.asList(getResources().getStringArray(R.array.complexion)).contains(complexion1)) {
                                    complexion.setSelection(Arrays.asList(getResources().getStringArray(R.array.complexion)).indexOf(complexion1));
                                }
                              String phystus= feedObj.isNull("mat_physical")?"-":feedObj.getString("mat_physical");
                                if (Arrays.asList(getResources().getStringArray(R.array.physicalstatus)).contains(phystus)) {
                                    physicalstatus.setSelection(Arrays.asList(getResources().getStringArray(R.array.physicalstatus)).indexOf(phystus));
                                }

                                String blood= feedObj.isNull("mat_blood")? "-":feedObj.getString("mat_blood");
                                if (Arrays.asList(getResources().getStringArray(R.array.bloodgroup)).contains(blood)) {
                                    bloodgroup.setSelection(Arrays.asList(getResources().getStringArray(R.array.bloodgroup)).indexOf(blood));
                                }
                                String bodytype1=feedObj.isNull("mat_bodytype")?"-":feedObj.getString("mat_bodytype");
                                if (Arrays.asList(getResources().getStringArray(R.array.bodytype)).contains(bodytype1)) {
                                    bodytype.setSelection(Arrays.asList(getResources().getStringArray(R.array.bodytype)).indexOf(bodytype1));
                                }
                              String  eatinghabit=feedObj.isNull("mat_eat")?"-":feedObj.getString("mat_eat");
                                if (Arrays.asList(getResources().getStringArray(R.array.eatinghabit)).contains(eatinghabit)) {
                                    Ehabit.setSelection(Arrays.asList(getResources().getStringArray(R.array.eatinghabit)).indexOf(eatinghabit));
                                }
                                String drinking=feedObj.isNull("mat_drink")?"-":feedObj.getString("mat_drink");
                                if (Arrays.asList(getResources().getStringArray(R.array.drinkinghabit)).contains(drinking)) {
                                    Dhabit.setSelection(Arrays.asList(getResources().getStringArray(R.array.drinkinghabit)).indexOf(drinking));
                                }
                              String  smoking= feedObj.isNull("mat_smoke")?"-":feedObj.getString("mat_smoke");

                                if (Arrays.asList(getResources().getStringArray(R.array.smokinghabit)).contains(smoking)) {
                                    Shabit.setSelection(Arrays.asList(getResources().getStringArray(R.array.smokinghabit)).indexOf(smoking));
                                }
                                String employedin1=feedObj.isNull("mat_employedin")?"-":feedObj.getString("mat_employedin");
                                if (Arrays.asList(getResources().getStringArray(R.array.employedin)).contains(employedin1)) {
                                    employedin.setSelection(Arrays.asList(getResources().getStringArray(R.array.employedin)).indexOf(employedin1));
                                }
                               String  jobcategory1=feedObj.isNull("mat_jobp")? "-":feedObj.getString("mat_jobp");
                                if (JobpositionList.contains(jobcategory1)) {
                                    jobcategory.setSelection(JobpositionList.indexOf(jobcategory1));
                                }
                                designation.setText(feedObj.isNull("mat_designation")?"-":feedObj.getString("mat_designation"));
                                worklocation.setText(feedObj.isNull("mat_workloc")?"-":feedObj.getString("mat_workloc"));
                                companyname.setText(feedObj.isNull("mat_company")?"-":feedObj.getString("mat_company"));
                                String monthlyincome=feedObj.isNull("mat_mincome")?"-":feedObj.getString("mat_mincome");
                                if (Arrays.asList(getResources().getStringArray(R.array.monthlyincome)).contains(monthlyincome)) {
                                    monthyincome.setSelection(Arrays.asList(getResources().getStringArray(R.array.monthlyincome)).indexOf(monthlyincome));
                                }

                                String qualification1=feedObj.isNull("mat_degree")?"-":feedObj.getString("mat_degree");
                                if (qualification2.contains(qualification1)) {
                                    qualification.setSelection(qualification2.indexOf(qualification1));
                                }
                                fieldofstudy.setText(feedObj.isNull("mat_edu")? "-":feedObj.getString("mat_edu"));
                                addiqualification.setText(feedObj.isNull("mat_addqual")?"-":feedObj.getString("mat_addqual"));
                                String famiytype1=feedObj.isNull("mat_familytype")?"-":feedObj.getString("mat_familytype");
                                if (Arrays.asList(getResources().getStringArray(R.array.familytype)).contains(famiytype1)) {
                                    famiytype.setSelection(Arrays.asList(getResources().getStringArray(R.array.familytype)).indexOf(famiytype1));
                                }
//                                familymembers.setText(feedObj.isNull("mat_family")?"-":feedObj.getString("mat_family"));

                               String familystatus1= feedObj.isNull("mat_familystatus")?"-":feedObj.getString("mat_familystatus");
                                if (Arrays.asList(getResources().getStringArray(R.array.familystatus)).contains(familystatus1)) {
                                    familystatus.setSelection(Arrays.asList(getResources().getStringArray(R.array.familystatus)).indexOf(familystatus1));
                                }
//                                .setText( feedObj.isNull("mat_fname")?"-":feedObj.getString("mat_fname"));
//                                fatherjob.setText(feedObj.isNull("mat_fjob")? "-":feedObj.getString("mat_fjob"));
//                                mothername.setText(feedObj.isNull("mat_mname")?"-":feedObj.getString("mat_mname"));
//                                motherjob.setText(feedObj.isNull("mat_mjob")?"-":feedObj.getString("mat_mjob"));
//                                noofsis.setText(feedObj.isNull("mat_nsis")?"-":feedObj.getString("mat_nsis"));
//                                noofbros.setText( feedObj.isNull("mat_nbro")?"-":feedObj.getString("mat_nbro"));
//                                othermember.setText( feedObj.isNull("mat_othermem")?"-":feedObj.getString("mat_othermem"));
                              String  star1=feedObj.isNull("mat_star")? "-":feedObj.getString("mat_star");
                                if (Arrays.asList(getResources().getStringArray(R.array.malayalamstar)).contains(star1)) {
                                    star.setSelection(Arrays.asList(getResources().getStringArray(R.array.malayalamstar)).indexOf(star1));
                                }
                               String jathakam=feedObj.isNull("mat_jathakam")?"-":feedObj.getString("mat_jathakam");
                                if (Arrays.asList(getResources().getStringArray(R.array.typeofjathakam)).contains(jathakam)) {
                                    typeofjathakam.setSelection(Arrays.asList(getResources().getStringArray(R.array.typeofjathakam)).indexOf(jathakam));
                                }
                                birthplace.setText(feedObj.isNull("mat_rasi")?"":feedObj.getString("mat_rasi"));
                                dobinmalayalam.setText(feedObj.isNull("mat_dobmal")?"":feedObj.getString("mat_dobmal"));
                                birthtime.setText( feedObj.isNull("mat_btime")?"-":feedObj.getString("mat_btime"));
//                                prefage.setText( feedObj.isNull("mat_agebw")?"-":feedObj.getString("mat_agebw"));
//                                prefheight.setText(feedObj.isNull("mat_heightbw")? "-":feedObj.getString("mat_heightbw"));
//                                prefjob.setText(feedObj.isNull("mat_prejob")?"-":feedObj.getString("mat_prejob"));
                                pWorklocation.setText(feedObj.isNull("mat_preworkl")?"-":feedObj.getString("mat_preworkl"));
                               String matchingstar=feedObj.isNull("mat_mstar")?"-":feedObj.getString("mat_mstar");
                                if (Arrays.asList(getResources().getStringArray(R.array.matchingstar)).contains(matchingstar)) {
                                    pMatchingStar.setSelection(Arrays.asList(getResources().getStringArray(R.array.matchingstar)).indexOf(matchingstar));
                                }
                                pNativelocation.setText( feedObj.isNull("mat_prelocation")?"-":feedObj.getString("mat_prelocation"));

                                String Pfamilystatus1=feedObj.isNull("mat_prefstatus")?"-":feedObj.getString("mat_prefstatus");
                                if (Arrays.asList(getResources().getStringArray(R.array.familystatus)).contains(Pfamilystatus1)) {
                                    Pfamilystatus.setSelection(Arrays.asList(getResources().getStringArray(R.array.familystatus)).indexOf(Pfamilystatus1));
                                }
                                concept.setText(feedObj.isNull("mat_exconcept")? "-":feedObj.getString("mat_exconcept"));
//                                profimage.setImageUrl(feedObj.isNull("image")?DatabaseInfo.MatrimonialImagePath+"default.jpg":DatabaseInfo.MatrimonialImagePath+feedObj.getString("image"),imageLoader);
//                                graganila.setImageUrl(feedObj.isNull("mat_grahanila")?DatabaseInfo.GrahanilaImagePath:DatabaseInfo.GrahanilaImagePath+feedObj.getString("mat_grahanila"),imageLoader);
                                String privacy=feedObj.isNull("privacy")? "":feedObj.getString("privacy");
                                if(privacy=="hide"){
                                    rgPrivacy.check(R.id.rb_matrimonyregistration_edit_yes);
                                }else {
                                    rgPrivacy.check(R.id.rb_matrimonyregistration_edit_no);
                                }




                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(MatrimonyRegistrationEdit.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mid", String.valueOf(matriid));
                params.put("uid", String.valueOf(uid));

//                Log.e("matri id",matriid);

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
    private void variableInitialize(){
        profilefor=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_profilefor);
        maritialstatus=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_maritialstatus);
        country=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_country);
        state=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_state);
        religion=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_religion);
        caste=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_caste);
        city=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_city);
        bodytype=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_bodytype);
        physicalstatus=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_physicalstatus);
        complexion=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_complexion);
        bloodgroup=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_bloodgroup);
        weight=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_weight);
        height=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_height);
        Ehabit=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_eatinghabit);
        Dhabit=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_drinkinghabit);
        Shabit=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_smokinghabit);
        employedin=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_eployedin);
        jobcategory=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_jobcategory);
        monthyincome=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_monthlyincome);
        qualification=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_qualification);
        famiytype=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_familytype);
        familystatus=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_familystatus);
        star=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_star);
        typeofjathakam=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_typeofjathakam);
        rasi=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_typeofrasi);
        agebetweento=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_agebetweento);
        agebetweenfrom=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_agebetweenfrom);
        heightfrom=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_heightbetweenfrom);
        heightto=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_heightbetweento);
        Pmaritialstatus=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_partnermaritialstatus);
        Pfamilystatus=(Spinner)findViewById(R.id.sp_matrimonyregistration_edit_preferedfamilystatus);

        email=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_email);
        presentaddress=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_presentaddress);
        mobile=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_mobile);
        communicationaddress=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_communicationaddress);
        name=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_name);
        landline=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_landline);
        dob=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_dob);
        designation=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_designation);
        worklocation=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_worklocation);
        companyname=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_companyname);
        fieldofstudy=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_fieldofstudy);
        addiqualification=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_additionqualification);
        birthplace=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_birthplace);
        dobinmalayalam=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_dobinmalayalam);
        birthtime=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_birthtime);
        pWorklocation=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_partenrworklocation);
        pNativelocation=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_nativelocation);
        concept=(MaterialEditText)findViewById(R.id.et_matrimonyregistration_edit_concept);

        rgGender=(RadioGroup)findViewById(R.id.rg_matrimonyregistration_edit_gender);
        rgPrivacy=(RadioGroup)findViewById(R.id.rg_matrimonyregistration_edit_modeofprovacy);

        cbFather=(CheckBox)findViewById(R.id.cb_matrimonyregistration_edit_father);
        cbMother=(CheckBox)findViewById(R.id.cb_matrimonyregistration_edit_mother);
        cbBrother=(CheckBox)findViewById(R.id.cb_matrimonyregistration_edit_brother);
        cbSister=(CheckBox)findViewById(R.id.cb_matrimonyregistration_edit_sister);
        cbOther=(CheckBox)findViewById(R.id.cb_matrimonyregistration_edit_others);

        save=(Button)findViewById(R.id.btn_matrimonyregistration_edit_save);
        uploadGrahanila=(Button)findViewById(R.id.btn_matrimonyregistration_edit_uploadgrahanila);

        pJobPosition=(MultiSelectSpinner)findViewById(R.id.sp_matrimonyregistration_edit_jobposition);
        pMatchingStar=(MultiSelectSpinner)findViewById(R.id.sp_matrimonyregistration_edit_matchingstar);


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
                        Toast.makeText(MatrimonyRegistrationEdit.this,error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MatrimonyRegistrationEdit.this,error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MatrimonyRegistrationEdit.this, error.toString(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MatrimonyRegistrationEdit.this,error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MatrimonyRegistrationEdit.this,error.toString(), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MatrimonyRegistrationEdit.this,error.toString(), Toast.LENGTH_LONG).show();
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
    public void getQualificationCode(final Spinner qualifi){
//        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetJobQualificationURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONArray jArray;
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
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
                                    qualification2.add(i,sname);




                                }


                            } catch (Exception e) {
                            }
                            qualification2.add(0,"Select Qualification");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,qualification2);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            qualifi.setAdapter(spinnerAdapter);
                            qualifi.setSelection(0);
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
                        Toast.makeText(MatrimonyRegistrationEdit.this,error.toString(), Toast.LENGTH_LONG).show();
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
    private void setDateofbirth() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                dob.setText(dayOfMonth + "/" + month + "/" + year);
                day1=String.valueOf(dayOfMonth);
                month1=String.valueOf(monthOfYear);
                year1=String.valueOf(year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }
    private void setAgebetween() {
        String above40 = "Above 40";

        for (int i = 18; i <= 41; i++) {

            if (i >40) {
                agebetween.add(String.valueOf(above40));
            } else {
                agebetween.add(String.valueOf(i));
            }
        }
        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,agebetween);
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_iltem);
        agebetweenfrom.setAdapter(spinnerAdapter1);
        agebetweento.setAdapter(spinnerAdapter1);
    }
    private void setHeightbetween() {



        for (int i = 120; i <= 230; i++) {

            heightbetween.add(String.valueOf(i)+" cm");

        }
        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,heightbetween);
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_iltem);
        heightfrom.setAdapter(spinnerAdapter1);
        heightto.setAdapter(spinnerAdapter1);
        height.setAdapter(spinnerAdapter1);
    }
    private void setWeight() {
        String above100 = "Above 100 Kg";
        String selectwght="Select Weight";
        List<String> weight1=new ArrayList<String>();

        weight1.add(selectwght);
        weightlist.add(selectwght);
        for (int i = 40; i <= 100; i++) {

            if (i >100) {
                weightlist.add(String.valueOf(above100)+" Kg");
                weight1.add(String.valueOf(above100)+" Kg");
            } else {
                weightlist.add(String.valueOf(i)+" Kg");
                weight1.add(String.valueOf(i)+" Kg");
            }
        }
        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,weight1);
        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_iltem);
        weight.setAdapter(spinnerAdapter1);
    }
    private void setHeight() {
        String selectwght="Select Height";

        List<String> height1=new ArrayList<String>();
        height1.add(selectwght);
        heightllist.add(selectwght);
        for (int i = 120; i <= 230; i++) {

            height1.add(String.valueOf(i)+" cm");
            heightllist.add(String.valueOf(i)+" cm");

        }
        ArrayAdapter<String> spinnerAdapter1 =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,height1);

        height.setAdapter(spinnerAdapter1);
    }
    private void setUpdate() {

        String profilefor1=profilefor.getSelectedItem().toString();
        String maritialstatus1=validateSp(maritialstatus);
        String country1 =validateSp(country);
        String  state1=validateSp(state);
        String religion1  =validateSp(religion);
        String  caste1=validateSp(caste);
        String city1 =validateSp(city);
        String bodytype1  =validateSp(bodytype);
        String bloodgroup1=validateSp(bloodgroup);
        String  weight1=validateSp(weight);
        String height1=validateSp(height);
        String Ehabit1=validateSp(Ehabit);
        String Dhabit1 =validateSp(Dhabit);
        String Shabit1 =validateSp(Shabit);
        String complexion1=validateSp(complexion);
        String employedin1 =validateSp(employedin);
        String jobcategory1 =validateSp(jobcategory);
        String monthyincome1 =validateSp(monthyincome);
        String qualification1  =validateSp(qualification);
        String  famiytype1=validateSp(famiytype);
        String familystatus1 =validateSp(familystatus);
        String star1 =validateSp(star);
        String typeofjathakam1 =validateSp(typeofjathakam);
        String rasi1 =validateSp(rasi);
        String  agebetweento1=validateSp(agebetweento);
        String  agebetweenfrom1=validateSp(agebetweenfrom);
        String heightfrom1 =validateSp(heightfrom);
        String heightto1 =validateSp(heightto);
        String  Pmaritialstatus1=validateSp(Pmaritialstatus);
        String  Pfamilystatus1=validateSp(Pfamilystatus);

        String email1 =validateEtMamdatory(email);
        String presentaddress1 =validateEtMamdatory(presentaddress);
        String mobile1 =validateEtMamdatory(mobile);
        String communicationaddress1 =validateEtMamdatory(communicationaddress);
        String  name1=validateEtMamdatory(name);
        String landline1 =validateEtMamdatory(landline);
        String dob1 =validateEtMamdatory(dob);
        String designation1 =validateEtMamdatory(designation);
        String  worklocation1=validateEtMamdatory(worklocation);
        String  companyname1=validateEtMamdatory(companyname);
        String  fieldofstudy1=validateEtMamdatory(fieldofstudy);
        String addiqualification1 =validateEtMamdatory(addiqualification);
        String  birthplace1=validateEtMamdatory(birthplace);
        String  dobinmalayalam1=validateEtMamdatory(dobinmalayalam);
        String  birthtime1=validateEtMamdatory(birthtime);
        String pWorklocation1=validateEtMamdatory(pWorklocation);
        String  pNativelocation1=validateEtMamdatory(pNativelocation);
        String concept1 =validateEtMamdatory(concept);
        String mfather="";
        String mfjob="";
        String mmother="";
        String mmjob="";
        String mns="";
        String motherfam="";
        String mnb="";
        String ageBetweenFromTo1=agebetweenfrom1+"-"+agebetweento1;
        String heightBetweenFromTo1=heightfrom1+"-"+heightto1;
        String rgGender1="";
        if(rgGender.getCheckedRadioButtonId()==R.id.rb_matrimonyregistration_male||
                rgGender.getCheckedRadioButtonId()==R.id.rb_matrimonyregistration_female){
            rgGender1=((RadioButton)findViewById( rgGender.getCheckedRadioButtonId())).getText().toString();
        }else rgGender1="";
        String rgPrivacy1="";
        if(rgPrivacy.getCheckedRadioButtonId()==R.id.rb_matrimonyregistration_no||
                rgPrivacy.getCheckedRadioButtonId()==R.id.rb_matrimonyregistration_yes){
            rgPrivacy1 =((RadioButton)findViewById(rgPrivacy.getCheckedRadioButtonId())).getText().toString();
        }else {rgPrivacy1="";}
        String msdetiails="";
        String privacy1;
        if(rgPrivacy1.equals("Yes")){privacy1="hide";}else{privacy1="";}
        String familymember="";
        if(cbFather.isChecked()){familymember+=cbFather.getText().toString()+",";}
        if(cbMother.isChecked()){familymember+=cbMother.getText().toString()+",";}
        if(cbBrother.isChecked()){familymember+=cbBrother.getText().toString()+",";}
        if(cbSister.isChecked()){familymember+=cbSister.getText().toString()+",";}
        if(cbOther.isChecked()){familymember+=cbOther.getText().toString()+",";}

        String pJobPosition1=pJobPosition.getSelectedItemsAsString();
        String pMachingstar1=pMatchingStar.getSelectedItemsAsString();
        country_code="0";
        state_code="0";
        city_code="0";

        if(country.getSelectedItemPosition()!=0){
            country_code=String.valueOf(countrycodid.get(country.getSelectedItemPosition()));
        }else{country_code="0";}
        if(state.getCount()==0){
            state_code="0";
        }
        else {if(state.getSelectedItemPosition()!=0){
            state_code=String.valueOf(statecodeid.get(state.getSelectedItemPosition()));}
        else {state_code="0";}
        }

        if(city.getCount()==0) {
            city_code = "0";
        }else{
            if (city.getSelectedItemPosition() != 0) {
                city_code = String.valueOf(citycodeid.get(city.getSelectedItemPosition()));
            } else {
                city_code = "0";
            }
        }
//        byte[] newbyteFile= new byte[10000];
        if(resumepath==""){
            RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;
            params.put("mprofile", profilefor1);
            params.put("memail", email1);
            params.put("mname", name1);
            params.put("mgender", rgGender1);
            params.put("mdob", dob1);
            params.put("mreligion", religion1);
            params.put("mcaste", caste1);
            params.put("mstatus", maritialstatus1);
            params.put("mcomplex", complexion1);
            params.put("mjob", jobcategory1);
            params.put("mpstatus", Pmaritialstatus1);
            params.put("mcompany", companyname1);
            params.put("maddqual", addiqualification1);
            params.put("mbdetail", birthplace1);
            params.put("msdetail", msdetiails);
            params.put("mdobtime", birthtime1);

            params.put("mcountry", country_code);
            params.put("mstate", state_code);
            params.put("mlocation", city_code);
            params.put("mhide", privacy1);
            params.put("mmobile", mobile1);
            params.put("mland", landline1);
            params.put("mpaddress", presentaddress1);
            params.put("mcaddress", communicationaddress1);
            params.put("mbtype", bodytype1);
            params.put("mblood", bloodgroup1);
            params.put("mweight", weight1);
            params.put("mheight", height1);
            params.put("meat", Ehabit1);
            params.put("mdrink", Dhabit1);
            params.put("msmoke", Shabit1);
            params.put("memp", employedin1);
            params.put("mdesi", designation1);
            params.put("mwork", worklocation1);
            params.put("msalary", monthyincome1);
            params.put("mdegree", qualification1);
            params.put("medu", fieldofstudy1);
            params.put("mftype", famiytype1);
            params.put("mfstatus", familystatus1);
            params.put("fam", familymember);
            params.put("mfather", mfather);
            params.put("mfjob", mfjob);
            params.put("mmother", mmother);
            params.put("mmjob", mmjob);
            params.put("mnb", mnb);
            params.put("mns", mns);
            params.put("motherfam", motherfam);
            params.put("mstar", star1);
            params.put("mjathakm", typeofjathakam1);
            params.put("mrasi", rasi1);
            params.put("mbirth", birthplace1);
            params.put("mdobmal", dobinmalayalam1);
            params.put("fname", resumepath);
            params.put("magebw", ageBetweenFromTo1);
            params.put("mheightbw", heightBetweenFromTo1);
            params.put("prferdjob", pJobPosition1);
            params.put("prferwork", pWorklocation1);
            params.put("matchstar", pMachingstar1);
            params.put("mmstatus", Pmaritialstatus1);
            params.put("perloc", pNativelocation1);
            params.put("mpfstatus", Pfamilystatus1);
            params.put("mexcon", concept1);
            params.put("profileid", profileid);
            params.put("mid", matriid);

            // params.put("uid", Uid_from_sharedpref);
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(DatabaseInfo.MatrimonyUpdateURL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
//                addImageView(inHorizontalScrollView);
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                    byte[] bite = responseBody;
                    try {
                        final String response = new String(bite, "UTF-8");
                        System.out.println("response " + response);
//                    stsimgsid[imgcoutpos]=response;
//                    String tempcmid=response;
//                    String [] Commnt1 = Constants.Commentid;
//                    String[] temp =new String[1];
//                    temp[0]=tempcmid;
//                    String [] comd2=ArrayUtils.addAll(Commnt1,temp);
//                    Constants.Commentid=comd2;
//
//                    String [] Commntlike = Constants.CommentLikeCount;
//                    String[] temp1 =new String[1];
//
//                    temp1[0]="0";
//                    String [] comdlike=ArrayUtils.addAll(Commntlike,temp1);
//                    Constants.CommentLikeCount=comdlike;
                        Log.e("image response", response);
                        boolean containerContainsContent = StringUtils.containsIgnoreCase(response, "Record Updated suceessfully");
                        if(containerContainsContent) Toast.makeText(getApplicationContext(), "Record Updated suceessfully", Toast.LENGTH_LONG).show();
//                    imageUploadid=imageUploadid.concat(response.trim()+",");
//                    Toast.makeText(getApplicationContext(),String.valueOf(imageUploadid),Toast.LENGTH_SHORT).show();
//                    Log.e("image names",String.valueOf(response));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


//                Log.e("file path", file);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("file not send", "failure code " + error);

//                imageupdateprogress.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Grahanila Selection Unsuccessfull!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
//             imageupdateprogress.setVisibility(View.GONE);
                }

            });
        }else {
            File photo = new File(resumepath);

//        File resume=new File(resumepath);
////        Log.e("resumepath",resumepath);
            RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;
            try {
                params.put("mprofile", profilefor1);
                params.put("memail", email1);
                params.put("mname", name1);
                params.put("mgender", rgGender1);
                params.put("mdob", dob1);
                params.put("mreligion", religion1);
                params.put("mcaste", caste1);
                params.put("mstatus", maritialstatus1);
                params.put("mcomplex", complexion1);
                params.put("mjob", jobcategory1);
                params.put("mpstatus", Pmaritialstatus1);
                params.put("mcompany", companyname1);
                params.put("maddqual", addiqualification1);
                params.put("mbdetail", birthplace1);
                params.put("msdetail", msdetiails);
                params.put("mdobtime", birthtime1);

                params.put("mcountry", country_code);
                params.put("mstate", state_code);
                params.put("mlocation", city_code);
                params.put("mhide", privacy1);
                params.put("mmobile", mobile1);
                params.put("mland", landline1);
                params.put("mpaddress", presentaddress1);
                params.put("mcaddress", communicationaddress1);
                params.put("mbtype", bodytype1);
                params.put("mblood", bloodgroup1);
                params.put("mweight", weight1);
                params.put("mheight", height1);
                params.put("meat", Ehabit1);
                params.put("mdrink", Dhabit1);
                params.put("msmoke", Shabit1);
                params.put("memp", employedin1);
                params.put("mdesi", designation1);
                params.put("mwork", worklocation1);
                params.put("msalary", monthyincome1);
                params.put("mdegree", qualification1);
                params.put("medu", fieldofstudy1);
                params.put("mftype", famiytype1);
                params.put("mfstatus", familystatus1);
                params.put("fam", familymember);
                params.put("mfather", mfather);
                params.put("mfjob", mfjob);
                params.put("mmother", mmother);
                params.put("mmjob", mmjob);
                params.put("mnb", mnb);
                params.put("mns", mns);
                params.put("motherfam", motherfam);
                params.put("mstar", star1);
                params.put("mjathakm", typeofjathakam1);
                params.put("mrasi", rasi1);
                params.put("mbirth", birthplace1);
                params.put("mdobmal", dobinmalayalam1);
                params.put("fname", photo);
                params.put("magebw", ageBetweenFromTo1);
                params.put("mheightbw", heightBetweenFromTo1);
                params.put("prferdjob", pJobPosition1);
                params.put("prferwork", pWorklocation1);
                params.put("matchstar", pMachingstar1);
                params.put("mmstatus", Pmaritialstatus1);
                params.put("perloc", pNativelocation1);
                params.put("mpfstatus", Pfamilystatus1);
                params.put("mexcon", concept1);
                params.put("profileid", profileid);
                params.put("mid", matriid);

                // params.put("uid", Uid_from_sharedpref);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(DatabaseInfo.MatrimonyUpdateURL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
//                addImageView(inHorizontalScrollView);
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                    byte[] bite = responseBody;
                    try {
                        final String response = new String(bite, "UTF-8");
                        System.out.println("response " + response);
//                    stsimgsid[imgcoutpos]=response;
//                    String tempcmid=response;
//                    String [] Commnt1 = Constants.Commentid;
//                    String[] temp =new String[1];
//                    temp[0]=tempcmid;
//                    String [] comd2=ArrayUtils.addAll(Commnt1,temp);
//                    Constants.Commentid=comd2;
//
//                    String [] Commntlike = Constants.CommentLikeCount;
//                    String[] temp1 =new String[1];
//
//                    temp1[0]="0";
//                    String [] comdlike=ArrayUtils.addAll(Commntlike,temp1);
//                    Constants.CommentLikeCount=comdlike;
                        Log.e("image response", response);
                        boolean containerContainsContent = StringUtils.containsIgnoreCase(response, "Record Updated suceessfully");
                        if(containerContainsContent) Toast.makeText(getApplicationContext(), "Record Updated suceessfully", Toast.LENGTH_LONG).show();
//                   //                    imageUploadid=imageUploadid.concat(response.trim()+",");
//                    Toast.makeText(getApplicationContext(),String.valueOf(imageUploadid),Toast.LENGTH_SHORT).show();
//                    Log.e("image names",String.valueOf(response));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


//                Log.e("file path", file);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("file not send", "failure code " + error);

//                imageupdateprogress.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Grahanila Selection Unsuccessfull!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
//             imageupdateprogress.setVisibility(View.GONE);
                }

            });
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
            // returncheck= spinner.getSelectedItem().toString();

        }else {
            returncheck="";
        }
        return returncheck;
    }
    private String validatemultispinner(MultiSelectSpinner spinner){
        String returncheck="";
        if(spinner.getSelectedItemsAsString().length()<1){
            returncheck="";
        }else {
            returncheck=spinner.getSelectedItemsAsString();
        }

        return  returncheck;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

//            Uri uri = data.getData();
            Uri uri = data.getData();
            resumepath= FilePath.getPath(this,uri);
//            photopath=resumepath;
            Toast.makeText(getApplicationContext(),resumepath,Toast.LENGTH_SHORT).show();
            Log.e("file",resumepath);
        }
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri uri = data.getData();
            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
//                Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String picturePath = cursor.getString(columnIndex); // returns null
            cursor.close();
            photopath=picturePath;
        }
    }
}

