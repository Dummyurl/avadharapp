package com.avadharwebworld.avadhar.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.ProfileAdapter;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.CommentItem;
import com.avadharwebworld.avadhar.Data.NewsFeedItem;
import com.avadharwebworld.avadhar.Data.ProfileViewItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.ProfileImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.ArrayUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {
    private RecyclerView rv_profile;
    private ProgressDialog dialog;
    private ProfileAdapter adapter;
    private List<NewsFeedItem> feedItems;
    String picturePath, picturePathcover,uid;
    private List<ProfileViewItem> profileViewItems;
    JSONArray Jsonarraymsgid;
    JSONObject Jsonobjectmsgid;
    private RecyclerView.LayoutManager mLayoutManager;
    JSONObject jsonObject;
    ProfileImageView profilepic,coverpic;
    boolean userScrolled = false;
    private int[] mdataType = null;
    public String[] msgIDFULL, messageid = {"", ""};
    Constants consts;
    private String sp_uid, sp_name, sp_profileimage, sp_profileimgpath;

    int pastVisiblesItems, OFFSET = 0, visibleItemCount, totalItemCount, previousTotal, screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        Intent i=getIntent();
        uid= i.getExtras().getString("id");
        final SharedPreferences sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid = sp.getString(Constants.UID, "");
        sp_name = sp.getString(Constants.U_NAME, "");
        sp_profileimage = sp.getString(Constants.PROFILEIMG, "");
        sp_profileimgpath = sp.getString(Constants.PROFILEIMGPATH, "");

        feedItems = new ArrayList<NewsFeedItem>();
        profileViewItems = new ArrayList<ProfileViewItem>();
        rv_profile = (RecyclerView) findViewById(R.id.recycle_profile);
        profilepic=(ProfileImageView)findViewById(R.id.prof_profileimage) ;
        coverpic=(ProfileImageView)findViewById(R.id.prof_coverimage);
        adapter = new ProfileAdapter(this, getApplicationContext(), feedItems, profileViewItems);
        rv_profile.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rv_profile.setLayoutManager(mLayoutManager);
        rv_profile.setItemAnimator(new DefaultItemAnimator());
        Cache cache = AppController.getInstance().getRequestQueue().getCache();

        rv_profile.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) ;
                {
                    userScrolled = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();

                Log.e("total item count", String.valueOf(totalItemCount));
                pastVisiblesItems = ((LinearLayoutManager) rv_profile.getLayoutManager()).findFirstVisibleItemPosition();
                Log.e("past Visible item", String.valueOf(pastVisiblesItems));
                Log.e("visible item count", String.valueOf(visibleItemCount));

                if (dy > 0) {


                    if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                        userScrolled = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getNewsUpdates();
                            }
                        }, 3000);


                    }
                }
            }


        });
        dialog.show();
        getProfileDetails();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
        Intent intent = null;
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        } else {
            switch (item.getItemId()) {

                case R.id.menu_profile_followers:
                    intent = new Intent(this, Followers.class);

                    break;
                case R.id.menu_profile_following:
                    intent = new Intent(this, Following.class);

                    break;
            }
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void parseJsonProfile(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("profiledetails");


            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                ProfileViewItem item = new ProfileViewItem();
                item.setConversationcount(feedObj.getString("conversation_count"));
                item.setCoverpic(DatabaseInfo.CoverPicUploadURl + feedObj.getString("cover_photo"));
                item.setEmail(feedObj.getString("email"));
                item.setName(feedObj.isNull("full_name") ? "null" : feedObj.getString("full_name"));
                item.setProfileid(feedObj.getString("profileid"));
                item.setProfilepic_status(feedObj.getString("profile_pic_status"));
                item.setUid(Integer.parseInt(feedObj.getString("uid")));
                item.setUpdates_count(feedObj.getString("updates_count"));
                item.setProfilepic(DatabaseInfo.ProfilepicURL + feedObj.getString("profile_pic"));
                item.setUsername(feedObj.getString("username"));
                item.setFriend_count(feedObj.getString("friend_count"));
                Log.e("profile details", String.valueOf(response));
                profileViewItems.add(item);
                getNewsUpdates();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("profile");
            String f1[] = new String[feedArray.length()];
            String M1[] = new String[feedArray.length()];
            String M2[] = new String[feedArray.length()];
            String M3[] = new String[feedArray.length()];
            String T1[] = new String[feedArray.length()];
            String S1[] = new String[feedArray.length()];
            String L1[] = new String[feedArray.length()];
            String C1[] = new String[feedArray.length()];
            String S2[] = new String[feedArray.length()];
            String M4[] = new String[feedArray.length()];
            String msgfull[] = new String[feedArray.length()];
            String S3[] = new String[feedArray.length()];
            int type[] = new int[feedArray.length() + 1];
            mdataType = new int[feedArray.length() + 1];
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                String Fid, Musername, Mimage, M, Timestamp, Sharecount, Likecount, Commentcount, Shareuid, Mid, Mdifull, Shareouid, imagepath;
                String comments, comments_image, comments_id, comments_username, comments_like_count, comments_created, comments_uid_fk;
                String[] Acomments, Acomments_image, Acomments_id, Acomments_username, Acomments_like_count, Acomments_created, Acomments_uid_fk, NewsfeedimagePath;
                if (i == 0) {
                    type[i] = 0;
                } else {
                    type[i] = i;
                }

                NewsFeedItem item = new NewsFeedItem();
                CommentItem comditm = new CommentItem();
                Mid = feedObj.getString("msg_id");
                Mdifull = feedObj.getString("msg_id");
                M4[i] = Mid;
                msgfull[i] = Mdifull;
                Log.e("message id", Mid);

                item.setMessageid(Integer.parseInt(feedObj.getString("msg_id")));
                item.setId(feedObj.getInt("uid_fk"));
                item.setName(feedObj.getString("name"));
                imagepath = feedObj.getString("upload_image");
                if (imagepath != "") {
                    NewsfeedimagePath = imagepath.split(",");
                    for (int j = 0; j < NewsfeedimagePath.length; j++) {
                        NewsfeedimagePath[j] = NewsfeedimagePath[j].replace(NewsfeedimagePath[j], DatabaseInfo.NewsfeedimageURL + NewsfeedimagePath[j]);
                    }
                } else {
                    NewsfeedimagePath = null;
                }

                item.setImgpath(NewsfeedimagePath);
                Log.e("image path", String.valueOf(Arrays.asList(NewsfeedimagePath)));
                Log.e("imagepath count", String.valueOf(NewsfeedimagePath.length));
                Log.e(String.valueOf(feedObj.getString("name")), "getdata1");// Image might be null sometimes
                String image = feedObj.isNull("uploads") ? null : feedObj.getString("uploads");
                item.setImge("");
                String shre = feedObj.getString("share_check");
                if (shre.equals("true")) {
                    item.setShare(String.valueOf("Share"));
                } else if (shre.equals("false")) {
                    item.setShare(String.valueOf("Unshare"));
                }

                Log.e(String.valueOf(feedObj.getString("uploads")), "getdata2");// Image might be null sometimes
                String like = feedObj.getString("like_check");
                if (like.equals("true")) {
                    item.setLike(String.valueOf("Like"));
                } else if (like.equals("false")) {
                    item.setLike(String.valueOf("Unlike"));
                }
                Log.e("like_check", item.getLike());

                item.setStatus((String.valueOf(feedObj.getString("message"))).replaceAll("[\\n\\r]+", "\\\\n").replaceAll((String.valueOf(extractUrls(feedObj.getString("message")))).substring(1, (String.valueOf(extractUrls(feedObj.getString("message")))).length() - 1), " ").replace("\\n", " ").replace("\"", ""));
                Log.e(String.valueOf(feedObj.getString("message")), "getdata3");// Image might be null sometimes
                String msg_uid_profilepic = feedObj.isNull("uid_fk_profile_pic") ? null : feedObj.getString("uid_fk_profile_pic");
                Log.e("imgpath :", msg_uid_profilepic + " uid : " + feedObj.getInt("uid_fk"));
                item.setProfilePic(DatabaseInfo.ProfilepicURL + msg_uid_profilepic);
                Timestamp = feedObj.getString("created");
                item.setTimeStamp(feedObj.getString("created"));
                Log.e(String.valueOf(feedObj.getString("created")), "getdata4");// Image might be null sometimes

                 String feedUrl = feedObj.isNull("message") ? null : feedObj
                        .getString("message");
                String tempurl = (String.valueOf(extractUrls(feedObj.getString("message")))).substring(1, (String.valueOf(extractUrls(feedObj.getString("message")))).length() - 1);
                String msg = (String.valueOf(String.valueOf(extractUrls(feedObj.getString("message")))).replace(tempurl, ""));
                String msgurl = msg.replaceAll("(?:https?|ftps?)://[\\w/%.-]+", "<a href='$0'>$0</a>");
                item.setUrl((String.valueOf(extractUrls(feedObj.getString("message")))).substring(1, (String.valueOf(extractUrls(feedObj.getString("message")))).length() - 1));

                String tempSuid = feedObj.getString("share_uid");
                if (!feedObj.getString("share_uid").equals("0")) {
                    item.setShareUid(feedObj.getString("share_uid"));
                } else {
                    item.setShareUid("0");
                }
                Log.e("Share UID", String.valueOf(feedObj.getString("share_uid")));
                Log.e("Share name", String.valueOf(feedObj.getString("share_ouid_fk_username")));
                Log.e("Share profile pic", String.valueOf(feedObj.getString("share_ouid_fk_profile_pic")));

                if (!feedObj.getString("share_ouid_fk_username").equals("0")) {
                    item.setShareUsername(feedObj.getString("share_ouid_fk_username"));
                } else {
                    item.setShareUsername("0");
                }

                if (!feedObj.getString("share_ouid_fk_profile_pic").equals("0")) {
                    item.setShareprofilepic(DatabaseInfo.ProfilepicURL + feedObj.getString("share_ouid_fk_profile_pic"));
                } else {
                    item.setShareprofilepic(DatabaseInfo.ProfilepicURL + "0");
                }

                feedItems.add(item);

                Log.e("fetch", String.valueOf(item));
                dialog.hide();


            }
            adapter.notifyDataSetChanged();

            messageid = M4;
            mdataType = type;
            Constants.PROFDATASET = mdataType;
            msgIDFULL = ArrayUtils.addAll(msgIDFULL, msgfull);
            Log.e("messagefull", String.valueOf(Arrays.asList(msgIDFULL)));
            Log.e("final message id", String.valueOf(messageid));
            Log.e("item type", String.valueOf(Arrays.toString(mdataType)));
            Log.e("Item const type", String.valueOf(Arrays.toString(Constants.PROFDATASET)));

        } catch (JSONException e) {
            e.printStackTrace();
            dialog.hide();
        }
        Jsonobjectmsgid = new JSONObject();
        jsonObject = new JSONObject();
        try {

            Jsonarraymsgid = new JSONArray((Arrays.asList(messageid)));
            for (int i = 0; i < Jsonarraymsgid.length(); i++) {
                String obj = Jsonarraymsgid.getString(i);
                Log.e("obj", obj);
                Jsonobjectmsgid.put("messageid", messageid);
            }
            jsonObject.put("messageid", Arrays.deepToString(messageid));
            Log.e("message id new", String.valueOf(jsonObject));
        } catch (JSONException e) {
        }

        dialog.hide();
        adapter.notifyDataSetChanged();
        rv_profile.scrollToPosition(totalItemCount);
        rv_profile.setAdapter(adapter);

        Log.e("adas", "adasd");

         Constants.MessageID = msgIDFULL;
       }

    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public void getNewsUpdates() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.ProfileUpdatesURL+"?uid="+sp_uid+"&offset="+OFFSET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            parseJsonFeed(Jobject);
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        dialog.hide();
                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(uid));
//                params.put("offset", String.valueOf(OFFSET));
//                OFFSET+=+15;
                Log.e("Uid send", String.valueOf(sp_uid));
                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


//        if (entry != null) {
//            // fetch the data from cache
//            try {
//                String data = new String(entry.data, "UTF-8");
//                try {
//                    parseJsonFeed(new JSONObject(data));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            // making fresh volley request and getting json
//            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
//                    Url_newsfeed, null, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    VolleyLog.d(TAG, "Response: " + response.toString());
//                    if (response != null) {
//                        parseJsonFeed(response);
//                    }
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                }
//
//            }){
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("offset", String.valueOf(OFFSET));
//                    return params;
//                }
//
//            };
//
//            // Adding request to volley request queue
//            AppController.getInstance().addToRequestQueue(jsonReq);
//

    }

    public void getProfileDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.ProfileDatailsURL+"?uid="+uid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            parseJsonProfile(Jobject);

                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        dialog.hide();
                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(uid));
//                params.put("offset", String.valueOf(OFFSET));
//                OFFSET+=+15;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.profile_menu, menu);
//
        return true;
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id;
        if (requestCode == 1) {
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
                updateProfilePicture();

            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath1 = cursor.getString(columnIndex);
                cursor.close();

                picturePathcover = picturePath1;
                coverpic.setImageBitmap(BitmapFactory.decodeFile(picturePathcover));
                updateCoverPicture();


            }
        }

    }

    private void updateProfilePicture() {
        if (picturePath != "") {
            File photo = new File(picturePath);
//        File resume=new File(resumepath);
////        Log.e("resumepath",resumepath);

            RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;
            try {

                params.put("file", photo);

                params.put("uid", sp_uid);

                // params.put("uid", Uid_from_sharedpref);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(DatabaseInfo.UpdateProfilePicURL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
//                addImageView(inHorizontalScrollView);
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                    byte[] bite = responseBody;
                    try {
                        final String response = new String(bite, "UTF-8");
                        System.out.println("response " + response);
//
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
    private void updateCoverPicture() {
        if (picturePathcover != "") {
            File photo = new File(picturePathcover);
//        File resume=new File(resumepath);
////        Log.e("resumepath",resumepath);

            RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;
            try {

                params.put("file", photo);

                params.put("uid", sp_uid);

                // params.put("uid", Uid_from_sharedpref);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(DatabaseInfo.UpdateCoverPicURL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
//                addImageView(inHorizontalScrollView);
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                    byte[] bite = responseBody;
                    try {
                        final String response = new String(bite, "UTF-8");
                        System.out.println("response " + response);
//
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
}
