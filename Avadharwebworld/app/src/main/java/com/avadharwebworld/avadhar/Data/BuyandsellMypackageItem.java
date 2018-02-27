package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 12/15/2016.
 */

public class BuyandsellMypackageItem {
    private String id,name,proftype,image,mprofileid,price,details,location,avadharid,activitytype,property,pricefrom,priceto;
    public BuyandsellMypackageItem() {
    }
    public void setPricefrom(String pricefrom) {
        this.pricefrom = pricefrom;
    }

    public String getPricefrom() {
        return pricefrom;
    }

    public String getPriceto() {
        return priceto;
    }

    public void setPriceto(String priceto) {
        this.priceto = priceto;
    }

    public void setProperty(String property1){this.property=property1;}
    public String getProperty(){return this.property;}
    public void setActivitytype(String activitytype1){this.activitytype=activitytype1;}
    public String getActivitytype(){return this.activitytype;}
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

