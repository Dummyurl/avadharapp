package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class ConfirmOtp extends AppCompatActivity {
    static EditText et_otp1,et_otp2,et_otp3,et_otp4,et_otp5,et_otp6;
    String otpResult;
    public String otp="";
    JSONObject Jobject;
    JSONArray jArray;
    Button confirm;
    DatabaseInfo db=new DatabaseInfo();
    String uid,profileid;

    String url_confirmotp=DatabaseInfo.ConfirmOtpURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_confirm_otp);
        getSupportActionBar().hide();
        Intent i=getIntent();
        profileid= i.getExtras().getString("profileid");
        uid=i.getExtras().getString("uid");
        Log.e("uid",uid);
        Log.e("Profile id",profileid);
        et_otp1=(EditText)findViewById(R.id.et_otp1);
        et_otp2=(EditText)findViewById(R.id.et_otp2);
        et_otp3=(EditText)findViewById(R.id.et_otp3);
        et_otp4=(EditText)findViewById(R.id.et_otp4);
        et_otp5=(EditText)findViewById(R.id.et_otp5);
        et_otp6=(EditText)findViewById(R.id.et_otp6);
        confirm=(Button)findViewById(R.id.btn_confirm);
        et_otp1.requestFocus();
        edittextAutoFocus();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOtp();
            }
        });
    }
    public void recivedSms(String message)
    {

        try
        {

//            for (int i = 0;i < message.length(); i++){
//                System.out.println(message.charAt(i));
//                otp[i]=String.valueOf(message.charAt(i));
//                et_otp1.setTextColor(message.charAt(i));
//            }
            Log.e("otp recieved",String.valueOf(message));
            String otp1=message;

            otp=otp1;
            Log.e("otp1",otp1);
//            Toast.makeText(getApplicationContext(),message+"sadasdasdasd",Toast.LENGTH_SHORT).show();
            et_otp1.setText(String.valueOf(message.charAt(0)));
            et_otp2.setText(String.valueOf(message.charAt(1)));
//            Toast.makeText(getApplicationContext(),message.charAt(1),Toast.LENGTH_SHORT).show();
            et_otp3.setText(String.valueOf(message.charAt(2)));
            et_otp4.setText(String.valueOf(message.charAt(3)));
            et_otp5.setText(String.valueOf(message.charAt(4)));
            et_otp6.setText(String.valueOf(message.charAt(5)));

        }
        catch (Exception e)
        {
        }
    }
    public void edittextAutoFocus(){
        et_otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(et_otp1.getText().toString().length()==1) {
                    et_otp1.clearFocus();
                    et_otp2.requestFocus();
                    et_otp2.setCursorVisible(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_otp1.clearFocus();
                et_otp2.requestFocus();
                et_otp2.setCursorVisible(true);
            }
        });
        et_otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(et_otp2.getText().toString().length()==1) {
                    et_otp2.clearFocus();
                    et_otp3.requestFocus();
                    et_otp3.setCursorVisible(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_otp2.clearFocus();
                et_otp3.requestFocus();
                et_otp3.setCursorVisible(true);
            }
        });
        et_otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(et_otp3.getText().toString().length()==1) {
                    et_otp3.clearFocus();
                    et_otp4.requestFocus();
                    et_otp4.setCursorVisible(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_otp3.clearFocus();
                et_otp4.requestFocus();
                et_otp4.setCursorVisible(true);
            }
        });
        et_otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(et_otp4.getText().toString().length()==1) {
                    et_otp4.clearFocus();
                    et_otp5.requestFocus();
                    et_otp5.setCursorVisible(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_otp4.clearFocus();
                et_otp5.requestFocus();
                et_otp5.setCursorVisible(true);
            }
        });
        et_otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(et_otp5.getText().toString().length()==1) {
                    et_otp5.clearFocus();
                    et_otp6.requestFocus();
                    et_otp6.setCursorVisible(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_otp5.clearFocus();
                et_otp6.requestFocus();
                et_otp6.setCursorVisible(true);
            }
        });
        et_otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if(et_otp6.getText().toString().length()==1){
                    et_otp6.clearFocus();

                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                et_otp6.clearFocus();
            }
        });
    }

    public void confirmOtp(){
        otp=et_otp1.getText().toString()+et_otp2.getText().toString()+et_otp3.getText().toString()+et_otp4.getText().toString()+et_otp5.getText().toString()+et_otp6.getText().toString();
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_confirmotp,
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

                            String otp_status = Jobject.getString("status");
                            otpResult=otp_status;
                            Log.e(otp_status, "stats");
                            Log.e("got",Jobject.toString());
                        } catch (JSONException e) {}

                        pDialog.hide();
//                        Toast.makeText(getApplicationContext(),mStatus,Toast.LENGTH_LONG).show();

                           if(otpResult.equals("success")){
//                               Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                               Intent i=new Intent(getApplicationContext(),MainActivity.class);
                               i.putExtra("profileid",profileid);
                               i.putExtra("uid",uid);
                               startActivity(i);
                           }
                            else
                           {
                               Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                           }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(ConfirmOtp.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", String.valueOf(otp));
                params.put("uid", String.valueOf(uid));
                Log.e("uid pass",String.valueOf(uid));
                Log.e("otp pass",String.valueOf(otp));
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
