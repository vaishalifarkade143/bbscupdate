
package com.bbsc.Adapter;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.content.Context;
        import android.content.ContextWrapper;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.CountDownTimer;
        import android.text.Html;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bbsc.Activity.MainActivity;
        import com.bbsc.Activity.QuizListActivity;
        import com.bbsc.Api.Api;
        import com.bbsc.Api.RetrofitClient;
        import com.bbsc.DB.DBManager;
        import com.bbsc.Model.GetDetail;
        import com.bbsc.Model.QlistRes;
        import com.bbsc.Model.Quiz;
        import com.bbsc.Model.User;
        import com.bbsc.R;
        import com.bbsc.SharedPrefManagerFile.SharedPrefManager;
        import com.google.gson.Gson;

        import org.json.JSONArray;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.net.URL;
        import java.text.DateFormat;
        import java.text.DecimalFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.time.ZonedDateTime;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;
        import java.util.TimeZone;
        import java.util.concurrent.TimeUnit;

        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

        import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_WRITE;
        import static com.facebook.FacebookSdk.getApplicationContext;


//before start visible
public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    Context context;
    List<Quiz.Qinfo> ques;
    RadioGroup.LayoutParams rprms;
    Calendar calendar;
    DateFormat formatter ;
    CountDownTimer countDownTimer = null;
    User user;
    List<Quiz.Qinfo> listItems;
    private DBManager dbManager;
    @SuppressLint("DefaultLocale") String hms;
    String format = "yyyy-MM-dd HH:mm:ss";
    String mSavedInfo;

    boolean getrlistactive = true;
    SimpleDateFormat sdf;
    private Quiz.Qinfo QueResponse;
    @SuppressLint("SimpleDateFormat")
    public QuizAdapter(Context mContext, List<Quiz.Qinfo> ques) {
        this.context = mContext;
        this.ques = ques;
        Log.d("ques_size", String.valueOf(ques.size()));
        sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        user = SharedPrefManager.getInstance(mContext).getUser();
        dbManager = new DBManager(context);
        dbManager.open();

        listItems = dbManager.getAllQuizzes();
        dbManager.close();

    }

    @NonNull
    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_quiz, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.ViewHolder holder, int position) {
        QueResponse = ques.get(holder.getAdapterPosition());
//        Log.d("ques", new Gson().toJson(ques));
        holder.title.setText(ques.get(holder.getAdapterPosition()).getExamTitle());


        //for description
        if(ques.get(holder.getAdapterPosition()).getDescription().isEmpty())
            holder.desc.setVisibility(View.GONE);
        else
            holder.desc.setText(Html.fromHtml(Html.fromHtml(ques.get(holder.getAdapterPosition()).getDescription()).toString()));

        //for instruction
        if(ques.get(holder.getAdapterPosition()).getInstructions().isEmpty())
            holder.instLL.setVisibility(View.GONE);
        else {
            holder.inst.setText(Html.fromHtml(Html.fromHtml(ques.get(holder.getAdapterPosition()).getInstructions()).toString()));
        }


//for compulsary quitions
        if(ques.get(holder.getAdapterPosition()).getCompulsory_que() != null && Integer.parseInt(ques.get(holder.getAdapterPosition()).getCompulsory_que()) >0 ) {
            holder.CompQueLL.setVisibility(View.VISIBLE);
            holder.compQue.setText(ques.get(holder.getAdapterPosition()).getCompulsory_que());
        }
//to get how many marks
        holder.marks.setText(ques.get(holder.getAdapterPosition()).getTotalMarks());

        //to get date of adding exam
        holder.exdate.setText(ques.get(holder.getAdapterPosition()).getEx_start_date());
        //for duration
        DecimalFormat durFormat = new DecimalFormat("00");
        String dur_map = "min";
        // Trim the input to remove any extra spaces
        String durationH = ques.get(holder.getAdapterPosition()).getDurationH().trim();
        String durationM = ques.get(holder.getAdapterPosition()).getDurationM().trim();
        try {
            // Check if hours are greater than 0, update the dur_map to "hr"
            if (Integer.parseInt(durationH) > 0) {
                dur_map = "hr";
            }
            // Set the formatted duration text
            holder.dur.setText(durFormat.format(Integer.parseInt(durationH)) + ":" + durFormat.format(Integer.parseInt(durationM)) + " " + dur_map);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Optionally set a default value or show an error message
            holder.dur.setText("Invalid duration");
        }
        holder.on_time.setText(convert12(ques.get(holder.getAdapterPosition()).getEx_start_time())+" - "+ convert12(ques.get(holder.getAdapterPosition()).getEx_end_time()));

        //coundown
        holder.ques.setText(ques.get(holder.getAdapterPosition()).getQcount());

        //for score
        holder.passing.setText(ques.get(holder.getAdapterPosition()).getPassingScore()+"%");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());




//oldlogic app was crashe here
//        String date = GetDetail.getRealTime(); // Fetch the real time directly
//        Log.d("Time get real1: " , String.valueOf(date));


        GetDetail.getRealTimeAsync(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String datetime = response.body();
                    Log.d("Time get real1:", datetime);  // Log the received date or fallback
                    // Use the datetime safely in your app
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Time get real1:", "Failed to fetch real time", t);
                // Handle failure (e.g., show error message)
            }
        });





        holder.expire.setVisibility(View.VISIBLE);

//        int attemptLimit = Integer.parseInt(ques.get(holder.getAdapterPosition()).getAttemptLimit());
//        int currentAttempt = Integer.parseInt(ques.get(holder.getAdapterPosition()).getAtt());
//        Log.d("Attempt Data", "Att: " + currentAttempt + " AttemptLimit: " + attemptLimit);

        if(Integer.parseInt(ques.get(holder.getAdapterPosition()).getTimeLimitB())>0
                && ques.get(holder.getAdapterPosition()).getEx_start_date() != null)
        {
            if (ques.get(holder.getAdapterPosition()).getEx_start_date().equals(currentDate))
            {
//                int attempts = Integer.parseInt(ques.get(holder.getAdapterPosition()).getExam_last_attempt());
//                int attemptLimit = Integer.parseInt(ques.get(holder.getAdapterPosition()).getAttemptLimit());

                //retake logic starts
//                if (attempts < attemptLimit || attemptLimit > 10) {


                int attemptLimit = Integer.parseInt(ques.get(holder.getAdapterPosition()).getAttemptLimit());
                String LastAttempt  = ques.get(holder.getAdapterPosition()).getExam_last_attempt();
                int currentAttempt = (LastAttempt != null) ? Integer.parseInt(LastAttempt) : 0;

                //modified logic

                if ((ques.get(holder.getAdapterPosition()).getRetake().equals("1") &&
                       attemptLimit >= currentAttempt))
                    //old logic
//                        (Integer.parseInt(ques.get(holder.getAdapterPosition()).getExam_last_attempt())
//                                <= Integer.parseInt(ques.get(holder.getAdapterPosition()).getAttemptLimit())
//                                || Integer.parseInt(ques.get(holder.getAdapterPosition()).getAttemptLimit()) >10)) ||
//                        ques.get(holder.getAdapterPosition()).getRetake().equals("0")
//                                && Integer.parseInt(ques.get(holder.getAdapterPosition()).getExam_last_attempt()) <= 1)
                {
                    holder.startBtn.setVisibility(View.VISIBLE);
//                            Toast.makeText(context, "3*"+GetDetail.att_data.length(), Toast.LENGTH_SHORT).show();

                    List<QlistRes.Ques> quelistItems = dbManager.getQuizQues(ques.get(holder.getAdapterPosition()).getExamId());
                    if(quelistItems.size()<=0)
                        getList(ques.get(holder.getAdapterPosition()).getExamId(), ques.get(holder.getAdapterPosition()).getExam_last_attempt(), "0");
                    else {

                        if (ques.get(holder.getAdapterPosition()).getModified_date() != null && quelistItems.size() > 0) {
                            Date mode_date = null, ques_date = null;

                            // Adjusted date format
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                            try {
                                // Parsing the modified date of the current question
                                mode_date = sdf.parse(ques.get(holder.getAdapterPosition()).getModified_date());

                                // Check that quelistItems has elements and get the modified date safely
                                if (quelistItems.get(0).getModified_date() != null) {
                                    ques_date = sdf.parse(quelistItems.get(0).getModified_date());
                                } else {
                                    Log.e("DateError", "Modified date for the question list item is null.");
                                    return; // Exit if ques_date is null
                                }

                                if (mode_date != null && ques_date != null) {
                                    // Calculate the difference in milliseconds
                                    long diff2 = mode_date.getTime() - ques_date.getTime();

                                    // Calculate the differences in various units
                                    int diffDays2 = (int) (diff2 / (24 * 60 * 60 * 1000));
                                    int diffHours2 = (int) (diff2 / (60 * 60 * 1000));
                                    int diffMin2 = (int) (diff2 / (60 * 1000));
                                    int diffSec2 = (int) (diff2 / 1000);

                                    Log.d("updatediff", "diffDays2: " + diffDays2 + ", diffHours: " + diffHours2 + ", diffMin: " + diffMin2 + ", diffSec: " + diffSec2);

                                    // Example condition to trigger getList() call
                                    if (diffHours2 > 0 || diffMin2 > 0 || diffSec2 > 0) { // Adjust logic based on your requirement
                                        getList(ques.get(holder.getAdapterPosition()).getExamId(), ques.get(holder.getAdapterPosition()).getExam_last_attempt(), "1");
                                    }
                                } else {
                                    Log.e("DateError", "One of the parsed dates is null.");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Log.e("DateError", "Failed to parse dates. Error: " + e.getMessage());
                            }
                        } else {
                            Log.e("DateError", "Modified date or question list is empty.");
                        }

                    }
                    holder.timer_LL.setVisibility(View.GONE);
                    holder.expire.setVisibility(View.GONE);

                    Date dateObj1 = null, dateObj2 = null;
                    try {
                        dateObj1 = sdf.parse(ques.get(holder.getAdapterPosition()).getEx_start_date() + " " + ques.get(holder.getAdapterPosition()).getEx_start_time());
                        dateObj2 = sdf.parse(currentDate + " " + currentTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println("dateObj1**"+dateObj1);
                    System.out.println("dateObj2**"+dateObj2 + "\n");

                    DecimalFormat crunchifyFormatter = new DecimalFormat("###,###");

                    // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
                    long diff = dateObj1.getTime() - dateObj2.getTime();

                    int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
                    System.out.println("difference between days: " + diffDays);

                    int diffhours = (int) (diff / (60 * 60 * 1000));
                    System.out.println("difference between hours: " + crunchifyFormatter.format(diffhours));

                    int diffmin = (int) (diff / (60 * 1000));
                    System.out.println("difference between minutues: " + crunchifyFormatter.format(diffmin));

                    int diffsec = (int) (diff / (1000));
                    System.out.println("difference between seconds: " + crunchifyFormatter.format(diffsec));

                    holder.expire.setText("diffhours: "+diffhours+" diffmin:"+diffmin+" diffsec:"+diffsec);
                    long ex_min = 0;
                    if (Integer.parseInt(crunchifyFormatter.format(diffhours)) > 0)
                        ex_min = Integer.parseInt(crunchifyFormatter.format(diffhours)) ;
                    if (diffmin > 0)
                        ex_min += diffmin;

                    long noOfMinutes = (ex_min) * 60 * 1000;

                    Date dateObj3 = null, dateObj4 = null;
                    try {
                        dateObj3 = sdf.parse(ques.get(holder.getAdapterPosition()).getEx_end_date() + " " + ques.get(holder.getAdapterPosition()).getEx_end_time());
                        dateObj4 = sdf.parse(currentDate + " " + currentTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(dateObj1);
                    System.out.println(dateObj2 + "\n");

                    DecimalFormat crunchifyFormatter2 = new DecimalFormat("###,###");

                    // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
                    long diff2 = dateObj3.getTime() - dateObj4.getTime();

                    int diffDays2 = (int) (diff2 / (24 * 60 * 60 * 1000));

                    int diffhours2 = (int) (diff2 / (60 * 60 * 1000));

                    int diffmin2 = (int) (diff2 / (60 * 1000));

                    int diffsec2 = (int) (diff2 / (1000));
                    if (diffhours <= 0 && diffmin <= 0 && diffsec <= 0 && diffhours2 > 0 && diffmin2 > 0 && diffsec2 > 0) {
                        holder.startBtn.setVisibility(View.VISIBLE);
                        holder.timer_LL.setVisibility(View.GONE);
                        holder.expire.setVisibility(View.GONE);
                    } else if (diffhours2 <= 0 && diffmin2 <= 0 && diffsec2 <= 0) {
                        holder.startBtn.setVisibility(View.GONE);
                        holder.timer_LL.setVisibility(View.GONE);
                        holder.expire.setVisibility(View.VISIBLE);
                        if(Integer.parseInt(ques.get(holder.getAdapterPosition()).getExam_last_attempt())>1) {
                            holder.expire.setText("Submited.");
                            holder.expire.setTextColor(Color.parseColor("#FF008000"));
                        }
                        else
                            holder.expire.setText("Expired");
                    } else {
                        holder.startBtn.setVisibility(View.GONE);
                        holder.timer_LL.setVisibility(View.VISIBLE);
                        holder.expire.setVisibility(View.GONE);

                        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
                            @SuppressLint("DefaultLocale")
                            public void onTick(long millisUntilFinished) {
                                int holderPosition = holder.getAdapterPosition();
                                long millis = millisUntilFinished;
                                Log.d("hr: ", TimeUnit.MILLISECONDS.toHours(millis) + " min: " + TimeUnit.MILLISECONDS.toMinutes(millis));

                                holder.tmTxt.setText("Time left to start: ");
                                if (TimeUnit.MILLISECONDS.toHours(millis) <=24) {


                                    if (holderPosition != RecyclerView.NO_POSITION) {
                                        List<QlistRes.Ques> quelistItems = dbManager.getQuizQues(ques.get(holderPosition).getExamId());
                                        // Proceed with your logic here
                                    } else {
                                        // Handle the case when the position is invalid

                                        toastMsg("Invalid adapter position:"+holderPosition);
                                    }

                                    if(quelistItems.size() <= 0) {

                                        toastMsg("Adapter position:"+holderPosition);
                                        if(ques.size()<=0){
                                            getList(ques.get(holderPosition).getExamId(), ques.get(holderPosition).getExam_last_attempt(), "0");
                                        }

                                    }else{
                                        //added for date formate error app crash

                                        if (ques.size() > 0 && quelistItems.size() > 0) {

                                            /*ques.get(adapterPosition).getModified_date() != null*/
                                            Date mode_date = null, ques_date = null;
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); // Replace with your date format

                                            try {

                                                Log.e("DateError", "Modified date for the question list item is null.");
                                                if(ques.size()>0 && holderPosition >0) {

                                                    // Parsing the modified date of the current question
                                                    mode_date = sdf.parse(ques.get(holderPosition).getModified_date());
                                                }
                                                // Check that quelistItems has elements and get the modified date safely
                                                if (quelistItems.get(0).getModified_date() != null) {
                                                    ques_date = sdf.parse(quelistItems.get(0).getModified_date());
                                                } else {
                                                    Log.e("DateError", "Modified date for the question list item is null.");
                                                    return; // Exit if ques_date is null
                                                }

                                                if (mode_date != null && ques_date != null) {
                                                    // Calculate the difference in milliseconds
                                                    long diff2 = mode_date.getTime() - ques_date.getTime();

                                                    // Calculate the differences in various units
                                                    int diffDays2 = (int) (diff2 / (24 * 60 * 60 * 1000));
                                                    int diffHours2 = (int) (diff2 / (60 * 60 * 1000));
                                                    int diffMin2 = (int) (diff2 / (60 * 1000));
                                                    int diffSec2 = (int) (diff2 / 1000);

                                                    Log.d("updatediff", "diffDays2: " + diffDays2 + ", diffHours: " + diffHours2 + ", diffMin: " + diffMin2 + ", diffSec: " + diffSec2);

                                                    // Example condition to trigger getList() call
                                                    if (diffHours2 > 0 || diffMin2 > 0 || diffSec2 > 0) { // Adjust logic based on your requirement
                                                        getList(ques.get(holderPosition).getExamId(), ques.get(holderPosition).getExam_last_attempt(), "1");
                                                    }
                                                } else {
                                                    Log.e("DateError", "One of the parsed dates is null.");
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                                Log.e("DateError", "Failed to parse dates. Error: " + e.getMessage());
                                            }
                                        }
                                        else {
                                            Log.e("DateError", "Modified date or question list is empty.");
                                        }

                                    }

                                    holder.timer.setTextColor(Color.parseColor("#FFF55625"));

                                    //Log.d("exam_id onTick", ques.get(holderPosition).getExamId());
                                }
                                hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                                holder.timer.setText(hms);//set
//                                        }
                            }

                            public void onFinish() {
                                holder.startBtn.setVisibility(View.VISIBLE);
//                                        Toast.makeText(context, "1*"+GetDetail.att_data.length(), Toast.LENGTH_SHORT).show();
                                List<QlistRes.Ques> quelistItems = dbManager.getQuizQues(ques.get(holder.getAdapterPosition()).getExamId());
                                if(quelistItems.size()<=0)
                                    getList(ques.get(holder.getAdapterPosition()).getExamId(), ques.get(holder.getAdapterPosition()).getExam_last_attempt(), "0");
                                Log.d("set get Exam Id", "Exam Id"+ques.get(holder.getAdapterPosition()).getExamId());

                                holder.timer_LL.setVisibility(View.GONE);
                                holder.expire.setVisibility(View.GONE);
                                countDownTimer = null;
                            }
                        }.start();
                    }

                }

                else {
                    holder.startBtn.setVisibility(View.GONE);
                    holder.timer_LL.setVisibility(View.GONE);
                    holder.expire.setVisibility(View.VISIBLE);
                    holder.expire.setText("Submited.");
                    holder.expire.setTextColor(Color.parseColor("#FF008000"));
                }
                //retake logic close

            }
            else{
                holder.startBtn.setVisibility(View.GONE);
                holder.timer_LL.setVisibility(View.GONE);
                holder.expire.setVisibility(View.GONE);
            }
        }
        else {
            holder.dateLL.setVisibility(View.GONE);
            holder.ex_timeLL.setVisibility(View.GONE);
            holder.durLL.setVisibility(View.GONE);

            //added
//            holder.startBtn.setVisibility(View.GONE);
//            holder.timer_LL.setVisibility(View.GONE);
//            holder.expire.setVisibility(View.VISIBLE);
//            holder.expire.setText("Submited.");
//            holder.expire.setTextColor(Color.parseColor("#FF008000"));

            if(ques.get(holder.getAdapterPosition()).getRetake().equals("1")) {
                if (Integer.parseInt(ques.get(holder.getAdapterPosition()).getExam_last_attempt())
                        <= Integer.parseInt(ques.get(holder.getAdapterPosition()).getAttemptLimit())) {
                    holder.startBtn.setVisibility(View.VISIBLE);
//                    Toast.makeText(context, "2*"+GetDetail.att_data.length(), Toast.LENGTH_SHORT).show();

                    List<QlistRes.Ques> quelistItems = dbManager.getQuizQues(ques.get(holder.getAdapterPosition()).getExamId());
                    if(quelistItems.size()<=0)
                        getList(ques.get(holder.getAdapterPosition()).getExamId(),
                                ques.get(holder.getAdapterPosition()).getExam_last_attempt(), "0");


                    holder.timer_LL.setVisibility(View.GONE);
                    holder.expire.setVisibility(View.GONE);
                }
                else{
                    holder.startBtn.setVisibility(View.GONE);
                    holder.timer_LL.setVisibility(View.GONE);
                    holder.expire.setVisibility(View.VISIBLE);
                    holder.expire.setText("Submited.");
                    holder.expire.setTextColor(Color.parseColor("#FF008000"));

                }
            }
            else{
                holder.startBtn.setVisibility(View.GONE);
                holder.timer_LL.setVisibility(View.GONE);
                holder.expire.setVisibility(View.VISIBLE);
                holder.expire.setText("Submited.");
                holder.expire.setTextColor(Color.parseColor("#FF008000"));

            }

        }

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

                if (networkInfo ==null) {
                    toastMsg("Check internet connection!");
                }
                else{
                    Date dateObj3 = null, dateObj4 = null;
                    try {
                        dateObj3 = sdf.parse(ques.get(holder.getAdapterPosition()).getEx_end_date() + " " + ques.get(holder.getAdapterPosition()).getEx_end_time());
                        dateObj4 = sdf.parse(currentDate + " " + currentTime);
                        Log.d("dateObj3dateObj3", dateObj3 + "   " + dateObj4);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    DecimalFormat crunchifyFormatter2 = new DecimalFormat("###,###");

                    // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
                    long diff2 = dateObj3.getTime() - dateObj4.getTime();

                    int diffDays2 = (int) (diff2 / (24 * 60 * 60 * 1000));

                    int diffhours2 = (int) (diff2 / (60 * 60 * 1000));

                    int diffmin2 = (int) (diff2 / (60 * 1000));

                    int diffsec2 = (int) (diff2 / (1000));
                    Log.d("diff22", diffhours2 + " :: " + diffsec2 + " :: " + diffmin2);
                    if (diffhours2 > 0 || diffmin2 > 0 || diffsec2 > 0) {

                        Log.d("sel exam", String.valueOf(ques.get(holder.getAdapterPosition()).getExamId() + " position: " + holder.getAdapterPosition()));

                        Intent i = new Intent(context, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.putExtra("exam_id", ques.get(holder.getAdapterPosition()).getExamId());
                        i.putExtra("exam_title", ques.get(holder.getAdapterPosition()).getExamTitle());
                        i.putExtra("st_date", ques.get(holder.getAdapterPosition()).getEx_start_date());
                        i.putExtra("st_time", ques.get(holder.getAdapterPosition()).getEx_start_time());
                        i.putExtra("end_date", ques.get(holder.getAdapterPosition()).getEx_end_date());
                        i.putExtra("end_time", ques.get(holder.getAdapterPosition()).getEx_end_time());
                        i.putExtra("exam_hr", ques.get(holder.getAdapterPosition()).getDurationH());
                        i.putExtra("exam_min", ques.get(holder.getAdapterPosition()).getDurationM());
                        i.putExtra("showtime", ques.get(holder.getAdapterPosition()).getTimeLimitB());
                        i.putExtra("seeresult", ques.get(holder.getAdapterPosition()).getSeeResult());
                        i.putExtra("att_no", ques.get(holder.getAdapterPosition()).getExam_last_attempt());
                        Log.d("sel exam attempt data",  " attempt data on quiz adapter: " + ques.get(holder.getAdapterPosition()).getExam_last_attempt());

                        context.startActivity(i);
                    } else {
                        toastMsg("Quiz time expired");
                        holder.startBtn.setVisibility(View.GONE);
                        holder.expire.setVisibility(View.VISIBLE);


                        if (Integer.parseInt(ques.get(holder.getAdapterPosition()).getExam_last_attempt()) > 1) {
                            holder.expire.setText("Submited.");
                            holder.expire.setTextColor(Color.parseColor("#FF008000"));
                        } else
                            holder.expire.setText("Expired.");
                    }
                }
            }
        });


    }







    private void updateCountdown(TextView timerView, long millisUntilFinished) {
        int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
        millisUntilFinished %= (1000 * 60 * 60);
        int minutes = (int) (millisUntilFinished / (1000 * 60));
        millisUntilFinished %= (1000 * 60);
        int seconds = (int) (millisUntilFinished / 1000);
        timerView.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }


    public  void  toastMsg(String msg){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE ); ;
        View toast = inflater.inflate(R.layout.toast, null);

        TextView text = (TextView) toast.findViewById(R.id.toast_txt);
        text.setText(msg);
        Toast view  = new Toast(context);
        // Set layout to toast
        view.setView(toast);
        view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
        view.setDuration(Toast.LENGTH_SHORT);
        view.show();

    }
    public String convert12(String str)
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        Date _24HourDt = null;
        try {
            _24HourDt = _24HourSDF.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        System.out.println(_24HourDt);
        assert _24HourDt != null;
//        System.out.println(_12HourSDF.format(_24HourDt));

        return _12HourSDF.format(_24HourDt);


    }


    private void getList(String examid, String att, String modify) {
        if(getrlistactive) {
            if(modify.equals("1")){

            }
            Log.d("Ques_res", "Ques_resQues_res" + examid);
            Api apiService = RetrofitClient.getApiService();
            Call<QlistRes> userResponse = apiService.QList(examid);
            userResponse.enqueue(new Callback<QlistRes>() {
                public QlistRes QueResponse;

                @Override
                public void onResponse(Call<QlistRes> call, Response<QlistRes> response) {
                    QueResponse = response.body();
                    GetDetail.current_que = QueResponse.getData();
                    Log.d("Ques_res", new Gson().toJson(QueResponse));
                    GetDetail.att_data = new JSONArray();

                    JSONArray jsonArray = new JSONArray();
                    for (QlistRes.Ques quearr : QueResponse.getData()) {
                        Log.d("saveToInternalStorage11", "saveToInternalStorage");

                        if (!quearr.getImage().isEmpty()){
//                            if (checkPermission()) {
                            URL url = null;
                            Bitmap bitmap = null;
                            String urlString = quearr.getImage();
                            urlString=  urlString.substring(urlString.lastIndexOf('/') + 1).split("\\?")[0].split("#")[0];

                            try {
                                url = new URL("http://bbsc.createonlineacademy.com/public/uploads/questions/1622104732.jpg");
                                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            mSavedInfo = saveToInternalStorage(bitmap, urlString);
                            Log.d("quearr.getImage()", quearr.getImage());
                        }


//                        add in sqllite
                        dbManager.open();
                        dbManager.insertQuesList("Questions", QueResponse.getData());



                        dbManager.close();
                    }

                    Log.d("att_data***", new Gson().toJson(GetDetail.att_data));

                    SharedPrefManager.getInstance(context).addQuesData(jsonArray.toString(), jsonArray.toString(), QueResponse.getData());
                }

                @Override
                public void onFailure(Call<QlistRes> call, Throwable t) {
                    getList(examid, att, "modify");
                    Log.d("Ques_res", "Ques_resFailure");
                }
            });

            getrlistactive = false;
        }
    }
    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 10);
        }
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    private String saveToInternalStorage(Bitmap bitmapImage,String urlString){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());

        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, urlString);
        if(!mypath.exists()) {
            FileOutputStream fos = null;

            try {

                fos = new FileOutputStream(mypath);

                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

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

    @Override
    public int getItemCount() {
        return ques.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,desc,inst, marks, ques, passing, tmTxt, timer,expire, exdate, dur, on_time, compQue;
        Button startBtn;
        LinearLayout instLL, timer_LL, CompQueLL, dateLL, ex_timeLL, durLL;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.desc);
            inst = view.findViewById(R.id.instruction);
            marks = view.findViewById(R.id.marks);
            ques = view.findViewById(R.id.noQue);
            passing = view.findViewById(R.id.passing);
            startBtn = view.findViewById(R.id.startBtn);
            instLL = view.findViewById(R.id.inst);
            tmTxt = view.findViewById(R.id.tmTxt);
            timer = view.findViewById(R.id.timer);
            expire = view.findViewById(R.id.expire);
            timer_LL = view.findViewById(R.id.timer_LL);
            exdate = view.findViewById(R.id.exdate);
            dur = view.findViewById(R.id.duration);
            on_time = view.findViewById(R.id.start_tm);
            CompQueLL = view.findViewById(R.id.CompQueLL);
            compQue = view.findViewById(R.id.compQue);
            dateLL = view.findViewById(R.id.dateLL);
            ex_timeLL = view.findViewById(R.id.ex_timeLL);
            durLL = view.findViewById(R.id.durLL);
        }
    }
}