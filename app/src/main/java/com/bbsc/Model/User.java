package com.bbsc.Model;

public class User {

    private int id;
    private String username, email, name, password, first_name, last_name, images;
    private String enrolled, mobile, currency;
    private int pending_status;
    private String att_data_ques,att_data,exam_title, roll_no, enrollment_no, current_que;


    public User(int id, String username, String email, String name, String password, String first_name, String last_name, String images, String enrolled, String mobile, String currency, int pending_status, String roll_no, String enrollment_no, String exam_title, String att_data, String att_data_ques, String current_que) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.images = images;
        this.enrolled = enrolled;
        this.mobile = mobile;
        this.currency = currency;
        this.pending_status = pending_status;
        this.roll_no = roll_no;
        this.enrollment_no = enrollment_no;
        this.exam_title = exam_title;
        this.att_data = att_data;
        this.att_data_ques = att_data_ques;
        this.current_que = current_que;
    }

    public String getAtt_data_ques() {
        return att_data_ques;
    }

    public void setAtt_data_ques(String att_data_ques) {
        this.att_data_ques = att_data_ques;
    }

    public String getCurrent_que() {
        return current_que;
    }

    public void setCurrent_que(String current_que) {
        this.current_que = current_que;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getEnrollment_no() {
        return enrollment_no;
    }

    public void setEnrollment_no(String enrollment_no) {
        this.enrollment_no = enrollment_no;
    }

    public String getExam_title() {
        return exam_title;
    }

    public String getAtt_data() {
        return att_data;
    }

    public int getpending_status() {
        return pending_status;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getImages() {
        return images;
    }

    public String getPassword() {
        return password;
    }


    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }

    public String getEntroll() {
        return enrolled;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}




