
package com.bbsc.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.bbsc.Api.Api;
import com.bbsc.Api.RetrofitClient;
import com.bbsc.DB.DBManager;
import com.bbsc.Model.GetDetail;
import com.bbsc.Model.Qsubmit;
import com.bbsc.Model.QuizRequestBody;
import com.bbsc.Model.User;
import com.bbsc.R;
import com.bbsc.Receiver.NetworkStateReceiver;
import com.bbsc.SharedPrefManagerFile.SharedPrefManager;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    String exam_title, submit;
    TextView title, err_txt, att_date, obtmark, tot, percentage, status, holdMsg, couldnt_reach_internet_txt, try_again_txt;
    RelativeLayout waitLL, errorLL, internetOffRL;
    CardView resultLL;
    LinearLayout showRes;
    Button retry;
    boolean resubmit = false;
    static ProgressBar simpleProgressBar;
    static boolean isNetworkAvailable;
    private NetworkStateReceiver networkStateReceiver;
    private DBManager dbManager;
    String att_no;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_activity);

        title = findViewById(R.id.title);
        waitLL = findViewById(R.id.waitLL);
        resultLL = findViewById(R.id.resultLL);
        errorLL = findViewById(R.id.errorLL);
        err_txt = findViewById(R.id.err_txt);
        retry = findViewById(R.id.retry);
        showRes = findViewById(R.id.showRes);
        obtmark = findViewById(R.id.obt_mark);
        tot = findViewById(R.id.tot);
        percentage = findViewById(R.id.percent);
        status = findViewById(R.id.status);
        holdMsg = findViewById(R.id.holdMsg);
        internetOffRL = findViewById(R.id.internetOffRL);
        couldnt_reach_internet_txt = findViewById(R.id.couldnt_reach_internet_txt);
        try_again_txt = findViewById(R.id.try_again_txt);
        simpleProgressBar = findViewById(R.id.simpleProgressBar_internet);

        dbManager = new DBManager(this);
        dbManager.open();

        simpleProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#EC5252"), android.graphics.PorterDuff.Mode.MULTIPLY);
// Fetch att_no from intent
        att_no = getIntent().getStringExtra("att_no");

        // Display or use att_no as needed
        Log.d("SuccessActivity", "Received att_no: " + att_no);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(Color.WHITE);

        }

        // Get data from Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            exam_title = bundle.getString("exam_title");
            submit = bundle.getString("submit");

            if (submit != null && !submit.isEmpty()) {
                resubmit = true;
            }

            title.setText(exam_title);
        }

        retry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                resubmit=true;

                if(resubmit) {
                    resubmit=false;
                    quiz_submit();
                }
            }
        });

        /* Internet state code */
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
        Intent returnIntent = new Intent();
        boolean result = true;
        returnIntent.putExtra("result", result);
        setResult(RESULT_OK, returnIntent);
        finish();
        startActivity(new Intent(SuccessActivity.this, QuizInfo.class).putExtra("refresh", "refresh").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        overridePendingTransition(0, 0);
    }


//private void quiz_submit() {
//    waitLL.setVisibility(View.VISIBLE);
//    resultLL.setVisibility(View.GONE);
//    errorLL.setVisibility(View.GONE);
//
//    // Log submission data
//    Log.d("Submit_res1", new Gson().toJson(GetDetail.att_data));
//
//    // Retrieve the user information
//    User user = SharedPrefManager.getInstance(SuccessActivity.this).getUser();
//
//    // Retrieve previous exam ID and attempt number from SharedPreferences
//    SharedPreferences sharedPreferences = getSharedPreferences("my_shared_preff", MODE_PRIVATE);
//    String prevExamId = sharedPreferences.getString("prev_exam_id", "");  // Default to empty string if not found
//    int prevAttemptNo = sharedPreferences.getInt("prev_attempt_no", 1);  // Default attempt number is 1 if not found
//
//    // Create a list to hold the attempts
//    List<QuizRequestBody.Attempt> attemptList = new ArrayList<>();
//
//    try {
//        // Populate the attemptList with data from GetDetail.att_data
//        int att_data_length = GetDetail.att_data.length();
//        Log.d("att_data_length", "quiz_submit: " + att_data_length);
//
//        for (int i = 0; i < GetDetail.att_data.length(); i++) {
//            JSONObject jsonObject = GetDetail.att_data.getJSONObject(i);
//            QuizRequestBody.Attempt attempt = new QuizRequestBody.Attempt(
//                    jsonObject.getString("que_id"),
//                    jsonObject.getString("exam_id"),
//                    jsonObject.getInt("stud_id"),
//                    jsonObject.getString("pro_id"),
//                    jsonObject.getString("correct_ans"),
//                    Integer.parseInt(jsonObject.optString("stud_given_ans", "0")), // Fetch and parse stud_given_ans safely
//                    jsonObject.getString("is_correct"),
//                    jsonObject.getString("attempt_no"),
//                    jsonObject.getString("ans_marks")
//            );
//            attemptList.add(attempt);
//        }
//    } catch (JSONException e) {
//        e.printStackTrace(); // Handle JSON parsing error
//        waitLL.setVisibility(View.GONE);
//        Toast.makeText(this, "Error parsing submission data", Toast.LENGTH_SHORT).show();
//        return;
//    }
//
//    Log.d("quiz_submit: ", "attemptList" + attemptList);
//
//    // Get the first object's exam ID (assuming att_data contains the quiz attempt data)
//    String currentExamId = "";
//    try {
//        JSONObject firstQuizObject = GetDetail.att_data.getJSONObject(0);  // Get the first quiz object
//        currentExamId = firstQuizObject.getString("exam_id");
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//
//    // Determine whether to reset or increment the attempt number
//    int newAttemptNo;
//    if (!prevExamId.equals(currentExamId)) {
//        // If exam IDs are different, reset attempt number to 1
//        newAttemptNo = 1;
//        Log.d("Attempt No", "New exam detected. Resetting attempt number to 1.");
//    } else {
//        // If exam IDs are the same, increment the attempt number
//        newAttemptNo = prevAttemptNo + 1;
//        Log.d("Attempt No", "Same exam. Incrementing attempt number to " + newAttemptNo);
//    }
//
//    // Store the new exam ID and attempt number in SharedPreferences
//    SharedPreferences.Editor editor = sharedPreferences.edit();
//    editor.putString("prev_exam_id", currentExamId);
//    editor.putInt("prev_attempt_no", newAttemptNo);
//    editor.apply();
//
//    // Log the updated attempt number
//    Log.d("Updated Attempt", "Attempt No: " + newAttemptNo);
//
//    // Create the QuizAttemptData object
//    QuizRequestBody attemptData = new QuizRequestBody();
//    attemptData.setAttempt(attemptList);
//    attemptData.setRoll_no(user.getRoll_no());  // Use the user's roll number
//    attemptData.setEnrollment_no(user.getEnrollment_no());  // Use the user's enrollment number
//
//    // Log the request body
//    Log.d("attemptList json", "data is " + new Gson().toJson(attemptData));
//
//    // Create the API call
//    Api apiService = RetrofitClient.getApiService();
//    Call<Qsubmit> call = apiService.QuizSubmit(attemptData);
//
//    // Handle the API response
//    String finalCurrentExamId = currentExamId;
//    call.enqueue(new Callback<Qsubmit>() {
//        @Override
//        public void onResponse(@NotNull Call<Qsubmit> call, @NotNull Response<Qsubmit> response) {
//            Log.d("API Response 1", "Response Code: " + response.code());
//            Log.d("Submit_respons is:", new Gson().toJson(response.body()));
//
//            waitLL.setVisibility(View.GONE);
//
//            if (response.isSuccessful()) {
//                Qsubmit res = response.body();
//
//                if (res == null) {
//                    Log.e("Submit Error", "Response body is null");
//                    Toast.makeText(SuccessActivity.this, "Failed to receive a valid response", Toast.LENGTH_SHORT).show();
//                    errorLL.setVisibility(View.VISIBLE);
//                    return;
//                }
//
//                if (res.getError()) {
//                    Toast.makeText(SuccessActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
//                    errorLL.setVisibility(View.VISIBLE);
//                } else {
//                    // Update user status and quiz attempt
//                    String examTitle = exam_title != null ? exam_title : "";  // Default empty string if null
//                    JSONArray attData = GetDetail.att_data != null ? GetDetail.att_data : new JSONArray();  // Empty if null
//                    String attemptNumber = String.valueOf(newAttemptNo);  // Use the new attempt number
//
//                    SharedPrefManager.getInstance(SuccessActivity.this).editUser2(0, examTitle, attData, attemptNumber);
//
//                    // Assuming att_data contains the quiz attempt data
//                    JSONArray jsonArray = GetDetail.att_data;
//
//                    try {
//                        JSONObject jsonObject = jsonArray.getJSONObject(0);  // Get the first object
//                        Log.d("session_exam_id", String.valueOf(jsonObject.get("exam_id")));
//
//                        // Update the quiz list in the database with the incremented attempt
//                        dbManager.updateQuizList(finalCurrentExamId, attemptNumber); // Pass the new attempt number as a String
//                        Log.d("New Attempt Number", "Attempt number updated to: " + newAttemptNo);
//
//                    } catch (JSONException e) {
//                        Log.e("Update Error", e.getMessage());
//                    }
//
//                    Toast.makeText(SuccessActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
//                    resultLL.setVisibility(View.VISIBLE);
//                    errorLL.setVisibility(View.GONE);
//                    showResult(res.getData());
//                }
//            } else {
//                Log.e("Submit Error", "Response not successful: " + response.code());
//                Toast.makeText(SuccessActivity.this, "Failed to submit. Server error.", Toast.LENGTH_SHORT).show();
//                errorLL.setVisibility(View.VISIBLE);
//                finish(); // Finish SecondActivity
//            }
//        }
//
//        @Override
//        public void onFailure(@NotNull Call<Qsubmit> call, @NotNull Throwable t) {
//            Log.d("Submit Error2:", t.getLocalizedMessage());
//            waitLL.setVisibility(View.GONE);
//            errorLL.setVisibility(View.VISIBLE);
//            resubmit = true; // Allow retry on failure
//        }
//    });
//}


    private void quiz_submit() {
        waitLL.setVisibility(View.VISIBLE);
        resultLL.setVisibility(View.GONE);
        errorLL.setVisibility(View.GONE);

        // Log submission data
        Log.d("Submit_res1", new Gson().toJson(GetDetail.att_data));

        // Retrieve the user information
        User user = SharedPrefManager.getInstance(SuccessActivity.this).getUser();

        // Create a list to hold the attempts
        List<QuizRequestBody.Attempt> attemptList = new ArrayList<>();

        try {
            // Populate the attemptList with data from GetDetail.att_data
            int att_data_length = GetDetail.att_data.length();
            Log.d("att_data_length", "quiz_submit: " + att_data_length);
            for (int i = 0; i < att_data_length; i++) {
                JSONObject jsonObject = GetDetail.att_data.getJSONObject(i);
                String examId = jsonObject.getString("exam_id");

                // Get or initialize attempt_no for the current examId
                int storedAttemptNo = getAttemptNumberForExam(examId);

                QuizRequestBody.Attempt attempt = new QuizRequestBody.Attempt(
                        jsonObject.getString("que_id"),
                        examId,
                        jsonObject.getInt("stud_id"),
                        jsonObject.getString("pro_id"),
                        jsonObject.getString("correct_ans"),
                        Integer.parseInt(jsonObject.optString("stud_given_ans", "0")),
                        jsonObject.getString("is_correct"),
                        String.valueOf(storedAttemptNo), // Use the stored attempt number
                        jsonObject.getString("ans_marks")
                );
                attemptList.add(attempt);
            }
        } catch (JSONException e) {
            e.printStackTrace(); // Handle JSON parsing error
            waitLL.setVisibility(View.GONE);
            Toast.makeText(this, "Error parsing submission data", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("quiz_submit: ", "attemptList" + attemptList);

        // Create the QuizAttemptData object
        QuizRequestBody attemptData = new QuizRequestBody();
        attemptData.setAttempt(attemptList);
        attemptData.setRoll_no(user.getRoll_no());
        attemptData.setEnrollment_no(user.getEnrollment_no());

        // Log the request body
        Log.d("attemptList json", "data is " + new Gson().toJson(attemptData));

        // Create the API call
        Api apiService = RetrofitClient.getApiService();
        Call<Qsubmit> call = apiService.QuizSubmit(attemptData);

        // Handle the API response
        call.enqueue(new Callback<Qsubmit>() {
            @Override
            public void onResponse(@NotNull Call<Qsubmit> call, @NotNull Response<Qsubmit> response) {
                Log.d("API Response 1", "Response Code: " + response.code());
                Log.d("Submit_respons is:", new Gson().toJson(response.body()));

                waitLL.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Qsubmit res = response.body();

                    if (res == null) {
                        Log.e("Submit Error", "Response body is null");
                        Toast.makeText(SuccessActivity.this, "Failed to receive a valid response", Toast.LENGTH_SHORT).show();
                        errorLL.setVisibility(View.VISIBLE);
                        return;
                    }

                    if (res.getError()) {
                        Toast.makeText(SuccessActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        errorLL.setVisibility(View.VISIBLE);
                    } else {
                        // Update user status and quiz attempt
                        String examTitle = exam_title != null ? exam_title : "";  // Default empty string if null
                        JSONArray attData = GetDetail.att_data != null ? GetDetail.att_data : new JSONArray();  // Empty if null
                        String attemptNumber = null;  // Default attempt number
                        try {
                            attemptNumber = String.valueOf(GetDetail.att_data.getJSONObject(0).getInt("attempt_no"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        SharedPrefManager.getInstance(SuccessActivity.this).editUser2(0, examTitle, attData, attemptNumber);

                        // Handle the response and update the attempt number for the current examId
                        String examId = null;
                        try {
                            examId = GetDetail.att_data.getJSONObject(0).getString("exam_id");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        updateAttemptNumberForExam(examId); // Increment the attempt number for this examId

                        Toast.makeText(SuccessActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        resultLL.setVisibility(View.VISIBLE);
                        errorLL.setVisibility(View.GONE);
                        showResult(res.getData());
                    }
                } else {
                    Log.e("Submit Error", "Response not successful: " + response.code());
                    Toast.makeText(SuccessActivity.this, "Failed to submit. Server error.", Toast.LENGTH_SHORT).show();
                    errorLL.setVisibility(View.VISIBLE);
                    finish(); // Finish the activity
                }
            }

            @Override
            public void onFailure(@NotNull Call<Qsubmit> call, @NotNull Throwable t) {
                Log.d("Submit Error2:", t.getLocalizedMessage());
                waitLL.setVisibility(View.GONE);
                errorLL.setVisibility(View.VISIBLE);
            }
        });
    }

    // Helper method to get the attempt number for the given examId
    private int getAttemptNumberForExam(String examId) {
        SharedPreferences prefs = getSharedPreferences("quiz_attempts", MODE_PRIVATE);
        return prefs.getInt(examId, 1); // Default to 1 if the examId doesn't exist
    }

    // Helper method to update the attempt number for the given examId
    private void updateAttemptNumberForExam(String examId) {
        SharedPreferences prefs = getSharedPreferences("quiz_attempts", MODE_PRIVATE);
        int currentAttempt = prefs.getInt(examId, 1);  // Get current attempt, default to 1 if not found
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(examId, currentAttempt + 1);  // Increment and save the new attempt number
        editor.apply();
    }




    @SuppressLint("SetTextI18n")
    private void showResult(Qsubmit.Score_res data) {
        GetDetail.att_data = new JSONArray();
        GetDetail.current_que = null;


        //new logic
        if ("1".equals(data.getSee_result())) {
            holdMsg.setVisibility(View.GONE);
            showRes.setVisibility(View.VISIBLE);

            tot.setText("Score: " + data.getObtainMarks() + " out of " + data.getTotMarks());
            percentage.setText("You have got " + data.getPercentage() + "%");

            if ("pass".equals(data.getStatus())) {
                status.setText(data.getPass());
            } else {
                status.setText(data.getFail());
            }
        } else {
            holdMsg.setVisibility(View.VISIBLE);
            showRes.setVisibility(View.GONE);
        }
        Log.d("Result Data", new Gson().toJson(data));
    }

    @Override
    public void onAttach(Activity activity) {
        // Implement this method if needed, or remove if not used
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return null;
    }

    @Override
    public void networkAvailable() {
        internetOffRL.setVisibility(View.GONE);
        couldnt_reach_internet_txt.setVisibility(View.GONE);

        if (resubmit) {
            resubmit = false;
            if (!submit.isEmpty()) {
                quiz_submit();
            }
        }
    }

    @Override
    public void networkUnavailable() {
        internetOffRL.setVisibility(View.VISIBLE);
        try_again_txt.setOnClickListener(v -> {
            try_again_txt.setVisibility(View.GONE);
            simpleProgressBar.setVisibility(View.VISIBLE);

            if (!isNetworkAvailable()) {
                showNoInternetError();
            } else {
                networkAvailable(); // Trigger resubmit if available
            }
        });
    }

    private void showNoInternetError() {
        simpleProgressBar.postDelayed(() -> {
            couldnt_reach_internet_txt.setVisibility(View.VISIBLE);
            try_again_txt.setVisibility(View.VISIBLE);
            simpleProgressBar.setVisibility(View.GONE);

            couldnt_reach_internet_txt.postDelayed(() -> couldnt_reach_internet_txt.setVisibility(View.GONE), 2000);
        }, 2000);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        // Implement if needed
    }
}
