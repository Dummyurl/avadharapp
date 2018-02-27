package com.avadharwebworld.avadhar.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.avadharwebworld.avadhar.R.string.subcategory;

public class JobPostResume extends AppCompatActivity {
    List<String> qualification2=new ArrayList<String>();
    List<String> university2=new ArrayList<String>();
    List<String>experience2=new ArrayList<String>();
    List<String>jobrole2=new ArrayList<String>();
    String profileid,day1="0",month1="0",year1="0",resumepath="",photopath="",resumepath1="",photopath1="",uid;
    private SharedPreferences sp;
    private MaterialEditText name, address, dateofbirth,location,number,email,resumeheadline,yearopass,preflocation,keyskill,workhistory,otherhistory;
    RadioGroup jobtype,modeofstudy,rgGender,maritialstatus;
    Spinner nationality,interestedfield,experience,expsalary,qualification,university;
    RadioButton jobtypefulltime,jobtypeparttime,jobtypedistance,modeofstudyfulltime,modeofstudyparttime,modeofstudydistance,male,female,single,married;
    Button submit,uploadresume,uploadphoto;
    CheckBox malayalam,english,tamil,other,hindi;
    MultiSelectSpinner jobrole;
    String jobprofileid="";
    private int day, month, year,parentid=0;
    private int[] layouts;
    private Calendar cal;
    private Button next;
    private ViewPager jobviewpager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Jobs objJobs=new Jobs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_resume);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.postresume);

        variableInitialize();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        profileid=sp.getString(Constants.PROFILEID,"");
        uid=sp.getString(Constants.UID,"");
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateofbirth.setText(R.string.dateofbirth);
        dateofbirth.setFocusableInTouchMode(false);
        dateofbirth.setFocusable(false);
        layouts = new int[]{R.layout.job_post_resume_section1,R.layout.job_post_resume_section2,R.layout.job_post_resume_section3};
        getinterestedfieldCode(interestedfield);

        getQualificationCode(qualification);

        getUniversityCode(university);

        interestedfield.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id!=0) {
                    getjobroleCode(jobrole, String.valueOf(id));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        myViewPagerAdapter = new MyViewPagerAdapter();
//        jobviewpager.setAdapter(myViewPagerAdapter);
//        jobviewpager.addOnPageChangeListener(viewPagerPageChangeListener);

        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateofbirth();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (jobprofileid.equals("")) {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        email.setError("Please fill this field!!!");
                        email.requestFocus();
                    } else if ((number.getText().toString().length() != 10)) {
                        number.setError("Mobile number must have 10 digits");
                        number.requestFocus();
                    } else if ((name.getText().toString().length() < 1)) {
                        name.setError("Please fill this field!!!");
                        name.requestFocus();
                    } else if ((dateofbirth.getText().toString().equals(R.string.dateofbirth))) {
                        dateofbirth.setError("Please fill this field!!!");
                        dateofbirth.requestFocus();
                    } else if (interestedfield.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Select Interested Field", Toast.LENGTH_SHORT).show();
                    } else if (rgGender.getCheckedRadioButtonId() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                    } else if (modeofstudy.getCheckedRadioButtonId() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Select Mode Of Study", Toast.LENGTH_SHORT).show();
                    } else {
                        PostResume();
                    }

                } else {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                        email.setError("Please fill this field!!!");
                        email.requestFocus();
                    } else if ((number.getText().toString().length() != 10)) {
                        number.setError("Mobile number must have 10 digits");
                        number.requestFocus();
                    } else if ((name.getText().toString().length() < 1)) {
                        name.setError("Please fill this field!!!");
                        name.requestFocus();
                    } else if ((dateofbirth.getText().toString().equals(R.string.dateofbirth))) {
                        dateofbirth.setError("Please fill this field!!!");
                        dateofbirth.requestFocus();
                    } else if (interestedfield.getSelectedItemPosition() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Select Interested Field", Toast.LENGTH_SHORT).show();
                    } else if (rgGender.getCheckedRadioButtonId() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                    } else if (modeofstudy.getCheckedRadioButtonId() == 0) {
                        Toast.makeText(getApplicationContext(), "Please Select Mode Of Study", Toast.LENGTH_SHORT).show();
                    } else {
                        updateResume();
                    }
                }
            }
        });
        uploadresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });
        uploadphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });

        getProfileid();

    }

    private int getItem(int i) {
        return jobviewpager.getCurrentItem() + i;
    }

    private void setDateofbirth() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                dateofbirth.setText(dayOfMonth + "/" + month + "/" + year);
                day1=String.valueOf(dayOfMonth);
                month1=String.valueOf(monthOfYear);
                year1=String.valueOf(year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                next.setText(getString(R.string.next));

            } else {
                // still pages are left

            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void postResume(){
        String name1=name.getText().toString();
        final String address1=address.getText().toString();
        String dob=dateofbirth.getText().toString();
        final String location1=location.getText().toString();
        final String number1=number.getText().toString();
        final String email1=email.getText().toString();
        final String resumeheadline1=resumeheadline.getText().toString();
        final String preflocation1=preflocation.getText().toString();
        final String keyskill1=keyskill.getText().toString();
        final String workhistory1=workhistory.getText().toString();
        final String otherhistory1=otherhistory.getText().toString();
        final String jobtype1=((RadioButton)findViewById(jobtype.getCheckedRadioButtonId())).getText().toString();
        final String modeofstudy1=((RadioButton)findViewById(modeofstudy.getCheckedRadioButtonId())).getText().toString();
        final String rgGender1=((RadioButton)findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
        final String maritialstatus1=((RadioButton)findViewById(maritialstatus.getCheckedRadioButtonId())).getText().toString();
        final String nationality1=nationality.getSelectedItem().toString();
        final String interestedfield1=interestedfield.getSelectedItem().toString();
        final String expectedsalary1=expsalary.getSelectedItem().toString();
        final String qualification1=qualification.getSelectedItem().toString();
        final String yearofpass1=yearopass.getText().toString();
        final String experience1=experience.getSelectedItem().toString();
        final String university1=university.getSelectedItem().toString();
        final String jobrole1=jobrole.getSelectedItemsAsString().toString();
        String languageknown="";
        if(malayalam.isChecked()){languageknown+=malayalam.getText().toString()+",";}
        if(tamil.isChecked()){languageknown+=tamil.getText().toString()+",";}
        if(hindi.isChecked()){languageknown+=hindi.getText().toString()+",";}
        if(english.isChecked()){languageknown+=english.getText().toString()+",";}
        if(other.isChecked()){languageknown+=other.getText().toString()+",";}
        Toast.makeText(getApplicationContext(),"radio button selected "+jobtype1,Toast.LENGTH_SHORT).show();

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);
        final String finalLanguageknown = languageknown;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.PostResumeURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
//                        try {
////                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
//                            Log.e("jobnect", String.valueOf(Jobject));
//                            jobFeed(Jobject);
                            pDialog.hide();
//                            if(popupWindow!=null)
//                            {popupWindow.dismiss();}
//                        } catch (JSONException e) {
//
//                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        // dialog.hide();
                        Toast.makeText(JobPostResume.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("profileid",profileid);
                params.put("name1",profileid);
                params.put("address",address1);
                params.put("day",day1);
                params.put("month",month1);
                params.put("year",year1);
                params.put("location",location1);
                params.put("number",number1);
                params.put("email",email1);
                params.put("resumeheadline",resumeheadline1);
                params.put("preflocation",preflocation1);
                params.put("keyskill",keyskill1);
                params.put("experience",experience1);
                params.put("workhistory",workhistory1);
                params.put("otherhistory",otherhistory1);
                params.put("jobtype",jobtype1);
                params.put("modeofstudy",modeofstudy1);
                params.put("rgGender",rgGender1);
                params.put("maritialstatus",maritialstatus1);
                params.put("nationality",nationality1);
                params.put("interestedfield",interestedfield1);
                params.put("expsalary",expectedsalary1);
                params.put("language", finalLanguageknown);
                params.put("qualification",qualification1);
                params.put("studymode",modeofstudy1);
                params.put("yearofpass",yearofpass1);
                params.put("university",university1);
                params.put("jobrole",jobrole1);
                params.put("photo","");
                params.put("rfile","");
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

        name=(MaterialEditText)findViewById(R.id.et_jobpost_name);
        address=(MaterialEditText)findViewById(R.id.et_jobpost_address);
        dateofbirth=(MaterialEditText)findViewById(R.id.et_jobpost_dob);
        location=(MaterialEditText)findViewById(R.id.et_jobpost_currentlocation);
        number=(MaterialEditText)findViewById(R.id.et_jobpost_mobile);
        email=(MaterialEditText)findViewById(R.id.et_jobpost_email);
        resumeheadline=(MaterialEditText)findViewById(R.id.et_jobpost_resumeheadline);
        preflocation=(MaterialEditText)findViewById(R.id.et_jobpost_prefjob);
        keyskill=(MaterialEditText)findViewById(R.id.et_jobpost_keyskill);
        workhistory=(MaterialEditText)findViewById(R.id.et_jobpost_workhistory);
        otherhistory=(MaterialEditText)findViewById(R.id.et_jobpost_otherdetails);
        jobtype=(RadioGroup)findViewById(R.id.rg_jobpost_jobtype);
        modeofstudy=(RadioGroup)findViewById(R.id.rg_jobpost_modeofstudy);
        rgGender=(RadioGroup)findViewById(R.id.rg_jobpost_gender);
        maritialstatus=(RadioGroup) findViewById(R.id.rg_jobpost_mStatus);
        nationality=(Spinner)findViewById(R.id.sp_jobpost_nationality);
        interestedfield=(Spinner)findViewById(R.id.sp_jobpost_interestedfield);
        experience=(Spinner)findViewById(R.id.sp_jobpost_experiance);
        expsalary=(Spinner)findViewById(R.id.sp_jobpost_expectedsalary);
        qualification=(Spinner)findViewById(R.id.sp_jobpost_qualification);
        yearopass=(MaterialEditText) findViewById(R.id.et_jobpost_yearoupassout);
        university=(Spinner)findViewById(R.id.sp_jobpost_university);
        jobtypedistance=(RadioButton)findViewById(R.id.rb_jobpost_distance);
        jobtypeparttime=(RadioButton)findViewById(R.id.rb_jobpost_parttime);
        jobtypefulltime=(RadioButton)findViewById(R.id.rb_jobpost_fulltime);
        modeofstudyfulltime=(RadioButton)findViewById(R.id.rb_jobpost_modefulltime);
        modeofstudyparttime=(RadioButton)findViewById(R.id.rb_jobpost_modeparttime);
        modeofstudydistance=(RadioButton)findViewById(R.id.rb_jobpost_modedistance);
        male=(RadioButton)findViewById(R.id.rb_jobpost_male);
        female=(RadioButton)findViewById(R.id.rb_jobpost_female);
        single=(RadioButton)findViewById(R.id.rb_jobpost_single);
        married=(RadioButton)findViewById(R.id.rb_jobpost_married);
        submit=(Button)findViewById(R.id.btn_jobpost_submit);
        uploadphoto=(Button)findViewById(R.id.btn_jobpost_uploadphoto);
        uploadresume=(Button)findViewById(R.id.btn_jobpost_uploadresume);
        jobrole=(MultiSelectSpinner)findViewById(R.id.sp_jobpost_jobrole);
        dateofbirth = (MaterialEditText) findViewById(R.id.et_jobpost_dob);
        jobviewpager = (ViewPager) findViewById(R.id.jobpost_view_pager);
        malayalam=(CheckBox)findViewById(R.id.cb_jobpost_malayalam);
        english=(CheckBox)findViewById(R.id.cb_jobpost_english);
        tamil=(CheckBox)findViewById(R.id.cb_jobpost_tamil);
        hindi=(CheckBox)findViewById(R.id.cb_jobpost_hindi);
        other=(CheckBox)findViewById(R.id.cb_jobpost_others);

    }
    private void PostResume() {
        String jobtype1="";
        String rgGender1="";
        String modeofstudy1="";
        String maritialstatus1="";
        String jobrole1="";
        String name1=name.getText().toString();
        final String address1=address.getText().toString();
        String dob=dateofbirth.getText().toString();
        final String location1=location.getText().toString();
        final String number1=number.getText().toString();
        final String email1=email.getText().toString();
        final String resumeheadline1=resumeheadline.getText().toString();
        final String preflocation1=preflocation.getText().toString();
        final String keyskill1=keyskill.getText().toString();
        final String workhistory1=workhistory.getText().toString();
        final String otherhistory1=otherhistory.getText().toString();
        if(jobtype.getCheckedRadioButtonId()!=-1){
            jobtype1=((RadioButton)findViewById(jobtype.getCheckedRadioButtonId())).getText().toString();
        }
        if(rgGender.getCheckedRadioButtonId()!=-1){
             rgGender1=((RadioButton)findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
        }
        if(modeofstudy.getCheckedRadioButtonId()!=-1){
            modeofstudy1=((RadioButton)findViewById(modeofstudy.getCheckedRadioButtonId())).getText().toString();
        }
        if(maritialstatus.getCheckedRadioButtonId()!=-1){
          maritialstatus1=((RadioButton)findViewById(maritialstatus.getCheckedRadioButtonId())).getText().toString();
        }
        final String nationality1=validateSp(nationality);
        final String interestedfield1=String.valueOf(validateSpCode(interestedfield));
        final String expectedsalary1=validateSp(expsalary);
        final String qualification1=validateSp(qualification);
        final String yearofpass1=yearopass.getText().toString();
        final String experience1=validateSp(experience);
        final String university1=validateSp(university);
        if(interestedfield1!="0"){
            jobrole1=validateMSp(jobrole);
        }else {
            jobrole1="";

        }

        String languageknown="";
        if(malayalam.isChecked()){languageknown+=malayalam.getText().toString()+",";}
        if(tamil.isChecked()){languageknown+=tamil.getText().toString()+",";}
        if(hindi.isChecked()){languageknown+=hindi.getText().toString()+",";}
        if(english.isChecked()){languageknown+=english.getText().toString()+",";}
        if(other.isChecked()){languageknown+=other.getText().toString()+",";}

        byte[] newbyteFile= new byte[10000];
        File photo=new File(photopath);
        File resume=new File(resumepath);
//        Log.e("resumepath",resumepath);
        RequestParams params = new RequestParams();
        final String finalLanguageknown = languageknown;
        try {
            params.put("uid",uid);
            params.put("profileid",profileid);
            params.put("name",name1);
            params.put("address",address1);
            params.put("day",day1);
            Log.e("day",day1);
            params.put("month",month1);
            Log.e("month",month1);
            params.put("year",year1);
            Log.e("year",year1);
            params.put("location",location1);
            params.put("number",number1);
            params.put("email",email1);
            params.put("resumeheadline",resumeheadline1);
            params.put("preflocation",preflocation1);
            params.put("keyskill",keyskill1);
            params.put("experience",experience1);
            params.put("workhistory",workhistory1);
            params.put("otherhistory",otherhistory1);
            params.put("jobtype",jobtype1);
            params.put("modeofstudy",modeofstudy1);
            params.put("rgGender",rgGender1);
            params.put("maritialstatus",maritialstatus1);
            params.put("nationality",nationality1);
            params.put("interestedfield",interestedfield1);
            params.put("expsalary",expectedsalary1);
            params.put("language", finalLanguageknown);
            params.put("qualification",qualification1);
            params.put("studymode",modeofstudy1);
            params.put("yearofpass",yearofpass1);
            params.put("university",university1);
            params.put("jobrole",jobrole1);
            params.put("photo",  photo,"image/jpg");
            params.put("rfile",  resume);
           // params.put("uid", Uid_from_sharedpref);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(DatabaseInfo.PostResumeURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
//                addImageView(inHorizontalScrollView);
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                byte[] bite=responseBody;
                try {
                    final   String response=new String(bite,"UTF-8");

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
                    Log.e("image response",response);
                    Toast.makeText(getApplicationContext(),String.valueOf(response.trim()),Toast.LENGTH_LONG).show();
//                    imageUploadid=imageUploadid.concat(response.trim()+",");
//                    Toast.makeText(getApplicationContext(),String.valueOf(imageUploadid),Toast.LENGTH_SHORT).show();
//                    Log.e("image names",String.valueOf(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }



//                Log.e("file path", file);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("file not send","failure");
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Image Selection Unsuccessfull!!!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFinish() {
//             imageupdateprogress.setVisibility(View.GONE);
            }

        });

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
                        Toast.makeText(JobPostResume.this,error.toString(), Toast.LENGTH_LONG).show();
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
    public void getUniversityCode(final Spinner qualifi){
//        final ProgressDialog pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
//        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetUniversityURL,
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
                                    sname = json_data.getString("university");
                                    Log.e(sname, "got");
                                    Log.e("got",json_data.toString());
                                    Log.e(cid, "got");
                                    c1[i]=cid;
                                    c3[i]=sname;
                                    //  Log.e("list sizzeeeee",String.valueOf(countrycodename.size()+1));
                                    university2.add(i,sname);




                                }


                            } catch (Exception e) {
                            }
                            university2.add(0,"Select University");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,university2);
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
                        Toast.makeText(JobPostResume.this,error.toString(), Toast.LENGTH_LONG).show();
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
    public void getinterestedfieldCode(final Spinner ifield){
        final List<String>interestedfield2=new ArrayList<String>();
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
                            interestedfield2.add(0,"Select Interested Field");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,interestedfield2);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            ifield.setAdapter(spinnerAdapter);
                            ifield.setSelection(0);
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
                        Toast.makeText(JobPostResume.this,error.toString(), Toast.LENGTH_LONG).show();
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

    public void getjobroleCode(final MultiSelectSpinner qualifi, final String id){
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
                            JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
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
                                    jobrole2.add(i,sname);




                                }


                            } catch (Exception e) {
                            }
                            //  jobrole.add(0,"Select Job Role");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,jobrole2);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            // qualifi.setAdapter(spinnerAdapter);
                            qualifi.setItems(jobrole2);

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
                        Toast.makeText(JobPostResume.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("parentid", String.valueOf(id));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

//            Uri uri = data.getData();


            Uri uri = data.getData();

            resumepath= FilePath.getPath(this,uri);
            resumepath1= FilePath.getPath(this,uri);
//                Toast.makeText(getApplicationContext(),resumepath,Toast.LENGTH_SHORT).show();
            Constants.REALESTATEIMAGE=FilePath.getPath(this,uri);
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
            photopath1=picturePath;
            Constants.REALESTATEIMAGE=picturePath;


        }
    }


    private String getFileNameByUri(Context context, Uri uri)
     {
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().toString().compareTo("content") == 0)
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        }
        else
        if (uri.getScheme().compareTo("file") == 0)
        {
            try
            {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            }
            catch (URISyntaxException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            filepath = uri.getPath();
        }
        return filepath;
    }

    public void  getProfileid(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.JobResumeDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("job profile get",response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("jobresume");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);

                                jobprofileid=feedObj.isNull("profileid")? "":feedObj.getString("profileid");

                                String natil=feedObj.isNull("nationality")? "":feedObj.getString("nationality");
                                String[] enatil=getResources().getStringArray(R.array.Nationalitywithhelp);
                                for (int j = 0; j < enatil.length; j++) {if (enatil[j].contains(natil)) {nationality.setSelection(j);}}


                                String jmodestd=feedObj.isNull("studymode")? "":feedObj.getString("studymode");
                                if(jmodestd.equals("Full-time")){modeofstudy.check(R.id.rb_jobpost_modefulltime);}
                                else if(jmodestd.equals("Part-Time")){ modeofstudy.check(R.id.rb_jobpost_modeparttime);}
                                else if(jmodestd.equals("Distance")) {modeofstudy.check(R.id.rb_jobpost_modedistance);}

                                String jgender=feedObj.isNull("gender")? "":feedObj.getString("gender");
                                if(jgender.equals("Male")){rgGender.check(R.id.rb_jobpost_male);}
                                else if(jgender.equals("Female")){rgGender.check(R.id.rb_jobpost_female);}

                                String marig=feedObj.isNull("mstatus")? "":feedObj.getString("mstatus");
                                if(marig.equals("Single")){maritialstatus.check(R.id.rb_jobpost_single);}
                                else if(marig.equals("Married")) {maritialstatus.check(R.id.rb_jobpost_married);}

                                String jobtype1=feedObj.isNull("jobtype")? "":feedObj.getString("jobtype");
                                if(jobtype1.equals("Full-time")){jobtype.check(R.id.rb_jobpost_fulltime);}
                                else if(jobtype1.equals("Part-Time")){ jobtype.check(R.id.rb_jobpost_parttime);}
                                else if(jobtype1.equals("Distance")) {jobtype.check(R.id.rb_jobpost_distance);}

                                String expsal=  feedObj.isNull("salary")? "":feedObj.getString("salary");
                                String[] expsalr=getResources().getStringArray(R.array.expectedsalary);
                                for (int j = 0; j < expsalr.length; j++) {if (expsalr[j].contains(expsal)) {expsalary.setSelection(j);}}

                                String exp=   feedObj.isNull("experience")? "":feedObj.getString("experience");
                                String[] exp1=getResources().getStringArray(R.array.experience);
                                for (int j = 0; j < exp1.length; j++) {if (exp1[j].contains(exp)) {experience.setSelection(j);}}


                                yearopass.setText(feedObj.isNull("passyear")? "":feedObj.getString("passyear"));
                                keyskill.setText(feedObj.isNull("keyskills")? "":feedObj.getString("keyskills"));
                                resumeheadline.setText(feedObj.isNull("resumetitle")? "":feedObj.getString("resumetitle"));
                                location.setText(feedObj.isNull("location")? "":feedObj.getString("location"));
                                number.setText(feedObj.isNull("contactno")? "":feedObj.getString("contactno"));
                                String isdob=feedObj.isNull("dob")? "":feedObj.getString("dob");
                                if(!isdob.equals("")){
                                    dateofbirth.setText(feedObj.isNull("dob")? "":feedObj.getString("dob"));
                                }

                                email.setText(feedObj.isNull("email")? "":feedObj.getString("email"));
                                name.setText(feedObj.isNull("uname")? "":feedObj.getString("uname"));
                                otherhistory.setText(feedObj.isNull("otherdet")? "":feedObj.getString("otherdet"));
                                preflocation.setText(feedObj.isNull("joblocation")? "":feedObj.getString("joblocation"));

                            }
                        } catch (JSONException e) {

                        }


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

                params.put("id",String.valueOf(profileid));



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

    private void updateResume() {

        String jobtype1="";
        String rgGender1="";
        String modeofstudy1="";
        String maritialstatus1="";
        String jobrole1="";
        String name1=name.getText().toString();
        final String address1=address.getText().toString();
        String dob=dateofbirth.getText().toString();
        final String location1=location.getText().toString();
        final String number1=number.getText().toString();
        final String email1=email.getText().toString();
        final String resumeheadline1=resumeheadline.getText().toString();
        final String preflocation1=preflocation.getText().toString();
        final String keyskill1=keyskill.getText().toString();
        final String workhistory1=workhistory.getText().toString();
        final String otherhistory1=otherhistory.getText().toString();
        if(jobtype.getCheckedRadioButtonId()!=-1){
            jobtype1=((RadioButton)findViewById(jobtype.getCheckedRadioButtonId())).getText().toString();
        }
        if(rgGender.getCheckedRadioButtonId()!=-1){
            rgGender1=((RadioButton)findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
        }
        if(modeofstudy.getCheckedRadioButtonId()!=-1){
            modeofstudy1=((RadioButton)findViewById(modeofstudy.getCheckedRadioButtonId())).getText().toString();
        }
        if(maritialstatus.getCheckedRadioButtonId()!=-1){
            maritialstatus1=((RadioButton)findViewById(maritialstatus.getCheckedRadioButtonId())).getText().toString();
        }
        final String nationality1=validateSp(nationality);
        final String interestedfield1=String.valueOf(validateSpCode(interestedfield));
        final String expectedsalary1=validateSp(expsalary);
        final String qualification1=validateSp(qualification);
        final String yearofpass1=yearopass.getText().toString();
        final String experience1=validateSp(experience);
        final String university1=validateSp(university);
        if(interestedfield1!="0"){
            jobrole1=validateMSp(jobrole);
        }else {
            jobrole1="";

        }

        String languageknown="";
        if(malayalam.isChecked()){languageknown+=malayalam.getText().toString()+",";}
        if(tamil.isChecked()){languageknown+=tamil.getText().toString()+",";}
        if(hindi.isChecked()){languageknown+=hindi.getText().toString()+",";}
        if(english.isChecked()){languageknown+=english.getText().toString()+",";}
        if(other.isChecked()){languageknown+=other.getText().toString()+",";}

        byte[] newbyteFile= new byte[10000];

        File photo=new File(photopath);
        File resume=new File(resumepath);
//        Log.e("resumepath",resumepath);
        RequestParams params = new RequestParams();
        try {
        final String finalLanguageknown = languageknown;
        params.put("uid",uid);
        params.put("profileid",profileid);
        params.put("name",name1);
        params.put("address",address1);
        params.put("day",day1);
        params.put("month",month1);
        params.put("year",year1);
        params.put("location",location1);
        params.put("number",number1);
        params.put("email",email1);
        params.put("resumeheadline",resumeheadline1);
        params.put("preflocation",preflocation1);
        params.put("keyskill",keyskill1);
        params.put("experience",experience1);
        params.put("workhistory",workhistory1);
        params.put("otherhistory",otherhistory1);
        params.put("jobtype",jobtype1);
        params.put("modeofstudy",modeofstudy1);
        params.put("rgGender",rgGender1);
        params.put("maritialstatus",maritialstatus1);
        params.put("nationality",nationality1);
        params.put("interestedfield",interestedfield1);
        params.put("expsalary",expectedsalary1);
        params.put("language", finalLanguageknown);
        params.put("qualification",qualification1);
        params.put("studymode",modeofstudy1);
        params.put("yearofpass",yearofpass1);
        params.put("university",university1);
        params.put("jobrole",jobrole1);
            params.put("photo",  photo,"image/jpg");
            params.put("rfile",  resume,"image/jpg");
        // params.put("uid", Uid_from_sharedpref);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(DatabaseInfo.JobResumeUpdateURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
//                addImageView(inHorizontalScrollView);
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                byte[] bite=responseBody;
                try {
                    final   String response=new String(bite,"UTF-8");
                    System.out.println("response "+response);
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
                    Log.e("image response",response);
                    Toast.makeText(getApplicationContext(),String.valueOf(response.trim()),Toast.LENGTH_LONG).show();
//                    imageUploadid=imageUploadid.concat(response.trim()+",");
//                    Toast.makeText(getApplicationContext(),String.valueOf(imageUploadid),Toast.LENGTH_SHORT).show();
//                    Log.e("image names",String.valueOf(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }



//                Log.e("file path", file);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("file not send","failure");
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Image Selection Unsuccessfull!!!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFinish() {
//             imageupdateprogress.setVisibility(View.GONE);
            }

        });

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
    private String validateMSp(MultiSelectSpinner spinner){
        String returncheck="";
        if(spinner.getSelectedItemPosition()!=0||spinner.getSelectedItemPosition()!=-1){
            returncheck="";
            returncheck= spinner.getSelectedItemsAsString().toString();
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

}