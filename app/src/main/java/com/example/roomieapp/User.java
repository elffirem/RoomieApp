package com.example.roomieapp;

import java.io.Serializable;

public class User implements Serializable {
    private String email,UID,department,studentClass,distance,duration,status,contact,photoUrl,name;
    private boolean isUserUpdated=false;



    public User(){

    }

    public User(String email,String UID){
        this.email=email;
        this.UID=UID;

    }
    public void UpdateUser(String name,String department,String studentClass,String distance,String duration,String status,String contact,String photoUrl){
        isUserUpdated=true;
        this.name=name;
        this.contact=contact;
        this.department=department;
        this.distance=distance;
        this.studentClass=studentClass;
        this.duration=duration;
        this.status=status;
        this.photoUrl=photoUrl;
    }
    public String getUid() {
        return UID;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public String getEmail() {
        return email;
    }
    public String getContact() {
        return contact;
    }
    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDistance() {
        return distance;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

}