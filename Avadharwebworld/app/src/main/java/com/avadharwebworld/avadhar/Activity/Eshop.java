package com.avadharwebworld.avadhar.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.avadharwebworld.avadhar.R;
public class Eshop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eshop);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            case R.id.menu_follower_profile:
                intent=new Intent(this,Profile.class);
                break;
            case R.id.menu_follower_follower:
                intent=new Intent(this,Followers.class);
                break;
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.followin_menu, menu);
//        final EditText editText = (EditText) menu.findItem(
//                R.id.menu_search).getActionView();
//        editText.addTextChangedListener(textWatcher);

//        MenuItem menuItem = menu.findItem(R.id.menu_search);
       // final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
      //  SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
     //   final EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
      //  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
      //  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         //   @Override
        //    public boolean onQueryTextSubmit(String query) {
          //      Toast.makeText(getApplicationContext(),query+"/ edit text value : "+searchPlate.getText().toString(),Toast.LENGTH_LONG ).show();
          //      return false;
         //   }

         //   @Override
         //   public boolean onQueryTextChange(String newText) {
         //       return false;
         //   }
     //   });
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
}

