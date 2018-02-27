package com.avadharwebworld.avadhar.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Activity.Followers;
import com.avadharwebworld.avadhar.Activity.Following;
import com.avadharwebworld.avadhar.Activity.Login;
import com.avadharwebworld.avadhar.Activity.MainActivity;
import com.avadharwebworld.avadhar.Activity.SocialMedia;
import com.avadharwebworld.avadhar.Activity.StatusView;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.CommentItem;
import com.avadharwebworld.avadhar.Data.NewsFeedItem;
import com.avadharwebworld.avadhar.Data.ProfileViewItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;
import com.avadharwebworld.avadhar.Support.ProfileImageView;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user-03 on 08-Dec-16.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.FirstFrameViewHolder> {
    String filePath;
    private SocialMedia socialMedia;
    private int lastPosition = -1;
    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private int MessagePosition;
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageButton comment_add_image;
    public String picturePath;
    final CommentItem item=new CommentItem();
    Login objlogin=new Login();
    Constants objconsts=new Constants();
    PopupWindow comment_window;
    private CommentItem comd;
    private EditText edit_text_comment;
    public String messageid_like;
    public static TextView tv_comment_count;
    private Activity activity;
    private String Userprofileic;

    private LayoutInflater inflater;
    private List<NewsFeedItem> feedItems;
    private RecyclerView rvsocial,commentrecycler;
    private int id;
    public static int comment_no;
    private LinearLayoutManager lln;
    private List<CommentItem> cmditem;

    private Activity mActivity;
    private Context mContext;
    private List<NewsFeedItem> newsFeedItems;
    private List<ProfileViewItem>profileViewItems;
    private static int selectedItem = -1;
    private int[] mDatasettype={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private ImageView comment_profile_pic;
    private String profile_bitmap;
    SharedPreferences sp;
    private String name,status,image,timestamp,url,profilepic,sp_uid,tempimg,tempimgpath,sp_profileimg,sp_Usrname,sp_profileimgpath;
    public TextView Uname,Ustatus,Utimestamp,UsrUrl;
    public NetworkImageView profilepicimg;
    public  FeedImageView feedimage;
    SocialMedia sm;
    private String[] MsgidFull,newsfeedimagepath,commentitems_uid,commentlikecount;
    private CommentAdapter CAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //    private ProgressBar progressBar;
    private ImageSliderAdapter ObjSliderAdapter;
    private NewsFeedAdapter.OnLoadMoreListener mOnLoadMoreListener;
    MediaController videocontorls;
    private boolean loading;
    private int visibleThreshold = 5,lineCnt;
    private int lastVisibleItem, totalItemCount;

    String[] imgurls;
    String suid,sname,stime,slike,sshare,sid,profimgurl;
    //    public ImageView UStatusimage,Userprofilepic;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    public ProfileAdapter(Activity activity, Context context,List<NewsFeedItem> item,List<ProfileViewItem> prof){
        this.mActivity=activity;
        this.mContext=context;
        this.newsFeedItems=item;
        this.profileViewItems=prof;

        Log.e("pro adptr datatype",String.valueOf(Arrays.toString(mDatasettype)));

    }
    @Override
    public ProfileAdapter.FirstFrameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        if(viewType== Constants.PROFILECARD) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_first, parent, false);
            return new FirstFrameViewHolder(itemView);

        }
        else {

            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsfeed_withpic, parent, false);
            return new NewsfeedViewHolder(itemView);
        }



    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(ProfileAdapter.FirstFrameViewHolder viewHolder, int position) {

        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        sp_profileimg=sp.getString(Constants.PROFILEIMG,"");
        profile_bitmap=sp.getString(Constants.PROFILEIMGPNG,"");
        sp_Usrname=sp.getString(Constants.U_NAME,"");
        Log.e("sharedprefrence id",sp_uid);
        sp_profileimgpath=sp.getString(Constants.PROFILEIMGPATH,"");
        if(viewHolder.getItemViewType()==Constants.PROFILECARD){
            FirstFrameViewHolder holder=(FirstFrameViewHolder)viewHolder;
            holder.itemView.setTag(position);
            ProfileViewItem item=profileViewItems.get(position);

            holder.profCoverimage.setImageUrl(item.getCoverpic(),imageLoader);
            holder.profimage.setImageUrl(item.getProfilepic(),imageLoader);

            if(item.getName().equals("null")){
                holder.tv_profname.setText(item.getUsername());
            }else {
                holder.tv_profname.setText(item.getName());
            }
            if(String.valueOf(item.getUid()).equals(sp_uid)){
                holder.follow.setVisibility(View.GONE);
                holder.changeprofilepic.setVisibility(View.VISIBLE);
                holder.changecoverpic.setVisibility(View.VISIBLE);
            }
            else{
                holder.follow.setVisibility(View.VISIBLE);
                holder.changecoverpic.setVisibility(View.GONE);
                holder.changeprofilepic.setVisibility(View.GONE);
            }
        }else{

            NewsfeedViewHolder holder=(NewsfeedViewHolder) viewHolder;
            final NewsfeedViewHolder holder1=(NewsfeedViewHolder) viewHolder;
//            holder.profimagechoose.setVisibility(View.VISIBLE);

            holder.itemView.setTag(position);


            NewsFeedItem bind=newsFeedItems.get(position);
            if(String.valueOf(bind.getId()).equals(sp_uid)){
                holder.Ushare.setVisibility(View.INVISIBLE);
            }

            sid=String.valueOf(bind.getMessageid());
            sname=bind.getName();
            stime=bind.getTimeStamp();
            slike=bind.getLike();
            sshare=bind.getShare();
            suid=String.valueOf(bind.getId());
            profimgurl=bind.getProfilePic();
            imgurls=bind.getImgpath();


            holder.Ushare.setText(bind.getShare());
            if(holder.Ushare.getText().equals("Unshare")){
                holder. Ushare.setTextColor(Color.parseColor("#4cb3d2"));

            }else {
                holder. Ushare.setTextColor(Color.parseColor("#747474"));

            }
            if(String.valueOf(bind.getId()).equals(sp_uid)){
                holder.deleteStatus.setVisibility(View.VISIBLE);
            }else{
                holder.deleteStatus.setVisibility(View.GONE);
            }
            String[] testImgpath= new String[]{DatabaseInfo.NewsfeedimageURL};
//        Log.e("test image path",String.valueOf(Arrays.asList(bind.getImgpath())));
      /*      if(bind.getImgpath()!=null||!bind.getImgpath().equals(testImgpath)) {

                ObjSliderAdapter = new ImageSliderAdapter(mContext, bind.getImgpath());
                holder.imageslider.setAdapter(ObjSliderAdapter);
                holder.imageslider.setVisibility(View.VISIBLE);
                Log.e("image path of newsfeed",String.valueOf(Arrays.asList(bind.getImgpath())));

            }else {holder.imageslider.setVisibility(View.GONE);}*/
            if(Arrays.equals(bind.getImgpath(),testImgpath)){
                holder.imageslider.setVisibility(View.GONE);
                Log.e("image path of newsfeed1",String.valueOf(Arrays.asList(testImgpath)));
            }
            holder.Uname.setText(bind.getName());
            if(selectedItem == position) holder.itemView.setSelected(true);
            Date date=new Date();

            SimpleDateFormat format=new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            String dt=format.format(date);
            try{
                Calendar calendar = Calendar.getInstance();
                TimeZone tz = calendar.getTimeZone();
                calendar.setTimeInMillis(Long.parseLong(bind.getTimeStamp()) * 1000);
                calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss aa");
                Date currenTimeZone = (Date) calendar.getTime();
                CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(Long.parseLong(bind.getTimeStamp())*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                holder.Utimestamp.setText(timeAgo);
            }catch (Exception e) {
            }
//        Log.e("date timestamp",timeAgo.toString());


            holder1.Ustatus.post(new Runnable() {
                @Override
                public void run() {
                    lineCnt = holder1.Ustatus.getLineCount();
                    if(lineCnt>3){
                        NewsFeedAdapter.makeTextViewResizable(holder1.Ustatus,3,"..Read More",true);
                    }
                    // Perform any actions you want based on the line count here.
                }
            });

            // Chcek for empty status message
            if (!TextUtils.isEmpty(bind.getStatus())) {
                holder.  Ustatus.setText(bind.getStatus());
                holder. Ustatus.setVisibility(View.VISIBLE);
//            holder.Ustatus.setTypeface(EasyFonts.walkwaySemiBold(mContext));

            } else {
                // status is empty, remove from view
                holder. Ustatus.setVisibility(View.GONE);
            }
            if(!bind.getShareUid().equals("0")){
                holder.Shareprofilepic.setVisibility(View.VISIBLE);
                holder.Shareprofilepic.setImageUrl(bind.getShareprofilepic(),imageLoader);
                holder.ShareUname.setText(bind.getShareUsername()+" Shared this");
                holder.ShareUname.setVisibility(View.VISIBLE);
                holder.rooler.setVisibility(View.VISIBLE);
            }else{
                holder.ShareUname.setVisibility(View.GONE);
                holder.Shareprofilepic.setVisibility(View.GONE);
                holder.rooler.setVisibility(View.GONE);
            }

            // Checking for null feed url
            if (bind.getUrl() != null) {
                holder.  UsrUrl.setText(Html.fromHtml("<a href=\"" + bind.getUrl() + "\">"
                        + bind.getUrl() + "</a> "));

                // Making url clickable
                holder.UsrUrl.setMovementMethod(LinkMovementMethod.getInstance());
                holder. UsrUrl.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(bind.getUrl());
//            if ("www.youtube.com".equals(uri.getHost())){
//               holder.UsrUrl.setVisibility(View.GONE);
//                holder.Ufeedvideo.setVisibility(View.VISIBLE);
//                holder.Ufeedvideo.setVideoURI(uri);
//                holder.Ufeedvideo.start();
//                }else{
//                holder.Ufeedvideo.setVisibility(View.GONE);
//            }
            } else {
                // url is null, remove from the view
                holder. UsrUrl.setVisibility(View.GONE);
            }

            // user profile pic
            holder.Userprofilepic.setImageUrl(bind.getProfilePic(), imageLoader);
//        if(bind.getLike()!=null){
            holder.Ulike.setText(bind.getLike());
            holder.Ulike.setVisibility(View.VISIBLE);
            if(holder.Ulike.getText().equals("Unlike")){
                holder. Ulike.setTextColor(Color.parseColor("#4cb3d2"));
//                Ulike.setText("Unlike");
            }else {
                holder. Ulike.setTextColor(Color.parseColor("#747474"));
//                Ulike.setText("Like");
            }

        }
    }
    @Override
    public int getItemViewType(int position) {

        return mDatasettype[position];
    }
    @Override
    public int getItemCount() {
        return newsFeedItems.size();
    }

    public class FirstFrameViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_profname,changeprofilepic,changecoverpic;

        public Button about,follower,follow,following;
        public ProfileImageView profimage,profCoverimage;
        public FirstFrameViewHolder(View itemView) {
            super(itemView);
            tv_profname=(TextView)itemView.findViewById(R.id.prof_name);
            profimage=(ProfileImageView)itemView.findViewById(R.id.prof_profileimage);
            profCoverimage=(ProfileImageView)itemView.findViewById(R.id.prof_coverimage);
            changecoverpic=(TextView) itemView.findViewById(R.id.tv_profile_editcoverpic);
            changeprofilepic=(TextView) itemView.findViewById(R.id.tv_profile_editprofilepic);
            follower=(Button)itemView.findViewById(R.id.prof_btn_followers);
            follow=(Button)itemView.findViewById(R.id.prof_btn_follow);
            following=(Button)itemView.findViewById(R.id.prof_btn_following);
             changeprofilepic.setOnClickListener(new View.OnClickListener() {
                   @Override
               public void onClick(View v) {
                   Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                  mActivity. startActivityForResult(i, 1);
               }
            });
        /*   changecoverpic.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                   Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                   mActivity. startActivityForResult(i, 2);
               }
           });*/
        /*   follower.setOnClickListener(new View.OnClickListener() {
              @Override
                public void onClick(View v) {
                   ProfileViewItem item=profileViewItems.get(getAdapterPosition());
                   Intent i=new Intent(mContext, Followers.class);
                   i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra("uid",item.getUid());

                   mContext.startActivity(i);

               }
           });*/
/*            following.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileViewItem item=profileViewItems.get(getAdapterPosition());
                    Intent i=new Intent(mContext, Following.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra("uid",item.getUid());

                    mContext.startActivity(i);

                }
            });*/
        }
    }

    public class NewsfeedViewHolder extends FirstFrameViewHolder{
        public String name,statusMsg,image,timestamp,url,profilepic,sp_uid;
        public TextView Uname,Ustatus,Utimestamp,UsrUrl,Ulike,Ucomment,Ushare,ShareUname;
        //        public ImageView UStatusimage,Userprofilepic;
        public NetworkImageView Userprofilepic,Shareprofilepic;
        public FeedImageView newsfeedimg ,deleteStatus;
        public LinearLayout commentLayout,imageloader_layout;
        private Context context;
        public RecyclerView commentrecycler;
        public String[] newsfeedimagepath;
        public  String tempimg,tempimgpath;
        public PopupWindow comment_window;
        LayoutInflater inflater;
        public int position,commentpos;
        public ViewPager imageslider;
        public VideoView Ufeedvideo;
        public View rooler;
        public NewsfeedViewHolder(View itemView) {
            super(itemView);
 //            commentLayout=(LinearLayout)itemView.findViewById(R.id.ll_comment_box);
//            imageloader_layout=(LinearLayout)itemView.findViewById(R.id.message_LL_image_view);
            imageslider=(ViewPager)itemView.findViewById(R.id.viewpager_postimage);
             deleteStatus=(FeedImageView)itemView.findViewById(R.id.iv_status_delete);
            rooler=(View)itemView.findViewById(R.id.view_strightline_below);
            Uname = (TextView) itemView.findViewById(R.id.tv_elementname_withpic);
            Userprofilepic=(NetworkImageView)itemView.findViewById(R.id.share_profile_image_withpic_card) ;
            ShareUname=(TextView)itemView.findViewById(R.id.tv_share_details) ;
            Utimestamp = (TextView) itemView.findViewById(R.id.tv_elementposttime_withpic);
            Ustatus = (TextView) itemView.findViewById(R.id.tv_elementpost_text_with_pic);
            UsrUrl = (TextView) itemView.findViewById(R.id.tv_elementpost_url_with_pic);
            Shareprofilepic  = (NetworkImageView) itemView.findViewById(R.id.profile_image_withpic_card);
          //  newsfeedimg = (FeedImageView) itemView.findViewById(R.id.iv_withpic_card);
            Ulike=(TextView)itemView.findViewById(R.id.tv_element_like_withpic);
            Ucomment=(TextView)itemView.findViewById(R.id.tv_element_comment_withpic);
            Ushare=(TextView)itemView.findViewById(R.id.tv_element_share_withpic);
            deleteStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteStatusConfirmation(getAdapterPosition(),v);
                 }
            });
             Ushare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    if(Ushare.getText().equals("Share")) {
                        Share(position);
                        Ushare.setText("Unshare");
                        Ushare.setTextColor(Color.parseColor("#4cb3d2"));
                    }else if(Ushare.getText().equals("Unshare")){
                        Unshare(position);
                        Ushare.setText("Share");
                        Ushare.setTextColor(Color.parseColor("#747474"));
                    }
                }
            });
             Ucomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentpos=getAdapterPosition();

                    CommentBox(commentpos);
                    Log.e("comment pos",String.valueOf(commentpos));

                }
            });
            Ulike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext.getApplicationContext(),String.valueOf(  rvsocial.getChildAdapterPosition(rvsocial.getFocusedChild())),Toast.LENGTH_SHORT).show();
                    position=getAdapterPosition();
                    if(Ulike.getText().equals("Like")){


                        Like(position,sp_uid);
                        Ulike.setTextColor(Color.parseColor("#4cb3d2"));
                        Ulike.setText(R.string.unlike);
                    }else {
                        Unlike(position,sp_uid);
                        Ulike.setTextColor(Color.parseColor("#747474"));
                        Ulike.setText(R.string.like);
                    }
                }
            });
         }
     }
    private void CommentBox(int position){
        final int commpos=position;
        String comment;
//      final CommentItem item=new CommentItem();
        LayoutInflater layout = (LayoutInflater) mActivity.getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layout.inflate(R.layout.commentlayout, null);
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
//        popupView.getBackground().setAlpha(128);
         // Create custom dialog object
//        inflater=(LayoutInflater)mgetSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Include dialog.xml file
        final Dialog dialog=new Dialog(mActivity, R.style.FullHeightDialog);
        dialog.setContentView(R.layout.commentlayout);
 //        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set dialog title
//        dialog.setTitle("Comments");

        commentrecycler=(RecyclerView)popupView.findViewById(R.id.recycler_comment);

        final   RecyclerView rv_comments=(RecyclerView)popupView.findViewById(R.id.recycler_comment);
        comment_profile_pic=(ImageView)popupView.findViewById(R.id.iv_comment_profilepic);
        tv_comment_count=(TextView)popupView.findViewById(R.id.tv_comment_commentcount);
        edit_text_comment=(EditText)popupView.findViewById(R.id.et_comment_);
        edit_text_comment.requestFocus();
        comment_add_image=(ImageButton)popupView.findViewById(R.id.button_add_image);
        final Button comment_comment=(Button)popupView.findViewById(R.id.button_comment);
        final Button comment_close=(Button)popupView.findViewById(R.id.button_comment_close);
        try {
            File file = new File(sp_profileimgpath);
            comment_profile_pic.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
        }catch (Exception e){

        }
        comment_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        cmditem=new ArrayList<CommentItem>();
        mLayoutManager=new LinearLayoutManager(mActivity);
        comments(commpos);
        CAdapter=new CommentAdapter(mActivity,cmditem,mContext);
        commentrecycler.setLayoutManager(mLayoutManager);
        commentrecycler.setItemAnimator(new ScaleInRightAnimator());
        popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, 0, 0);
//        commentrecycler.smoothScrollToPosition(cmditem.size()-1);
//        if(comd.getTotalcomments()==null){
//            comment_count.setText(comd.getTotalcomments()+R.string.commentcount);
//        }else{
//            comment_count.setText("0 "+R.string.commentcount);
//        }
        comment_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
                //creating new thread to handle Http Operations
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                activity. startActivityForResult(i, 2);
                setPosition(commpos);
//                        photoComment(commpos,String.valueOf(edit_text_comment.getText()));
//                        uploadcommentphoto(Constants.PHOTOCOMMENTSTRING,commpos);
//                    }
//                }).start();

            }
        });

        comment_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempcomment=String.valueOf(edit_text_comment.getText());
                Log.e("comment edit text",tempcomment);
                if( String.valueOf(edit_text_comment.getText()).equals("")){
                    edit_text_comment.setError("Input comment");
                    Toast.makeText(mContext,"Input commentsssssssssss",Toast.LENGTH_SHORT).show();
                }else {
                    Calendar calendar = Calendar.getInstance();
                    long timeNow = System.currentTimeMillis()/1000L;
//                    String currentTimestamp =new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss aa").format(new Date());
                    String currentTimestamp=String.valueOf(timeNow);
                    Update_text_Comment(commpos,String.valueOf(edit_text_comment.getText()));
                    item.setComment(String.valueOf(edit_text_comment.getText()));
                    item.setTimeStamp(String.valueOf(currentTimestamp));
                    item.setLikecount("0");
                    item.setName(sp_Usrname);
                    item.setUid_fk(sp_uid);
                    item.setLikecheck("false");
                    item.setProfilePic(DatabaseInfo.ProfilepicURL + sp_profileimg);
                    Log.e("timestamp",String.valueOf(currentTimestamp));
                    cmditem.add(item);
                    edit_text_comment.setText("");
                    CAdapter.notifyDataSetChanged();

                    comment_no+=1;

                    tv_comment_count.setText(String.valueOf(comment_no)+mContext.getResources().getString(R.string.commentcount));
//                    comd.setProfilePic();
                }
            }
        });

//        dialog.show();
        ;

    }

    private void comments(int position){
        final int itemposition=position;
        String[]msg= Constants.MessageID;
        String par="?messageid="+Constants.MessageID+"&uid="+sp_uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.CommentURL+par,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONObject Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            JSONArray feedArray = Jobject.getJSONArray("comments");
                            comment_no=feedArray.length();
                            String cmdlikcout[]=new String[feedArray.length()];
                            String cmdid[]=new String[feedArray.length()];
                            String cmd;
                            tv_comment_count.setText(String.valueOf(comment_no)+mContext.getResources().getString(R.string.commentcount));
                            for (int i = 0; i < feedArray.length(); i++) {
                                JSONObject feedObj = (JSONObject) feedArray.get(i);
                                CommentItem comditm=new CommentItem();
                                String comments,comments_image,comments_id,comments_username,comments_like_count,comments_created,comments_uid_fk,comment_user_pic,comment_msg_uid,comment_msg_id;
                                comments=feedObj.getString("comment");
                                comditm.setCommentCount(String.valueOf(feedArray.length()));
                                Log.e("comment",String.valueOf(comments));
                                comditm.setComment(comments);
                                comments_id=feedObj.getString("com_id");
                                Log.e("comment id",String.valueOf(comments_id));
                                comditm.setId(Integer.parseInt(comments_id));
                                comments_image= feedObj.isNull("img_path") ? "00" : DatabaseInfo.CommentImageURL+feedObj.getString("img_path");
                                Log.e("comment image",String.valueOf(comments_image));
                                comditm.setImge(comments_image);
                                comments_created=feedObj.getString("created");
                                Log.e("comment created",String.valueOf(comments_created));
                                comditm.setTimeStamp(comments_created);
                                comments_uid_fk=feedObj.getString("uid_fk");
                                comditm.setUid_fk(comments_uid_fk);

                                cmdid[i]=comments_id;

                                Log.e("comment ui fk",String.valueOf(comments_uid_fk));
                                comments_like_count=feedObj.getString("like_count");
                                cmdlikcout[i]=comments_like_count;
                                comditm.setLikecount(comments_like_count);
                                comments_username=feedObj.getString("name");
                                Log.e("comment like count",String.valueOf(comments_like_count));
                                Log.e("comment username",String.valueOf(comments_username));
                                comditm.setName(comments_username);
                                comment_user_pic=feedObj.getString("uid_fk_profile_pic");
                                comditm.setProfilePic(DatabaseInfo.ProfilepicURL+comment_user_pic);
                                Log.e("comment profile pic",String.valueOf(DatabaseInfo.ProfilepicURL+comment_user_pic));
                                comditm.setmsgid(feedObj.getString("msg_id_fk"));
                                comditm.setMsguid(feedObj.getString("msg_uid_fk"));
                                comditm.setLikecheck(feedObj.getString("likecheck"));
                                Log.e("like Check",feedObj.getString("likecheck"));
                                cmditem.add(comditm);
                                CAdapter.notifyDataSetChanged();
                            }

                            commentitems_uid=cmdid;
                            commentlikecount=cmdlikcout;
                            Log.e("constants comment id",String.valueOf(Arrays.asList(commentitems_uid)));
//                            commentitems_uid= ArrayUtils.addAll(commentitems_uid,cmdid);

                        }catch (JSONException e){

                        }

                        commentrecycler.setAdapter(CAdapter);
                        objconsts.setCommentid(commentitems_uid);
                        objconsts.setCommentLikeCount(commentlikecount);
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
                params.put("uid",String.valueOf(sp_uid));
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void Update_text_Comment(int position, final String comment){
        final int itemposition=position;
        final String cmd=comment;
        String[]msg= Constants.MessageID;
        String par="?mag_id="+msg[itemposition]+"&comment="+cmd+"&uid="+sp_uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.UpdateTextCommentURL+par,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());
                        try {
                            JSONObject  Jobject = (JSONObject) new JSONTokener(response).nextValue();
                            String newcmdid=Jobject.getString("commentid");
                            Log.e("new comment id",newcmdid);

                            int tempcmdlegth=Constants.Commentid.length+1;
                            String [] Commnt1 = Constants.Commentid;
                            String[] temp =new String[1];
                            temp[0]=newcmdid;
                            String [] comd2= ArrayUtils.addAll(Commnt1,temp);
                            Constants.Commentid=comd2;

                            String [] Commntlike = Constants.CommentLikeCount;
                            String[] temp1 =new String[1];

                            temp1[0]="0";
                            String [] comdlike=ArrayUtils.addAll(Commntlike,temp1);
                            Constants.CommentLikeCount=comdlike;
                            Toast.makeText(mContext,String.valueOf(Arrays.asList(Constants.CommentLikeCount)),Toast.LENGTH_SHORT).show();
                        }catch (JSONException e){
                        }
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
                params.put("mag_id",String.valueOf(msg[itemposition]));
                Log.e("message id @ posotion",String.valueOf(msg[itemposition]));
                params.put("comment",String.valueOf(cmd));
                params.put("uid",String.valueOf(sp_uid) );
                Log.e("uid id",sp_uid);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void cmdImage(String image,String time){
        item.setImge(image);
//        item.setComment(String.valueOf(edit_text_comment.getText()));
        item.setTimeStamp(String.valueOf(time));
        item.setLikecount("0");
        item.setName(sp_Usrname);
        item.setUid_fk(sp_uid);
        item.setProfilePic(DatabaseInfo.ProfilepicURL + sp_profileimg);
        item.setLikecheck("false");
        cmditem.add(item);
        CAdapter.notifyDataSetChanged();
        comment_no+=1;
        tv_comment_count.setText(String.valueOf(comment_no)+mContext.getResources().getString(R.string.commentcount));
    }
    public String getStringImage(Bitmap bmp, File file) throws FileNotFoundException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//
//    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        File tempfile=file;
        tempfile.getAbsolutePath();
        InputStream inputStream = new FileInputStream(tempfile);//You can get an inputStream using any IO API
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();
        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
        return encodedString;
    }
    private void deleteStatusConfirmation(final int position, final View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Confirm");
        alertDialog.setMessage("Are you sure for delete this?");
        alertDialog.setIcon(R.mipmap.logo);

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,  int which) {

                        dialog.cancel();

                    }
                });
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                        deleteStatus(position);
                        setAnimation(view,position);
                        Toast.makeText(mContext, "Status Successfully Deleted", Toast.LENGTH_SHORT).show();

                    }
                });

        alertDialog.show();


    }
    public  void Like(int position, String uids){
        final String uid=uids;
        final int itemposition=position;  String[]msg= Constants.MessageID;
        String par="?mag_id="+msg[itemposition]+"&uid="+sp_uid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.LikeURL,
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
                Log.e("messageid like",String.valueOf(Arrays.asList(msg)));
                params.put("messageid",String.valueOf(msg[itemposition]));
                params.put("uid",String.valueOf(sp_uid) );
                Log.e("uid id like",sp_uid);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void Unlike(int position,String uids){
        final String uid=uids;
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.UnlikeURL,
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
                params.put("uid", String.valueOf(sp_uid ));
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void deleteStatus(final int position){
        newsFeedItems.remove(position);
        notifyItemRemoved(position);  String[]msg= Constants.MessageID;
        String paramas="?messageid="+msg[position]+"&uid="+sp_uid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.DeleteStatusURL+paramas,
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
                Log.e("messageid",String.valueOf(msg[position]));
                params.put("messageid",String.valueOf(msg[position]));
                params.put("uid", String.valueOf(sp_uid));
                Log.e("message id and uid pass","got");
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public String messageid(int position){
        return String.valueOf(position);
    }



    public void Unshare(int position){
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DatabaseInfo.UnshareURL,
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
                params.put("uid", String.valueOf(sp_uid ));
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public void Share(int position){
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.ShareURL,
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
                params.put("uid", String.valueOf(sp_uid ));
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
    public void setPosition(int postion){
        MessagePosition=postion;
    }
}
