package com.bbsc.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bbsc.Adapter.QuizAdapter;
import com.bbsc.Api.Api;
import com.bbsc.Api.RetrofitClient;
import com.bbsc.DB.DBManager;
import com.bbsc.Model.GetDetail;
import com.bbsc.Model.QlistRes;
import com.bbsc.Model.Quiz;
import com.bbsc.Model.User;
import com.bbsc.Model.userRoll;
import com.bbsc.R;
import com.bbsc.Receiver.NetworkStateReceiver;
import com.bbsc.SharedPrefManagerFile.SharedPrefManager;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_WRITE;

public class QuizInfo extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, NetworkStateReceiver.NetworkStateReceiverListener{

    public List<Quiz.Qinfo> QueResponse;
    QuizAdapter quizAdapter;
    RecyclerView Quizlist;
    RelativeLayout waitLL, errorLL;
    User user;
    Button retry;
    TextView srno, Que, ClearChk, couldnt_reach_internet_txt, try_again_txt, roll_text, enroll_text, roll, enroll;
    RadioGroup optionRadioGroup;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar simpleProgressBar;
    static boolean isNetworkAvailable;
    private NetworkStateReceiver networkStateReceiver;
    RelativeLayout internetOffRL;
    String refresh, txt_roll_no, txt_enrollment_no;
    private DBManager dbManager;
    List<Quiz.Qinfo> listItems;
    ImageView edit;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;
    public static Clock currentGnssTimeClock() {
        return null;
    }

    //for auto refrsh
//    private Handler autoRefreshHandler;
//    private Runnable autoRefreshRunnable;
//    private static final long AUTO_REFRESH_INTERVAL = 10000;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_info);

        Quizlist = findViewById(R.id.QuizList);
        srno = findViewById(R.id.srno);
        Que = findViewById(R.id.Que);
        waitLL = findViewById(R.id.waitLL);
        errorLL = findViewById(R.id.errorLL);
        ClearChk = findViewById(R.id.clearChk);
        edit = findViewById(R.id.edit);
        roll = findViewById(R.id.roll);
        enroll = findViewById(R.id.enroll);
        retry = findViewById(R.id.retry);
        user = SharedPrefManager.getInstance(QuizInfo.this).getUser();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        refresh = bundle.getString("refresh");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        dbManager = new DBManager(this);
        dbManager.open();
        optionRadioGroup = findViewById(R.id.optionRadioGroup);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowForm();
            }
        });

        Log.d("usersession", new Gson().toJson(SharedPrefManager.getInstance(QuizInfo.this).getUser()));

        String strJson = SharedPrefManager.getInstance(QuizInfo.this).getUser().getCurrent_que();//second parameter is necessary ie.,Value to return if this preference does not exist.
        String strJson_att_data = SharedPrefManager.getInstance(QuizInfo.this).getUser().getAtt_data_ques();//second parameter is necessary ie.,Value to return if this preference does not exist.

        if (strJson != null || strJson_att_data != null) {
            try {
                JSONArray response = new JSONArray(strJson);
                JSONArray response2 = new JSONArray(strJson_att_data);
                ArrayList<QlistRes.Ques> listItems = new ArrayList<QlistRes.Ques>();
                for(int i=0; i < response.length(); i++) {
                    JSONObject jsonobject = response.getJSONObject(i);
                    QlistRes.Ques note = new QlistRes.Ques();

                    note.id = jsonobject.getString("id");
                    note.queTitle = jsonobject.getString("que_title");
                    note.sectionId = jsonobject.getString("section_id");
                    note.queType = jsonobject.getString("que_type");
                    note.image = jsonobject.getString("image");
                    note.options = jsonobject.getString("options");
                    note.queOption = jsonobject.getString("que_option");
                    note.correctAns = jsonobject.getString("correct_ans");
                    note.queMarks = jsonobject.getString("que_marks");
                    listItems.add(i, note);

                }
                GetDetail.current_que = listItems;

                GetDetail.att_data = response2;
                Log.d("sessionatt_data", new Gson().toJson(GetDetail.current_que));

            } catch (JSONException e) {
                Log.d("sessionatt_data JSONException", e.getMessage());

            }
        }
        else{
            Log.d("sessionatt_data22", new Gson().toJson(GetDetail.att_data));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.inflateMenu(R.menu.main_manu);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        internetOffRL  = (RelativeLayout) findViewById(R.id.internetOffRL);
        couldnt_reach_internet_txt  = (TextView) findViewById(R.id.couldnt_reach_internet_txt);
        try_again_txt  = (TextView) findViewById(R.id.try_again_txt);
        simpleProgressBar  = (ProgressBar) findViewById(R.id.simpleProgressBar_internet);
        simpleProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#EC5252"), PorterDuff.Mode.MULTIPLY);
        listItems = dbManager.getAllQuizzes();
        boolean userLogin = SharedPrefManager.getInstance(this).isLoggedIn();

        if(userLogin) {
            roll.setText(user.getRoll_no());
            enroll.setText(user.getEnrollment_no());

            if(user.getpending_status() == 1)
            {
                Intent intent = new Intent(QuizInfo.this,SuccessActivity.class);
                intent.putExtra("exam_title",user.getExam_title());
                intent.putExtra("submit","1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else {
                if (user.getRoll_no() == null || user.getRoll_no().equals("")) {
                    Quizlist.setVisibility(View.GONE);
                    ShowForm();
                } else {

                    if (listItems.size() > 0)
                            getDBQList();
                    else {
                        networkStateReceiver = new NetworkStateReceiver();
                        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
                        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                    }
                }
            }
        }
        /*internet code*/
        retry.setOnClickListener(view -> {
            Quizlist.setVisibility(View.GONE);
            waitLL.setVisibility(View.VISIBLE);
            retry.setVisibility(View.GONE);
            getQuiz();
        });


//refresh logic
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.refresh_menu) {
                waitLL.setVisibility(View.VISIBLE);
                Quizlist.setVisibility(View.GONE);
                errorLL.setVisibility(View.GONE);
                getQuiz();
            }
            return false;
        });

        //auto refresh
        // Initialize the Handler
//        autoRefreshHandler = new Handler();
//
//        // Initialize the Runnable for auto-refresh
//        autoRefreshRunnable = new Runnable() {
//            @Override
//            public void run() {
//                getQuiz(); // Call the method to refresh quiz data
//                autoRefreshHandler.postDelayed(this, AUTO_REFRESH_INTERVAL); // Schedule the next refresh
//            }
//        };
//
//        // Start the auto-refresh when activity is created
//        autoRefreshHandler.post(autoRefreshRunnable);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_WRITE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }


            @Override
    public void onResume()
    {
        super.onResume();
        if (listItems.size() > 0) {
                getDBQList();
        }
        else {
            networkStateReceiver = new NetworkStateReceiver();
            networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
            this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void ShowForm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enter your details");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.roll_layout, null);
        builder.setView(dialogView);
        roll_text = dialogView.findViewById(R.id.roll_ed);
        enroll_text = dialogView.findViewById(R.id.enroll_ed);
        roll_text.setText(user.getRoll_no());
        enroll_text.setText(user.getEnrollment_no());

// Set up the buttons
        builder.setPositiveButton("Submit", (dialog, which) -> {
            txt_roll_no = roll_text.getText().toString().trim();
            txt_enrollment_no = enroll_text.getText().toString().trim();
            if(txt_roll_no.isEmpty()) {
                roll_text.setError("Enter roll number!");
                roll_text.requestFocus();
                builder.show();
            }
            else{



                Api apiService = RetrofitClient.getApiService();
                Call<userRoll> userResponse = apiService.addRollno(user.getId(), txt_roll_no, txt_enrollment_no);
                userResponse.enqueue(new Callback<userRoll>() {
                    @Override
                    public void onResponse(@NotNull Call<userRoll> call, @NotNull Response<userRoll> response) {
                        if (response.body() != null) {
                            userRoll data = response.body();
                            Log.d("userRoll", new Gson().toJson(data));
                            if (data.getUpdate() > 0) {
                                roll.setText(txt_roll_no);
                                enroll.setText(txt_enrollment_no);
                                SharedPrefManager.getInstance(QuizInfo.this).addRollno(txt_roll_no, txt_enrollment_no);
                                Quizlist.setVisibility(View.VISIBLE);
                                if (listItems.size() <= 0) {
                                    getQuiz();
                                } else {
                                    getDBQList();
                                }
                                dialog.dismiss();
                            }
                        } else {
                            // Handle null response body
                            Log.e("QuizInfo", "Response body is null");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<userRoll> call, @NotNull Throwable t) {
                        // Handle the failure
                        Log.e("QuizInfo", "Failed to get response", t);
                    }
                });



            }
            });

        builder.show();
    }
    @Override
    public void onRestart() {
        super.onRestart();
 if (listItems.size() > 0) {
            getDBQList();
        }
        else {
            networkStateReceiver = new NetworkStateReceiver();
            networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
            this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void getDBQList(){

        quizAdapter = new QuizAdapter(QuizInfo.this, listItems);
        LinearLayoutManager llm = new LinearLayoutManager(QuizInfo.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        Quizlist.setLayoutManager(llm);
        Quizlist.setAdapter(quizAdapter);
        waitLL.setVisibility(View.GONE);
        Quizlist.setVisibility(View.VISIBLE);
        quizAdapter.notifyDataSetChanged();

    }

    public void getOffQuiz()
    {
        waitLL.setVisibility(View.GONE);
        Quizlist.setVisibility(View.VISIBLE);
        retry.setVisibility(View.GONE);
        quizAdapter = new QuizAdapter(QuizInfo.this, GetDetail.QuizList);
        LinearLayoutManager llm = new LinearLayoutManager(QuizInfo.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        Quizlist.setLayoutManager(llm);
        Quizlist.setAdapter(quizAdapter);
        quizAdapter.notifyDataSetChanged();
    }


    public void getQuiz() {
        dbManager.open();
        Api apiService = RetrofitClient.getApiService();
        Call<Quiz> quizCall = apiService.GetQuiz(String.valueOf(user.getId()));
        quizCall.enqueue(new Callback<Quiz>() {
            @Override
            public void onResponse(@NotNull Call<Quiz> call, @NotNull Response<Quiz> response) {
                if (response.body() != null) {
                    dbManager.deleteQuizList();
                    retry.setVisibility(View.GONE);
                    QueResponse = response.body().getData();
                    GetDetail.QuizList = QueResponse;

                    // Log the number of retakes for each quiz
                    for (Quiz.Qinfo quizItem : QueResponse) {
                        String quizId = quizItem.getExamId(); // Assuming each quiz has an ID
                        int attemptLimit = Integer.parseInt(quizItem.getAttemptLimit()); // Assuming getAtt() returns the remaining attempts

                        // Log the quiz ID and attempts left
                        Log.d("QuizInfo", "Quiz ID: " + quizId + ", Attempts Limit: " + attemptLimit);
                    }

                    // Pass retake information to the adapter
                    quizAdapter = new QuizAdapter(QuizInfo.this, QueResponse);
                    LinearLayoutManager llm = new LinearLayoutManager(QuizInfo.this);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    Quizlist.setLayoutManager(llm);
                    Quizlist.setAdapter(quizAdapter);
                    waitLL.setVisibility(View.GONE);
                    Quizlist.setVisibility(View.VISIBLE);
                    quizAdapter.notifyDataSetChanged();
                    dbManager.insertQzList("QuizList", QueResponse);
                    dbManager.close();
                } else {
                    Log.e("QuizInfo", "Response body is null");
                    waitLL.setVisibility(View.GONE);
                    Quizlist.setVisibility(View.GONE);
                    errorLL.setVisibility(View.VISIBLE);
                    retry.setVisibility(View.VISIBLE);
                    dbManager.close();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Quiz> call, @NotNull Throwable t) {
                waitLL.setVisibility(View.GONE);
                Quizlist.setVisibility(View.GONE);
                errorLL.setVisibility(View.VISIBLE);
                retry.setVisibility(View.VISIBLE);
                dbManager.close();
            }
        });
    }



    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public void onAttach(Activity activity) {
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return null;
    }

    @Override
    public void networkAvailable() {
        final Handler handler = new Handler();
        Runnable delayrunnable = () -> {
            internetOffRL.setVisibility(View.GONE);
            couldnt_reach_internet_txt.setVisibility(View.GONE);
            getQuiz();
        };
        handler.postDelayed(delayrunnable, 3000);
    }

    @Override
    public void networkUnavailable() {
        internetOffRL.setVisibility(View.VISIBLE);
        try_again_txt.setOnClickListener(v -> {
            try_again_txt.setVisibility(View.GONE);
            simpleProgressBar.setVisibility(View.VISIBLE);
            if(!isNetworkAvailable()){
                simpleProgressBar.postDelayed(() -> couldnt_reach_internet_txt.postDelayed(() -> {
                    couldnt_reach_internet_txt.setVisibility(View.VISIBLE);
                    try_again_txt.setVisibility(View.VISIBLE);
                    simpleProgressBar.setVisibility(View.GONE);
                    couldnt_reach_internet_txt.postDelayed(() -> couldnt_reach_internet_txt.setVisibility(View.GONE), 2000);
                }, 2000), 2000);
            }
            else{
                networkUnavailable();
            }
        });
    }

    public boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null;
    }
    public  void  toastMsg(String msg){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE );
        @SuppressLint("InflateParams") View toast = inflater.inflate(R.layout.toast, null);
        TextView text = (TextView) toast.findViewById(R.id.toast_txt);
        text.setText(msg);
        Toast view  = new Toast(this);
        // Set layout to toast
        view.setView(toast);
        view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
        view.setDuration(Toast.LENGTH_SHORT);
        view.show();

    }
    public static boolean isNetworkAvailable(Context context) {
        isNetworkAvailable = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            for (NetworkInfo networkInfo : info) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    isNetworkAvailable = true;
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}