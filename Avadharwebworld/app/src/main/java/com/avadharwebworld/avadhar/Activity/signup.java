package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;


public class signup extends AppCompatActivity {
    EditText email,username,password,phone;
    Spinner country_code;
    AutoCompleteTextView city;
    Button create;
    DatabaseInfo db=new DatabaseInfo();
     String url_countrycode=DatabaseInfo.CountryCodeURl;
     String url_signupvalidation=DatabaseInfo.SignupValidationurl;
    String url_signup=DatabaseInfo.SignupUrl;
    String mStatus,eStatus,uStatus;
    JSONObject Jobject;
    JSONArray jArray;
    String[] countryid=null,countrycode=null,sortname=null;
    TextView login;
    public  String profileid,UID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        email=(EditText)findViewById(R.id.et_email);
        username=(EditText)findViewById(R.id.et_username);
        password=(EditText)findViewById(R.id.et_password);
        phone=(EditText)findViewById(R.id.et_phone);

        country_code=(Spinner)findViewById(R.id.sp_countrycode);
        GetCountrycode();
        create=(Button)findViewById(R.id.btn_create);
        login=(TextView)findViewById(R.id.tv_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setError(null);
                username.setError(null);
                password.setError(null);
                phone.setError(null);
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    email.setError("Enter valid email address");
                    email.requestFocus();

                }else if(username.getText().toString().length()<6){
                    username.setError("Username must have minimum 6 charaters");
                    username.requestFocus();
                }else if (password.getText().toString().length()<8){
                    password.setError("Password must have minimum 8 characters");
                    password.requestFocus();
                }else if (phone.getText().toString().length()!=10){
                    phone.setError("Mobile number must have 10 digits");
                    phone.requestFocus();
                }
                else{
                    Validation();
                }

                Toast.makeText(getApplicationContext(),"Account Created",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void GetCountrycode(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_countrycode,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("volleyJson", Jobject.toString());
                            try {
                                jArray = Jobject.getJSONArray("result");
                                String c1[] = new String[jArray.length()];
                                String c2[] = new String[jArray.length()];
                                String c3[] = new String[jArray.length()];
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json_data = jArray.getJSONObject(i);

                                    String cid = json_data.getString("id");
                                    String ccode = json_data.getString("std_code");
                                    String sname = json_data.getString("sortname");
                                    Log.e(sname, "got");
                                    Log.e("got",json_data.toString());
                                    Log.e(cid, "got");
                                    Log.e(ccode, "got");
                                    c1[i]=cid;
                                    c2[i]=ccode;
                                    c3[i]=sname;

                                    countrycode = c2;
                                    countryid = c1;
                                    sortname = c3;
                                }

                            } catch (Exception e) {
                            }
                            ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_iltem,countrycode);
                            spinnerAdapter.setDropDownViewResource(R.layout.spinner_iltem);

                            country_code.setAdapter(spinnerAdapter);

                            country_code.setSelection(38);

                        } catch (Exception e) {
                        }
                        pDialog.hide();
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        Toast.makeText(signup.this,error.toString(), Toast.LENGTH_LONG).show();
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
    private boolean isValidEntry(String pass) {
        if (pass != null && pass.length() > 0) {
            return false;
        }
        return true;
    }

    public void Validation(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
String pa="?username="+username.getText().toString().trim()+"&email="+email.getText().toString().trim()+"&mobile="+phone.getText();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_signupvalidation+pa,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        try {
                            Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("volleyJson", Jobject.toString());
//                          Log.e("url string",url);
                             JSONObject json_data=null;
//                                jArray = Jobject.getJSONArray("result");
//                                Log.e("Jarray count", jArray.toString());

                            String M_status = Jobject.getString("mobile_status");
                            String E_status = Jobject.getString("email_status");
                            String U_status = Jobject.getString("username_status");
                            Log.e(M_status, "got");
                            Log.e("got",Jobject.toString());
                            Log.e(E_status, "got");
                            Log.e(U_status, "got");
                            mStatus = M_status;
                            eStatus = E_status;
                            uStatus=U_status;
                } catch (JSONException e) {}


//                        Toast.makeText(getApplicationContext(),mStatus,Toast.LENGTH_LONG).show();
                        if(mStatus.equals("1")){
                            pDialog.hide();
                            phone.setError("Mobile number already exist!!");
                        }
                        if(eStatus.equals("1")){
                            pDialog.hide();
                            email.setError("Email already exist!!");
                        }
                        if(uStatus.equals("1")){
                            pDialog.hide();
                            username.setError("Username already exist!!");
                        }
                        if(uStatus!="1"&&mStatus!="1"&&eStatus!="1"){

                            signup();
                            pDialog.hide();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(signup.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", phone.getText().toString());
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

public void signup(){
    String pa="?username="+username.getText().toString().trim()+"&email="+email.getText().toString().trim()+"&mobile="+phone.getText()+"&password="+password.getText();
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url_signup+pa,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Volleyresponse",response.toString());

                    try {
                        Jobject = (JSONObject) new JSONTokener(response).nextValue();
                        Log.e("volleyJson", Jobject.toString());
//                          Log.e("url string",url);
                        JSONObject json_data=null;
//                                jArray = Jobject.getJSONArray("result");
//                                Log.e("Jarray count", jArray.toString());

                        String profid = Jobject.getString("profileids");
                        String uid = Jobject.getString("uid");
                        Log.e("got",profid);
                        UID=uid;
                        profileid=profid;
                        Log.e("uid got",UID);
//                        Toast.makeText(getApplicationContext(),profileid,Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(getApplicationContext(),ConfirmOtp.class);
                        i.putExtra("profileid",profileid);
                        i.putExtra("uid",UID);
                        startActivity(i);
                    } catch (JSONException e) {}
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(signup.this,error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            String mobile="";
            if(country_code.getSelectedItem().toString().equals("91"))
            {
                mobile=phone.getText().toString();
            }else{
                    mobile="+"+country_code.getSelectedItem().toString()+phone.getText().toString();
            }
            params.put("username", username.getText().toString());
            params.put("email", email.getText().toString());
            params.put("mobile", mobile);
            params.put("password", password.getText().toString());
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
