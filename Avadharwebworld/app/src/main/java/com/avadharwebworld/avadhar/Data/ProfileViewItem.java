package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 10-Dec-16.
 */

public class ProfileViewItem {
    private String name,coverpic,profilepic,username,profileid,email,conversationcount,updates_count,profilepic_status,friend_count;
    private int uid;
    public ProfileViewItem(){
    }
    public ProfileViewItem(String Name,String Coverpic,String Profilepic,String Username,String Profileid, String Email,String Conversationcount,String Updates_count,String Profile_status,int Uid,String Friend_count){
        super();
        this.name=Name;
        this.coverpic=Coverpic;
        this.profilepic=Profilepic;
        this.username=Username;
        this.profileid=Profileid;
        this.email=Email;
        this.conversationcount=Conversationcount;
        this.updates_count=Updates_count;
        this.profilepic_status=Profile_status;
        this.friend_count=Friend_count;
        this.uid=Uid;
    }
    public void setName(String name1){
        this.name=name1;
    }
    public void setCoverpic(String coverpic1){
        this.coverpic=coverpic1;
    }
    public void setProfilepic(String profilepic1){
        this.profilepic=profilepic1;
    }
    public void setUsername(String username1){
        this.username=username1;
    }
    public void setProfileid(String profileid1){
        this.profileid=profileid1;
    }
    public void setEmail(String email1){
        this.email=email1;
    }
    public void setConversationcount(String conversationcount1){
        this.conversationcount=conversationcount1;
    }
    public void setUpdates_count(String updates_count1){
        this.updates_count=updates_count1;
    }
    public void setProfilepic_status(String profilepic_status1){
        this.profilepic_status=profilepic_status1;
    }
    public void setUid(int uid1){
        this.uid=uid1;
    }

    public int getUid() {
        return uid;
    }

    public String getConversationcount() {
        return conversationcount;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getProfileid() {
        return profileid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getProfilepic_status() {
        return profilepic_status;
    }

    public String getUpdates_count() {
        return updates_count;
    }

    public String getUsername() {
        return username;
    }
    public void setFriend_count(String friend_count1){
        this.friend_count=friend_count1;
    }
    public String getFriend_count(){
        return friend_count;
    }
}
