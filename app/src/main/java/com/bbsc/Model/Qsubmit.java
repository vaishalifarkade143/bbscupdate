
package com.bbsc.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Qsubmit {
    @SerializedName("data")
    @Expose
    private Score_res data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public Score_res getData() {
        return data;
    }

    public void setData(Score_res data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public class Score_res {
        @SerializedName("stud_id")
        @Expose
        private int studId;
        @SerializedName("exam_id")
        @Expose
        private String examId;
        @SerializedName("pro_id")
        @Expose
        private String proId;
        @SerializedName("attempt_no")
        @Expose
        private String attemptNo;
        @SerializedName("obtain_marks")
        @Expose
        private int obtainMarks;
        @SerializedName("tot_marks")
        @Expose
        private String totMarks;
        @SerializedName("percentage")
        @Expose
        private int percentage;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("attempt_date")
        @Expose
        private String attemptDate;
        @SerializedName("exam_title")
        @Expose
        private String examTitle;
        @SerializedName("see_result")
        @Expose
        private String see_result;

        @SerializedName("pass")
        @Expose
        private String pass;
        @SerializedName("fail")
        @Expose
        private String fail;




        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getFail() {
            return fail;
        }

        public void setFail(String fail) {
            this.fail = fail;
        }

        public String getSee_result() {
            return see_result;
        }

        public void setSee_result(String see_result) {
            this.see_result = see_result;
        }

        public int getStudId() {
            return studId;
        }

        public void setStudId(int studId) {
            this.studId = studId;
        }

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getProId() {
            return proId;
        }

        public void setProId(String proId) {
            this.proId = proId;
        }

        public String getAttemptNo() {
            return attemptNo;
        }

        public void setAttemptNo(String attemptNo) {
            this.attemptNo = attemptNo;
        }

        public int getObtainMarks() {
            return obtainMarks;
        }

        public void setObtainMarks(int obtainMarks) {
            this.obtainMarks = obtainMarks;
        }

        public String getTotMarks() {
            return totMarks;
        }

        public void setTotMarks(String totMarks) {
            this.totMarks = totMarks;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAttemptDate() {
            return attemptDate;
        }

        public void setAttemptDate(String attemptDate) {
            this.attemptDate = attemptDate;
        }

        public String getExamTitle() {
            return examTitle;
        }

        public void setExamTitle(String examTitle) {
            this.examTitle = examTitle;
        }

    }

}