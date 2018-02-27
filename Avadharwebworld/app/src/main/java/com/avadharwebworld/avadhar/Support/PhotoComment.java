package com.avadharwebworld.avadhar.Support;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.avadharwebworld.avadhar.Adapter.CommentAdapter;
import com.avadharwebworld.avadhar.Data.CommentItem;

import java.util.List;

/**
 * Created by user-03 on 14-Nov-16.
 */

public class PhotoComment extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    private String picturePath;
    private Context myContext;
    CommentItem item=new CommentItem();
    private List<CommentItem> cmditem;
//    CommentAdapter Cadapter=new CommentAdapter(this,cmditem, myContext);

    private void setImageComment(){


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath1 = cursor.getString(columnIndex);
            cursor.close();

            picturePath=picturePath1;

        }


    }
}
