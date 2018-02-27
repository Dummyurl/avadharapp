package com.avadharwebworld.avadhar.Support;

/**
 * Created by Vishnu on 01-10-2016.
 */

public class Constants {

    public static final String PREF_NAME="sp_preff";
    public static final String UID="uid";
    public static final String PROFILEID="profileid";
    public static final String U_NAME="name";
    public static final String ISLOGIN="is_login";
    public static final String PASSWORD="password";
    public static final String USERNAME="username";
    public static final String PROFILEIMG="profilepath";
    public static final String PROFILEIMGPNG="profileimg";
    public static       String PHOTOCOMMENTSTRING=null;
    public static       String[] MessageID=null,Commentid=null,CommentLikeCount,Followid,Followerid,Suggetionid;
    public static       byte[] PHOTOCOMMENTBYTE=null;
    public static  String PROFILEIMGPATH="profilesdcardpath";
    public static final String tempprofilepic=DatabaseInfo.ProfilepicURL+"147096675262.jpg";
    public static final int PROFILECARD=0;
    public static final int PROFILENEWSFEED=1;
    public static final int ADSFEED=2;
    public static       int[]PROFDATASET=null;
    public static       String JOBPOSTRESUMEUPLOAD="";
    public static       String JOBPOSTPHOTOUPLOAD="";
    public static       String REALESTATEIMAGE="";
    public static       String BUYANDSELLIMAGE="";

    public Constants(){
    }

    public void setMessageID(String[] messageID) {
        MessageID = messageID;
    }
    public void setCommentid(String[] cmdid){
        Commentid=cmdid;
    }
    public void setCommentLikeCount(String[] Clikecount){
        CommentLikeCount=Clikecount;
    }
}
