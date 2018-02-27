package com.avadharwebworld.avadhar.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.avadharwebworld.avadhar.R;

public class Settings extends AppCompatActivity {
private ListView rvSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_settings);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.action_settings);
        rvSettings=new ListView(this);

        rvSettings.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.settings)));
        setContentView(rvSettings);

      rvSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Intent intent=null;
              if(position==0){
                  intent=new Intent(getApplicationContext(),SettingsProfile.class);
                  startActivity(intent);
              }
              if(position==1){
                  intent=new Intent(getApplicationContext(),SettingsPersonal.class);
                  startActivity(intent);
              }
              if(position==2){
                  intent=new Intent(getApplicationContext(),SettingsAcademic.class);
                  startActivity(intent);
              }
              if(position==3){
                  intent=new Intent(getApplicationContext(),SettingsAbout.class);
                  startActivity(intent);
              }
              if(position==4){
                  intent=new Intent(getApplicationContext(),SettingsAccount.class);
                  startActivity(intent);
              }

              if(position==5){
                  intent=new Intent(getApplicationContext(),SettingsMyteam.class);
                  startActivity(intent);
              }
          }
      });
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


}
