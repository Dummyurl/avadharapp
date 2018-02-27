package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class ViewBuyandSellDetails extends AppCompatActivity {
    Button enq,send;
    LinearLayout llenquiry;
    boolean enqcheck=false;
    String rid , sp_uid;
    PopupWindow popupWindow=null;
    private SharedPreferences sp;
    TextView category,productid,price,title,name,contactnumber,email,country,state,city,ageofitem;
    FeedImageView imageView;
    MaterialEditText query;
    LayoutInflater inflater;
    private ProgressDialog dialog;
    PopupWindow pw;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_view_buyand_sell_details);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.nav_buyandsell);
        Intent i=getIntent();
        rid= i.getExtras().getString("id");
        initialize();
        sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        TabHost host = (TabHost) findViewById(R.id.th_buyandsell_details_details);
        host.setup();
        host.setFocusable(false);
        host.clearFocus();
        host.setFocusableInTouchMode(false);
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.buydescription));
        spec.setContent(R.id.buyandsell_tab1);
        spec.setIndicator(getResources().getString(R.string.buydescription));
        host.addTab(spec);

        enq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showPopup();
            }
        });
        viewDetails();


    }

    private void initialize(){

        category=(TextView)findViewById(R.id.tv_buyandsell_details_category);
        productid=(TextView)findViewById(R.id.tv_buyandsell_details_pid);
        price=(TextView)findViewById(R.id.tv_buyandsell_details_price);
        title=(TextView)findViewById(R.id.tv_buyandsell_details_title);
        name=(TextView)findViewById(R.id.tv_buyandsell_details_name);
        email=(TextView)findViewById(R.id.tv_buyandsell_details_email);
        country=(TextView)findViewById(R.id.tv_buyandsell_details_country);
        state=(TextView)findViewById(R.id.tv_buyandsell_details_state);
        city=(TextView)findViewById(R.id.tv_buyandsell_details_city);
        ageofitem=(TextView)findViewById(R.id.tv_buyandsell_details_ageofitem);
        contactnumber=(TextView)findViewById(R.id.tv_buyandsell_details_contactnumber);
        imageView=(FeedImageView)findViewById(R.id.iv_buyandsell_details_feed);
//
        ageofitem.setFocusableInTouchMode(false);
        enq=(Button)findViewById(R.id.btn_buyandsell_view);
//
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

    private void viewDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.BuyandsellViewDetailsURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            String c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13;

                            JSONArray jsonArray=Jobject.getJSONArray("buyandsell");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject feedObj = (JSONObject) jsonArray.get(i);
                                String image1=feedObj.isNull("image")? DatabaseInfo.BuyansellImagePathURL+"default.jpg":DatabaseInfo.BuyansellImagePathURL+feedObj.getString("image");
                                imageView.setImageUrl(image1,imageLoader);
                                title.setText(feedObj.isNull("addtitle")? "":feedObj.getString("addtitle"));
                                category.setText(feedObj.isNull("ctype")? "":feedObj.getString("ctype"));
                                productid.setText(feedObj.isNull("buyandsell_id")? "":feedObj.getString("buyandsell_id"));
                                price.setText(feedObj.isNull("price")? "":"Rs : "+feedObj.getString("price"));
                                name.setText(feedObj.isNull("name")? "":feedObj.getString("name"));
                                contactnumber.setText(feedObj.isNull("mobileno")? "":feedObj.getString("mobileno"));
                                country.setText(feedObj.isNull("country1")? "":feedObj.getString("country1"));
                                email.setText(feedObj.isNull("email")? "":feedObj.getString("email"));
                                state.setText(feedObj.isNull("state1")? "":feedObj.getString("state1"));
                                city.setText(feedObj.isNull("city1")? "":feedObj.getString("city1"));
                                ageofitem.setText(feedObj.isNull("description")? "":feedObj.getString("description"));
                            }

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(ViewBuyandSellDetails.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rid", String.valueOf(rid));



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
    private void showPopup() {
        try {
            final Button send1;


//            llenquiry=(LinearLayout)findViewById(R.id.ll_buyandsell_enquiry);
            final MaterialEditText title,description;
//            android.view.Display display = ((android.view.WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//            int height = displaymetrics.heightPixels;
//            int width = displaymetrics.widthPixels;
// We need to get the instance of the LayoutInflater

            LayoutInflater layout = (LayoutInflater) this.getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layout.inflate(R.layout.sendbuyandsellquery, null);
            Display display = this.getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int width = display.getWidth();
            llenquiry=(LinearLayout)popupView.findViewById(R.id.ll_buyandsell_enquiry);
            send1=(Button)popupView.findViewById(R.id.btn_buyandsell_send);
            description=(MaterialEditText) popupView.findViewById(R.id.et_buyandsell_enquiry);

            popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                        popupWindow.dismiss();

                        return true;

                    }
//                    eduLayout.getForeground().setAlpha(0);
                    return  false;
                }
            });

            send1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEnquiry(sp_uid,email.getText().toString(),name.getText().toString(),productid.getText().toString(),description.getText().toString());
                    popupWindow.dismiss();

                }
            });
            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
//        dialog.setTitle("Search");
            popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);;

//            popup_close.setOnClickListener(cancel_button);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  setEnquiry(final String uid1, final String email1,final String usename1,final String cat,final String content){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.BuyandsellSendEnquiryURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        String   favoriteResult =response.toString();


                        Toast.makeText(getApplicationContext(),favoriteResult,Toast.LENGTH_SHORT).show();

//                        setAnimationForFavoriteText(response,layout);
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
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
                params.put("uid", String.valueOf(uid1));
                params.put("email",String.valueOf(email1));
                params.put("username", String.valueOf(usename1));
                params.put("cat",String.valueOf(cat));
                params.put("content",String.valueOf(content));
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
