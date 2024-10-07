package com.bbsc.Api;

import com.bbsc.Model.DefaultResponse;
import com.bbsc.Model.FBLoginResponse;
import com.bbsc.Model.GlobleTime;
import com.bbsc.Model.LoginResponse;
import com.bbsc.Model.QlistRes;
import com.bbsc.Model.Qsubmit;
import com.bbsc.Model.Quiz;
import com.bbsc.Model.QuizRequestBody;
import com.bbsc.Model.SysTime;
import com.bbsc.Model.test;
import com.bbsc.Model.userRoll;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {

    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );



    //
    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @FormUrlEncoded // annotation used in POST type requests
    @POST("signup")
// API's endpoints
    Call<DefaultResponse> registration(@Field("firstname") String firstname,
                                       @Field("email") String email,
                                       @Field("mob") String mob,
                                       @Field("password") String password);
    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @POST("updateProfile")
    @FormUrlEncoded
    Call<DefaultResponse> updateProfile(@Field("id") int id,
                                        @Field("firstname") String username,
                                        @Field("mob") String mob,
                                        @Field("password") String password,
                                        @Field("myBitmap") String myBitmap,
                                        @Field("old_img") String old_img);


//                                    @Field("percent") Float percent)
    //******  Terms of use ********
/*@Headers({
        "Host:bbsc.createonlineacademy.com",
        "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
        "Username:mos_admin",
        "Password:VeerIT@121!",
        "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
        "Cache-Control:no-cache",
        "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
})
    @GET("terms")
 Call<TermsOfUse> getContent();

    //******  Privacy Policy ********


    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @GET("privacypolicy")

        // API's endpoints
    Call<PrivacyPolicy> getprivacy();*/

    //******  Fb login ********

    @Headers({
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
    })
    @FormUrlEncoded
    @POST("fbLogin")
    Call<FBLoginResponse> fbLogin(
            @Field("id") String id,
            @Field("email") String email,
            @Field("username") String username
    );

    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @POST("Ques")
    @FormUrlEncoded
    Call<QlistRes> QList(@Field("exam_id") String exam_id);


    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache"
    })
    @POST("submitQuiz")
    Call<Qsubmit> QuizSubmit(
            @Body QuizRequestBody quizRequestBody // Use @Body to send JSON
    );


    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @POST("getQuiz")
    @FormUrlEncoded
    Call<Quiz> GetQuiz(@Field("user_id") String user_id);


    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @GET("getCurrTime")
    Call<SysTime> getSysTime();

    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @POST("updateRollno")
    @FormUrlEncoded
    Call<userRoll> addRollno(@Field("user_id") int id, @Field("roll_no") String roll_no, @Field("enrollment_no") String enrollment_no);

    @Headers({
            "Host:bbsc.createonlineacademy.com",
            "Authorization:Basic bW9zX2FkbWluOlZlZXJJVEAxMjEh",
            "Username:mos_admin",
            "Password:VeerIT@121!",
            "X-API-KEY:AIzaSyAaQK24c5a7IbDKXiEVJKuqF_iZMwxcmos",
            "Cache-Control:no-cache",
            "Postman-Token:7ca771d2-603d-2e6f-93a6-8609ef8cad9e"
    })
    @POST("subQuiz")
    @FormUrlEncoded
    Call<Qsubmit> QuizSubmit2(@Field("attempt") String att_data, @Field("roll_no") String roll_no, @Field("enrollment_no") String enrollment_no);

    @GET
    Call<GlobleTime> getRealTime(@Url String url);

//    myonlineshiksha.com/api/restmosapi/forgetPass

}
