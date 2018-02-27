package com.avadharwebworld.avadhar.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.transition.Explode;
import android.view.Menu;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Adapter.CommentAdapter;
import com.avadharwebworld.avadhar.Adapter.NewsFeedAdapter;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.CommentItem;
import com.avadharwebworld.avadhar.Data.NewsFeedItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.HidingScrollListener;
import com.avadharwebworld.avadhar.Support.ScalingUtilities;
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
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SocialMedia extends AppCompatActivity {
    private String Uid_from_sharedpref,sp_name,sp_profileimgpath,sp_profileimage;
    ImageButton logout, profile, settings;
    de.hdodenhof.circleimageview.CircleImageView popopProfileimg;
    ProgressBar imageupdateprogress;
    private RecyclerView rv_social;
    LayoutInflater inflater;
    private ProgressDialog dialog;

    Constants consts=new Constants();
    private static final String TAG = SocialMedia.class.getSimpleName();
    CommentItem msgid;
    private String imageUploadid="",uid;
    private NewsFeedAdapter listAdapter;
    private CommentAdapter cmdAdapter;
    private List<NewsFeedItem> newsfeedItems,likeitems,mergenewsfeedItems;
    private List<CommentItem> commentItems;
    DatabaseInfo db=new DatabaseInfo();
    String Url_newsfeed=DatabaseInfo.NewsFeedURL,Url_likecheck=DatabaseInfo.LikeCheckURL,Url_Like=DatabaseInfo.LikeURL;
    String messageid_like;
    String UrldomainUploads=DatabaseInfo.ip+"avadharAndroid/uploads/";
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    public  String [] msgIDFULL,MsgidFull, timestamp,friendid,message_username,userprofpic,messageid={"",""},message,messageimage,messageurl,likecount,sharecount,commentcount,shareuid,shareouid,tempmsgid,templikeststus,stsimgsid;
    PopupWindow pw;
    Button popup_close,addphoto,update;
    Toolbar bottombar,toolbar;
    JSONArray Jsonarraymsgid,imageuid;
    JSONObject Jsonobjectmsgid,imguid;
    Handler handler;
    private LinearLayoutManager lm;
    private RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fab;
    int imgviewid=0,closebtnid=100,progressimgid=200;
    private static int RESULT_LOAD_IMAGE = 1;
    public String picturePath,commentimagepath;
    EditText statusupdateText;
    Button addinHorizontalScrollView, addinScrollView;
    LinearLayout inHorizontalScrollView, inScrollView;
    private int OFFSET=0;
    //Declare these variables globally in class
    int mVisibleThreshold = 5;
    int mCurrentPage = 0;
    int mPreviousTotal = 0;
    boolean mLoading = true;
    boolean mLastPage = false;
    boolean userScrolled = false;
    int getcurrentitem;
    private static RelativeLayout bottomLayout;
    JSONObject Jobjectlike;
    JSONObject jsonObject;

    CommentItem cmditems=new CommentItem();
    private NewsFeedAdapter newsadp;
    int pastVisiblesItems, visibleItemCount, totalItemCount,previousTotal,screenWidth,screenHeight;
    Display display;
    Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTEXT_MENU);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_social_media);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();//
        StrictMode.setThreadPolicy(policy);
        final SharedPreferences sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        Uid_from_sharedpref = sp.getString(Constants.UID, "");
        sp_name = sp.getString(Constants.U_NAME, "");
        sp_profileimage = sp.getString(Constants.PROFILEIMG, "");
        sp_profileimgpath = sp.getString(Constants.PROFILEIMGPATH, "");
        Log.e("sharedprefrence id", Uid_from_sharedpref);
//        Intent i=getIntent();
//        profileid= i.getExtras().getString("profileid");
//        uid=i.getExtras().getString("uid");
        dialog = new ProgressDialog(this);

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        logout = (ImageButton) findViewById(R.id.btn_main_logout);
        bottomLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_recyclerView);
        profile = (ImageButton) findViewById(R.id.btn_main_Profile);
        settings = (ImageButton) findViewById(R.id.btn_main_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.showContextMenu();
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dialog.setMessage("Loading...");
        dialog.show();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        setSupportActionBar(toolbar);
//        bottombar=(Toolbar)findViewById(R.id.bottom_toolbar);
        // bottombar.setVisibility(View.GONE);
        newsfeedItems = new ArrayList<NewsFeedItem>();
        likeitems = new ArrayList<NewsFeedItem>();
        mergenewsfeedItems = new ArrayList<NewsFeedItem>(likeitems);
//        prepareAdapterData();

        commentItems = new ArrayList<CommentItem>();
        cmdAdapter = new CommentAdapter(this, commentItems, getApplicationContext());

        listAdapter = new NewsFeedAdapter(this, newsfeedItems, getApplicationContext());
//        listAdapter=new NewsFeedAdapter(this,newsfeedItems,getApplicationContext(),commentItems); //test
        rv_social = (RecyclerView) findViewById(R.id.recycle_social);
        rv_social.setHasFixedSize(true);
//        rv_social.invalidate();
//        lm=new LinearLayoutManager(this);


        mLayoutManager = new LinearLayoutManager(this);
        rv_social.setLayoutManager(mLayoutManager);
//        rv_social.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

        rv_social.setItemAnimator(new DefaultItemAnimator());

//        listAdapter.setOnLoadMoreListener(new NewsFeedAdapter.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                newsfeedItems.add(null);
//                listAdapter.notifyItemInserted(newsfeedItems.size() - 1);
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        newsfeedItems.remove(newsfeedItems.size() - 1);
//                        listAdapter.notifyItemRemoved(newsfeedItems.size());
//                            getNewsUpdates();
//                            listAdapter.notifyItemInserted(newsfeedItems.size());
//
//                        listAdapter.setLoaded();
//                    }
//                }, 2000);
////                System.out.println("load");
//            }
//        });
//
//
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newpost();
            }
        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
//            }
//        });
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "profile", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "setting", Toast.LENGTH_SHORT).show();
//            }
//        });
        rv_social.addOnScrollListener(new OnScrollListener() {
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
                pastVisiblesItems = ((LinearLayoutManager) rv_social.getLayoutManager()).findFirstVisibleItemPosition();
                Log.e("past Visible item", String.valueOf(pastVisiblesItems));
                Log.e("visible item count", String.valueOf(visibleItemCount));

                if (dy > 0) {

//                    if(userScrolled){
//                        if (totalItemCount > previousTotal) {
//                            userScrolled = true;
//                            previousTotal = totalItemCount;
//                        }

                    if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                        userScrolled = false;
                        bottomLayout.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getNewsUpdates();
//                                listAdapter.
                                bottomLayout.setVisibility(View.GONE);
                            }
                        }, 3000);


                    }
                }
            }


        });
        rv_social.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();

            }
        });
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Url_newsfeed);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url_newsfeed+"?uid="+Uid_from_sharedpref+"&offset="+OFFSET,
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
                        dialog.hide();
                        Toast.makeText(SocialMedia.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", String.valueOf(Uid_from_sharedpref));
                params.put("offset", String.valueOf(OFFSET));
                OFFSET = OFFSET + 15;
                Log.e("uid send", Uid_from_sharedpref);
                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("newsfeed");
            String f1[]=new String[feedArray.length()];
            String M1[]=new String[feedArray.length()];
            String M2[]=new String[feedArray.length()];
            String M3[]=new String[feedArray.length()];
            String T1[]=new String[feedArray.length()];
            String S1[]=new String[feedArray.length()];
            String L1[]=new String[feedArray.length()];
            String C1[]=new String[feedArray.length()];
            String S2[]=new String[feedArray.length()];
            String M4[]=new String[feedArray.length()];
            String msgfull[]=new String[feedArray.length()];
            String S3[]=new String[feedArray.length()];

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                String Fid,Musername,Mimage,M,Timestamp,Sharecount,Likecount,Commentcount,Shareuid,Mid,Mdifull,Shareouid,imagepath;
                String comments,comments_image,comments_id,comments_username,comments_like_count,comments_created,comments_uid_fk;
                String []Acomments,Acomments_image,Acomments_id,Acomments_username,Acomments_like_count,Acomments_created,Acomments_uid_fk,NewsfeedimagePath;

                NewsFeedItem item = new NewsFeedItem();
                CommentItem comditm=new CommentItem();
//                Fid=feedObj.getString("uid_fk");
//                f1[i] =Fid;
//                Log.e("friend id",Fid);
//                Musername=feedObj.getString("username");
//                M1[i]=Musername;
//                Log.e("message username",Musername);
//                Mimage= feedObj.isNull("uploads")? null: feedObj.getString("uploads");
//                Mimage=minus(Mimage);
//                M2[i]=Mimage;
//                Log.e("message image",Mimage);
//                M=feedObj.getString("message");
//                M3[i]=M;
//                Log.e("message",M);
//                Timestamp=feedObj.getString("created");
//                T1[i]=Timestamp;
//                Log.e("Time stamp",Timestamp);
//                Sharecount=feedObj.getString("share_count");
//                S1[i]=Sharecount;
//                Log.e("Share count",Sharecount);
//                Likecount=feedObj.getString("like_count");
//                L1[i]=Likecount;
//                Log.e("Like Count",Likecount);
//                Commentcount=feedObj.getString("comment_count");
//                C1[i]=Commentcount;
//                Log.e("Comment Count",Commentcount);
//                Shareuid=feedObj.getString("share_uid");
//                S2[i]=Shareuid;
//                Log.e("Share UID",Shareuid);
                Mid=feedObj.getString("msg_id");
                Mdifull=feedObj.getString("msg_id");
                M4[i]=Mid;
                msgfull[i]=Mdifull;
                Log.e("message id",Mid);
//                Shareouid=feedObj.getString("share_ouid");
//                S3[i]=Shareouid;
//                Log.e("Share outid",Shareouid);

//                userprofpic[i]=feedObj.getString("");

                item.setMessageid(Integer.parseInt(feedObj.getString("msg_id")));
                item.setId(feedObj.getInt("uid_fk"));
                item.setName(feedObj.getString("name"));
                imagepath=feedObj.getString("upload_image");
                if(imagepath!=""){
                    NewsfeedimagePath=imagepath.split(",");
                    for(int j=0;j<NewsfeedimagePath.length;j++){
                        NewsfeedimagePath[j]=NewsfeedimagePath[j].replace(NewsfeedimagePath[j],DatabaseInfo.NewsfeedimageURL+NewsfeedimagePath[j]);
                    }
                }else{ NewsfeedimagePath=null;}

                item.setImgpath(NewsfeedimagePath);
                Log.e("image path",String.valueOf(Arrays.asList(NewsfeedimagePath)));
                Log.e("imagepath count",String.valueOf(NewsfeedimagePath.length));
                Log.e( String.valueOf(feedObj.getString("name")),"getdata1");// Image might be null sometimes
                String image = feedObj.isNull("uploads") ? null : feedObj.getString("uploads");
                item.setImge("");
                String shre=feedObj.getString("share_check");
                if(shre.equals("true")){
                    item.setShare(String.valueOf("Share"));
                }else if(shre.equals("false")){
                    item.setShare(String.valueOf("Unshare"));
                }

                Log.e( String.valueOf(feedObj.getString("uploads")),"getdata2");// Image might be null sometimes
                String like=feedObj.getString("like_check");
                if(like.equals("true")){
                    item.setLike(String.valueOf("Like"));
                }else if(like.equals("false")){
                    item.setLike(String.valueOf("Unlike"));
                }
                Log.e("like_check", item.getLike());

                item.setStatus((String.valueOf(feedObj.getString("message"))).replaceAll("[\\n\\r]+", "\\\\n").replaceAll((String.valueOf(extractUrls(feedObj.getString("message")))).substring(1,(String.valueOf(extractUrls(feedObj.getString("message")))).length()-1)," ").replace("\\n"," ").replace("\"",""));
              String a=(String.valueOf(feedObj.getString("message"))).replaceAll("[\\n\\r]+", "\\\\n").replaceAll((String.valueOf(extractUrls(feedObj.getString("message")))).substring(1,(String.valueOf(extractUrls(feedObj.getString("message")))).length()-1)," ").replace("\\n"," ").replace("\"","");


                Log.e(String.valueOf(feedObj.getString("message")), "getdata3");// Image might be null sometimes
                String msg_uid_profilepic=feedObj.isNull("uid_fk_profile_pic") ? null: feedObj.getString("uid_fk_profile_pic");
                Log.e("imgpath,uid",msg_uid_profilepic+ " uid "+feedObj.getInt("uid_fk"));
                item.setProfilePic(DatabaseInfo.ProfilepicURL+msg_uid_profilepic);
                Timestamp=feedObj.getString("created");
                item.setTimeStamp(feedObj.getString("created"));
                Log.e( String.valueOf(feedObj.getString("created")),"getdata4");// Image might be null sometimes

                // url might be null sometimes
                String feedUrl = feedObj.isNull("message") ? null : feedObj
                        .getString("message");
                String tempurl=(String.valueOf(extractUrls(feedObj.getString("message")))).substring(1,(String.valueOf(extractUrls(feedObj.getString("message")))).length()-1);
                String msg = (String.valueOf(String.valueOf(extractUrls(feedObj.getString("message")))).replace(tempurl,""));
                String msgurl = msg.replaceAll("(?:https?|ftps?)://[\\w/%.-]+", "<a href='$0'>$0</a>");
                item.setUrl((String.valueOf(extractUrls(feedObj.getString("message")))).substring(1,(String.valueOf(extractUrls(feedObj.getString("message")))).length()-1));

                String tempSuid=feedObj.getString("share_uid");
                if(!feedObj.getString("share_uid").equals("0")){
                    item.setShareUid(feedObj.getString("share_uid"));
                }else{item.setShareUid("0");}
                Log.e("Share UID",String.valueOf(feedObj.getString("share_uid")));
                Log.e("Share name",String.valueOf(feedObj.getString("share_ouid_fk_username")));
                Log.e("Share profile pic",String.valueOf(feedObj.getString("share_ouid_fk_profile_pic")));

                if(!feedObj.getString("share_ouid_fk_username").equals("0")){
                    item.setShareUsername(feedObj.getString("share_ouid_fk_username"));
                }else {item.setShareUsername("0");}

                if(!feedObj.getString("share_ouid_fk_profile_pic").equals("0")){
                    item.setShareprofilepic(DatabaseInfo.ProfilepicURL+feedObj.getString("share_ouid_fk_profile_pic"));
                }else {item.setShareprofilepic(DatabaseInfo.ProfilepicURL+"0");}

                newsfeedItems.add(item);

                //comment checkview
//                comments=feedObj.getString("comments");
//                comments_image=feedObj.getString("comments_image");
//                comments_id=feedObj.getString("comments_id");
//                comments_username=feedObj.getString("comments_username");
//                comments_like_count=feedObj.getString("comments_like_count");
//                comments_created=feedObj.getString("comments_created");
//                comments_uid_fk=feedObj.getString("comments_uid_fk");
//
//                Acomments=comments.split("#comment_seperate#");
//                Acomments_image=comments_image.split("#commentimg_seperate#");
//                Acomments_id=comments_id.split("#commentid_seperate#");
//                Acomments_username=comments_username.split("#commentusername_seperate#");
//                Acomments_like_count=comments_like_count.split("#commentlikecount_seperate#");
//                Acomments_created=comments_created.split("#commentcreated_seperate#");
//                Acomments_uid_fk=comments_uid_fk.split("#commentuid_seperate#");
////                for (int k=0;i<=Acomments_uid_fk.length;k++){
////                    if(Acomments_id!=null) {
////                        Log.e("commentid", String.valueOf(Arrays.asList(Acomments_id)));
//////                        comditm.setId(Integer.parseInt(Acomments_id[k]));
////                    }
////                    Log.e("comment",String.valueOf(Acomments[k]));
////                    comditm.setComment(Acomments[k]);
//////                    comditm.setLike(Acomments_like[k]);
////                    Log.e("comment_username",String.valueOf(Acomments_username[k]));
////                    comditm.setName(Acomments_username[k]);
////                    Log.e("comment_timestamp",String.valueOf(Acomments_created[k]));
////                    comditm.setTimeStamp(Acomments_created[k]);
//////                    comditm.setImge(Acomments_image[k]);
////                    Log.e("comment_uidfk",String.valueOf(Acomments_uid_fk[k]));
////                    comditm.setProfilePic("http://api.androidhive.info/feed/img/nat.jpg");
////                    commentItems.add(comditm);
////                }


                Log.e("fetch",String.valueOf(item));
//                imagepathid.put(M2[i]);
//                listAdapter .notifyItemInserted(newsfeedItems.size()-1);
//                listAdapter.  notifyItemRangeChanged(totalItemCount, newsfeedItems.size());
//                listAdapter.setSelectedItem(totalItemCount);

                listAdapter.notifyDataSetChanged();

            }


//            friendid=f1;
//            Log.e("final frined id",String.valueOf(friendid));
////            Log.e("message id ",String.valueOf(M4));
//
//            message_username=M1;
//            Log.e("message user",String.valueOf(message_username));
//            messageimage=M2;
//            Log.e("mesgeimg",String.valueOf(messageimage));
//            message=M3;
//            Log.e("message",String.valueOf(message));
//            timestamp=T1;
//            Log.e("timest",String.valueOf(timestamp));
//            sharecount=S1;
//            Log.e("final  share count",String.valueOf(sharecount));
//            likecount=L1;
//            Log.e("final  like con",String.valueOf(likecount));
//            commentcount=C1;
//            Log.e("final coment cont",String.valueOf(commentcount));
//            shareuid=S2;
//            Log.e("final  sahe id",String.valueOf(shareuid));
            messageid=M4;
//
//            msgIDFULL=msgfull;
//            String[] tempmsgIDFULL=null;
//            tempmsgIDFULL=msgIDFULL;

            msgIDFULL= ArrayUtils.addAll(msgIDFULL,msgfull);
            Log.e("messagefull", String.valueOf(Arrays.asList(msgIDFULL)));
            Log.e("final message id",String.valueOf(messageid));
//            shareouid=S3;
//            Log.e("final share o id",String.valueOf(shareouid));
////            Log.e("json ",imagepathid.toString());
//            imageuid=new JSONArray();
//            imagepathid=new JSONArray();
//            imageuid=new JSONArray(Arrays.asList(friendid));
//            imagepathid=new JSONArray(Arrays.asList(messageimage));
//            String tmp=imagepathid.toString();
//            Log.e("json array",tmp);
////            for (int i = 0; i < imagepathid.length(); i++) {
////                String obj=imagepathid.getString(i);
////                imgpathid.put("message_upload_id",obj);
////            }
//               imguid=new JSONObject();
//                imgpathid=new JSONObject();
//                imgpathid.put("message_upload_id",imagepathid);
//                imguid.put("friend_id",imageuid);
////
//            Log.e("friend id",String.valueOf(imguid));
//            Log.e("json",String.valueOf(imgpathid));
//            imgpathid;

            // notify data changes to list adapater
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Jsonobjectmsgid=new JSONObject();
        jsonObject=new JSONObject();
        try{

            Jsonarraymsgid=new JSONArray((Arrays.asList(messageid)));
            for(int i=0;i<Jsonarraymsgid.length();i++){
                String obj=Jsonarraymsgid.getString(i);
                Log.e("obj",obj);
                Jsonobjectmsgid.put("messageid",messageid);
            }
            jsonObject.put("messageid",Arrays.deepToString(messageid));
            Log.e("message id new",String.valueOf(jsonObject));
        }catch (JSONException e){}
//        Toast.makeText(getApplicationContext(),String.valueOf(Jsonobjectmsgid),Toast.LENGTH_SHORT).show();


        listAdapter.notifyDataSetChanged();
        rv_social.scrollToPosition(totalItemCount);
        Log.e("adas","adasd");
//        LikeCheck();
//        Toast.makeText(getApplicationContext(),"total item count"+String.valueOf(totalItemCount),Toast.LENGTH_SHORT).show();
        rv_social.setAdapter(listAdapter);
        dialog.hide();
        consts.setMessageID(msgIDFULL);
//        Toast.makeText(getApplicationContext(),String.valueOf(Arrays.asList(msgIDFULL)),Toast.LENGTH_LONG).show();

//        Toast.makeText(getApplicationContext(),String.valueOf(  rv_social.getChildAdapterPosition(rv_social.getFocusedChild())),Toast.LENGTH_SHORT).show();
    }


    private void hideViews() {
//        getSupportActionBar().setShowHideAnimationEnabled(true);
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//        getSupportActionBar().hide();
//        bottombar.animate().translationY(bottombar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab.getLayoutParams();
//        int fabBottomMargin = lp.bottomMargin;
//        fab.animate().translationY(fab.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
//        bottombar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//        bottombar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
//        getSupportActionBar().show();
    }
    private void newpost() {
        try {

// We need to get the instance of the LayoutInflater
            inflater = (LayoutInflater)SocialMedia.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.statusupdate,
                    (ViewGroup) findViewById(R.id.cv_status_update));
            pw = new PopupWindow(layout,screenWidth-10, screenHeight/2, true);
            pw.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            popup_close = (Button) layout.findViewById(R.id.btn_close_popup_status);
            imageupdateprogress=(ProgressBar)layout.findViewById(R.id.pbimage);
            popopProfileimg=(de.hdodenhof.circleimageview.CircleImageView)layout.findViewById(R.id.iv_profile_img);
            File file = new File(sp_profileimgpath);
            popopProfileimg.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

            imageupdateprogress.setVisibility(View.INVISIBLE);
            statusupdateText=(EditText)layout.findViewById(R.id.et_status);
            update=(Button)layout.findViewById(R.id.btn_update_status);

            statusupdateText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    statusupdateText.setTextIsSelectable(false);
//                    statusupdateText.selectAll();
                return true;
                }
            });

            inHorizontalScrollView=(LinearLayout)layout.findViewById(R.id.ll_image_view);
            popup_close.setOnClickListener(cancel_button);
            addphoto=(Button)layout.findViewById(R.id.btn_addphoto);
            addphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
//                    Toast.makeText(getApplicationContext(),picturePath,Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUploadid.equals("")){imageUploadid="0";}
                updateStatus(String.valueOf(statusupdateText.getText()),imageUploadid);
                imageUploadid="";
                statusupdateText.setText("");
                pw.dismiss();
            }
        });
        deleteviewImage(inHorizontalScrollView);
    }

    public void deleteviewImage(final LinearLayout layout){
        ImageView imageView=new ImageView(this);
        final Button btn=new Button(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeViewAt(btn.getId()-100);
            }
        });

    }
    private View.OnClickListener cancel_button = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
//            layout.getForeground().setAlpha(0);
        }
    };
    public void addImageView(final LinearLayout layout) {
        int tempimgid,tempbtnid,tempprogrssid;
        final ImageView imageView = new ImageView(this);
        imageView.setId(imgviewid+1);
        tempimgid=imgviewid+1;

        final Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
        imageView.setImageBitmap(bmImg);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
        imageView.setPadding(5,5,5,5);
        imageView.setLayoutParams(layoutParams);

        final Button button=new Button(this);
        button.setBackgroundResource(R.drawable.ic_cancel_black_24dp);
        LinearLayout.LayoutParams layoutParamsbtn = new LinearLayout.LayoutParams(40, 40);
        button.setLayoutParams(layoutParamsbtn);
        button.setPadding(-2,-5,0,0);
        button.setId(closebtnid+1);
        tempbtnid=closebtnid+1;

//        final ProgressBar progressimage=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
//        progressimage.setPadding(0,-10,-40,-40);
//        LinearLayout.LayoutParams layoutParamsPB = new LinearLayout.LayoutParams( 200,40);
//        progressimage.setLayoutParams(layoutParamsPB);
//        progressimage.setId(progressimgid+1);
//
//        tempprogrssid=progressimgid+1;


        layout.addView(imageView);
        layout.addView(button);

//        layout.addView(imageupdateprogress);
      //  layout.addView(progressimage);
        imgviewid=tempimgid;
        closebtnid=tempbtnid;
//        progressimgid=tempprogrssid;
        button.setOnClickListener(new View.OnClickListener() {
            int index;
            @Override
            public void onClick(View v) {
                index=layout.indexOfChild(v);
                layout.removeViewAt(index);
                layout.removeViewAt(index-1);
//                layout.removeViewInLayout(v);
//                  layout.indexOfChild(v);
//                Toast.makeText(getApplicationContext(),"index Id "+  v.getTag().toString(),Toast.LENGTH_SHORT).show();
//                layout.vi
                Log.e("index "+String.valueOf(layout.indexOfChild(v))," Index id ");
                ;


        //layout.removeView(v);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id;
        Toast.makeText(getApplicationContext(),"request code :"+String.valueOf(requestCode),Toast.LENGTH_SHORT).show();
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
               imageupdateprogress.setVisibility(View.VISIBLE);
              // inHorizontalScrollView.addView(imageupdateprogress);
               statusImageUpload(picturePath1,imgviewid);
               Constants.REALESTATEIMAGE=picturePath;


           }
       }
        else if(requestCode==2) {
           if (resultCode == RESULT_OK && null != data) {
//               cmdAdapter=newsadp.cmdadapter();
               Uri selectedImage = data.getData();
               String[] filePathColumn = {MediaStore.Images.Media.DATA};

               Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
               cursor.moveToFirst();

               int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             final  String picturePath1 = cursor.getString(columnIndex);
//               commentimagepath=picturePath1;
               Toast.makeText(getApplicationContext(), picturePath1, Toast.LENGTH_SHORT).show();

               cursor.close();
               File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),picturePath1);

              final Bitmap cmBitmap=BitmapFactory.decodeFile(picturePath1);
//               ByteArrayOutputStream baos = new ByteArrayOutputStream();
//               cmBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//               byte[] imageBytes = baos.toByteArray();
//               Constants.PHOTOCOMMENTBYTE=imageBytes;


                   listAdapter.uploadcommentphoto(reduceFileSize(picturePath1,250,250),listAdapter.getPosition());



               Log.i("complete","method run");
               try {
                   Constants.PHOTOCOMMENTSTRING=listAdapter.getStringImage(cmBitmap,file);

                   Toast.makeText(getApplicationContext(),Constants.PHOTOCOMMENTSTRING,Toast.LENGTH_SHORT).show();
//                   new Thread(new Runnable() {
//                       @Override
//                       public void run() {
//                           //creating new thread to handle Http Operations
//                           listAdapter.uploadcommentphoto(picturePath1,listAdapter.getPosition());
                           Log.i("complete","method run");
//                       }
//                   }).start();
////
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
               }
               long timeNow = System.currentTimeMillis()/1000L;
               String currentTimestamp=String.valueOf(timeNow);
               listAdapter.cmdImage(picturePath1,currentTimestamp);
               cmBitmap.recycle();
//               Toast.makeText(getApplicationContext(), "toast new", Toast.LENGTH_SHORT).show();
           }
       }
//

    }




    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();

        Intent intent = new Intent(SocialMedia.this, MainActivity.class);
        startActivity(intent);
//        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=null;
        switch (item.getItemId()){
            case android.R.id.home:
              onBackPressed();
            case R.id.menu_home_profile:
                intent=new Intent(this,Profile.class);
                intent.putExtra("id",  Uid_from_sharedpref);
                break;
            case R.id.menu_home_following:
                intent=new Intent(this,Following.class);

                break;
            case R.id.menu_home_followers:
                intent=new Intent(this,Followers.class);
                break;
            case R.id.menu_home_settings:
                intent=new Intent(this,SettingsActivity.class);
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.activity_home_drawer, menu);
//        final EditText editText = (EditText) menu.findItem(
//                R.id.menu_search).getActionView();
//        editText.addTextChangedListener(textWatcher);

//        MenuItem menuItem = menu.findItem(R.id.menu_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.socialmedia_action_search));
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        final EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(getApplicationContext(),query+"/ edit text value : "+searchPlate.getText().toString(),Toast.LENGTH_LONG ).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when collapsed
//                return true; // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//               // editText.clearFocus();
//                return true; // Return true to expand action view
//            }
//        });
        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }

  public String minus(String string){
        string=string.substring(0,string.length()-1);
      return string;
  }
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }


    public void getNewsUpdates() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,  Url_newsfeed+"?uid="+Uid_from_sharedpref+"&offset="+OFFSET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse", response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("jobnect", String.valueOf(Jobject));
                            parseJsonFeed(Jobject); OFFSET+=15;
                        } catch (JSONException e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        Toast.makeText(SocialMedia.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid",String.valueOf(Uid_from_sharedpref));
                params.put("offset", String.valueOf(OFFSET));
                OFFSET+=15;
                Log.e("Uid send",String.valueOf(Uid_from_sharedpref));
                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
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
    public void LikeCheck(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url_likecheck,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        try {
                            Jobjectlike= (JSONObject) new JSONTokener(response).nextValue();
                            Log.e("volleyJson", Jobjectlike.toString());
                            Toast.makeText(getApplicationContext(),String.valueOf(Jobjectlike),Toast.LENGTH_SHORT).show();
//                          Log.e("url string",url);
                            // JSONObject json_data=null;
//                                jArray = Jobject.getJSONArray("result");
//                                Log.e("Jarray count", jArray.toString());
//  String Status = Jobject.getString("status");


                                JSONArray msglike=Jobjectlike.getJSONArray("message_like");
                                String msg1[]=new String[msglike.length()];
                                String status1[]=new String[msglike.length()];
                            for (int i=0;i<msglike.length();i++){
                                JSONObject obj=(JSONObject)msglike.get(i);
                                String msg=obj.getString("msgid");
                                msg1[i]=msg;
                                Log.e("msg",msg);
                                String sts=obj.getString("status");
                                status1[i]=sts;
                                Log.e("status",sts);
                            }
                            tempmsgid=msg1;
                            templikeststus=status1;
                            Log.e("status",Arrays.toString(templikeststus));



                        } catch (JSONException e) {}
                        _likecheck();
//                        rv_social.setAdapter(listAdapter);
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        Toast.makeText(SocialMedia.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//                Log.e("message id",Jsonobjectmsgid.toString());
                params.put("messageid",Jsonarraymsgid.toString());
//                params.put("uid",uid );
                Log.e("message id",Jsonobjectmsgid.toString());
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
    public void _likecheck(){
        NewsFeedItem items=new NewsFeedItem();
        for(int i=0;i<templikeststus.length;i++){
            Log.e("temp like status",String.valueOf(templikeststus[i]));
            if(templikeststus[i].equals("true")){

                items.setLike("Like");
            }else if(templikeststus[i].equals("false")){
                items.setLike("Unlike");
            }
            likeitems.add(items);
        }
        newsfeedItems.addAll(likeitems);
        mergenewsfeedItems.addAll(newsfeedItems);
//        mergenewsfeedItems.addAll(newsfeedItems);

        listAdapter.notifyDataSetChanged();
        rv_social.scrollToPosition(totalItemCount);
        rv_social.setAdapter(listAdapter);

    }

    public void setMsgid(String[] msgid){
        this.MsgidFull=msgid;
        Toast.makeText(getApplicationContext(),String.valueOf(Arrays.asList(getMsgidFull())),Toast.LENGTH_LONG).show();

    }
    public String[] getMsgidFull(){
        return this.MsgidFull;
    }
    private String reduceFileSize(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

    }
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

//            if (null != mAdapter) {
//                mAdapter.getFilter().filter(s);
//            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    private void statusImageUpload(String Filepath,int imgcount) {
        final String file=Filepath;
        final int imgcoutpos=imgcount;
        byte[] newbyteFile= new byte[8012];
        File resultfile=new File(file);
        RequestParams params = new RequestParams();

//        newbyteFile=imgbyte;
        try {
            params.put("statusimg",  resultfile,"image/jpg");
            params.put("uid", Uid_from_sharedpref);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(DatabaseInfo.StatusImageUploadURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
                addImageView(inHorizontalScrollView);
                imageupdateprogress.setVisibility(View.INVISIBLE);
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
                    imageUploadid=imageUploadid.concat(response.trim()+",");
                    Log.e("image names",String.valueOf(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }



                Log.e("file path", file);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("file not send","failure");
                imageupdateprogress.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Image Selection Unsuccessfull!!!",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFinish() {
//             imageupdateprogress.setVisibility(View.GONE);
            }

        });
    }
    public void updateStatus(String update,String imageid){
        final String updte=update;
        final String imgid=imageid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.UpdateStatusURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("Volleyresponse",response.toString());

                        try {
//
                            long timeNow = System.currentTimeMillis()/1000L;
                            String currentTimestamp=String.valueOf(timeNow);
                            int zero=0;
                            String Url=((String.valueOf(extractUrls(updte))).substring(1,(String.valueOf(extractUrls(updte))).length()-1));
                            addItemToNewsfeed(updte,currentTimestamp,Url,Integer.parseInt(response.trim()),"",zero);

                            //update message id array ******
                            String[] tempmsgfull=Constants.MessageID;
                            String[] tempnewmsgfull=new String[1];
                            tempnewmsgfull[0]=response.trim();
                            Constants.MessageID=ArrayUtils.addAll(tempnewmsgfull,tempmsgfull);
                            //*********
                        } catch (Exception e) {}

                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pDialog.hide();
                        Toast.makeText(SocialMedia.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
//                Log.e("message id",Jsonobjectmsgid.toString());
                params.put("uid",String.valueOf(Uid_from_sharedpref));
                params.put("update",String.valueOf(updte));
                params.put("uploadid",String.valueOf(imgid));
                Log.e("update uid",Uid_from_sharedpref);
                Log.e("update status",String.valueOf(updte));
                Log.e("upload image id",String.valueOf(imgid));
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
    // add item to newsfeed adapter offline
    public void addItemToNewsfeed(String status,String timestamp,String url,int messageid,String statusImage,int position){
        String[] temp=new String[0];
        NewsFeedItem item=new NewsFeedItem();
        item.setShare(getResources().getString(R.string.share));
        item.setLike(getResources().getString(R.string.like));
        item.setCommentcount("0");
        item.setShareUid("0");
        item.setName(sp_name);
        item.setProfilePic(DatabaseInfo.ProfilepicURL + sp_profileimage);
        item.setStatus(status);
        item.setTimeStamp(timestamp);
        item.setUrl(url);
        item.setMessageid(messageid);
        item.setId(Integer.parseInt(Uid_from_sharedpref));
        item.setImgpath(temp);

        this.newsfeedItems.add(0,item);

        listAdapter.notifyItemInserted(0);

        Log.e("adapter position",String.valueOf(newsfeedItems.size()-1));
        rv_social.scrollToPosition(0);
    }
}


