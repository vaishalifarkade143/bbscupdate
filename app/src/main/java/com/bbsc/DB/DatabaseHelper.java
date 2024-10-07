package com.bbsc.DB;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "QuizList";
    public static final String TABLE_NAME_Ques = "Questions";

    // Table columns
    public static final String _ID = "_id";
    public static final String SUBJECT = "subject";
    public static final String DESC = "description";

    // Database Information
    static final String DB_NAME = "BBSC.DB";

    // database version
    static final int DB_VERSION = 2;

//    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
//            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT NOT NULL, " + DESC + " TEXT);";

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +

    "exam_id" + " TEXT NOT NULL,"
            + "exam_title"  + " TEXT ,"
            + "compulsory_que"  + " TEXT,"
            + "description"  + " TEXT ,"
            + "exam_type"  + " TEXT ,"
            + "published"  + " TEXT ,"
            + "total_marks"  + " TEXT ,"
            + "instructions"  + " TEXT ,"
            + "passing_score"  + " TEXT ,"
            + "pass_feedback"  + " TEXT ,"
            + "fail_feedback"  + " TEXT ,"
            + "view_score_realtime"  + " TEXT ,"
            + "retake"  + " TEXT ,"
            + "attempt_limit"  + " TEXT ,"
            + "time_limit_b"  + " TEXT ,"
            + "duration_h"  + " TEXT ,"
            + "duration_m"  + " TEXT ,"
            +"see_result"  + " TEXT ,"
            +"show_right_answers"  + " TEXT ,"
            + "created_by"  + " TEXT ,"
            + "created_date"  + " TEXT ,"
            + "modified_date"  + " TEXT ,"
            +"startpublish"  + " TEXT ,"
            +"endpublish"  + " TEXT ,"
            + "ordering"  + " TEXT ,"
            +"exam_category"  + " TEXT ,"
            +"info_hide"  + " TEXT ,"
            +"ex_start_date"  + " TEXT ,"
            +"ex_end_date"  + " TEXT ,"
            +"ex_start_time"  + " TEXT ,"
            +"ex_end_time"  + " TEXT ,"
            +"att"  + " TEXT ,"
            +"Qcount"  + " TEXT );";

    private static final String CREATE_TABLE_Ques = "create table " + TABLE_NAME_Ques + "(" +

            "id" + " TEXT NOT NULL,"
            + "section_id"  + " TEXT ,"
            + "que_title"  + " TEXT,"
            + "que_type"  + " TEXT ,"
            + "image"  + " TEXT ,"
            + "options"  + " TEXT ,"
            + "que_option"  + " TEXT ,"
            + "correct_ans"  + " TEXT ,"
            + "que_marks"  + " TEXT ,"
            + "exam_id"  + " TEXT,"
            + "datetime"  + " TEXT  );";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_Ques);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Ques);
        onCreate(db);
    }
}
