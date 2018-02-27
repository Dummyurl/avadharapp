package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 12/15/2016.
 */

public class MatrimonyInterestRecievedItem {
    private String id,name,proftype,religion,image,mprofileid,interesteduid;
    public MatrimonyInterestRecievedItem() {
    }
    public MatrimonyInterestRecievedItem(String name,String id,String proftype,String image,String religion,String mprofileid,String intuid) {
        this.name=name;
        this.id=id;
        this.proftype=proftype;
        this.religion=religion;
        this.image=image;
        this.mprofileid=mprofileid;
        this.interesteduid=intuid;
    }
    public void setInteresteduid(String interesteduid1){
        this.interesteduid=interesteduid1;
    }
    public String getInteresteduid(){return  this.interesteduid;}
    public void setMprofileid(String mprofileid1){this.mprofileid=mprofileid1;}
    public String getMprofileid(){return  this.mprofileid;}
    public void setId(String id1){
        this.id=id1;
    }
    public void setName(String name1){
        this.name=name1;
    }
    public void setProftype(String proftype1){
        this.proftype=proftype1;
    }
    public void setReligion(String religion1){
        this.religion=religion1;
    }
    public void setImage(String image1){
        this.image=image1;
    }
    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getProftype(){
        return this.proftype;
    }
    public String getReligion(){
        return this.religion;
    }
    public String getImage(){
        return this.image;
    }
}

