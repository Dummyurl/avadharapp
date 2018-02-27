package com.avadharwebworld.avadhar.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.avadharwebworld.avadhar.Support.FilePath;
import com.avadharwebworld.avadhar.Support.MultiSelectSpinner;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobPostVacancy extends AppCompatActivity {
    MaterialEditText company,number,email,jobtitle,qualification,joblocation,noOfvacancy,agelimit,lastdate,description;
    android.widget.Spinner category,experience,salary;
    MultiSelectSpinner jobrole;
    private Calendar cal;
    String photopath,profileid;
    private SharedPreferences sp;

    RadioGroup jobtype;
    RadioButton fulltime,parttime;
    Button upload,sumbit;
    private int day, month, year,parentid=0;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_vacancy);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.postvacancy);
        validation();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        profileid=sp.getString(Constants.PROFILEID,"");
        getinterestedfieldCode(category);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        lastdate.setText(R.string.lastdate);
        lastdate.setFocusableInTouchMode(false);
        lastdate.setFocusable(false);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        lastdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLastdate();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);
            }
        });
        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testPostResume();
            }
        });
    }
    private void validation(){
        company=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_name);
        number=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_mobile);
        email=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_address);
        jobtitle=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_jobtitile);
        qualification=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_qualification);
        joblocation=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_joblocation);
        noOfvacancy=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_vacancyno);
        agelimit=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_agelimit);
        category=(Spinner)findViewById(R.id.sp_jobpost_vacancy_jobcategory);
        experience=(Spinner)findViewById(R.id.sp_jobpost_vacancy_experiance);
        salary=(Spinner)findViewById(R.id.sp_jobpost_vacancy_salary);
        jobtype=(RadioGroup)findViewById(R.id.rg_jobpost_vacancy_type);
        fulltime=(RadioButton)findViewById(R.id.rb_jobpost_vacancy_fulltime);
        parttime=(RadioButton)findViewById(R.id.rb_jobpost_vacancy_parttime);
        jobrole=(MultiSelectSpinner)findViewById(R.id.sp_jobpost_vacancy_jobrole);
        upload=(Button)findViewById(R.id.btn_jobpost_vacancy_uploadlogo);
        sumbit=(Button)findViewById(R.id.btn_jobpost_vacancy_submit);
        lastdate=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_lastdate);
        description=(MaterialEditText)findViewById(R.id.et_jobpost_vacancy_description);

    }
    public void getinterestedfieldCode(final Spinner ifield){
        final List<String> interestedfield2=new ArrayList<String>();
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

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
                            interestedfield2.add(0,"Select Interested Filed");
//                            countrycodid.add(0,"0");

                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,interestedfield2);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            ifield.setAdapter(spinnerAdapter);
                            ifield.setSelection(0);
                            //   Toast.makeText(getApplicationContext()," country code size "+String.valueOf(Arrays.asList(countrycodid.size())),Toast.LENGTH_SHORT).show();

                            //  Toast.makeText(getApplicationContext(),"country string array count "+countrycodename.length,Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                        }
                        pDialog.hide();
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(JobPostVacancy.this,error.toString(), Toast.LENGTH_LONG).show();
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
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        final List<String>jobrole2=new ArrayList<String>();
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
                        pDialog.hide();
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(JobPostVacancy.this,error.toString(), Toast.LENGTH_LONG).show();
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
    private void setLastdate() {

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int month = monthOfYear + 1;
                lastdate.setText(dayOfMonth + "/" + month + "/" + year);
//                day1=String.valueOf(dayOfMonth);
//                month1=String.valueOf(monthOfYear);
//                year1=String.valueOf(year);

            }
        };

        DatePickerDialog dpDialog = new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

//            Uri uri = data.getData();


            Uri uri = data.getData();

//            resumepath= FilePath.getPath(this,uri);
//            Toast.makeText(getApplicationContext(),resumepath,Toast.LENGTH_SHORT).show();

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

            Toast.makeText(getApplicationContext(),photopath,Toast.LENGTH_SHORT).show();

        }
    }

    private void testPostResume() {

        String name1=company.getText().toString();

        String lastdate1=lastdate.getText().toString();
        final String location1=joblocation.getText().toString();
        final String number1=number.getText().toString();
        final String email1=email.getText().toString();
        final String jobtitile1=jobtitle.getText().toString();
        final String qualification1=qualification.getText().toString();
        final String noofvancany1=noOfvacancy.getText().toString();
        final String agelimit1=agelimit.getText().toString();
        final String description1=description.getText().toString();
        final String jobtype1=((RadioButton)findViewById(jobtype.getCheckedRadioButtonId())).getText().toString();
       // final String modeofstudy1=((RadioButton)findViewById(modeofstudy.getCheckedRadioButtonId())).getText().toString();
      //  final String rgGender1=((RadioButton)findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
        // String maritialstatus1=((RadioButton)findViewById(maritialstatus.getCheckedRadioButtonId())).getText().toString();
    //    final String nationality1=nationality.getSelectedItem().toString();
        final String category1=category.getSelectedItem().toString();
        final String experience1=experience.getSelectedItem().toString();
        final String salary1=salary.getSelectedItem().toString();
        final String jobrole1=jobrole.getSelectedItemsAsString().toString();


        byte[] newbyteFile= new byte[10000];
        File photo=new File(photopath);
//        File resume=new File(resumepath);
//        Log.e("resumepath",resumepath);
        RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;
        try {
//            params.put("uid",uid);
            params.put("profileid",profileid);
            params.put("cname",name1);
            params.put("jobtitle",jobtitile1);
            params.put("jobtype",jobtype1);
            params.put("qualification",qualification1);
            params.put("category1",category1);
            params.put("jobrole",jobrole1);
            params.put("exp",experience1);
            params.put("jobloc",location1);
            params.put("numvacancy",noofvancany1);
            params.put("ldate",lastdate1);
            params.put("sal",salary1);
            params.put("jobdesc",description1);
            params.put("cno",number1);
            params.put("cmail",email1);
            params.put("Age",agelimit1);

            params.put("jobrole",jobrole1);
            params.put("photojob",  photo,"image/jpg");
//            params.put("rfile",  resume);
            // params.put("uid", Uid_from_sharedpref);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(DatabaseInfo.PostVacancyURL, params, new AsyncHttpResponseHandler() {

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
                    Toast.makeText(getApplicationContext(),String.valueOf("Response : "+response.trim()),Toast.LENGTH_LONG).show();
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

}
