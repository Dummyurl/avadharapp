package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
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
import com.avadharwebworld.avadhar.Adapter.ImageSliderAdapter;
import com.avadharwebworld.avadhar.Adapter.NewsFeedAdapter;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.avadharwebworld.avadhar.Activity.SocialMedia.extractUrls;
import static com.avadharwebworld.avadhar.Adapter.NewsFeedAdapter.makeTextViewResizable;

public class StatusView extends AppCompatActivity {
    private TextView profilename,statustime,status,statusurl,statuslike,statuscomment,statusShare;
    private NetworkImageView profilepic;
    private ViewPager statusImages;
    private String sp_uid,Suid,Sname,Stime,Slike,Sshare,Sid,Sprofimgurl,imagepath;
    private String[] SImageurls;
    Bundle bundle;
    private LinearLayout statuslayout;
    ImageSliderAdapter ObjSliderAdapter;
    int itemposition;
   private ProgressDialog dialog;
    private SocialMedia socialMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_status_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ElementInitialize();
        bundle=new Bundle();
        socialMedia=new SocialMedia();
        bundle=this.getIntent().getExtras();
        final SharedPreferences sp=getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
            dialog=new ProgressDialog(this);
            itemposition=getIntent().getIntExtra("position",0);
            dialog.setMessage("Loading");
            dialog.show();
            getStatus(itemposition);
            statuslike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(statuslike.getText().equals("Like")){
                        statuslike.setText(R.string.unlike);
                        statuslike.setTextColor(Color.parseColor("#4cb3d2"));
                       Like(itemposition,sp_uid);
                    }else {
                        statuslike.setText(R.string.like);
                        statuslike.setTextColor(Color.parseColor("#747474"));
                        Unlike(itemposition,sp_uid);
                    }

                }
            });
    }
    private void ElementInitialize(){
        profilename=(TextView)findViewById(R.id.statusview_tv_elementname_withpic);
        statustime=(TextView)findViewById(R.id.statusview__tv_elementposttime_withpic);
        status=(TextView)findViewById(R.id.statusview_tv_elementpost_text_with_pic);
        statusurl=(TextView)findViewById(R.id.statusview_tv_elementpost_url_with_pic);
        statuslike=(TextView)findViewById(R.id.statusview_tv_element_like_withpic);
        statuscomment=(TextView)findViewById(R.id.statusview_tv_element_comment_withpic);
        statusShare=(TextView)findViewById(R.id.statusview_tv_element_share_withpic);
        profilepic=(NetworkImageView)findViewById(R.id.statusview_profileimg);
        statusImages=(ViewPager)findViewById(R.id.statusview_viewpager_postimage);
        statuslayout=(LinearLayout)findViewById(R.id.statusview_news_feed_card);
        statusShare.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();

        Intent intent = new Intent(this, SocialMedia.class);
        startActivity(intent);
//
    }
    @Override
    public void finish() {
        super.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getStatus(final int itemposition){
        final ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.StatusViewURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            JSONArray jArray=Jobject.getJSONArray("statusres");
                            for(int i=0;i<jArray.length();i++){
                                JSONObject jsonObject=(JSONObject) jArray.get(i);
                                profilename.setText(jsonObject.getString("name"));
                                String time=jsonObject.getString("created");
                                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(Long.parseLong(time)*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                                statustime.setText(timeAgo);
                                status.setText(((String.valueOf(jsonObject.getString("message"))).replaceAll("[\\n\\r]+", "\\\\n").replaceAll((String.valueOf(extractUrls(jsonObject.getString("message")))).substring(1,(String.valueOf(extractUrls(jsonObject.getString("message")))).length()-1)," ").replace("\\n"," ").replace("\"","")));

                                String tempurl=(String.valueOf(extractUrls(jsonObject.getString("message")))).substring(1,(String.valueOf(extractUrls(jsonObject.getString("message")))).length()-1);
                                String msg = (String.valueOf(String.valueOf(extractUrls(jsonObject.getString("message")))).replace(tempurl,""));
                                String msgurl = msg.replaceAll("(?:https?|ftps?)://[\\w/%.-]+", "<a href='$0'>$0</a>");
                                String finalURl=(String.valueOf(extractUrls(jsonObject.getString("message")))).substring(1,(String.valueOf(extractUrls(jsonObject.getString("message")))).length()-1);
                                statusurl.setText ((Html.fromHtml("<a href=\"" +finalURl + "\">"
                                        +finalURl + "</a> ")));
                                String like=jsonObject.getString("like_check");

                                if(like.equals("true")){
                                    statuslike.setText(R.string.like);
                                    statuslike.setTextColor(Color.parseColor("#747474"));
                                }else {statuslike.setText(R.string.unlike);
                                    statuslike.setTextColor(Color.parseColor("#4cb3d2"));
                                  }
                                String[] testImgpath= new String[]{DatabaseInfo.NewsfeedimageURL};
                                profilepic.setImageUrl(DatabaseInfo.ProfilepicURL+jsonObject.getString("profile_pic"),imageLoader);
                                status.post(new Runnable() {
                                    @Override
                                    public void run() {
                                      int  lineCnt = status.getLineCount();
                                        if(lineCnt>3){
                                            makeTextViewResizable(status,3,"..Read More",true);
                                        }
                                        // Perform any actions you want based on the line count here.
                                    }
                                });

                                String [] NewsfeedimagePath=null;
                                        imagepath=jsonObject.getString("statusimage");
                                if(imagepath!=""){
                                    NewsfeedimagePath=imagepath.split(",");
                                    for(int j=0;j<NewsfeedimagePath.length;j++){
                                        NewsfeedimagePath[j]=NewsfeedimagePath[j].replace(NewsfeedimagePath[j],DatabaseInfo.NewsfeedimageURL+NewsfeedimagePath[j]);
                                    }
                                }else{ NewsfeedimagePath=null;}

                                if(NewsfeedimagePath!=null||!Arrays.equals(NewsfeedimagePath,testImgpath)) {

                                    ObjSliderAdapter = new ImageSliderAdapter(getApplicationContext(),NewsfeedimagePath);
                                    statusImages.setAdapter(ObjSliderAdapter);
                                    statusImages.setVisibility(View.VISIBLE);
                                    Log.e("image path of newsfeed ",String.valueOf(Arrays.asList(NewsfeedimagePath)));

                                }else {statusImages.setVisibility(View.GONE);}
                                if(Arrays.equals(NewsfeedimagePath,testImgpath)){
                                    statusImages.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {

                        }
                        dialog.hide();
                        statuslayout.setVisibility(View.VISIBLE);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        dialog.hide();
                        statuslayout.setVisibility(View.GONE);
//                        Toast.makeText(SocialMedia.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//                Log.e("message id",Jsonobjectmsgid.toString());
//                String[]msg=objsocial.messagefull();
//                Log.e("messagefull", String.valueOf(Arrays.asList(objsocial.msgIDFULL)));
                String[]msg= Constants.MessageID;
//                Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(msg)),Toast.LENGTH_LONG).show();
                Log.e("messageid",String.valueOf(Arrays.asList(msg)));
                params.put("msgid",String.valueOf(msg[itemposition]));
                params.put("uid",String.valueOf(sp_uid) );
                Log.e("uid id",sp_uid);

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

    public  void Like(int position, String uids){
        final String uid=uids;
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.LikeURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
//                        Toast.makeText(SocialMedia.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//                Log.e("message id",Jsonobjectmsgid.toString());
//                String[]msg=objsocial.messagefull();
//                Log.e("messagefull", String.valueOf(Arrays.asList(objsocial.msgIDFULL)));
                String[]msg= Constants.MessageID;
//                Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(msg)),Toast.LENGTH_LONG).show();
                Log.e("messageid",String.valueOf(Arrays.asList(msg)));
                params.put("messageid",String.valueOf(msg[itemposition]));
                params.put("uid",String.valueOf(uid) );
                Log.e("uid id",uid);

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
    public void Unlike(int position,String uids){
        final String uid=uids;
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.UnlikeURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
//                        Toast.makeText(SocialMedia.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//                Log.e("message id",Jsonobjectmsgid.toString());
//                String[]msg=objsocial.messagefull();
//                Log.e("messagefull", String.valueOf(Arrays.asList(objsocial.msgIDFULL)));
                String[]msg= Constants.MessageID;
//                Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(msg)),Toast.LENGTH_LONG).show();
                Log.e("messageid",String.valueOf(Arrays.asList(msg)));
                params.put("messageid",String.valueOf(msg[itemposition]));
                params.put("uid", String.valueOf(uid ));
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

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
