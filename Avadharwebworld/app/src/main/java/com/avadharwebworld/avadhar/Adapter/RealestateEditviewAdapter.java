package com.avadharwebworld.avadhar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.avadharwebworld.avadhar.Activity.RealestateRegistrationEdit;
import com.avadharwebworld.avadhar.Activity.RealestateViewAds;
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Activity.ViewMatrimonyDetails;
import com.avadharwebworld.avadhar.Activity.ViewRealestateDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.MatrimonyEditItem;
import com.avadharwebworld.avadhar.Data.MatrimonyItem;
import com.avadharwebworld.avadhar.Data.RealestateItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;
import com.avadharwebworld.avadhar.Support.FilePath;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by user-03 on 12/24/2016.
 */

public class RealestateEditviewAdapter extends RecyclerView.Adapter<RealestateEditviewAdapter.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid,profileid,currentItem;
    int screenWidth,screenHeight;
    Display display;
    Point size;

    private List<RealestateItem> realestateItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public RealestateEditviewAdapter(Activity activity,Context context,List<RealestateItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.realestateItems=item;
    }
    public RealestateEditviewAdapter(){

    }
    @Override
    public RealestateEditviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.realestateviewadslayout, parent, false);
        return new RealestateEditviewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RealestateEditviewAdapter.ViewHolder holder, int position) {
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        profileid=sp.getString(Constants.PROFILEID,"");
        RealestateItem bind=realestateItems.get(position);
        holder.Name.setText(bind.getName());

        holder.status.setVisibility(View.GONE);
//        holder.religion.setText(bind.getReligion());


    }

    @Override
    public int getItemCount() {
        return realestateItems.size();
    }
    public String getCurrentItem(){
        return currentItem;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name,type,price,location1,status;
        public FeedImageView image;
        public ImageView favorite,thump;
        public Button edit,delete,pic;
        public ViewHolder(final View itemView) {
            super(itemView);

            Name=(TextView)itemView.findViewById(R.id.tv_realestate_viewads_editview_name);
            status=(TextView)itemView.findViewById(R.id.tv_realestate_viewads_editview_status);
            edit=(Button) itemView.findViewById(R.id.btn_realestate_viewads_editview_edit);
            delete=(Button)itemView.findViewById(R.id.btn_realestate_viewads_editview_delete);
            pic=(Button)itemView.findViewById(R.id.btn_realestate_viewads_editview_upload);
            WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            display=wm.getDefaultDisplay();
            size=new Point();
            display.getSize(size);
            screenHeight=size.y;
            screenWidth=size.x;
            LinearLayout.LayoutParams parmsIMG = new LinearLayout.LayoutParams(screenWidth/2,150);
//            InstImage.setLayoutParams(parmsIMG);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RealestateItem eduItem=realestateItems.get(getLayoutPosition());
                    Intent intent=new Intent(mContext,RealestateRegistrationEdit.class);
                    intent.setFlags( FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra("id",  eduItem.getId());
                    mContext.startActivity(intent);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RealestateItem eduItem=realestateItems.get(getLayoutPosition());
                    deleteProfile(eduItem.getId(),profileid);
                    realestateItems.remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                    notifyItemRangeChanged(getAdapterPosition(),realestateItems.size());

                }
            });
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RealestateViewAds obj=new RealestateViewAds();
                    RealestateItem eduItem=realestateItems.get(getLayoutPosition());
                    currentItem=eduItem.getId();
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mActivity.startActivityForResult(i,1);
                    Log.e("boolean value obj",String.valueOf(obj.checkimagev));
                    Log.e("image path from cam",Constants.REALESTATEIMAGE);


                }
            });


        }
    }
    public void  deleteProfile(final String uid, final String pid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.RealestateDeleteProfileURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        String   favoriteResult =response.toString();


                        Toast.makeText(mContext,favoriteResult,Toast.LENGTH_SHORT).show();

//                        setAnimationForFavoriteText(response,layout);
                        // Toast.makeText(getActivity().getApplication(),response,Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("avadharid", String.valueOf(pid));
                params.put("id",String.valueOf(uid));
                Log.e("profile id",pid);
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

    public void upoladimage(String id){

        File photo=new File(Constants.REALESTATEIMAGE);
//        File resume=new File(resumepath);
        Log.e("resumepath",Constants.REALESTATEIMAGE);
            RequestParams params = new RequestParams();
//        final String finalLanguageknown = languageknown;
            try {

                params.put("fname", photo);

                params.put("id", id);

                // params.put("uid", Uid_from_sharedpref);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(DatabaseInfo.RealestateUploadImageURL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
//                    System.out.println("statusCode "+statusCode);//statusCode 200
//                addImageView(inHorizontalScrollView);
//                imageupdateprogress.setVisibility(View.INVISIBLE);
                    byte[] bite = responseBody;
                    try {
                        final String response = new String(bite, "UTF-8");
//
                        Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
                        //                    imageUploadid=imageUploadid.concat(response.trim()+",");
//                    Toast.makeText(getApplicationContext(),String.valueOf(imageUploadid),Toast.LENGTH_SHORT).show();
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

                }

                @Override
                public void onFinish() {
                }

            });
    }


}
