package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 12/15/2016.
 */

public class RealestateItem {
    private String id,name,proftype,image,mprofileid,price,details,location,avadharid;
    public RealestateItem() {
    }
    public RealestateItem(String name,String id,String proftype,String image,String religion,String mprofileid,String price,String details,String location,String avid) {
        this.name=name;
        this.id=id;
        this.proftype=proftype;
        this.price=price;
        this.details=details;
        this.image=image;
        this.mprofileid=mprofileid;
        this.location=location;
        this.avadharid=avid;
    }
    public void setAvadharid(String avadharid1){this.avadharid=avadharid1;}
    public String getAvadharid(){return this.avadharid;}
    public void setLocation(String location1){this.location=location1;}
    public String getLocation(){return this.location;}
    public void setPrice(String price1){this.price=price1;}
    public String getPrice(){return this.price;}
    public void setDetails(String details1){this.details=details1;}
    public String getDetails(){return this.details;}
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
//    public void setReligion(String religion1){
//        this.religion=religion1;
//    }
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
//    public String getReligion(){
//        return this.religion;
//    }
    public String getImage(){
        return this.image;
    }
}

