package com.avadharwebworld.avadhar.Data;

/**
 * Created by Vishnu on 10-09-2016.
 */
public class NewsFeedItem {
    private int id,messageid;
    private String[] imgpath;
    private String name,status,image,timestamp,url,profilepic,like,commentcount,share,shareUsername,shareprofilepic,shareUid;
    public NewsFeedItem() {
    }
    public NewsFeedItem(int id, String name, String image, String status, String profilePic, String timeStamps, String url,String Like,int  MEssageid,String[] path,String comdcount,String shr,String shrusername,String shareprof, String sharuid ){
        super();
        this.id=id;
        this.name=name;
        this.status=status;
        this.image=image;
        this.timestamp=timeStamps;
        this.url=url;
        this.profilepic=profilePic;
        this.like=Like;
        this.messageid=MEssageid;
        this.imgpath=path;
        this.commentcount=comdcount;
        this.share=shr;
        this.shareUsername=shrusername;
        this.shareprofilepic=shareprof;
        this.shareUid=sharuid;
    }
    public String getShareUsername(){return  this.shareUsername;}
    public String getShareprofilepic(){return this.shareprofilepic;}
    public String getShareUid(){return this.shareUid;}
    public void setShareUsername(String uname){
        this.shareUsername=uname;
    }
    public void setShareprofilepic(String shareprof){
        this.shareprofilepic=shareprof;
    }
    public void setShareUid(String suid){
        this.shareUid=suid;
    }
    public void setShare(String shr){
        this.share=shr;
    }
    public String getShare(){return share;}
    public int getId() {
        return id;
    }
    public String getCommentcount(){
        return commentcount;
    }
    public void setCommentcount(String cmdcount){
        this.commentcount=cmdcount;
    }
    public void setId(int id) {
        this.id = id;
    }
    public  void setLike(String Likes){
        this.like=Likes;
    }
    public String getLike(){
        return like;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImge() {
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }
    public int getMessageid(){
        return messageid;
    }
    public void setMessageid(int messageid){
        this.messageid=messageid;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilepic;
    }

    public void setProfilePic(String profilePic) {
        this.profilepic = profilePic;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timestamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getImgpath(){
        return imgpath;
    }
    public void setImgpath(String[] imagepath){
        this.imgpath=imagepath;
    }
}

