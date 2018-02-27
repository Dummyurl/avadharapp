package com.avadharwebworld.avadhar.Activity;

/**
 * Created by Vishnu on 25-08-2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    Button login,signup, popup_close,popup_sumbit_email;
    TextView forgotpassword;
    EditText username,password;
    LayoutInflater inflater;
    PopupWindow pw;
    FrameLayout layout;
    DatabaseInfo db=new DatabaseInfo();
    String url=DatabaseInfo.LoginUrl;
    JSONObject Jobject;
    JSONArray jArray;

    String sp_islogin,sp_uid,sp_proid,sp_temp_login,sp_username,profileimgpath;
    String getname=null,status=null,getid=null,getprofileid=null,getprofilepic=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_islogin=sp.getString(Constants.ISLOGIN,"");
        if(sp_islogin.equals("1")){
            Intent i=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        login=(Button)findViewById(R.id.btn_login);
        signup=(Button)findViewById(R.id.btn_signup);
        username=(EditText)findViewById(R.id.et_username);
        password=(EditText)findViewById(R.id.et_password);
        popup_sumbit_email=(Button)findViewById(R.id.btn_recover_submit) ;
        layout=(FrameLayout) findViewById(R.id.layour_login);


      //  layout.getForeground().setAlpha(0);
        forgotpassword=(TextView)findViewById(R.id.t_forgot_password);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"hai",Toast.LENGTH_SHORT).show();
               layout.getForeground().setAlpha(220);
             //   pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                showPopup();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              final String tempusername=username.getText().toString();
                final String temppassword=password.getText().toString();

                if(username.getText().toString().length()==0){
                    username.setError("Please Enter Username!!!");
                }
                else if (password.getText().toString().length()==0){
                    password.setError("Please enter password!!!");
                }
                    else{
                    LoginCheck();
                }



//                Fragment hf=null;
//                hf=new Education();
//                FragmentManager fm= getSupportFragmentManager();
//                fm.beginTransaction().replace(R.id.fragment_education,hf).commit();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),signup.class);
                startActivity(i);

            }
        });
    }

    private void showPopup() {
        try {

// We need to get the instance of the LayoutInflater
            inflater = (LayoutInflater)Login.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.forgot_password_popup,
                    (ViewGroup) findViewById(R.id.popup_forgot_password));
            pw = new PopupWindow(layout,600, 400, true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            popup_close = (Button) layout.findViewById(R.id.close_popup);

            popup_close.setOnClickListener(cancel_button);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
            layout.getForeground().setAlpha(0);
        }
    };

    public void LoginCheck(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET,DatabaseInfo.ip+DatabaseInfo.folder+"login.php?username="+username.getText()+"&password="+password.getText(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Jobject = (JSONObject) new JSONTokener(response).nextValue();
                        Log.e("url string",Jobject.toString());
                            // JSONObject json_data=null;
//                                jArray = Jobject.getJSONArray("result");
//                                Log.e("Jarray count", jArray.toString());

                            String Status = Jobject.getString("status");
                            String naMe = Jobject.getString("name");
                            String v_id = Jobject.getString("uid");
                            String profid=Jobject.getString("profileid");
                            String profileimg=Jobject.getString("profile_pic");
                            Log.e(v_id, "got");
                            Log.e("got",Jobject.toString());
                            Log.e(Status, "got");
                            Log.e(naMe, "got");
                            status = Status;
                            getid = v_id;
                            getname = naMe;
                            getprofileid=profid;
                            getprofilepic=profileimg;

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        pDialog.hide();
                        switch (status) {
                            case "success":
                                Toast.makeText(getApplicationContext(),"Logged in..",Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                                i.putExtra("profileid",getprofileid);
                                i.putExtra("uid",getid);

//                                Toast.makeText(Login.this,"Welcomes You!",Toast.LENGTH_LONG).show();
                                sp_temp_login="1";

//                                try {
//                                    final URL imgpath=new URL(DatabaseInfo.ProfilepicURL+getprofilepic);
//                                    final Bitmap bitmap = BitmapFactory.decodeStream(imgpath.openConnection().getInputStream());
//                                }catch (Exception e){}

                                SharedPreferences sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Constants.ISLOGIN,sp_temp_login);
                                editor.putString(Constants.PASSWORD, password.getText().toString());
                                editor.putString(Constants.USERNAME, username.getText().toString());
                                editor.putString(Constants.U_NAME, getname.toString());
                                editor.putString(Constants.UID,getid.toString());
                                editor.putString(Constants.PROFILEID,getprofileid.toString());
                                editor.putString(Constants.PROFILEIMG,getprofilepic.toString());
                                editor.putString(Constants.PROFILEIMGPATH,profileimgpath);
//                                editor.putString(Constants.PROFILEIMG,encodeTobase64(bitma))
//                                editor.putString(Landline, "");
//                                editor.putString(Address, "");
//                                editor.putString(District,"");
//                                editor.putString(Country,"");
//                                editor.putString(State,"");
//                                editor.putString(Name,getname);

                                editor.commit();
                                startActivity(i);
                                break;
                            case "fail":
                                Toast.makeText(Login.this, "Login Failed !!    Please Check Your Username and Password", Toast.LENGTH_SHORT).show();
//
                                break;
//

                            default:
                                break;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.hide();
                        Toast.makeText(Login.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username.getText().toString());
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
    private boolean isValidEntry(String pass) {
        if (pass != null && pass.length() > 0) {
            return false;
        }
        return true;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }



}
