package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 12/15/2016.
 */

public class JobItemFavorites {
    private String companyname,id,profilid,salary,qualification,experience,location,jobtitle,image,jobtype;
    public JobItemFavorites() {
    }
    public JobItemFavorites(String companyname, String id, String profilid, String salary, String qualification, String experience, String location, String jobtitle, String image, String jobtype) {
        this.companyname=companyname;
        this.id=id;
        this.profilid=profilid;
        this.salary=salary;
        this.qualification=qualification;
        this.experience=experience;
        this.location=location;
        this.jobtitle=jobtitle;
        this.jobtype=jobtype;
        this.image=image;
    }
public void setCompanyname(String companyname1){this.companyname=companyname1;}
    public void setId(String id1){this.id=id1;}
    public void setProfilid(String profilid1){this.profilid=profilid1;}
    public void setSalary(String salary1){this.salary=salary1;}

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setJobtype(String jobtype) {
        this.jobtype = jobtype;
    }

    public String getJobtype() {
        return jobtype;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCompanyname() {
        return companyname;
    }

    public String getExperience() {
        return experience;
    }

    public String getId() {
        return id;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public String getLocation() {
        return location;
    }

    public String getProfilid() {
        return profilid;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSalary() {
        return salary;
    }
}

