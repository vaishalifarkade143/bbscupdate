package com.bbsc.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QlistRes {
    @SerializedName("data")
    @Expose
    private List<Ques> data = null;
    @SerializedName("error")
    @Expose
    private Boolean error;

    public List<Ques> getData() {
        return data;
    }

    public void setData(List<Ques> data) {
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public static class Ques {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("section_id")
        @Expose
        public String sectionId;
        @SerializedName("que_title")
        @Expose
        public String queTitle;
        @SerializedName("que_type")
        @Expose
        public String queType;
        @SerializedName("options")
        @Expose
        public String options;
        @SerializedName("que_option")
        @Expose
        public String queOption;
        @SerializedName("correct_ans")
        @Expose
        public String correctAns;
        @SerializedName("que_marks")
        @Expose
        public String queMarks;

        @SerializedName("image")
        @Expose
        public String image;

        @SerializedName("exam_id")
        @Expose
        public String exam_id;

        @SerializedName("modified_date")
        @Expose
        public String modified_date;

        public String getModified_date() {

            return modified_date;
        }

        public void setModified_date(String modified_date) {
            this.modified_date = modified_date;
        }

        public String getExam_id() {
            return exam_id;
        }

        public void setExam_id(String exam_id) {
            this.exam_id = exam_id;
        }



        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSectionId() {
            return sectionId;
        }

        public void setSectionId(String sectionId) {
            this.sectionId = sectionId;
        }

        public String getQueTitle() {
            return queTitle;
        }

        public void setQueTitle(String queTitle) {
            this.queTitle = queTitle;
        }

        public String getQueType() {
            return queType;
        }

        public void setQueType(String queType) {
            this.queType = queType;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions(String options) {
            this.options = options;
        }

        public String getQueOption() {
            return queOption;
        }

        public void setQueOption(String queOption) {
            this.queOption = queOption;
        }

        public String getCorrectAns() {
            return correctAns;
        }

        public void setCorrectAns(String correctAns) {
            this.correctAns = correctAns;
        }

        public String getQueMarks() {
            return queMarks;
        }

        public void setQueMarks(String queMarks) {
            this.queMarks = queMarks;
        }
    }
}
