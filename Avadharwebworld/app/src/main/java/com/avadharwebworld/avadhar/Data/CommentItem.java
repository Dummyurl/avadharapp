package com.avadharwebworld.avadhar.Data;

/**
 * Created by Vishnu on 10-09-2016.
 */
public class CommentItem {
    private int id;
    private String[] MsgidFull;
    private String name,status,image,timestamp,url,profilepic,like,totalcomments="0",share,likecount,uid_fk,msg_uid,msg_id,likecheck;
    public CommentItem() {
    }
    public CommentItem(int id, String name, String image, String status, String profilePic, String timeStamps, String url,String Like,String LIkecount,String uidfk,String totcomment,String uid,String msgid,String likeck){
        super();
        this.id=id;
        this.name=name;
        this.status=status;
        this.image=image;
        this.timestamp=timeStamps;
        this.url=url;
        this.profilepic=profilePic;
        this.like=Like;
        this.likecount=LIkecount;
        this.uid_fk=uidfk;
        this.totalcomments=totcomment;
        this.msg_uid=uid;
        this.msg_id=msgid;
        this.likecheck=likeck;
    }
    public void setLikecheck(String like){
        this.likecheck=like;
    }
    public String getLikecheck(){return likecheck;}
    public void setMsguid(String msguid){
        this.msg_uid=msguid;
    }
    public String getMsguid(){
       return msg_uid;
    }
    public void setmsgid(String msgid){
        this.msg_id=msgid;
    }
    public String getmsgid(){
        return msg_id;
    }
    public void setCommentCount(String cmdcount){
        this.totalcomments=cmdcount;
    }
    public String getTotalcomments(){
        return totalcomments;
    }
    public int getId() {
        return id;
    }
    public void setUid_fk(String uidFk){
        this.uid_fk=uidFk;
    }
    public String getUid_fk(){return  uid_fk;}
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
    public String getLikecount(){
        return likecount;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLikecount(String likecont){
        this.likecount=likecont;
    }
    public String getImge() {
        return image;
    }

    public void setImge(String image) {
        this.image = image;
    }

    public String getComment() {
        return status;
    }

    public void setComment(String Comment) {
        this.status = Comment;
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
}

