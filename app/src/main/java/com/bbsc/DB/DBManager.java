package com.bbsc.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.bbsc.Model.QlistRes;
import com.bbsc.Model.Quiz;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DBManager {

    private DatabaseHelper dbHelper;


    private Context context;
    @SuppressLint("DefaultLocale") String hms;
    String format = "yyyy-MM-dd HH:mm:ss";
    private SQLiteDatabase database;
    SimpleDateFormat sdf;
    String currentDate, currentTime;
    Date dateObj2 = null;
    public DBManager(Context c) {
        context = c;
        sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }



//    public void close() {
//
//        dbHelper.close();
//    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }


    public void insertQuesList(String TB, List<QlistRes.Ques> desc) {


        for (QlistRes.Ques desc1: desc) {
            ContentValues contentValue = new ContentValues();
            contentValue.put("id", String.valueOf(desc1.getId()));
            contentValue.put("section_id", String.valueOf(desc1.getSectionId()));
            contentValue.put("que_title", String.valueOf(desc1.getQueTitle()));
            contentValue.put("que_type", String.valueOf(desc1.getQueType()));
            contentValue.put("image", String.valueOf(desc1.getImage()));
            contentValue.put("options", String.valueOf(desc1.getOptions()));
            contentValue.put("que_option", String.valueOf(desc1.getQueOption()));
            contentValue.put("correct_ans", String.valueOf(desc1.getCorrectAns()));
            contentValue.put("que_marks", String.valueOf(desc1.getQueMarks()));
            contentValue.put("exam_id", String.valueOf(desc1.getExam_id()));
            contentValue.put("datetime", currentDate+" "+currentTime);
            try {
                long rowInserted = database.insert("Questions", null, contentValue);
                if(rowInserted != -1)
                    Log.d("got round", "insertQuesList call ");
//                    Toast.makeText(context, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void deleteQuestion() {
        try {
            database.delete("Questions", null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void insertQzList(String TB, List<Quiz.Qinfo> desc) {
//        for (Quiz.Qinfo desc1: desc) {
//            ContentValues contentValue = new ContentValues();
//            contentValue.put("exam_id", String.valueOf(desc1.getExamId()));
//            contentValue.put("exam_title", String.valueOf(desc1.getExamTitle()));
//            contentValue.put("compulsory_que", String.valueOf(desc1.getCompulsory_que()));
//            contentValue.put("description", String.valueOf(desc1.getDescription()));
//            contentValue.put("exam_type", String.valueOf(desc1.getExamType()));
//            contentValue.put("published", String.valueOf(desc1.getPublished()));
//            contentValue.put("total_marks", String.valueOf(desc1.getTotalMarks()));
//            contentValue.put("instructions", String.valueOf(desc1.getInstructions()));
//            contentValue.put("passing_score", String.valueOf(desc1.getPassingScore()));
//            contentValue.put("pass_feedback", String.valueOf(desc1.getPassFeedback()));
//            contentValue.put("fail_feedback", String.valueOf(desc1.getFailFeedback()));
//
//            contentValue.put("view_score_realtime", String.valueOf(desc1.getViewScoreRealtime()));
//            contentValue.put("retake", String.valueOf(desc1.getRetake()));
//            contentValue.put("attempt_limit", String.valueOf(desc1.getAttemptLimit()));
//            contentValue.put("time_limit_b", String.valueOf(desc1.getTimeLimitB()));
//            contentValue.put("duration_h", String.valueOf(desc1.getDurationH()));
//            contentValue.put("duration_m", String.valueOf(desc1.getDurationM()));
//            contentValue.put("see_result", String.valueOf(desc1.getSeeResult()));
//            contentValue.put("show_right_answers", String.valueOf(desc1.getShowRightAnswers()));
//            contentValue.put("created_by", String.valueOf(desc1.getCreatedBy()));
//            contentValue.put("created_date", String.valueOf(desc1.getCreatedDate()));
//            contentValue.put("modified_date", String.valueOf(desc1.getModified_date()));
//            contentValue.put("startpublish", String.valueOf(desc1.getStartpublish()));
//            contentValue.put("endpublish", String.valueOf(desc1.getEndpublish()));
//            contentValue.put("ordering", String.valueOf(desc1.getOrdering()));
//            contentValue.put("exam_category", String.valueOf(desc1.getExamCategory()));
//            contentValue.put("info_hide", String.valueOf(desc1.getInfoHide()));
//            contentValue.put("ex_start_date", String.valueOf(desc1.getEx_start_date()));
//            contentValue.put("ex_end_date", String.valueOf(desc1.getEx_end_date()));
//            contentValue.put("ex_start_time", String.valueOf(desc1.getEx_start_time()));
//            contentValue.put("ex_end_time", String.valueOf(desc1.getEx_end_time()));
//
////            contentValue.put("department",desc1.getd);
//            contentValue.put("att", String.valueOf(Integer.parseInt(desc1.getAtt())-1));
//            contentValue.put("Qcount", String.valueOf(desc1.getQcount()));
//            try {
//                database.insert("QuizList", null, contentValue);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//    }

//added 10/17/24 12:51 solve error
    public void insertQzList(String TB, List<Quiz.Qinfo> desc) {
        try {
            // Ensure the database is open before inserting
            if (database == null || !database.isOpen()) {
                open(); // Open the database if it's not open
            }

            for (Quiz.Qinfo desc1 : desc) {
                ContentValues contentValue = new ContentValues();
                contentValue.put("exam_id", String.valueOf(desc1.getExamId()));
                contentValue.put("exam_title", String.valueOf(desc1.getExamTitle()));
                contentValue.put("compulsory_que", String.valueOf(desc1.getCompulsory_que()));
                contentValue.put("description", String.valueOf(desc1.getDescription()));
                contentValue.put("exam_type", String.valueOf(desc1.getExamType()));
                contentValue.put("published", String.valueOf(desc1.getPublished()));
                contentValue.put("total_marks", String.valueOf(desc1.getTotalMarks()));
                contentValue.put("instructions", String.valueOf(desc1.getInstructions()));
                contentValue.put("passing_score", String.valueOf(desc1.getPassingScore()));
                contentValue.put("pass_feedback", String.valueOf(desc1.getPassFeedback()));
                contentValue.put("fail_feedback", String.valueOf(desc1.getFailFeedback()));
                contentValue.put("view_score_realtime", String.valueOf(desc1.getViewScoreRealtime()));
                contentValue.put("retake", String.valueOf(desc1.getRetake()));
                contentValue.put("attempt_limit", String.valueOf(desc1.getAttemptLimit()));
                contentValue.put("time_limit_b", String.valueOf(desc1.getTimeLimitB()));
                contentValue.put("duration_h", String.valueOf(desc1.getDurationH()));
                contentValue.put("duration_m", String.valueOf(desc1.getDurationM()));
                contentValue.put("see_result", String.valueOf(desc1.getSeeResult()));
                contentValue.put("show_right_answers", String.valueOf(desc1.getShowRightAnswers()));
                contentValue.put("created_by", String.valueOf(desc1.getCreatedBy()));
                contentValue.put("created_date", String.valueOf(desc1.getCreatedDate()));
                contentValue.put("modified_date", String.valueOf(desc1.getModified_date()));
                contentValue.put("startpublish", String.valueOf(desc1.getStartpublish()));
                contentValue.put("endpublish", String.valueOf(desc1.getEndpublish()));
                contentValue.put("ordering", String.valueOf(desc1.getOrdering()));
                contentValue.put("exam_category", String.valueOf(desc1.getExamCategory()));
                contentValue.put("info_hide", String.valueOf(desc1.getInfoHide()));
                contentValue.put("ex_start_date", String.valueOf(desc1.getEx_start_date()));
                contentValue.put("ex_end_date", String.valueOf(desc1.getEx_end_date()));
                contentValue.put("ex_start_time", String.valueOf(desc1.getEx_start_time()));
                contentValue.put("ex_end_time", String.valueOf(desc1.getEx_end_time()));
                contentValue.put("att", String.valueOf(Integer.parseInt(desc1.getAtt()) - 1));
                contentValue.put("Qcount", String.valueOf(desc1.getQcount()));

                // Perform the insert operation
                database.insert("QuizList", null, contentValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Always close the database after operations to avoid memory leaks
            close();
        }
    }



//    public void insertQzList(String TB, List<Quiz.Qinfo> desc) {
//        for (Quiz.Qinfo desc1 : desc) {
//            ContentValues contentValue = new ContentValues();
//            contentValue.put("exam_id", String.valueOf(desc1.getExamId()));
//            contentValue.put("exam_title", String.valueOf(desc1.getExamTitle()));
//            contentValue.put("compulsory_que", String.valueOf(desc1.getCompulsory_que()));
//            contentValue.put("description", String.valueOf(desc1.getDescription()));
//            contentValue.put("exam_type", String.valueOf(desc1.getExamType()));
//            contentValue.put("published", String.valueOf(desc1.getPublished()));
//            contentValue.put("total_marks", String.valueOf(desc1.getTotalMarks()));
//            contentValue.put("instructions", String.valueOf(desc1.getInstructions()));
//            contentValue.put("passing_score", String.valueOf(desc1.getPassingScore()));
//            contentValue.put("pass_feedback", String.valueOf(desc1.getPassFeedback()));
//            contentValue.put("fail_feedback", String.valueOf(desc1.getFailFeedback()));
//            contentValue.put("view_score_realtime", String.valueOf(desc1.getViewScoreRealtime()));
//            contentValue.put("retake", String.valueOf(desc1.getRetake()));
//            contentValue.put("attempt_limit", String.valueOf(desc1.getAttemptLimit()));
//            contentValue.put("time_limit_b", String.valueOf(desc1.getTimeLimitB()));
//            contentValue.put("duration_h", String.valueOf(desc1.getDurationH()));
//            contentValue.put("duration_m", String.valueOf(desc1.getDurationM()));
//            contentValue.put("see_result", String.valueOf(desc1.getSeeResult()));
//            contentValue.put("show_right_answers", String.valueOf(desc1.getShowRightAnswers()));
//            contentValue.put("created_by", String.valueOf(desc1.getCreatedBy()));
//            contentValue.put("created_date", String.valueOf(desc1.getCreatedDate()));
//            contentValue.put("modified_date", String.valueOf(desc1.getModified_date()));
//            contentValue.put("startpublish", String.valueOf(desc1.getStartpublish()));
//            contentValue.put("endpublish", String.valueOf(desc1.getEndpublish()));
//            contentValue.put("ordering", String.valueOf(desc1.getOrdering()));
//            contentValue.put("exam_category", String.valueOf(desc1.getExamCategory()));
//            contentValue.put("info_hide", String.valueOf(desc1.getInfoHide()));
//            contentValue.put("ex_start_date", String.valueOf(desc1.getEx_start_date()));
//            contentValue.put("ex_end_date", String.valueOf(desc1.getEx_end_date()));
//            contentValue.put("ex_start_time", String.valueOf(desc1.getEx_start_time()));
//            contentValue.put("ex_end_time", String.valueOf(desc1.getEx_end_time()));
//
//            // Decrease attempt number by 1
//            contentValue.put("att", String.valueOf(Integer.parseInt(desc1.getAtt()) - 1));
//            contentValue.put("Qcount", String.valueOf(desc1.getQcount()));
//
//            // Check if record already exists
//            String selectQuery = "SELECT COUNT(*) FROM " + TB + " WHERE exam_id = ?";
//            Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(desc1.getExamId())});
//            if (cursor != null) {
//                cursor.moveToFirst();
//                int count = cursor.getInt(0);
//                cursor.close();
//
//                if (count > 0) {
//                    // Record exists, update it
//                    database.update(TB, contentValue, "exam_id = ?", new String[]{String.valueOf(desc1.getExamId())});
//                    Log.d("DB Update", "Updated record for exam_id: " + desc1.getExamId());
//                } else {
//                    // Record doesn't exist, insert it
//                    database.insert(TB, null, contentValue);
//                    Log.d("DB Insert", "Inserted new record for exam_id: " + desc1.getExamId());
//                }
//            }
//        }
//    }


    public Cursor fetchQzList() {
        String[] columns = new String[] {
                "exam_id",
                "exam_title",
                "compulsory_que",
                "description",
                "exam_type",
                "published",
                "total_marks",
                "instructions",
                "passing_score",
                "pass_feedback",
                "fail_feedback",
                "view_score_realtime",
                "retake",
                "attempt_limit",
                "time_limit_b",
                "duration_h",
                "duration_m",
                "see_result",
                "show_right_answers",
                "created_by",
                "created_date",
                "modified_date",
                "startpublish",
                "endpublish",
                "ordering",
                "exam_category",
                "info_hide",
                "ex_start_date",
                "ex_end_date",
                "ex_start_time",
                "ex_end_time",
                "att",
                "Qcount",
        };
        Cursor cursor = database.query("QuizList", columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

//    public void updateQuizList(String ex_id, String attempt)
//    {
//        SQLiteOpenHelper database = new DatabaseHelper(context);
//        SQLiteDatabase db = database.getReadableDatabase();
//        String updateQuery ="UPDATE QuizList SET att = "+attempt+" WHERE exam_id = "+ex_id;
//        Cursor c= db.rawQuery(updateQuery, null);
//
//        c.moveToFirst();
//        c.close();
//    }



public void updateQuizList(String ex_id, String attempt) {
    SQLiteOpenHelper database = new DatabaseHelper(context);
    SQLiteDatabase db = database.getWritableDatabase(); // Use writable database

    // Use a parameterized query to avoid SQL injection risks
    String updateQuery = "UPDATE QuizList SET att = ? WHERE exam_id = ?";

    try {
        // Execute the update query
        db.execSQL(updateQuery, new String[]{attempt, ex_id});
        Log.d("DB Update", "Updated attempt number: " + attempt + " for exam_id: " + ex_id);
    } catch (Exception e) {
        Log.e("DB Update Error", "Error updating quiz list: " + e.getMessage());
    } finally {
        db.close(); // Always close the database to avoid memory leaks
    }
}

//    public void deleteQuizList() {
//        try {
//            database.delete("QuizList", null, null);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    //added 10/17/24 12:51 solve error
    // Delete data from the QuizList table
    public void deleteQuizList() {
        try {
            open(); // Open the database before performing the delete
            database.delete(DatabaseHelper.TABLE_NAME, null, null); // Perform the delete operation
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(); // Close the database after the operation is done
        }
    }



    public List<Quiz.Qinfo> getAllQuizzes() {
        SQLiteOpenHelper database = new DatabaseHelper(context);
        SQLiteDatabase db = database.getReadableDatabase();
        ArrayList<Quiz.Qinfo> listItems = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * from QuizList", null);

        if (cursor.moveToFirst()) {
            do {
                Quiz.Qinfo note = new Quiz.Qinfo();

                int examIdIndex = cursor.getColumnIndex("exam_id");
                int examTitleIndex = cursor.getColumnIndex("exam_title");
                int compulsoryQueIndex = cursor.getColumnIndex("compulsory_que");
                int descriptionIndex = cursor.getColumnIndex("description");
                int examTypeIndex = cursor.getColumnIndex("exam_type");
                int publishedIndex = cursor.getColumnIndex("published");
                int totalMarksIndex = cursor.getColumnIndex("total_marks");
                int instructionsIndex = cursor.getColumnIndex("instructions");
                int passingScoreIndex = cursor.getColumnIndex("passing_score");
                int passFeedbackIndex = cursor.getColumnIndex("pass_feedback");
                int failFeedbackIndex = cursor.getColumnIndex("fail_feedback");
                int viewScoreRealtimeIndex = cursor.getColumnIndex("view_score_realtime");
                int retakeIndex = cursor.getColumnIndex("retake");
                int attemptLimitIndex = cursor.getColumnIndex("attempt_limit");
                int timeLimitBIndex = cursor.getColumnIndex("time_limit_b");
                int durationHIndex = cursor.getColumnIndex("duration_h");
                int durationMIndex = cursor.getColumnIndex("duration_m");
                int seeResultIndex = cursor.getColumnIndex("see_result");
                int showRightAnswersIndex = cursor.getColumnIndex("show_right_answers");
                int createdByIndex = cursor.getColumnIndex("created_by");
                int createdDateIndex = cursor.getColumnIndex("created_date");
                int modifiedDateIndex = cursor.getColumnIndex("modified_date");
                int startPublishIndex = cursor.getColumnIndex("startpublish");
                int endPublishIndex = cursor.getColumnIndex("endpublish");
                int orderingIndex = cursor.getColumnIndex("ordering");
                int examCategoryIndex = cursor.getColumnIndex("exam_category");
                int infoHideIndex = cursor.getColumnIndex("info_hide");
                int exStartDateIndex = cursor.getColumnIndex("ex_start_date");
                int exEndDateIndex = cursor.getColumnIndex("ex_end_date");
                int exStartTimeIndex = cursor.getColumnIndex("ex_start_time");
                int exEndTimeIndex = cursor.getColumnIndex("ex_end_time");
                int attIndex = cursor.getColumnIndex("att");
                int qCountIndex = cursor.getColumnIndex("Qcount");

                if (examIdIndex != -1) note.examId = String.valueOf(cursor.getInt(examIdIndex));
                if (examTitleIndex != -1) note.examTitle = cursor.getString(examTitleIndex);
                if (compulsoryQueIndex != -1) note.compulsory_que = cursor.getString(compulsoryQueIndex);
                if (descriptionIndex != -1) note.description = cursor.getString(descriptionIndex);
                if (examTypeIndex != -1) note.examType = cursor.getString(examTypeIndex);
                if (publishedIndex != -1) note.published = cursor.getString(publishedIndex);
                if (totalMarksIndex != -1) note.totalMarks = cursor.getString(totalMarksIndex);
                if (instructionsIndex != -1) note.instructions = cursor.getString(instructionsIndex);
                if (passingScoreIndex != -1) note.passingScore = cursor.getString(passingScoreIndex);
                if (passFeedbackIndex != -1) note.passFeedback = cursor.getString(passFeedbackIndex);
                if (failFeedbackIndex != -1) note.failFeedback = cursor.getString(failFeedbackIndex);
                if (viewScoreRealtimeIndex != -1) note.viewScoreRealtime = cursor.getString(viewScoreRealtimeIndex);
                if (retakeIndex != -1) note.retake = cursor.getString(retakeIndex);
                if (attemptLimitIndex != -1) note.attemptLimit = cursor.getString(attemptLimitIndex);
                if (timeLimitBIndex != -1) note.timeLimitB = cursor.getString(timeLimitBIndex);
                if (durationHIndex != -1) note.durationH = cursor.getString(durationHIndex);
                if (durationMIndex != -1) note.durationM = cursor.getString(durationMIndex);
                if (seeResultIndex != -1) note.seeResult = cursor.getString(seeResultIndex);
                if (showRightAnswersIndex != -1) note.showRightAnswers = cursor.getString(showRightAnswersIndex);
                if (createdByIndex != -1) note.createdBy = cursor.getString(createdByIndex);
                if (createdDateIndex != -1) note.createdDate = cursor.getString(createdDateIndex);
                if (modifiedDateIndex != -1) note.modified_date = cursor.getString(modifiedDateIndex);
                if (startPublishIndex != -1) note.startpublish = cursor.getString(startPublishIndex);
                if (endPublishIndex != -1) note.endpublish = cursor.getString(endPublishIndex);
                if (orderingIndex != -1) note.ordering = cursor.getString(orderingIndex);
                if (examCategoryIndex != -1) note.examCategory = cursor.getString(examCategoryIndex);
                if (infoHideIndex != -1) note.infoHide = cursor.getString(infoHideIndex);
                if (exStartDateIndex != -1) note.ex_start_date = cursor.getString(exStartDateIndex);
                if (exEndDateIndex != -1) note.ex_end_date = cursor.getString(exEndDateIndex);
                if (exStartTimeIndex != -1) note.ex_start_time = cursor.getString(exStartTimeIndex);
                if (exEndTimeIndex != -1) note.ex_end_time = cursor.getString(exEndTimeIndex);
                if (attIndex != -1) note.att = cursor.getString(attIndex);
                if (qCountIndex != -1) note.Qcount= cursor.getString(qCountIndex);

                listItems.add(note);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listItems;
    }

    // Updated method with column index validation
    @SuppressLint("Range")
    public List<QlistRes.Ques> getQuizQues(String exam_id) {
        SQLiteOpenHelper database = new DatabaseHelper(context);
        SQLiteDatabase db = database.getReadableDatabase();
        ArrayList<QlistRes.Ques> listItems = new ArrayList<>();
        Log.d("DB Query", "Querying questions for exam_id: " + exam_id);
//        Cursor cursor = db.rawQuery("SELECT * from Questions", null);

//        Cursor cursor = db.rawQuery("SELECT * from Questions WHERE exam_id = ?", new String[]{exam_id});

        Cursor cursor = db.rawQuery("SELECT * from Questions where exam_id='"+exam_id+"'",
                null);


        if (cursor.moveToFirst()) {
            do {
                QlistRes.Ques note = new QlistRes.Ques();

//                int idIndex = cursor.getColumnIndex("id");
//                int sectionIdIndex = cursor.getColumnIndex("section_id");
//                int queTitleIndex = cursor.getColumnIndex("que_title");
//                int queTypeIndex = cursor.getColumnIndex("que_type");
//                int imageIndex = cursor.getColumnIndex("image");
//                int optionsIndex = cursor.getColumnIndex("options");
//                int queOptionIndex = cursor.getColumnIndex("que_option");
//                int correctAnsIndex = cursor.getColumnIndex("correct_ans");
//                int queMarksIndex = cursor.getColumnIndex("que_marks");
//                int examIdIndex = cursor.getColumnIndex("exam_id");
//
//                if (idIndex != -1) note.setId(String.valueOf(cursor.getInt(idIndex)));
//                if (sectionIdIndex != -1) note.setSectionId(String.valueOf(cursor.getInt(sectionIdIndex)));
//                if (queTitleIndex != -1) note.setQueTitle(cursor.getString(queTitleIndex));
//                if (queTypeIndex != -1) note.setQueType(cursor.getString(queTypeIndex));
//                if (imageIndex != -1) note.setImage(cursor.getString(imageIndex));
//                if (optionsIndex != -1) note.setOptions(cursor.getString(optionsIndex));
//                if (queOptionIndex != -1) note.setQueOption(cursor.getString(queOptionIndex));
//                if (correctAnsIndex != -1) note.setCorrectAns(cursor.getString(correctAnsIndex));
//                if (queMarksIndex != -1) note.setQueMarks(cursor.getString(queMarksIndex));
//                if (examIdIndex != -1) note.setExam_id(String.valueOf(cursor.getInt(examIdIndex)));
//                listItems.add(note);

                //modified
                note.id = cursor.getString(cursor.getColumnIndex("id"));
                note.sectionId = cursor.getString(cursor.getColumnIndex("section_id"));
                note.queTitle = cursor.getString(cursor.getColumnIndex("que_title"));
                note.queType = cursor.getString(cursor.getColumnIndex("que_type"));
                note.image = cursor.getString(cursor.getColumnIndex("image"));
                note.options = cursor.getString(cursor.getColumnIndex("options"));
                note.queOption = cursor.getString(cursor.getColumnIndex("que_option"));
                note.correctAns = cursor.getString(cursor.getColumnIndex("correct_ans"));
                note.queMarks = cursor.getString(cursor.getColumnIndex("que_marks"));
                note.exam_id = cursor.getString(cursor.getColumnIndex("exam_id"));
                note.modified_date = cursor.getString(cursor.getColumnIndex("datetime"));
                listItems.add(note);

            } while (cursor.moveToNext());
        }

        cursor.close();
        Log.d("listItemsdb", new Gson().toJson(listItems));
        return listItems;
    }





}
