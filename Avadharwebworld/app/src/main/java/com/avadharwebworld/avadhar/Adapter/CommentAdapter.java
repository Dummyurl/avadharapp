package com.avadharwebworld.avadhar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.ImageView;
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
import com.avadharwebworld.avadhar.Activity.SocialMedia;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.CommentItem;
import com.avadharwebworld.avadhar.Data.NewsFeedItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Vishnu on 10-09-2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public String messageid_like;
    private Activity activity;
    private static int selectedItem = -1;
    private Context mContext;
    private LayoutInflater inflater;
    private List<CommentItem> feedItems;
    private RecyclerView rvsocial;
    private int lastPosition = -1;
    private int id,commentlikecount;
    private Timer timer;
    private TimerTask task;
    private String name,status,image,timestamp,url,profilepic,sp_uid,comment_uid,messageid,message_uid;
    public TextView cname,cstatus,ctimestamp,UsrUrl;
    public NetworkImageView profilepicimg;
    public  FeedImageView feedimage;
    SocialMedia sm;
//    private ProgressBar progressBar;


    private boolean loading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    //    public ImageView UStatusimage,Userprofilepic;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public CommentAdapter(Activity Activity,List<CommentItem> Feeditems,Context context){
        this.activity=Activity;
        this.feedItems=Feeditems;
        this.mContext=context;
//
    }


    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments, parent, false);
        CommentAdapter.ViewHolder viewHolder = null;
        if(viewType == 1){
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments, parent, false);
            viewHolder = new ViewHolder(layoutView);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {

        final SharedPreferences sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        Log.e("sharedprefrence id",sp_uid);
        holder.itemView.setTag(position);
        CommentItem bind=feedItems.get(position);

        messageid= bind.getmsgid();
        message_uid=bind.getMsguid();
        comment_uid=bind.getUid_fk();
        holder.Cname.setText(bind.getName());
//        Log.e("comment uid",comment_uid);
       if(sp_uid.equals(comment_uid)){
            holder.deleteitem.setVisibility(View.VISIBLE);
           Log.e("inside check","got");
        }
        else {holder.deleteitem.setVisibility(View.GONE);
           Log.e("outside check","got");
       }
//        if(bind.getImge()!=null){
//            holder.Commentimage.setImageUrl(DatabaseInfo.CommentImageURL+bind.getImge(),imageLoader);
//            Log.e("comment image",DatabaseInfo.CommentImageURL+bind.getImge());
//            holder.Commentimage.setVisibility(View.VISIBLE);
////            holder.CcommentImagefromDevice.setVisibility(View.GONE);
////            holder.Cstatus.setVisibility(View.GONE);
//            holder.Commentimage
//                        .setResponseObserver(new FeedImageView.ResponseObserver() {
//                            @Override
//                            public void onError() {
//                            }
//
//                            @Override
//                            public void onSuccess() {
//                            }
//                        });
//        }else {
//            holder.Commentimage.setVisibility(View.GONE);
//        }

        if(bind.getProfilePic()==null){

        }else {
            holder.Comment_profile_img.setImageUrl(bind.getProfilePic(), imageLoader);
        }
        if(selectedItem == position) holder.itemView.setSelected(true);
        Date date=new Date(Long.parseLong(bind.getTimeStamp()));

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
            holder.Ctimestamp.setText(timeAgo);

        }catch (Exception e) {
        }
//        Log.e("date timestamp",timeAgo.toString());
        // Chcek for empty status message
        if(bind.getLikecount().equals(0)){
            holder.C_like_count.setVisibility(View.GONE);
        }

        Log.e("comment count",bind.getLikecount());

        if(!bind.getLikecount().equals("0")){
            commentlikecount=Integer.parseInt(bind.getLikecount());
            holder.C_like_count.setText("- "+String.valueOf(commentlikecount));
            holder.C_like_count.setVisibility(View.VISIBLE);
        }else{
            holder.C_like_count.setVisibility(View.GONE);
        }
//        Log.e("comment comment",bind.getComment());
        if (bind.getComment()!=null) {

            holder.  Cstatus.setText(bind.getComment());
            holder. Cstatus.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            holder. Cstatus.setVisibility(View.GONE);
        }



        // user profile pic
//        holder.newsfeedimg.setImageUrl(bind.getProfilePic(), imageLoader);
//        if(bind.getLike()!=null){
//        holder.C_like.setText(bind.getLike());
        if(bind.getLikecheck().equals("true")){
            holder.C_like.setText("Unlike");
        }else {holder.C_like.setText("Like");}
        if(holder.C_like.getText().equals("Unlike")){
            holder. C_like.setTextColor(Color.parseColor("#4cb3d2"));

//                Ulike.setText("Unlike");
        }else {
            holder. C_like.setTextColor(Color.parseColor("#747474"));
//                Ulike.setText("Like");
        }
//        }

//        holder.Commentimage.setImageUrl(bind.getImge(), imageLoader);
//        holder.  Commentimage.setVisibility(View.VISIBLE);
//        holder.Commentimage
//                .setResponseObserver(new FeedImageView.ResponseObserver() {
//                    @Override
//                    public void onError() {
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                    }
//                });
        // Feed image
//        Toast.makeText(mContext,"image path: "+bind.getImge(),Toast.LENGTH_SHORT).show();
        if (bind.getImge() != null) {
            if( URLUtil.isHttpUrl(bind.getImge())||URLUtil.isHttpsUrl(bind.getImge())){
//                holder.CcommentImagefromDevice.setVisibility(View.GONE);
                holder.Commentimage.setImageUrl(bind.getImge(), imageLoader);
                holder.  Commentimage.setVisibility(View.VISIBLE);
                holder. CcommentImagefromDevice.setVisibility(View.GONE);
                holder.Commentimage
                        .setResponseObserver(new FeedImageView.ResponseObserver() {
                            @Override
                            public void onError() {
                            }

                            @Override
                            public void onSuccess() {
                            }
                        });
            }else {
                holder. Commentimage.setVisibility(View.GONE);
                String imgPath = Environment.getDataDirectory() + bind.getImge();
                File imgFile = new  File(imgPath);
                Bitmap bitmap = BitmapFactory.decodeFile(bind.getImge());
                if(imgFile.exists()){bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());}
//                android.net.Uri uri=Uri.fromFile(imgFile);
                holder.CcommentImagefromDevice.setImageBitmap(bitmap);
//                Toast.makeText(mContext,"was set img",Toast.LENGTH_SHORT).show();
                holder.  CcommentImagefromDevice.setVisibility(View.VISIBLE);
//

//                bind.setImge(null);
            }

        }
        else {
            holder.  CcommentImagefromDevice.setVisibility(View.GONE);
            holder. Commentimage.setVisibility(View.GONE);
        }

        setAnimation(holder.commentLayout,position);
//        if(holder.CcommentImagefromDevice.getDrawable()==null){
//            holder.CcommentImagefromDevice.invalidate();
//        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public String name,statusMsg,image,timestamp,url,profilepic;
        public TextView Cname,Cstatus,Ctimestamp,UsrUrl,C_like,C_like_count,C_comment_count;
        //        public ImageView UStatusimage,Userprofilepic;
        public NetworkImageView newsfeedimg;
        public FeedImageView Commentimage,deleteitem;
        public LinearLayout commentLayout;
        public NetworkImageView Comment_profile_img;
        public ImageView CcommentImagefromDevice;

        public ViewHolder(final View itemView) {
            super(itemView);
            rvsocial=(RecyclerView)itemView. findViewById(R.id.recycler_comment);
            commentLayout=(LinearLayout)itemView.findViewById(R.id.comment_items);
            deleteitem=(FeedImageView)itemView.findViewById(R.id.iv_comment_delete);
            Cname = (TextView) itemView.findViewById(R.id.tv_comment_username);
            Ctimestamp = (TextView) itemView.findViewById(R.id.tv_comment_time);
            Cstatus = (TextView) itemView.findViewById(R.id.tv_comment_comment);
            CcommentImagefromDevice=(ImageView)itemView.findViewById(R.id.iv_fromdevice);
//            UsrUrl = (TextView) itemView.findViewById(R.id.tv_elementpost_url_with_pic);
            Comment_profile_img = (NetworkImageView) itemView.findViewById(R.id._comment_profile_image);
            Commentimage = (FeedImageView) itemView.findViewById(R.id.iv_comment_image);
            C_like=(TextView)itemView.findViewById(R.id.tv_comment_like);
            C_like_count=(TextView)itemView.findViewById(R.id.tv_comment_like_count);
//            Ucomment=(TextView)itemView.findViewById(R.id.tv_element_comment_withpic);
//            Ushare=(TextView)itemView.findViewById(R.id.tv_element_share_withpic);

//            timer = new Timer() ;
//            task = new TimerTask() {
//                @Override
//                public void run() {
                    deleteitem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(sp_uid.equals(comment_uid)){

                                deletecomment(getAdapterPosition());
                                setAnimation(v,getAdapterPosition());
                                int temcommentnp=  NewsFeedAdapter.comment_no-=1;
                                NewsFeedAdapter.tv_comment_count.setText(String.valueOf(temcommentnp)+mContext.getResources().getString(R.string.commentcount));
                                Toast.makeText(mContext.getApplicationContext(),"comment deleted",Toast.LENGTH_SHORT).show();
                            }else{
//                        deletecomment(getAdapterPosition());
                                Toast.makeText(mContext.getApplicationContext(),"comment not deleted",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                }
//            };
//            timer.scheduleAtFixedRate(task, 3000, 1000 ) ;

          C_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext.getApplicationContext(),String.valueOf(  rvsocial.getChildAdapterPosition(rvsocial.getFocusedChild())),Toast.LENGTH_SHORT).show();
                    if(C_like.getText().equals("Like")){
                            commentlike(getAdapterPosition());
                        Log.e("comment like count",String.valueOf(commentlikecount));
                        if(Constants.CommentLikeCount[getAdapterPosition()].equals("0")) {
                            C_like_count.setVisibility(View.VISIBLE);
                            int count= Integer.parseInt(Constants.CommentLikeCount[getAdapterPosition()]);
                            count+=1;
                            Constants.CommentLikeCount[getAdapterPosition()]=String.valueOf(count);
                            C_like_count.setText("- " + String.valueOf(Constants.CommentLikeCount[getAdapterPosition()]));
//                        Toast.makeText(mContext.getApplicationContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                        }else{
                            int count= Integer.parseInt(Constants.CommentLikeCount[getAdapterPosition()]);
                            count+=1;
                            Constants.CommentLikeCount[getAdapterPosition()]=String.valueOf(count);
                            C_like_count.setText("- " + String.valueOf(Constants.CommentLikeCount[getAdapterPosition()]));

                        }

//                        Like(position);
//                        Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(Constants.MessageID)),Toast.LENGTH_LONG).show();
                        C_like.setTextColor(Color.parseColor("#4cb3d2"));
                        C_like.setText(R.string.unlike);
                    }else {
//                        Unlike(position);
                        commentUnlike(getAdapterPosition());
                        int count= Integer.parseInt(Constants.CommentLikeCount[getAdapterPosition()]);
                        count-=1;
                        Constants.CommentLikeCount[getAdapterPosition()]=String.valueOf(count);
                        C_like_count.setText("- " + String.valueOf(Constants.CommentLikeCount[getAdapterPosition()]));
                        if(Constants.CommentLikeCount[getAdapterPosition()].equals("0")){
                            C_like_count.setVisibility(View.GONE);
                        }
                        C_like.setTextColor(Color.parseColor("#747474"));
                        C_like.setText(R.string.like);
                    }
                }
            });

        }
    }



    public void setSelectedItem(int position)
    {
        selectedItem = position;
    }
    public String getMessageid_like(){
        return messageid_like;
    }
    public String messageid(int position){
        return String.valueOf(position);
    }
    public void deletecomment(int position){

       deleteComment(position);
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
    public  void deleteComment(int position){
        final int itemposition=position;
        feedItems.remove(position);
        notifyItemRemoved(position);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.DeleteCommentURL,
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
                String[]cmd= Constants.Commentid;
//                Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(msg)),Toast.LENGTH_LONG).show();
                Log.e("commentid",String.valueOf(cmd[itemposition]));
                params.put("commentid",String.valueOf(cmd[itemposition]));
                params.put("uid", String.valueOf(sp_uid));
                Log.e("comment id and uid pass","got");
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void commentlike(int position){
        commentlikecount+=1;
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.CommentLikeURL,
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
                String[]cmd= Constants.Commentid;
//                Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(msg)),Toast.LENGTH_LONG).show();
                Log.e("commentid",String.valueOf(cmd[itemposition]));
                params.put("commentid",String.valueOf(cmd[itemposition]));
                params.put("uid", String.valueOf(sp_uid));
                Log.e("comment id and uid pass","got");
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void commentUnlike(int position){
        commentlikecount-=1;
        final int itemposition=position;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.CommentUnlikeURL,
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
                String[]cmd= Constants.Commentid;
//                Toast.makeText(mContext.getApplicationContext(),String.valueOf(Arrays.asList(msg)),Toast.LENGTH_LONG).show();
                Log.e("commentid",String.valueOf(cmd[itemposition]));
                params.put("commentid",String.valueOf(cmd[itemposition]));
                params.put("uid", String.valueOf(sp_uid));
                Log.e("comment id and uid pass","got");
//                Log.e("message id",String.valueOf(objsocial.msgIDFULL);

                return params;
            }

        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
