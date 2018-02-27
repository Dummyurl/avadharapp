package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 12/15/2016.
 */

public class EducationFeedItem {
    private String institution_name,inst_id,country,state,city,affiliatedto,affiliationmode,cperson,cnumber,address,website,email,course_id,coursename,eligibility,coursedetail,image;
    public EducationFeedItem() {
    }

    public void setInstitution_name(String insName){
        this.institution_name=insName;
    }
    public void setInst_id(String insid){
        this.inst_id=insid;
    }
    public void setCountry(String country1){
        this.country=country1;
    }
    public void setState(String state1){
        this.state=state1;
    }
    public void setCity(String city1){
        this.city=city1;
    }
    public void setAffiliatedto(String affiliatedto1){
        this.affiliatedto=affiliatedto1;
    }
    public void setAffiliationmode(String affiliationmode1){
        this.affiliationmode=affiliationmode1;
    }
    public void setCperson(String cperson1){
        this.cperson=cperson1;
    }
    public void setCnumber(String cnumber1){
        this.cnumber=cnumber1;
    }
    public void setAddress(String address1){
        this.address=address1;
    }
    public void setWebsite(String website1){
        this.website=website1;
    }
    public void setEmail(String email1){
        this.email=email1;
    }
    public void setCourse_id(String course_id1){
        this.course_id=course_id1;
    }
    public void setCoursename(String coursename1){
        this.coursename=coursename1;
    }
    public void setEligibility(String eligibility1){
        this.eligibility=eligibility1;
    }
    public void setCoursedetail(String coursedetail1){
        this.coursedetail=coursedetail1;
    }
    public void setImage(String image1){this.image=image1;}

    public String getInstitution_name(){
        return this.institution_name;
    }
    public String getInst_id(){
        return this.inst_id;
    }
    public String getCountry(){
        return this.country;
    }
    public String getState(){
        return this.state;
    }
    public String getCity(){
        return this.city;
    }
    public String getAffiliatedto(){
        return this.affiliatedto;
    }
    public String getAffiliationmode(){
        return this.affiliationmode;
    }
    public String getCperson(){
        return this.cperson;
    }
    public String getCnumber(){
        return this.cnumber;
    }
    public String getAddress(){
        return this.address;
    }
    public String getWebsite(){
        return this.website;
    }
    public String getEmail(){
        return this.email;
    }
    public String getCourse_id(){
        return this.course_id;
    }
    public String getCoursename(){
        return this.coursename;
    }
    public String getEligibility(){
        return this.eligibility;
    }
    public String getImage (){return  this.image;}

}
