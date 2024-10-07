package com.bbsc.Model;



import java.util.List;

public class QuizRequestBody {

    private List<Attempt> attempt;

    private String roll_no;
    private String enrollment_no;



//     Constructor
    public QuizRequestBody(List<Attempt> attempt, String roll_no, String enrollment_no) {
        this.attempt = attempt;  // Initialize attempt
        this.roll_no = roll_no;
        this.enrollment_no = enrollment_no;
    }

    public QuizRequestBody() {

    }

    public List<Attempt> getAttempt() {
        return attempt;
    }

    public void setAttempt(List<Attempt> attempt) {
        this.attempt = attempt;
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

    public static class Attempt {
        private String que_id;
        private String exam_id;
        private int stud_id;
        private String pro_id;
        private String correct_ans;
        private int stud_given_ans;
        private String is_correct;
        private String attempt_no;
        private String ans_marks;

        public Attempt(String que_id, String exam_id, int stud_id, String pro_id, String correct_ans, int stud_given_ans, String is_correct, String attempt_no, String ans_marks) {
            this.que_id = que_id;
            this.exam_id = exam_id;
            this.stud_id = stud_id;
            this.pro_id = pro_id;
            this.correct_ans = correct_ans;
            this.stud_given_ans = stud_given_ans;
            this.is_correct = is_correct;
            this.attempt_no = attempt_no;
            this.ans_marks = ans_marks;
        }

        // Constructors, getters, and setters

        public String getQue_id() {
            return que_id;
        }

        public void setQue_id(String que_id) {
            this.que_id = que_id;
        }

        public String getExam_id() {
            return exam_id;
        }

        public void setExam_id(String exam_id) {
            this.exam_id = exam_id;
        }

        public int getStud_id() {
            return stud_id;
        }

        public void setStud_id(int stud_id) {
            this.stud_id = stud_id;
        }

        public String getPro_id() {
            return pro_id;
        }

        public void setPro_id(String pro_id) {
            this.pro_id = pro_id;
        }

        public String getCorrect_ans() {
            return correct_ans;
        }

        public void setCorrect_ans(String correct_ans) {
            this.correct_ans = correct_ans;
        }

        public int getStud_given_ans() {
            return stud_given_ans;
        }

        public void setStud_given_ans(int stud_given_ans) {
            this.stud_given_ans = stud_given_ans;
        }

        public String getIs_correct() {
            return is_correct;
        }

        public void setIs_correct(String is_correct) {
            this.is_correct = is_correct;
        }

        public String getAttempt_no() {
            return attempt_no;
        }

        public void setAttempt_no(String attempt_no) {
            this.attempt_no = attempt_no;
        }

        public String getAns_marks() {
            return ans_marks;
        }

        public void setAns_marks(String ans_marks) {
            this.ans_marks = ans_marks;
        }
    }
}
