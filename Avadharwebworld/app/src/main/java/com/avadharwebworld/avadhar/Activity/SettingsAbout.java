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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class SettingsAbout extends AppCompatActivity {
    EditText favorite,hobbies,lifeevent;
    Spinner food;
    RadioGroup language;
    Button update;
    CheckBox english,hindi,other;
    private SharedPreferences sp;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_settings_about);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(R.string.about1);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initialize();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        uid=sp.getString(Constants.UID,"");
        viewDetails();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpdate();
            }
        });

    }
    private void initialize(){
        favorite=(EditText)findViewById(R.id.et_about_settings_yourfavorite);
        hobbies=(EditText)findViewById(R.id.et_about_settings_hobbies);
        food=(Spinner)findViewById(R.id.sp_about_settings_food);
        english=(CheckBox)findViewById(R.id.cb_about_settings_english);
        hindi=(CheckBox)findViewById(R.id.cb_about_settings_hindi);
        other=(CheckBox)findViewById(R.id.cb_about_settings_Other);

        update=(Button)findViewById(R.id.btn_about_settings_update);
        lifeevent=(EditText)findViewById(R.id.et_about_settings_lifeevent);

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
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("user");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);

                                favorite.setText(feedObj.isNull("ufav")? "":feedObj.getString("ufav"));
                                hobbies.setText(feedObj.isNull("uhobby")? "":feedObj.getString("uhobby"));
                                lifeevent.setText(feedObj.isNull("uevents")? "":feedObj.getString("uevents"));
                                String language=feedObj.isNull("language")? "":feedObj.getString("language");
                                if(StringUtils.containsIgnoreCase(language, getResources().getString(R.string.english))){
                                    english.setChecked(true);
                                }
                                if(StringUtils.containsIgnoreCase(language, getResources().getString(R.string.hindi))){
                                    hindi.setChecked(true);
                                }
                                if(StringUtils.containsIgnoreCase(language, getResources().getString(R.string.other1))){
                                    other.setChecked(true);
                                }
                                String food1=feedObj.isNull("food_status")? "":feedObj.getString("food_status");
                                if(food1.equals("veg")){food.setSelection(1);}
                                else if(food1.equals("veg")){food.setSelection(2);}
                                else if(food1.equals("veg")) {food.setSelection(3);}
                                else {food.setSelection(0);}

                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(SettingsAbout.this, error.toString(), Toast.LENGTH_LONG).show();
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
    private void setUpdate(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.UpdateAboutSettingsURL,
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

                        Toast.makeText(SettingsAbout.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String familymember="";
                String fd="";
                if(english.isChecked()){familymember+=english.getText().toString()+",";}
                if(hindi.isChecked()){familymember+=hindi.getText().toString()+",";}
                if(other.isChecked()){familymember+=other.getText().toString()+",";}
                    if(food.getSelectedItemId()==0){
                        fd="";
                    }else if(food.getSelectedItemId()==1){
                        fd="veg";
                    }else if(food.getSelectedItemId()==2){
                        fd="nonveg";
                    }
                    else if(food.getSelectedItemId()==3){
                        fd="both";
                    }

                params.put("uid",uid);
                params.put("food",fd);
                params.put("fav", favorite.getText().toString());
                params.put("lifeevent", lifeevent.getText().toString());
                params.put("language", familymember);
                params.put("hobby", hobbies.getText().toString());
                Log.e("language",familymember);

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
