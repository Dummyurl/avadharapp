package com.avadharwebworld.avadhar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
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
import com.avadharwebworld.avadhar.Activity.ViewEducationDetails;
import com.avadharwebworld.avadhar.Activity.ViewMatrimonyDetails;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.Data.EducationFeedItem;
import com.avadharwebworld.avadhar.Data.HisstoryItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestItem;
import com.avadharwebworld.avadhar.Data.MatrimonyInterestRecievedItem;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.avadharwebworld.avadhar.Support.FeedImageView;
import com.avadharwebworld.avadhar.Support.ViewMoreSpannable;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by user-03 on 12/24/2016.
 */

public class HisstoryAdapter3 extends RecyclerView.Adapter<HisstoryAdapter3.ViewHolder> {
    private Activity mActivity;
    private Context mContext;
    private SharedPreferences sp;
    private String sp_uid;
    int screenWidth,screenHeight,lineCnt;
    Display display;
    Point size;
    LayoutInflater inflater;
    PopupWindow popupWindow=null;

    private List<HisstoryItem> hisstoryItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public HisstoryAdapter3(Activity activity,Context context,List<HisstoryItem> item){
        this.mActivity=activity;
        this.mContext=context;
        this.hisstoryItems=item;
    }
    @Override
    public HisstoryAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hisstory_view3, parent, false);
        return new HisstoryAdapter3.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HisstoryAdapter3.ViewHolder holder, int position) {
        final HisstoryAdapter3.ViewHolder holder1=holder;
        sp=mContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        sp_uid=sp.getString(Constants.UID,"");
        HisstoryItem bind=hisstoryItems.get(position);
        if(!bind.getImage().equals("null")) holder.image.setImageUrl(bind.getImage(),imageLoader);

        holder.Name.setText(bind.getName());

        holder.title.setText(Html.fromHtml( bind.getTitle()));
        holder.date.setText(bind.getDate());
        holder.description.setText(Html.fromHtml(bind.getDescription()));
//        holder.description.post(new Runnable() {
//            @Override
//            public void run() {
//                lineCnt = holder1.description.getLineCount();
//                if(lineCnt>3){
//                    makeTextViewResizable(holder1.description,3,"..Read More",true);
//                }
//                // Perform any actions you want based on the line count here.
//            }
//        });
////        holder.religion.setText(bind.getReligion());


        Log.e("At Matrimony adapter","fetch");
    }

    @Override
    public int getItemCount() {
        return hisstoryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Name,date,title,description;
        public RecyclerView rv_education;
        public ImageView favorite,thump;
        public Button readmore,reject;
        public NetworkImageView image;

        public ViewHolder(final View itemView) {
            super(itemView);


            readmore=(Button)itemView.findViewById(R.id.btn_hisstory3_readmore);

            Name=(TextView)itemView.findViewById(R.id.tv_hisstory3_view_name);
            date=(TextView)itemView.findViewById(R.id.tv_hisstory3_time);
            description=(TextView)itemView.findViewById(R.id.tv_hisstory3_view_content);
            title=(TextView)itemView.findViewById(R.id.tv_hisstory3_view_title);
            image=(NetworkImageView) itemView.findViewById(R.id.iv_hisstory3_view_image);
            WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
            display=wm.getDefaultDisplay();
            size=new Point();
            display.getSize(size);
            screenHeight=size.y;
            screenWidth=size.x;
            LinearLayout.LayoutParams parmsIMG = new LinearLayout.LayoutParams(screenWidth/2,150);
//            InstImage.setLayoutParams(parmsIMG);

            readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HisstoryItem item=hisstoryItems.get(getLayoutPosition());
                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                    showDetailed(item.getImage(),item.getName(),item.getTitle(),item.getDate(),item.getDescription(),item.getId());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    HisstoryItem item=hisstoryItems.get(getLayoutPosition());

//                    Toast.makeText(mContext,"item position " +String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
//                    showDetailed(item.getImage(),item.getName(),item.getTitle(),item.getDate(),item.getDescription());

                }
            });
//            favorite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext,"favorite clicked",Toast.LENGTH_SHORT).show();
//
//                    MatrimonyInterestRecievedItem eduItem=MatrimonyInterestItems.get(getLayoutPosition());
//                    setFavorite(sp_uid,eduItem.getMprofileid());
//                }
//            });
//
//            thump.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext,"thump clicked",Toast.LENGTH_SHORT).show();
//
//                    MatrimonyInterestRecievedItem eduItem=MatrimonyInterestItems.get(getLayoutPosition());
//                    setInterestSend(sp_uid,eduItem.getMprofileid());
//                }
//            });
//


        }
    }
    public void  setInterestAccept(final String uid, final String pid){
        final String[] res = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyAcceptInterestURL,
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
                params.put("mid", String.valueOf(pid));
                params.put("uid",String.valueOf(uid));

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
    public void  setInterestReject(final String uid, final String pid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.MatrimonyRejectInterestURL,
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
                params.put("mid", String.valueOf(pid));
                params.put("uid",String.valueOf(uid));

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
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    public static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                           final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new ViewMoreSpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Read Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. Read More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }

    private void showDetailed(String image1, String name1, String title1, String date1, String content1, final String pid) {
        try {
            final TextView name,titile,date,content;
            final NetworkImageView image;
            final RatingBar ratingBar;

//            android.view.Display display = ((android.view.WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            mActivity. getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//            int height = displaymetrics.heightPixels;
//            int width = displaymetrics.widthPixels;
// We need to get the instance of the LayoutInflater
            LayoutInflater layout = (LayoutInflater) mActivity.getBaseContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layout.inflate(R.layout.hisstroy_view_detailed, null);
            Display display = mActivity.getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int width = display.getWidth();
            image=(NetworkImageView)popupView.findViewById(R.id.iv_hisstory_viewetailed_image);
            name=(TextView)popupView.findViewById(R.id.tv_hisstory_viewetailed_name);
            titile=(TextView)popupView.findViewById(R.id.tv_hisstory_viewdetailed_title);
            date=(TextView)popupView.findViewById(R.id.tv_hisstoryetailed_time);
            content=(TextView)popupView.findViewById(R.id.tv_hisstory_viewetailed_content);
            ratingBar=(RatingBar)popupView.findViewById(R.id.rb_hisstory_tab1);
            content.setMovementMethod(new ScrollingMovementMethod());

            popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
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
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    setRating(ratingBar.getNumStars(),pid);
                }
            });
            image.setImageUrl(image1,imageLoader);
            name.setText(name1);
            titile.setText(Html.fromHtml(title1));
            date.setText(date1);
            content.setText(Html.fromHtml(content1));
            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
//        dialog.setTitle("Search");
//            popupView.getBackground().setAlpha(0);
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);;

//            popup_close.setOnClickListener(cancel_button);
//            WindowManager wm = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);
//            WindowManager.LayoutParams p = (WindowManager.LayoutParams) popupView.getLayoutParams();
//            p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//            p.dimAmount = 0.3f;
//            wm.updateViewLayout(popupView, p);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  setRating(final int rating, final String pid){
        final String[] res = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DatabaseInfo.HisStoryAddRatingURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volleyresponse",response.toString());

                        String   favoriteResult =response.toString();

                        Toast.makeText(mContext,"You Rated...",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(mContext,favoriteResult,Toast.LENGTH_SHORT).show();

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
                params.put("postID", String.valueOf(pid));
                params.put("ratingPoints",String.valueOf(rating));

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
}
