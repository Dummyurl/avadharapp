package com.avadharwebworld.avadhar.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsProfile extends AppCompatActivity {
    boolean isonpenchangepassword=false,imagechanged=false;

    TextView profileid,email,changepassword;
    EditText name,dob,sponserid;
    private SharedPreferences sp;
    MaterialEditText currentpwd,newpwd,confirmpwd;
    ImageButton choosepic;
    Button update,changepwd;
    RadioGroup gender;
    NetworkImageView profilepic;
    Spinner status,religion;
    Space pwdspace;
    String uid,picturePath="",profilepicstustus;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    TableRow pwdrow,pwdrow1,pwdrow2,pwdrow3,pwdrow4;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.profilesettings);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(R.string.profilesettings);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initialize();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isonpenchangepassword){
                    pwdrow.setVisibility(View.VISIBLE);
                    pwdrow1.setVisibility(View.VISIBLE);
                    pwdrow2.setVisibility(View.VISIBLE);
                    pwdrow3.setVisibility(View.VISIBLE);
                    pwdrow4.setVisibility(View.VISIBLE);
                    pwdspace.setVisibility(View.VISIBLE);
                    isonpenchangepassword=true;

                }else {
                    pwdrow.setVisibility(View.GONE);
                    pwdrow1.setVisibility(View.GONE);
                    pwdrow2.setVisibility(View.GONE);
                    pwdrow3.setVisibility(View.GONE);
                    pwdrow4.setVisibility(View.GONE);
                    pwdspace.setVisibility(View.GONE);
                    isonpenchangepassword=false;
                }
            }
        });

        choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,1);
            }
        });

        viewDetails();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdate();
            }
        });
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentpwd.getText().toString() == "") {
                    currentpwd.setError("Enter Old Password....!!");
                    currentpwd.requestFocus();
                } else if (newpwd.getText().toString() == "") {
                    newpwd.setError("Enter New Password....!");
                    newpwd.requestFocus();
                } else if (confirmpwd.getText().toString()=="") {
                    confirmpwd.setError("Enter Confirm Password...!!");
                    confirmpwd.requestFocus();
                } else {
                    if (newpwd.getText().toString().equals(confirmpwd.getText().toString()) && !currentpwd.equals("")) {
                        setUpdatePassword();}
                }
            }
        });
    }



    private void initialize(){

        profileid=(TextView)findViewById(R.id.tv_profile_settings_profileid);
        email=(TextView)findViewById(R.id.tv_profile_settings_email);
        changepassword=(TextView)findViewById(R.id.tv_profile_settings_changepassword);
        name=(EditText)findViewById(R.id.et_profile_settings_name);
        dob=(EditText)findViewById(R.id.et_profile_settings_dob);
        sponserid=(EditText)findViewById(R.id.et_profile_settings_sponserid);
        currentpwd=(MaterialEditText) findViewById(R.id.et_profile_settings_oldpassword);
        newpwd=(MaterialEditText)findViewById(R.id.et_profile_settings_newpassword);
        confirmpwd=(MaterialEditText)findViewById(R.id.et_profile_settings_confirmpassword);
        choosepic=(ImageButton)findViewById(R.id.btn_profile_settings_changephoto);
        update=(Button)findViewById(R.id.btn_profile_settings_update);
        changepwd=(Button)findViewById(R.id.btn_profile_settings_changepassword);
        gender=(RadioGroup)findViewById(R.id.rg_profile_settings_gender);
        profilepic=(NetworkImageView) findViewById(R.id.iv_profile_settings_photo);
        status=(Spinner)findViewById(R.id.sp_profile_settings_status);
        religion=(Spinner)findViewById(R.id.sp_profile_settings_religion);
        pwdspace=(Space)findViewById(R.id.space_personal_settings);
        pwdrow1=(TableRow)findViewById(R.id.tr_profile_settings_changepassword4);
        pwdrow2=(TableRow)findViewById(R.id.tr_profile_settings_changepassword3);
        pwdrow3=(TableRow)findViewById(R.id.tr_profile_settings_changepassword2);
        pwdrow4=(TableRow)findViewById(R.id.tr_profile_settings_changepassword1);
        pwdrow=(TableRow)findViewById(R.id.tr_profile_settings_changepassword);
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
    private void viewDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.GetUserDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("user");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);
                                String image1=feedObj.isNull("profile_pic")? DatabaseInfo.ProfilepicURL+"":DatabaseInfo.ProfilepicURL+feedObj.getString("profile_pic");
                                profilepic.setImageUrl(image1,imageLoader);
                                name.setText(feedObj.isNull("name")? "":feedObj.getString("name"));
                                profileid.setText(feedObj.isNull("profileid")? "":feedObj.getString("profileid"));
                                email.setText(feedObj.isNull("email")? "":feedObj.getString("email"));
                                String genr= feedObj.isNull("gender")? "":feedObj.getString("gender");
                               profilepicstustus= feedObj.isNull("profile_pic_status")? "":feedObj.getString("profile_pic_status");
                                if(genr.equals("m")){
                                    gender.check(R.id.rb_profile_settings_male);
                                }else if(genr.equals("f")){
                                    gender.check(R.id.rb_profile_settings_female);
                                }
                                dob.setText(feedObj.isNull("birthday")? "":feedObj.getString("birthday"));
                             String stats = feedObj.isNull("status")? "":feedObj.getString("status");
                                if(!stats.equals("0")){
                                    status.setSelection(Integer.parseInt(stats));
                                }
                                String regn=feedObj.isNull("religion")? "":feedObj.getString("religion");

                                if (Arrays.asList(getResources().getStringArray(R.array.Religion)).contains(regn)) {
                                    religion.setSelection(Arrays.asList(getResources().getStringArray(R.array.Religion)).indexOf(regn));
                                }

                                sponserid.setText(feedObj.isNull("sponserid")? "":feedObj.getString("sponserid"));

                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(SettingsProfile.this, error.toString(), Toast.LENGTH_LONG).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id;

        if(requestCode==1) {
            if (resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath1 = cursor.getString(columnIndex);
                cursor.close();

                picturePath = picturePath1;
                profilepic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                imagechanged=true;
                profilepicstustus="1";
            }
        }


    }
    private void setUpdate(){

//        byte[] newbyteFile= new byte[10000];

        if(picturePath!="") {
            File photo=new File(picturePath);
//        File resume=new File(resumepath);
////        Log.e("resumepath",resumepath);
            String rgGender="";
            String rgGender1="";
            if(gender.getCheckedRadioButtonId()==R.id.rb_profile_settings_female||
                    gender.getCheckedRadioButtonId()==R.id.rb_profile_settings_female){
                rgGender =((RadioButton)findViewById(gender.getCheckedRadioButtonId())).getText().toString();
                Toast.makeText(getApplicationContext(),rgGender,Toast.LENGTH_SHORT).show();
                if(rgGender.equals(R.string.male)){
                    rgGender1="m";
                }else if(rgGender.equals(R.string.female)) rgGender1="f";
            }else {rgGender1="";}
            String religion1="";
            if(String.valueOf(religion.getSelectedItemId()).equals("0")){
                religion1="";
            }else {
                religion1=religion.getSelectedItem().toString();
            }

            RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;
            try {

                params.put("file", photo);
                params.put("Name", name.getText().toString());
                params.put("avatar", profilepicstustus);
                params.put("gender", rgGender1);
                params.put("Dob", dob.getText().toString());
                params.put("uid", uid);
                params.put("Status", String.valueOf(status.getSelectedItemId()));

                params.put("Religion", religion1);
                params.put("Sponserid", sponserid.getText().toString());


                // params.put("uid", Uid_from_sharedpref);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(DatabaseInfo.UpdateProfileSettingsURL, params, new AsyncHttpResponseHandler() {

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
                   Toast.makeText(getApplicationContext(),String.valueOf(response),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), " Unsuccessfull!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
//             imageupdateprogress.setVisibility(View.GONE);
                }

            });
        }else{

//        File resume=new File(resumepath);
////        Log.e("resumepath",resumepath);
            String rgGender="";
            String rgGender1="";
            if(gender.getCheckedRadioButtonId()==R.id.rb_profile_settings_male||
                    gender.getCheckedRadioButtonId()==R.id.rb_profile_settings_female){
                rgGender =((RadioButton)findViewById(gender.getCheckedRadioButtonId())).getText().toString();
                if(rgGender.equals("Male")){
                    rgGender1="m";
                }else rgGender1="f";
            }else {rgGender1="";}
            String religion1="";
            if(String.valueOf(religion.getSelectedItemId()).equals("0")){
                religion1="";
            }else {
                religion1=religion.getSelectedItem().toString();
            }
                RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;


                params.put("file", "");
                params.put("Name", name.getText().toString());
                params.put("avatar", profilepicstustus);

                params.put("gender", rgGender1);
                params.put("Dob", dob.getText().toString());
                params.put("uid", uid);
                params.put("Status", String.valueOf(status.getSelectedItemId()));

                params.put("Religion", religion1);
                params.put("Sponserid", sponserid.getText().toString());
            Log.e("log gender",rgGender1);

                // params.put("uid", Uid_from_sharedpref);
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(DatabaseInfo.UpdateProfileSettingsURL, params, new AsyncHttpResponseHandler() {

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
                            Toast.makeText(getApplicationContext(),String.valueOf("Updated..."),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), " Unsuccessfull!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
//             imageupdateprogress.setVisibility(View.GONE);
                    }

                });
            }
        }
        private void setUpdatePassword(){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.UpdatePasswordURL,
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

                            Toast.makeText(SettingsProfile.this,error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("uid",uid);
                    params.put("npassword", confirmpwd.getText().toString());
                    params.put("opassword", currentpwd.getText().toString());

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

