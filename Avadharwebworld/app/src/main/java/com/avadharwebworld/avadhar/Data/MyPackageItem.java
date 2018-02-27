package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 2/14/2017.
 */
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyPackageItem {
    private String userid,id,resumetitle,qualification,experience,location,jobtype,interstedfield,count,religion,familytype,mstatus,bridegroom;
    public MyPackageItem() {
    }
    public MyPackageItem(String resumetitle,String id,String profilid,String salary,String qualification,String experience,String location,String jobtitle,String image,String jobtype,String interstedfield,String count) {
        this.resumetitle=resumetitle;
        this.id=id;
        this.qualification=qualification;
        this.experience=experience;
        this.location=location;
        this.jobtype=jobtype;
        this.interstedfield=interstedfield;
        this.count=count;

    }
    public void setReligion(String religion1){this.religion=religion1;}
    public void setFamilytype(String familytype1){this.familytype=familytype1;}
    public void setMstatus(String mstatus1){this.mstatus=mstatus1;}
    public void setBridegroom(String bridegroom1){this.bridegroom=bridegroom1;}
    public String getReligion(){return  religion;}
    public String getFamilytype(){return  familytype;}
    public String getMstatus(){return  mstatus;}
    public String getBridegroom(){return  bridegroom;}

    public void setInterstedfield(String interstedfield1){this.interstedfield=interstedfield1;}
    public void setId(String id1){this.id=id1;}
    public void setResumetitle(String resumetitle1){this.resumetitle=resumetitle1;}
    public void setCount(String count1){this.count=count1;}
    public String getCount(){return  count;}
    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }
    public String getInterstedfield(){return this.interstedfield;}
    public String getJobtype() {
        return jobtype;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }


    public void setQualification(String qualification) {
        this.qualification = qualification;
    }


    public String getExperience() {
        return experience;
    }

    public String getId() {
        return id;
    }


    public String getLocation() {
        return location;
    }
    public String getResumetitle(){return resumetitle;}
    public String getQualification() {
        return qualification;
    }

}
