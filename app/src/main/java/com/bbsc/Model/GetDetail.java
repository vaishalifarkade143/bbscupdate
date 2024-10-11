package com.bbsc.Model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bbsc.Api.Api;
import com.bbsc.Api.RetrofitClient;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class GetDetail {
    public static JSONArray att_data = new JSONArray();
    public static int Current_index = 0;
    public static List<Quiz.Qinfo> QuizList= new List<Quiz.Qinfo>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Quiz.Qinfo> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] ts) {
            return null;
        }

        @Override
        public boolean add(Quiz.Qinfo qinfo) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Quiz.Qinfo> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, @NonNull Collection<? extends Quiz.Qinfo> collection) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Quiz.Qinfo get(int i) {
            return null;
        }

        @Override
        public Quiz.Qinfo set(int i, Quiz.Qinfo qinfo) {
            return null;
        }

        @Override
        public void add(int i, Quiz.Qinfo qinfo) {

        }

        @Override
        public Quiz.Qinfo remove(int i) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<Quiz.Qinfo> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<Quiz.Qinfo> listIterator(int i) {
            return null;
        }

        @NonNull
        @Override
        public List<Quiz.Qinfo> subList(int i, int i1) {
            return null;
        }
    };

    public static List<QlistRes.Ques> current_que = new List<QlistRes.Ques>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<QlistRes.Ques> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] ts) {
            return null;
        }

        @Override
        public boolean add(QlistRes.Ques ques) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends QlistRes.Ques> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, @NonNull Collection<? extends QlistRes.Ques> collection) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public QlistRes.Ques get(int i) {
            return null;
        }

        @Override
        public QlistRes.Ques set(int i, QlistRes.Ques ques) {
            return null;
        }

        @Override
        public void add(int i, QlistRes.Ques ques) {

        }

        @Override
        public QlistRes.Ques remove(int i) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<QlistRes.Ques> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<QlistRes.Ques> listIterator(int i) {
            return null;
        }

        @NonNull
        @Override
        public List<QlistRes.Ques> subList(int i, int i1) {
            return null;
        }
    };
    private static String currdatetime;

//    public static String getRealTime() {
//        final String[] datetime = new String[1]; // Store the datetime
//
//        String fallbackDate = "1970-01-01T00:00:00";
//        // Synchronous call approach (use carefully, generally not recommended)
//        try {
//            Api apiService = RetrofitClient.getApiService2();
//            Call<GlobleTime> userResponse = apiService.getRealTime("http://worldtimeapi.org/api/timezone/Asia/Kolkata");
//            Response<GlobleTime> response = userResponse.execute(); // Synchronous call
//
//            if (response.isSuccessful() && response.body() != null) {
//                GlobleTime data = response.body();
//                // Check if datetime is not null
//                if (data.getDatetime() != null) {
//                    datetime[0] = data.getDatetime(); // Assign to array
//                    GetDetail.currdatetime = datetime[0]; // Update current datetime
//                } else {
//                    datetime[0] = null; // or a default value
//                }
//            } else {
//                datetime[0] = null; // Handle unsuccessful response
//            }
//        } catch (IOException e) {
//            Log.e("GetDetail", "Error fetching real time", e);
//            e.printStackTrace();
//            datetime[0] = null; // Handle exceptions
//        }
//
//        // Use the fallbackDate if datetime[0] is null
//        String date = datetime[0] != null ? datetime[0] : fallbackDate;
//
////        return datetime[0]; // Return the datetime or null
//        return date;
//    }


    public static String getRealTime() {
        return getDeviceRealTime();
    }

    public static String getDeviceRealTime() {
        // Define the desired date format, including milliseconds and timezone offset
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Set the timezone
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }



    //oldlogic app was crashe here

//    public static void getRealTimeAsync(final retrofit2.Callback<String> callback) {
//        String fallbackDate = "1970-01-01T00:00:00"; // Fallback date in case of failure
//
//        Api apiService = RetrofitClient.getApiService2();
//        Call<GlobleTime> userResponse = apiService.getRealTime("http://worldtimeapi.org/api/timezone/Asia/Kolkata");
//
//        userResponse.enqueue(new retrofit2.Callback<GlobleTime>() {
//            @Override
//            public void onResponse(Call<GlobleTime> call, Response<GlobleTime> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    GlobleTime data = response.body();
//                    String date = data.getDatetime() != null ? data.getDatetime() : fallbackDate;
//                    GetDetail.currdatetime = date;  // Update the current datetime with either real or fallback
//                    callback.onResponse(null, Response.success(date));  // Pass the date as a string
//                } else {
//                    callback.onResponse(null, Response.success(fallbackDate));  // Pass fallback date as a string
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GlobleTime> call, Throwable t) {
//                Log.e("GetDetail", "Error fetching real time", t);
//                callback.onFailure(null, t);  // Pass failure
//            }
//        });
//    }



}
