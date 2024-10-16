package com.bbsc.SharedPrefManagerFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bbsc.Model.QlistRes;
import com.bbsc.Model.User;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefManager {
    public static final String LOGGED_IN_PREF = "logged_in_status";

    private static final String SHARED_PREF_NAME = "my_shared_preff";

    private static SharedPrefManager mInstance;
    private Context mCtx;
    private SharedPreferences sharedPreferences;

    android.content.SharedPreferences pref;
    android.content.SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
        pref = mCtx.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();

    }

    private static final String PREF_NAME = "testing";

    // All Shared Preferences Keys Declare as #public
    public static final String KEY_SET_APP_RUN_FIRST_TIME = "KEY_SET_APP_RUN_FIRST_TIME";


    public String setCurrency(String currency) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.remove(KEY_SET_APP_RUN_FIRST_TIME);
        editor.putString("currency", currency);
        Log.d("sharedcurrency", String.valueOf(currency));

//        editor.apply();
        editor.commit();
        return currency;
    }

    public void setApp_runFirst(String App_runFirst) {
        editor.remove(KEY_SET_APP_RUN_FIRST_TIME);
        editor.putString(KEY_SET_APP_RUN_FIRST_TIME, App_runFirst);
        editor.apply();
    }

    public String getApp_runFirst() {
        String App_runFirst = pref.getString(KEY_SET_APP_RUN_FIRST_TIME, "FIRST");
        return App_runFirst;
    }


    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void addRollno(String roll_no, String enrollment_no) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Toast.makeText(mCtx, "roll_no: "+roll_no+" entrollment_no: "+enrollment_no, Toast.LENGTH_SHORT).show();
        editor.putString("roll_no", roll_no);
        editor.putString("enrollment_no", enrollment_no);
        editor.apply();
    }

    public void addQuesData(String att_data_ques,String att_data, List<QlistRes.Ques> current_que) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(att_data_ques != null)
            editor.putString("att_data_ques", att_data_ques);
        else
            editor.putString("att_data_ques", null);
        if(att_data != null)
            editor.putString("att_data", att_data.toString());
        else
            editor.putString("att_data", null);
        Gson gson = new Gson();

        String json = gson.toJson(current_que);

        editor.putString("current_que", json);
        editor.apply();
    }



    public void editUser2(int p_status, String exam_title, JSONArray att_data, String att_no) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("pending_status", p_status);  // Save the pending status

        // Save att_data (if not null) and att_no to SharedPreferences
        if (att_data != null) {
            editor.putString("att_data", att_data.toString());
        }
        editor.putString("exam_title", exam_title);
        editor.putString("att_no", att_no);  // Save updated attempt number

        editor.apply();  // Commit changes to SharedPreferences
        Log.d("SharedPref Update", "Updated att_no: " + att_no);
    }    




    public void saveUser(User user) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id", user.getId());
        editor.putString("email", user.getEmail());
        editor.putString("name", user.getName());
        editor.putString("firstname", user.getFirst_name());
        editor.putString("lastname", user.getLast_name());
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putString("images", user.getImages());
        editor.putString("enroll", user.getEntroll());
        editor.putString("mobile", user.getMobile());
        editor.putInt("pending_status", 0);
        editor.putString("roll_no", user.getRoll_no());
        editor.putString("enrollment_no", user.getEnrollment_no());
        editor.putString("exam_title", null);
        editor.putString("att_data", null);
        editor.putString("att_data_ques", null);
        editor.putString("current_que", null);


        editor.apply();

    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public User getUser() {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("username", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("firstname", null),
                sharedPreferences.getString("lastname", null),
                sharedPreferences.getString("images", null),
                sharedPreferences.getString("enroll", null),
                sharedPreferences.getString("mobile", null),
                sharedPreferences.getString("currency", null),
                sharedPreferences.getInt("pending_status", 0),
                sharedPreferences.getString("roll_no", null),
                sharedPreferences.getString("enrollment_no", null),
                sharedPreferences.getString("exam_title", null),
                sharedPreferences.getString("att_data", null),
                sharedPreferences.getString("att_data_ques", null),
                sharedPreferences.getString("current_que", null));
    }

    public String get_Attdata() {

    SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            return sharedPreferences.getString("att_data","0");
}

    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    // Store the previous exam ID
    public void storePrevExamId(String examId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("prev_exam_id", examId);
        editor.apply();
    }

    // Retrieve the previous exam ID
    public String getPrevExamId() {
        return sharedPreferences.getString("prev_exam_id", "");
    }

    // Store the previous attempt number
    public void storePrevAttemptNo(int attemptNo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("prev_attempt_no", attemptNo);
        editor.apply();
    }

    // Retrieve the previous attempt number
    public int getPrevAttemptNo() {
        return sharedPreferences.getInt("prev_attempt_no", 1);  // Default to 1
    }




}