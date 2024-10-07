
//editing


package com.bbsc.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bbsc.Api.Api;
import com.bbsc.Api.RetrofitClient;
import com.bbsc.DB.DBManager;
import com.bbsc.Model.GetDetail;
import com.bbsc.Model.QlistRes;
import com.bbsc.Model.Quiz;
import com.bbsc.Model.SysTime;
import com.bbsc.Model.User;
import com.bbsc.R;
import com.bbsc.Receiver.NetworkStateReceiver;
import com.bbsc.SharedPrefManagerFile.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.RandomUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.HashSet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  implements NetworkStateReceiver.NetworkStateReceiverListener{
    private QlistRes QueResponse;
    RecyclerView Qlist;
    Button submit, Prev, Next;
    private static Context mContext;
    User user;
    LinearLayout wait;
    TextView msg;
    TextView title;
    TextView timer;
    TextView tmTxt, reloadimg;
    ImageView que_img;
    static TextView tot_att;
    TextView sr_num;
    ProgressBar progress;
    String exam_id, exam_title, hr, min, showtime, seeresult, att_no, st_date, st_time, end_date, end_time;
    private TextView srno, Que,  ClearChk;
    RadioGroup optionRadioGroup;
    int checkedposition;
    RadioGroup.LayoutParams rprms;
    RelativeLayout Qlayout;
    ProgressBar progressBar;
    CountDownTimer countDownTimer = null;
    boolean submitQuiz = true;
    boolean getque=true;
    boolean addAtt = true;
    String hms;
    TextView  couldnt_reach_internet_txt, try_again_txt;
    RelativeLayout internetOffRL, activityLL;
    static ProgressBar simpleProgressBar;
    static boolean isNetworkAvailable;
    private NetworkStateReceiver networkStateReceiver;
    long noOfMinutes;
    String mSavedInfo;
    static int opt_sel;
    String format = "yyyy-MM-dd HH:mm:ss";
    boolean success = false;
    ContextWrapper cw;
    SimpleDateFormat sdf;
    boolean isImageFitToScreen = false;
    private DBManager dbManager;
    List<QlistRes.Ques> quelistItems;



    private static final String SHARED_PREF_NAME = "my_shared_preff";
    private GetDetail GetDetail;

    @SuppressLint({"SimpleDateFormat", "SetTextI18n", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        submit = findViewById(R.id.btnSubmit);
        user = SharedPrefManager.getInstance(MainActivity.this).getUser();
        wait = findViewById(R.id.wait);
        msg = findViewById(R.id.msg);
        progress = findViewById(R.id.progress);
        srno = findViewById(R.id.srno);
        Que = findViewById(R.id.Que);
        Prev = findViewById(R.id.prev);
        Next = findViewById(R.id.next);
        title = findViewById(R.id.title);
        timer = findViewById(R.id.timer);
        tmTxt = findViewById(R.id.tmTxt);
        tot_att = findViewById(R.id.att_no);
        sr_num = findViewById(R.id.sr_num);
        activityLL = findViewById(R.id.activityLL);
        Qlayout = findViewById(R.id.Qlayout);
        progressBar = findViewById(R.id.progressBar);
        ClearChk = findViewById(R.id.clearChk);
        que_img = findViewById(R.id.que_img);
        optionRadioGroup = findViewById(R.id.optionRadioGroup);

        sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));


//added bcz not to get crash
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            exam_id = bundle.getString("exam_id");
            exam_title = bundle.getString("exam_title");
            Log.d("exam_title inside if", "exam_title is: "+exam_title);
            title.setText(": " + exam_title);
            Log.d("MainActivity", "Received Exam ID: " + exam_id);

            // Retrieve additional data
            String att_no = bundle.getString("att_no");
            Log.d("att_no inside if", "att_no is: "+att_no);
            tot_att.setText(att_no);
        } else {
            Log.e("MainActivity", "No extras found in intent!");
        }


        exam_id = bundle.getString("exam_id");
        exam_title = bundle.getString("exam_title");
        title.setText("Quiz : "+exam_title);
        Log.d("exam_title outside if", "exam_title is: "+exam_title);
        st_date = bundle.getString("st_date");
        st_time = bundle.getString("st_time");
        end_date = bundle.getString("end_date");
        end_time = bundle.getString("end_time");

        hr = bundle.getString("exam_hr");
        min = bundle.getString("exam_min");
        showtime = bundle.getString("showtime");
        seeresult = bundle.getString("seeresult");

        att_no = bundle.getString("att_no");
        //for attempt field
        tot_att.setText(att_no);
        Log.d("att_no outside if", "att_no is: "+att_no);


        if(showtime.equals("1"))
//        if ("1".equals(showtime))
        {
            tmTxt.setVisibility(View.VISIBLE);
            timer.setVisibility(View.VISIBLE);
//            tmTxt.setVisibility(View.VISIBLE);
            if(isNetworkAvailable())
                getEX_time();
            else
                getSys_time();//may be error
        }
        else {
            tmTxt.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
//            tmTxt.setVisibility(View.GONE);

        }

        cw = new ContextWrapper(getApplicationContext());

        dbManager = new DBManager(this);
        dbManager.open();

        //check this getting the quetion list may be error bcz of getQuizQues
        quelistItems = dbManager.getQuizQues(exam_id);

        Log.d("list Items ques", new Gson().toJson(quelistItems));
        Log.d("Quiz Items List Size", "Size: " + quelistItems.size());


        GetDetail.current_que = quelistItems;
        JSONArray jsonArray = new JSONArray();

        for (QlistRes.Ques quearr : quelistItems) {

            JSONObject attempt_arr = new JSONObject();
            try {
                Log.d("Ques Data", new Gson().toJson(quearr)); // Log individual question data
                attempt_arr.put("que_id", quearr.getId());
                attempt_arr.put("exam_id", exam_id);
                attempt_arr.put("stud_id", user.getId());
                attempt_arr.put("pro_id", "10");
                attempt_arr.put("correct_ans", "");
                attempt_arr.put("stud_given_ans", "");
                attempt_arr.put("is_correct", "0");
                attempt_arr.put("attempt_no", att_no);
                attempt_arr.put("ans_marks", "0");
                // Log JSON object before adding it to array
                Log.d("JSON Attempt Object", attempt_arr.toString());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(attempt_arr);

        }

        Log.d("att_data Length", "Populated Length: " + GetDetail.att_data.length());
        GetDetail.att_data = jsonArray;

        //correct
        Log.d("att_data2222", new Gson().toJson(GetDetail.att_data));

        //incorrect data
        Log.d("current_que222", new Gson().toJson(GetDetail.current_que));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Test Submit Warning")
                        .setCancelable(false)
//                            .setIcon(R.drawable.toolbarlogo)
                        .setMessage(R.string.submit_msg)
                        .setPositiveButton(Html.fromHtml("<font color='#FF008000' background='Green'>Yes, Submit Final Answers</font>"), (dialog, which) -> {


                            submitQuiz = false;
                            countDownTimer.cancel();
                            countDownTimer = null;
                            SubmitQuiz();

                        })
                        .setNegativeButton("No, Cancel", (dialog, i) -> {
                            dialog.dismiss();
                        })
                        .show();
//                SubmitQuiz();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                opt_sel = 0;
                int sr = GetDetail.Current_index + 1;
                if (sr < GetDetail.att_data.length()) {
                    Next.setVisibility(View.VISIBLE);

                    sr_num.setText((sr+1)+"/"+GetDetail.att_data.length());
                    getQue(sr);
                }
                if(sr ==GetDetail.att_data.length()-1){
                    Next.setVisibility(View.GONE);
                }
                Prev.setVisibility(View.VISIBLE);
            }
        });
        Prev.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                opt_sel = 0;

                int sr = GetDetail.Current_index-1;
                sr_num.setText((sr+ 1)+"/"+GetDetail.att_data.length());

                if(sr>=0)
                    getQue(sr);
                if(sr == 0) {
                    Prev.setVisibility(View.GONE);
                }
                else{
                    Prev.setVisibility(View.VISIBLE);
                }
                Next.setVisibility(View.VISIBLE);
            }
        });



        internetOffRL  = (RelativeLayout) findViewById(R.id.internetOffRL);
        couldnt_reach_internet_txt  = (TextView) findViewById(R.id.couldnt_reach_internet_txt);
        try_again_txt  = (TextView) findViewById(R.id.try_again_txt);

        simpleProgressBar  = (ProgressBar) findViewById(R.id.simpleProgressBar_internet);

        simpleProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#EC5252"),android.graphics.PorterDuff.Mode.MULTIPLY);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        countDownTimer=null;
        submitQuiz = false;
        finish();
        overridePendingTransition(0, 0);
    }

    public void onImageClicked(Uri imageUri) {
        Intent fullScreenIntent = new Intent(this, FullscreenActivity.class);
        fullScreenIntent.setData(imageUri);
        startActivity(fullScreenIntent);
        // Toast.makeText(MediaThumbMainActivity.this, "imageId = " + imageId, Toast.LENGTH_SHORT).show();
    }
    public Bitmap getBitmapFromURL(String src) {

        try {

            java.net.URL url = new java.net.URL(src);

            HttpURLConnection connection = (HttpURLConnection) url

                    .openConnection();

            connection.setDoInput(true);

            connection.connect();

            InputStream input = connection.getInputStream();

            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;

        } catch (IOException e) {

            e.printStackTrace();

            return null;
        }
    }
    private void getSys_time()
    {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
//Log.d("currentTime", currentTime+"  "+currentDate+" end_date : "+end_date);
        Date dateObj1 = null, dateObj2 = null;
        try {
            dateObj1 = sdf.parse(end_date+" "+end_time);
            dateObj2 = sdf.parse(currentDate + " " + currentTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("dateObj1: "+dateObj1);
        System.out.println("dateObj2: "+dateObj2 + "\n");

        DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");

        // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
        assert dateObj1 != null;
        long diff = dateObj1.getTime() - dateObj2.getTime();
        Log.d("ex_timediff", String.valueOf(diff));

        int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        System.out.println("difference between days: " + diffDays);

        int diffhours = (int) (diff / (60 * 60 * 1000));
        System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));

        int diffmin = (int) (diff / (60 * 1000));
        System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));

        int diffsec = (int) (diff / (1000));
        System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));

//                holder.expire.setText("diffhours: "+diffhours+" diffmin:"+diffmin+" diffsec:"+diffsec);
        long ex_min = 0;
        if (diffhours > 0)
            ex_min = diffhours;
        if (diffmin > 0)
            ex_min += diffmin;
        noOfMinutes = ex_min * 60 * 1000;

        if(GetDetail.att_data.length()>0) {
            JSONObject getarray = null;
            try {
                getarray = new JSONObject(GetDetail.att_data.get(0).toString());
                Log.d("getexam_id",getarray.getString("exam_id"));

                if(getarray.getString("exam_id").equals(exam_id))
                    st_quiz(exam_id);
                else{
//                    Toast.makeText(MainActivity.this, exam_id+ " *** "+getarray.getString("exam_id"), Toast.LENGTH_SHORT).show();
//                    getList(exam_id);
                    networkStateReceiver = new NetworkStateReceiver();
                    networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
                    this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else{
            networkStateReceiver = new NetworkStateReceiver();
            networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
            this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String urlString){

        cw = new ContextWrapper(getApplicationContext());

        // path to /data/data/yourapp/app_data/imageDir

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, urlString);
        if(!mypath.exists()) {
            // Create imageDir


            FileOutputStream fos = null;

            try {

                fos = new FileOutputStream(mypath);

                // Use the compress method on the BitMap object to write image to the OutputStream

                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                success = true;

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                try {

                    fos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }
        }

        return directory.getAbsolutePath();

    }

    private void getEX_time() {
        Api apiService = RetrofitClient.getApiService();
        Call<SysTime> userResponse = apiService.getSysTime();
        userResponse.enqueue(new Callback<SysTime>() {
            @Override
            public void onResponse(Call<SysTime> call, Response<SysTime> response) {

                SysTime time_res = response.body();
                Log.d("ex_time",end_date+" "+end_time);
                Log.d("ex_time", time_res.getDate()+" "+ time_res.getTime());
                Date dateObj1 = null, dateObj2 = null;
                try {
                    dateObj1 = sdf.parse(end_date+" "+end_time);
                    dateObj2 = sdf.parse(time_res.getDate() + " " + time_res.getTime());

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                System.out.println(dateObj1);
                System.out.println(dateObj2 + "\n");

                DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");

                // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
                long diff = dateObj1.getTime() - dateObj2.getTime();
                Log.d("ex_timediff", String.valueOf(diff));

                int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                System.out.println("difference between days: " + diffDays);

                int diffhours = (int) (diff / (60 * 60 * 1000));
                System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));

                int diffmin = (int) (diff / (60 * 1000));
                System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));

                int diffsec = (int) (diff / (1000));
                System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));
                long ex_min = 0;
                if (diffhours > 0)
                    ex_min = diffhours;
                if (diffmin > 0)
                    ex_min += diffmin;
                noOfMinutes = ex_min * 60 * 1000;

                Log.d("ex_minex_min", String.valueOf(ex_min));

                if(GetDetail.att_data.length()>0) {
                    JSONObject getarray = null;
                    try {
                        getarray = new JSONObject(GetDetail.att_data.get(0).toString());
                        Log.d("getexam_id",getarray.getString("exam_id"));

                        if(getarray.getString("exam_id").equals(exam_id))
                            st_quiz(exam_id);
                        else{
                            getList(exam_id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    getList(exam_id);
                }
            }

            @Override
            public void onFailure(Call<SysTime> call, Throwable t) {

            }
        });
    }



    private void startTimer(long noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                timer.setText(hms);//set text
//                Log.d("hms::::::", hms);
            }

            public void onFinish() {
                if(submitQuiz)
                    SubmitQuiz();
                timer.setText("TIME'S UP!!"); //On finish change timer text
                Log.d("hms::::::", "TIME'S UP!!");
                countDownTimer.cancel();
                countDownTimer = null;//set CountDownTimer to null
            }
        }.start();

    }

    // Inside your st_quiz method
    @SuppressLint("SetTextI18n")
    private void st_quiz(String examid) {
        dbManager = new DBManager(this);
        dbManager.open();

        // Get the question list from DB
        List<QlistRes.Ques> quelistItems = dbManager.getQuizQues(examid);
        dbManager.close();

        // Log the original list size and question IDs
        Log.d("Original Quiz Items List Size", "Size: " + quelistItems.size());
        for (QlistRes.Ques ques : quelistItems) {
            Log.d("Original Quiz Item ID", "Question ID: " + ques.getId());
        }

        // Remove duplicates based on question ID
        Set<String> uniqueIds = new HashSet<>();
        List<QlistRes.Ques> filteredQuestions = new ArrayList<>();

        for (QlistRes.Ques ques : quelistItems) {
            if (!uniqueIds.contains(ques.getId())) {
                uniqueIds.add(ques.getId());
                filteredQuestions.add(ques);
            }
        }

        // Log filtered list size and question IDs to confirm no duplicates
        Log.d("Filtered Quiz Items List Size", "Size: " + filteredQuestions.size());
        for (QlistRes.Ques ques : filteredQuestions) {
            Log.d("Filtered Quiz Item ID", "Question ID: " + ques.getId());
        }

        // Assign filtered questions to the GetDetail.current_que for further use
        GetDetail.current_que = filteredQuestions;

        // Create a new JSONArray for the attempt data
        JSONArray jsonArray = new JSONArray();
        for (QlistRes.Ques quearr : filteredQuestions) {
            JSONObject attempt_arr = new JSONObject();
            try {
                attempt_arr.put("que_id", quearr.getId());
                attempt_arr.put("exam_id", examid);
                attempt_arr.put("stud_id", user.getId());
                attempt_arr.put("pro_id", "10");
                attempt_arr.put("correct_ans", ""); // Modify if needed
                attempt_arr.put("stud_given_ans", "");
                attempt_arr.put("is_correct", "0");
                attempt_arr.put("attempt_no", att_no);
                attempt_arr.put("ans_marks", "0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(attempt_arr);
        }

        // Assign the created JSONArray to the GetDetail.att_data
        GetDetail.att_data = jsonArray;

        // Log the length of att_data to confirm
        Log.d("att_data Length", "Populated Length: " + GetDetail.att_data.length());
        Log.d("att_data Contents", new Gson().toJson(GetDetail.att_data));

        // Display the length in your UI
        sr_num.setText("1 / " + GetDetail.att_data.length());

        // Start the quiz
        Log.d("noOfMinutesnoOfMinutes", String.valueOf(noOfMinutes));
        getQue(0); // Get the first question

        // Start the timer if showtime > 0
        if (Integer.parseInt(showtime) > 0)
            startTimer(noOfMinutes);

        // Show the activity layout
        activityLL.setVisibility(View.VISIBLE);
    }


    //to get fast exam
    private void getList(String examid) {
        // Start measuring time
        long startTime = System.currentTimeMillis();

        Api apiService = RetrofitClient.getApiService();
        Call<QlistRes> userResponse = apiService.QList(examid);
        userResponse.enqueue(new Callback<QlistRes>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<QlistRes> call, Response<QlistRes> response) {
                // Measure the time taken for the response
                long responseTime = System.currentTimeMillis() - startTime;
                Log.d("API_CALL", "API Response time: " + responseTime + " ms");

                if (response.isSuccessful() && response.body() != null) {
                    QueResponse = response.body();
                    GetDetail.current_que = QueResponse.getData();
                    sr_num.setText("1/" + QueResponse.getData().size());

                    // Prepare JSON array for storing student answers
                    JSONArray jsonArray = new JSONArray();
                    for (QlistRes.Ques quearr : QueResponse.getData()) {
                        // Add question ID and other details to the JSON array
                        JSONObject attempt_arr = new JSONObject();
                        try {
                            attempt_arr.put("que_id", quearr.getId());
                            attempt_arr.put("exam_id", examid);
                            attempt_arr.put("stud_id", user.getId());
                            attempt_arr.put("pro_id", "10");
                            attempt_arr.put("correct_ans", "");
                            attempt_arr.put("stud_given_ans", "");
                            attempt_arr.put("is_correct", "0");
                            attempt_arr.put("attempt_no", att_no);
                            attempt_arr.put("ans_marks", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(attempt_arr);
                    }

                    getque = false;
                    GetDetail.att_data = jsonArray;
                    SharedPrefManager.getInstance(MainActivity.this).addQuesData(jsonArray.toString(), jsonArray.toString(), QueResponse.getData());

                    // Proceed to load questions
                    getQue(0);
                    if (Integer.parseInt(showtime) > 0) startTimer(noOfMinutes); // Start the timer
                    activityLL.setVisibility(View.VISIBLE);
                } else {
                    Log.e("API_CALL", "Response was not successful");
                    // Handle errors appropriately
                }
            }

            @Override
            public void onFailure(Call<QlistRes> call, Throwable t) {
                Log.e("API_CALL", "Error: " + t.getMessage());
                // Handle error
            }
        });
    }




    private void loadimg(String image) {
        que_img.setVisibility(View.VISIBLE);
        cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, image);

        if (mypath.exists()) {
            Glide.with(MainActivity.this)
                    .load(Drawable.createFromPath(mypath.toString()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(que_img);
        } else {
            Glide.with(MainActivity.this)
                    .load(image)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(que_img);
        }
    }




    @SuppressLint("SetTextI18n")
    private void getQue(int Qposition) {
//        Toast.makeText(MainActivity.this, Qposition, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        Qlayout.setVisibility(View.VISIBLE);
        GetDetail.Current_index = Qposition;
        QlistRes.Ques current_que = GetDetail.current_que.get(Qposition);

        Log.d("current_quecurrent_que", new Gson().toJson(current_que));

        srno.setText("Q. No. "+(Qposition+1));
        Que.setText(Html.fromHtml(Html.fromHtml(current_que.getQueTitle()).toString()));
        if(!current_que.getImage().isEmpty())
        {

            loadimg(current_que.getImage());

        }
        else que_img.setVisibility(View.GONE);
        optionRadioGroup.clearCheck();
        optionRadioGroup.removeAllViews();
        try {
            JSONArray jsonArray = new JSONArray(current_que.getOptions());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
//               final boolean ischecked = Qposition == checkedposition ? true : false;

                String X = jsonObject.getString(String.valueOf((i+1)));
                Log.d("X loop", X);
                RadioButton radioButton = new RadioButton(MainActivity.this);
                JSONObject getarray = new JSONObject(GetDetail.att_data.get(Qposition).toString());
                int opt = (i+1);
                radioButton.setText(X);
                radioButton.setId(opt);
                radioButton.setTextSize(20);
                radioButton.setPadding(20,10,20,10);
                radioButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                radioButton.setBackground(getResources().getDrawable(R.drawable.radio_button_selector));
                radioButton.setButtonDrawable(android.R.color.transparent);

//                Log.d("stud_given_ans** ", String.valueOf(getarray.get("stud_given_ans"))+ (i+1));

                if(getarray.get("stud_given_ans").equals(opt)) {
                    radioButton.setChecked(true);
                    opt_sel = optionRadioGroup.getCheckedRadioButtonId();

//                    Log.d("i+1", String.valueOf(opt));
                }
                rprms = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                rprms.setMargins(0, 15, 0, 15);


                optionRadioGroup.addView(radioButton, rprms);
                optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        int sel = optionRadioGroup.getCheckedRadioButtonId();

                    }
                });

                radioButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
//                        Log.d("attempt_prev", new Gson().toJson(GetDetail.att_data));
                        int sel = optionRadioGroup.getCheckedRadioButtonId();
                        int curr_att = Integer.parseInt(tot_att.getText().toString());

                        Log.d("curr_att", String.valueOf(curr_att));


                        try {
                            JSONObject jsonObject = new JSONObject(GetDetail.att_data.get(Qposition).toString());
                            if (opt_sel == sel) {
                                optionRadioGroup.clearCheck();

                                jsonObject.put("stud_given_ans", "");
                                jsonObject.put("correct_ans", current_que.getCorrectAns());

                                jsonObject.put("is_correct", "0");

                                jsonObject.put("ans_marks", "0");
                                GetDetail.att_data.put(Qposition, jsonObject);

                                opt_sel = 0;
                                if(curr_att>0)
                                    tot_att.setText(String.valueOf(curr_att-1));
                                addAtt = true;


                            } else {
                                opt_sel = sel;

                                if (Objects.equals(jsonObject.getString("stud_given_ans"), ""))
                                    addAtt = true;
                                else addAtt = false;
//                            if(curr_att < GetDetail.att_data.length()) {
                                if (addAtt)
                                    tot_att.setText(String.valueOf(curr_att + 1));

//                            GetDetail.att_data.get(position);
                                jsonObject.put("stud_given_ans", sel);
                                jsonObject.put("correct_ans", current_que.getCorrectAns());

                                if (current_que.getCorrectAns().equals(String.valueOf(sel))) {
                                    jsonObject.put("is_correct", "1");
                                    jsonObject.put("ans_marks", current_que.getQueMarks());
                                } else {
                                    jsonObject.put("is_correct", "0");
                                    jsonObject.put("ans_marks", "0");

                                }
                                GetDetail.att_data.put(Qposition, jsonObject);
//                            Log.d("attempt_after", new Gson().toJson(GetDetail.att_data));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        addAtt = true;
//                        Log.d("selbtn", String.valueOf(sel+" position: "+position));
                    }


                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void SubmitQuiz() {
        // Fetch the current attempt number from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String lastAttemptNoStr = sharedPreferences.getString("att_no", "0");  // Default to "0" if not found
        int lastAttemptNo = Integer.parseInt(lastAttemptNoStr);  // Convert to int

        int new_attempt_no = lastAttemptNo + 1;  // Increment attempt number

        // Update att_data with the new attempt number
        JSONArray attData = GetDetail.att_data; // Ensure att_data is properly initialized and not null
        for (int i = 0; i < attData.length(); i++) {
            try {
                JSONObject attemptObject = attData.getJSONObject(i);
                attemptObject.put("attempt_no", new_attempt_no);  // Update the attempt_no
            } catch (JSONException e) {
                e.printStackTrace();  // Handle JSON exception
            }
        }

        // Update SharedPreferences with the new attempt number and other quiz data
        SharedPrefManager.getInstance(MainActivity.this).editUser2(1, exam_title, attData, String.valueOf(new_attempt_no));
        // Hide submit button and show wait view
        submit.setVisibility(View.GONE);
        wait.setVisibility(View.VISIBLE);

        // Prepare intent for SuccessActivity
        Intent intent = new Intent(MainActivity.this, SuccessActivity.class);
        intent.putExtra("submit", "2");
        intent.putExtra("att_data", new Gson().toJson(attData));  // Pass the selected answers
        intent.putExtra("att_no", String.valueOf(new_attempt_no));  // Pass updated att_no as a String

        Log.d("Submission Data attDataJson:", new Gson().toJson(attData));
        Log.d("Submission Data att_no:", String.valueOf(new_attempt_no)); // Log the updated attempt number

        // Clear the activity stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();  // Optionally finish the current activity
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

        Runnable delayrunnable = new Runnable() {

            @SuppressLint("LongLogTag")
            @Override
            public void run() {

                internetOffRL.setVisibility(View.GONE);
                couldnt_reach_internet_txt.setVisibility(View.GONE);
                Log.d("Len***", String.valueOf(GetDetail.att_data.length()));

                if(getque){
                    if(GetDetail.att_data.length()>0) {
                        JSONObject getarray = null;
                        try {
                            getarray = new JSONObject(GetDetail.att_data.get(0).toString());
                            if(getarray.getString("exam_id").equals(exam_id))
                                st_quiz(exam_id);
                            else
                                getList(exam_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else getList(exam_id);
                    Log.d("exam_id networkAvailable",exam_id);


                }
            }
        };
        handler.postDelayed(delayrunnable, 3000);


    }

    @Override
    public void networkUnavailable() {

        if(getque) {

            internetOffRL.setVisibility(View.VISIBLE);

            try_again_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try_again_txt.setVisibility(View.GONE);
                    if (!isNetworkAvailable()) {
                        simpleProgressBar.postDelayed(new Runnable() {
                            public void run() {
                                couldnt_reach_internet_txt.postDelayed(new Runnable() {
                                    public void run() {
                                        couldnt_reach_internet_txt.setVisibility(View.VISIBLE);
                                        try_again_txt.setVisibility(View.VISIBLE);
                                        simpleProgressBar.setVisibility(View.GONE);
                                        couldnt_reach_internet_txt.postDelayed(new Runnable() {
                                            public void run() {
                                                couldnt_reach_internet_txt.setVisibility(View.GONE);


                                            }
                                        }, 2000);
                                    }
                                }, 2000);

                            }
                        }, 2000);
                    } else {
                        networkUnavailable();
                    }

                }
            });
        }

    }

    public boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager=(ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null;
    }


    public  void  toastMsg(String msg){

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE ); ;
        View toast = inflater.inflate(R.layout.toast, null);

        TextView text = (TextView) toast.findViewById(R.id.toast_txt);
        text.setText(msg);
        Toast view  = new Toast(this);
        // Set layout to toast
        view.setView(toast);
        view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
        view.setDuration(Toast.LENGTH_SHORT);
        view.show();

    }


    /* End code  Registration successfull or not*/
    public static boolean isNetworkAvailable(Context context) {
        isNetworkAvailable = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isNetworkAvailable = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}







