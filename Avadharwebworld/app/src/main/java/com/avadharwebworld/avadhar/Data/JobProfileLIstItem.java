package com.avadharwebworld.avadhar.Data;

/**
 * Created by user-03 on 1/9/2017.
 */

public class JobProfileLIstItem {

        private String name, id, profilid, salary, qualification, experience, location, resumetitle, image, jobtype;

        public JobProfileLIstItem() {
        }

        public JobProfileLIstItem(String name, String id, String profilid, String salary, String qualification, String experience, String location, String resumetitle, String image, String jobtype) {
            this.name = name;
            this.id = id;
            this.profilid = profilid;
            this.salary = salary;
            this.qualification = qualification;
            this.experience = experience;
            this.location = location;
            this.resumetitle = resumetitle;
            this.jobtype = jobtype;
            this.image = image;
        }

        public void setName(String name1) {
            this.name = name1;
        }

        public void setId(String id1) {
            this.id = id1;
        }

        public void setProfilid(String profilid1) {
            this.profilid = profilid1;
        }

        public void setSalary(String salary1) {
            this.salary = salary1;
        }

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

        public void setResumetitle(String resumetitle) {
            this.resumetitle = resumetitle;
        }

        public void setQualification(String qualification) {
            this.qualification = qualification;
        }

        public String getName() {
            return name;
        }

        public String getExperience() {
            return experience;
        }

        public String getId() {
            return id;
        }

        public String getResumetitle() {
            return resumetitle;
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

