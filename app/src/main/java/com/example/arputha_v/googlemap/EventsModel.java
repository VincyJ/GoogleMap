package com.example.arputha_v.googlemap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arputha_v on 3/9/2018.
 */

public class EventsModel {
    @SerializedName("sno")
    @Expose
    private String sno;
    @SerializedName("dpuri")
    @Expose
    private String dpuri;
    @SerializedName("eventimage")
    @Expose
    private String eventimage;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("posteddate")
    @Expose
    private String posteddate;
    @SerializedName("postedtime")
    @Expose
    private String postedtime;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("typeofevent")
    @Expose
    private String typeofevent;
    @SerializedName("attendees")
    @Expose
    private String attendees;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("countNo")
    @Expose
    private String countNo;
    @SerializedName("bookCount")
    @Expose
    private String bookCount;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getDpuri() {
        return dpuri;
    }

    public void setDpuri(String dpuri) {
        this.dpuri = dpuri;
    }

    public String getEventimage() {
        return eventimage;
    }

    public void setEventimage(String eventimage) {
        this.eventimage = eventimage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(String posteddate) {
        this.posteddate = posteddate;
    }

    public String getPostedtime() {
        return postedtime;
    }

    public void setPostedtime(String postedtime) {
        this.postedtime = postedtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTypeofevent() {
        return typeofevent;
    }

    public void setTypeofevent(String typeofevent) {
        this.typeofevent = typeofevent;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountNo() {
        return countNo;
    }

    public void setCountNo(String countNo) {
        this.countNo = countNo;
    }

    public String getBookCount() {
        return bookCount;
    }

    public void setBookCount(String bookCount) {
        this.bookCount = bookCount;
    }
}
