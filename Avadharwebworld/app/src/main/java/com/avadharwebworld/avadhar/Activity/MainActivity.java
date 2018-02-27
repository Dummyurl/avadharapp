package com.avadharwebworld.avadhar.Activity;


import android.app.SearchManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.avadharwebworld.avadhar.Application.AppController;
import com.avadharwebworld.avadhar.R;
import com.avadharwebworld.avadhar.Support.Constants;
import com.avadharwebworld.avadhar.Support.DatabaseInfo;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {
    ImageButton logout, profile, settings,socialmedia,education,jobs,matrimonial,realestate,buyandsell,eshop,hisstory,pages,pages2;
    ScrollView svitems;
    View mRooltView;
    Toolbar bottombar;
    LinearLayout section1,section2,section3,section4,section5;
    LinearLayout scrolllinear;
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    int width, height;
    private SharedPreferences sp;
    private String logincheck;
    String uid,profileid,profileimgpath,getimgname;
    ObservableScrollView scrollmain;

    private ImageView mImageView;
    private ImageView mImageViewInternal;
    private String mImageURLString =DatabaseInfo.ip+DatabaseInfo.folder+"user_profile_uploads/1474004058.jpg";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_main);
//        Intent i=getIntent();
//        profileid= i.getExtras().getString("profileid");
//        uid=i.getExtras().getString("uid");
        SharedPreferences sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        getimgname=sp.getString(Constants.PROFILEIMG,"");
        uid=sp.getString(Constants.UID,"");
        Log.e("uid got from login",uid);
       getProfileImageFromUrl(DatabaseInfo.ProfilepicURL + getimgname);

        socialmedia=(ImageButton)findViewById(R.id.ib_main_social) ;
        education=(ImageButton)findViewById(R.id.ib_main_educaion) ;
        jobs=(ImageButton)findViewById(R.id.ib_main_job) ;
        matrimonial=(ImageButton)findViewById(R.id.ib_main_matrimonial) ;
        realestate=(ImageButton)findViewById(R.id.ib_main_realestate) ;
        buyandsell=(ImageButton)findViewById(R.id.ib_main_buyandsell) ;
        eshop=(ImageButton)findViewById(R.id.ib_main_eshop) ;
        hisstory=(ImageButton)findViewById(R.id.ib_main_hisstory) ;
        pages=(ImageButton)findViewById(R.id.ib_main_pages1) ;
        pages2=(ImageButton)findViewById(R.id.ib_main_pages) ;

        section1=(LinearLayout)findViewById(R.id.ll_main_1);
        section2=(LinearLayout)findViewById(R.id.ll_main_2);
        section3=(LinearLayout)findViewById(R.id.ll_main_3);
        section4=(LinearLayout)findViewById(R.id.ll_main_4);
        section5=(LinearLayout)findViewById(R.id.ll_main_5);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        android.view.Display display = ((android.view.WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

//        8590202028
        section1.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth(),(int)display.getHeight()*33/100));
        socialmedia.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));
        education.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));

        section2.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth(),(int)display.getHeight()*33/100));
        jobs.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));
        matrimonial.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));

        section3.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth(),(int)display.getHeight()*33/100));
        realestate.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));
        buyandsell.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));

        section4.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth(),(int)display.getHeight()*33/100));
        eshop.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));
        hisstory.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));

        section5.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth(),(int)display.getHeight()*33/100));
        pages.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));
        pages2.setLayoutParams(new LinearLayout.LayoutParams((int)display.getWidth()*45/100,(int)display.getHeight()*30/100));


        scrollmain = (ObservableScrollView) findViewById(R.id.sv_main_items);
        scrollmain.setScrollViewCallbacks(this);
        scrolllinear=(LinearLayout)findViewById(R.id.lv_scroll);
        bottombar = (Toolbar) findViewById(R.id.bottom_toolbar);
        logout = (ImageButton) findViewById(R.id.btn_main_logout);
        profile = (ImageButton) findViewById(R.id.btn_main_Profile);
        settings = (ImageButton) findViewById(R.id.btn_main_setting);
        svitems = (ScrollView) findViewById(R.id.sv_main_items);



        svitems.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

            }
        });
        socialmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SocialMedia.class);
                i.putExtra("profileid",profileid);
                i.putExtra("uid",uid);
              //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);//

                startActivity(i);

            }
        });
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(Constants.PROFILEIMGPATH,profileimgpath);
        editor.commit();
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupWindowAnimations();
                Intent i=new Intent(getApplicationContext(),Education.class);

            //    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);//

                startActivity(i);
            }
        });
        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Jobs.class);

                startActivity(intent);
                //overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);//
            }
        });
        matrimonial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Matrimonial.class);

                startActivity(intent);
            }
        });
        hisstory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HisStory.class);

                startActivity(intent);
            }
        });
        realestate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),RealEstate.class);

                startActivity(intent);
            }
        });
        buyandsell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),BuyAndSell.class);

                startActivity(intent);
            }
        });
        eshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Eshop.class);

                startActivity(intent);
            }
        });
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    public MainActivity(){

    }
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {


    }
    public void  show(){
//       bottombar. animate().translationY(0).setDuration(500);
        bottombar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        bottombar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
       controlsVisible=true;
     //    bottombar.setVisibility(View.VISIBLE);
    }
    public void hide(){
        bottombar.animate().translationY(bottombar.getBottom()).setInterpolator(new AccelerateInterpolator(2)).start();
//      bottombar. animate().translationY(180).setDuration(500);
        controlsVisible=false;
//        bottombar.setVisibility(View.GONE);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void getProfileImgFromURl(String imgPath){

        AppController.getInstance().getImageLoader().get(DatabaseInfo.ip+
                "androidwebservice/android/user_profile_uploads/1474004058.jpg", new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
 error.printStackTrace() ;
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap=response.getBitmap();
                profileimgpath=saveToInternalStorage(bitmap);
                Constants.PROFILEIMGPATH=profileimgpath;
            }
        });


    }


    public  void getProfileImageFromUrl(String imageUrls){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new ImageRequest

        ImageRequest request=new ImageRequest(imageUrls,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                    //    mImageView.setImageBitmap(response);

                        // Save this downloaded bitmap to internal storage
                        profileimgpath=     saveToInternalStorage(response);

                        // Display the internal storage saved image to image view
                       // mImageViewInternal.setImageURI(uri);
                    }
                },
                0, // Image width
                0, // Image height

                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();

                    }
                }


        );
        requestQueue.add(request);
    }

    public String saveToInternalStorage(Bitmap bitmapImage){
     ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
     //   File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir

          File storageDir = new File(Environment.getExternalStorageDirectory(), "avadhar/images/");
       storageDir.mkdirs(); // make sure you call mkdirs() and not mkdir()
        File image = null;
        try {
             getimgname=getimgname.replace("images","profile_pic_");
            image =new File(storageDir+getimgname);
                    } catch (Exception e) {

        }

        // Save a file: path for use with ACTION_VIEW intents



        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(image);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG

                    , 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return image.getAbsolutePath();
    }
    private void setupWindowAnimations() {
     /*   Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setEnterTransition(slide);*/
    }
    private void setupWindowoutAnimations() {
     /*   Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);*/
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=null;
            switch (item.getItemId()){

                case R.id.main_menu_profile:
                    intent=new Intent(this,Profile.class);
                    intent.putExtra("id",  uid);
                    break;
                case R.id.main_menu_settings:
                    intent=new Intent(this,Settings.class);
                    break;
                case R.id.main_menu_logout:
                    SharedPreferences sp = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constants.ISLOGIN,"0");
                    editor.putString(Constants.PASSWORD, "");
                    editor.putString(Constants.USERNAME, "");
                    editor.putString(Constants.U_NAME, "");
                    editor.putString(Constants.UID,"");
                    editor.putString(Constants.PROFILEID,"");
                    editor.putString(Constants.PROFILEIMG,"");
                    editor.putString(Constants.PROFILEIMGPATH,"");
                    editor.commit();
                    intent=new Intent(this,Login.class);
                    break;

            }
            startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public void finish() {
        super.finish();
    }

}

