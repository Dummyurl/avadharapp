package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 12/15/2016.
 */

public class FollowerItem {
    private String uname,uid,followstatus,coverpic,profilepic,username;
    public FollowerItem() {
    }
    public FollowerItem(String uname1, String uid1, String followstatus1, String coverpic1, String profilepic1, String username1){
        this.uname=uname1;
        this.uid=uid1;
        this.followstatus=followstatus1;
        this.coverpic=coverpic1;
        this.profilepic=profilepic1;
        this.username=username1;
    }
    public void setUname(String uname1){
        this.uname=uname1;
    }
    public void setUid(String uid1){
        this.uid=uid1;
    }
    public void setFollowstatus(String followstatus1){
        this.followstatus=followstatus1;
    }
    public void setCoverpic(String coverpic1){
        this.coverpic=coverpic1;
    }
    public void setProfilepic(String profilepic1){
        this.profilepic=profilepic1;
    }
    public String getUname(){
        return this.uname;
    }
    public String getUid(){
        return this.uid;
    }
    public String getFollowstatus(){
        return this.followstatus;
    }
    public String getCoverpic(){
        return  this.coverpic;
    }
    public String getProfilepic(){
        return this.profilepic;
    }

    public void setUsername(String username1){
        this.username=username1;
    }
    public String getUsername(){
        return this.username;
    }
}
