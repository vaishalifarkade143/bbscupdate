package com.bbsc.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Quiz {
    @SerializedName("data")
    @Expose
    private List<Qinfo> data = null;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public List<Qinfo> getData() {
        return data;
    }

    public void setData(List<Qinfo> data) {
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }


    public static class Qinfo {
        @SerializedName("exam_id")
        @Expose
        public String examId;
        @SerializedName("exam_title")
        @Expose
        public String examTitle;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("exam_type")
        @Expose
        public String examType;
        @SerializedName("published")
        @Expose
        public String published;

        @SerializedName("total_marks")
        @Expose
        public String totalMarks;
        @SerializedName("instructions")
        @Expose
        public String instructions;
        @SerializedName("passing_score")
        @Expose
        public String passingScore;
        @SerializedName("pass_feedback")
        @Expose
        public String passFeedback;
        @SerializedName("fail_feedback")
        @Expose
        public String failFeedback;
        @SerializedName("view_score_realtime")
        @Expose
        public String viewScoreRealtime;
        @SerializedName("retake")
        @Expose
        public String retake;
        @SerializedName("attempt_limit")
        @Expose
        public String attemptLimit;
        @SerializedName("time_limit_b")
        @Expose
        public String timeLimitB;
        @SerializedName("duration_h")
        @Expose
        public String durationH;
        @SerializedName("duration_m")
        @Expose
        public String durationM;
        @SerializedName("see_result")
        @Expose
        public String seeResult;
        @SerializedName("show_right_answers")
        @Expose
        public String showRightAnswers;
        @SerializedName("created_by")
        @Expose
        public String createdBy;
        @SerializedName("created_date")
        @Expose
        public String createdDate;

        @SerializedName("modified_date")
        @Expose
        public String modified_date;

        @SerializedName("startpublish")
        @Expose
        public String startpublish;
        @SerializedName("endpublish")
        @Expose
        public String endpublish;
        @SerializedName("ordering")
        @Expose
        public String ordering;
        @SerializedName("exam_category")
        @Expose
        public String examCategory;
        @SerializedName("info_hide")
        @Expose
        public String infoHide;
        @SerializedName("att")
        @Expose
        public String att;
        @SerializedName("exam_last_attempt")
        @Expose
        public String exam_last_attempt;


        @SerializedName("ex_start_date")
        @Expose
        public String ex_start_date;
        @SerializedName("ex_end_date")
        @Expose
        public String ex_end_date;
        @SerializedName("ex_start_time")
        @Expose
        public String ex_start_time;
        @SerializedName("ex_end_time")
        @Expose
        public String ex_end_time;

        @SerializedName("compulsory_que")
        @Expose
        public String compulsory_que;

        @SerializedName("Qcount")
        @Expose
        public String Qcount;

        public String getQcount() {
            return Qcount;
        }

        public void setQcount(String qcount) {
            Qcount = qcount;
        }

        public String getCompulsory_que() {
            return compulsory_que;
        }

        public String getExam_last_attempt() {
            return exam_last_attempt;
        }

        public void setExam_last_attempt(String exam_last_attempt) {
            if(exam_last_attempt.isEmpty()){
                this.exam_last_attempt = "0";
            }else{
                this.exam_last_attempt = exam_last_attempt;
            }

        }

        public void setCompulsory_que(String compulsory_que) {
            this.compulsory_que = compulsory_que;
        }

        public String getEx_start_date() {
            return ex_start_date;
        }

        public void setEx_start_date(String ex_start_date) {
            this.ex_start_date = ex_start_date;
        }

        public String getEx_end_date() {
            return ex_end_date;
        }

        public void setEx_end_date(String ex_end_date) {
            this.ex_end_date = ex_end_date;
        }

        public String getEx_start_time() {
            return ex_start_time;
        }

        public void setEx_start_time(String ex_start_time) {
            this.ex_start_time = ex_start_time;
        }

        public String getEx_end_time() {
            return ex_end_time;
        }

        public void setEx_end_time(String ex_end_time) {
            this.ex_end_time = ex_end_time;
        }

        public String getAtt() {
            return String.valueOf(Integer.parseInt(att)+1);
        }

        public void setAtt(String att) {
            this.att = att;
        }

        public String getModified_date() {
            //modified_date
            return ex_start_date +' '+ ex_start_time;
        }

        public void setModified_date(String modified_date) {
            this.modified_date = modified_date;
        }

        public String getExamId() {
            return examId;
        }

        public void setExamId(String examId) {
            this.examId = examId;
        }

        public String getExamTitle() {
            return examTitle;
        }

        public void setExamTitle(String examTitle) {
            this.examTitle = examTitle;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getExamType() {
            return examType;
        }

        public void setExamType(String examType) {
            this.examType = examType;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getTotalMarks() {
            return totalMarks;
        }

        public void setTotalMarks(String totalMarks) {
            this.totalMarks = totalMarks;
        }

        public String getInstructions() {
            return instructions;
        }

        public void setInstructions(String instructions) {
            this.instructions = instructions;
        }

        public String getPassingScore() {
            return passingScore;
        }

        public void setPassingScore(String passingScore) {
            this.passingScore = passingScore;
        }

        public String getPassFeedback() {
            return passFeedback;
        }

        public void setPassFeedback(String passFeedback) {
            this.passFeedback = passFeedback;
        }

        public String getFailFeedback() {
            return failFeedback;
        }

        public void setFailFeedback(String failFeedback) {
            this.failFeedback = failFeedback;
        }

        public String getViewScoreRealtime() {
            return viewScoreRealtime;
        }

        public void setViewScoreRealtime(String viewScoreRealtime) {
            this.viewScoreRealtime = viewScoreRealtime;
        }

        public String getRetake() {
            return retake;
        }

        public void setRetake(String retake) {
            this.retake = retake;
        }

        public String getAttemptLimit() {
            return attemptLimit;
        }

        public void setAttemptLimit(String attemptLimit) {
            this.attemptLimit = attemptLimit;
        }

        public String getTimeLimitB() {
            return timeLimitB;
        }

        public void setTimeLimitB(String timeLimitB) {
            this.timeLimitB = timeLimitB;
        }

        public String getDurationH() {
            return durationH;
        }

        public void setDurationH(String durationH) {
            this.durationH = durationH;
        }

        public String getDurationM() {
            return durationM;
        }

        public void setDurationM(String durationM) {
            this.durationM = durationM;
        }

        public String getSeeResult() {
            return seeResult;
        }

        public void setSeeResult(String seeResult) {
            this.seeResult = seeResult;
        }

        public String getShowRightAnswers() {
            return showRightAnswers;
        }

        public void setShowRightAnswers(String showRightAnswers) {
            this.showRightAnswers = showRightAnswers;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }


        public String getStartpublish() {
            return startpublish;
        }

        public void setStartpublish(String startpublish) {
            this.startpublish = startpublish;
        }

        public String getEndpublish() {
            return endpublish;
        }

        public void setEndpublish(String endpublish) {
            this.endpublish = endpublish;
        }

        public String getOrdering() {
            return ordering;
        }

        public void setOrdering(String ordering) {
            this.ordering = ordering;
        }

        public String getExamCategory() {
            return examCategory;
        }

        public void setExamCategory(String examCategory) {
            this.examCategory = examCategory;
        }

        public String getInfoHide() {
            return infoHide;
        }

        public void setInfoHide(String infoHide) {
            this.infoHide = infoHide;
        }
    }
}
