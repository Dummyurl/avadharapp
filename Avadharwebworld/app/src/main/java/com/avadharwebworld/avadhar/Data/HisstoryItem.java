package com.avadharwebworld.avadhar.Data;

/**
 *
 */

public class HisstoryItem {
    private String id,name,proftype,religion,image,mprofileid,status,title,description,date,db,uid;

    public HisstoryItem() {
    }
    public HisstoryItem(String name,String id,String proftype,String image,String religion,String mprofileid,String s,String title1,String desc,String date1,String db1,String uid1) {
        this.name=name;
        this.status=s;
        this.id=id;
        this.proftype=proftype;
        this.religion=religion;
        this.image=image;
        this.mprofileid=mprofileid;
        this.date=date1;
        this.description=desc;
        this.title=title1;
        this.db=db1;this.uid=uid1;

    }
    public void setDb(String db1){this.db=db1;}
    public void setUid(String uid1){this.uid=uid1;}
    public String getDb(){return this.db;}
    public String getUid(){return this.uid;}
    public void setStatus(String status1){this.status=status1;}
    public String getStatus(){return this.status;}
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
    public void setTitle(String title1){this.title=title1;}
    public String getTitle(){return this.title;}
    public void setDescription(String description1){this.description=description1;}
    public String getDescription(){return this.description;}
    public void setDate(String date1){this.date=date1;}
    public String getDate(){return this.date;}
}

