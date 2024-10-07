package com.bbsc.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bbsc.Api.Api;
import com.bbsc.Api.RetrofitClient;
import com.bbsc.Model.DefaultResponse;
import com.bbsc.Model.FBLoginResponse;
import com.bbsc.R;
import com.bbsc.SharedPrefManagerFile.SharedPrefManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import studios.codelight.smartloginlibrary.SmartLogin;
//import studios.codelight.smartloginlibrary.SmartLoginCallbacks;
//import studios.codelight.smartloginlibrary.SmartLoginConfig;
//import studios.codelight.smartloginlibrary.users.SmartUser;
//import studios.codelight.smartloginlibrary.util.SmartLoginException;
//
//import static studios.codelight.smartloginlibrary.UserSessionManager.getCurrentUser;



public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, SmartLoginCallbacks {
    private Button create_Account;
    private EditText edt_username,edt_email, edt_mob, edt_firstname,edt_lastname,edt_password,edt_cnf_password;
    CheckBox pizza;
    TextView txt_Terms,txt_privacy;
    ProgressBar simpleProgressBar;
    DefaultResponse defaultResponse;
    Intent intent;
    static boolean isNetworkAvailable;
    LinearLayout Signin_txt,card_signin_with_facebook;

    SmartUser currentUser;
    LoginButton loginButton;
    CallbackManager callbackManager;
    private static final String TAG = MainActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        create_Account = (Button) findViewById(R.id.create_Account);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_mob = (EditText) findViewById(R.id.edt_mob);
        edt_firstname = (EditText) findViewById(R.id.edt_firstname);
        edt_password = (EditText) findViewById(R.id.edt_password);

        Signin_txt = (LinearLayout)findViewById(R.id.Signin_txt);
        txt_Terms = (TextView)findViewById(R.id.txt_Terms);
        txt_privacy = (TextView)findViewById(R.id.txt_privacy);
        card_signin_with_facebook = (LinearLayout)findViewById(R.id.card_signin_with_facebook);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        simpleProgressBar=(ProgressBar) findViewById(R.id.simpleProgressBar); // initiate the progress bar
        Window window = RegistrationActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(RegistrationActivity.this,R.color.black));
        Signin_txt.setOnClickListener(this);
        create_Account.setOnClickListener(this);
        txt_Terms.setOnClickListener(this);
        txt_privacy.setOnClickListener(this);
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    loginButton.performClick();
                }
            }
        };
        fbTracker.startTracking();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

                if (!loggedOut) {
                    getUserProfile(AccessToken.getCurrentAccessToken());
                }
            }
            @Override
            public void onCancel() {
                Log.d("onCancelr", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("fb error", String.valueOf(exception));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            String username = first_name+" "+last_name;
                            Api apiService = RetrofitClient.getApiService();
                            Call<FBLoginResponse> userResponse = apiService.fbLogin(id,email,username);
                            userResponse.enqueue(new Callback<FBLoginResponse>(){

                                @Override
                                public void onResponse(Call<FBLoginResponse> call, Response<FBLoginResponse> response) {

                                    FBLoginResponse loginResponse = response.body();
                                    if(response.isSuccessful()) {
                                        SharedPrefManager.getInstance(RegistrationActivity.this).saveUser(loginResponse.getUser());
                                        Intent intent = new Intent(RegistrationActivity.this,QuizInfo.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(Call<FBLoginResponse> call, Throwable t) {
                                    Log.d("fb;log", t.toString());
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                                    View toast = inflater.inflate(R.layout.toast_login_fail, null);
                                    Toast view  = Toast.makeText(RegistrationActivity.this,t.toString(), Toast.LENGTH_SHORT);
                                    view.setText(t.toString());
                                    view.setView(toast);
                                    view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
                                    view.setDuration(Toast.LENGTH_LONG);
                                    view.show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public void onClickFacebookButton(View view) {
        if (view == card_signin_with_facebook) {
            loginButton.performClick();
        }
    }

//    @Override
//    public void onLoginSuccess(SmartUser user) {
//        System.out.println("user.toString()"  + user.toString());
//        Profile profile = Profile.getCurrentProfile();
//        currentUser = getCurrentUser(RegistrationActivity.this);
//        String id = profile.getId();
//        String email =" ";
//        String username = profile.getName();
//        Api apiService = RetrofitClient.getApiService();
//        Call<FBLoginResponse> userResponse = apiService.fbLogin(id,email,username);
//        userResponse.enqueue(new Callback<FBLoginResponse>(){
//            @Override
//            public void onResponse(Call<FBLoginResponse> call, Response<FBLoginResponse> response) {
//                FBLoginResponse loginResponse = response.body();
//
//                if(response.isSuccessful()) {
//                    SharedPrefManager.getInstance(RegistrationActivity.this).saveUser(loginResponse.getUser());
//                    Intent intent = new Intent(RegistrationActivity.this,QuizInfo.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FBLoginResponse> call, Throwable t) {
//                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
//                View toast = inflater.inflate(R.layout.toast_login_fail, null);
//                Toast view  = new Toast(RegistrationActivity.this);
//                view.setText(t.toString());
//                view.setView(toast);
//                view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
//                view.setDuration(Toast.LENGTH_LONG);
//                view.show();
//            }
//        });
//    }

//    @Override
//    public void onLoginFailure(SmartLoginException e) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE ); ;
//        View toast = inflater.inflate(R.layout.toast_login_fail, null);
//        Toast view  = new Toast(RegistrationActivity.this);
//        view.setView(toast);
//        view.setText(e.toString());
//        view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
//        view.setDuration(Toast.LENGTH_LONG);
//        view.show();
//    }
//
//    @Override
//    public SmartUser doCustomLogin() {
//        return null;
//    }
//
//    @Override
//    public SmartUser doCustomSignup() {
//        return null;
//    }

    private void userSignUp() {
        String firstname = edt_firstname.getText().toString().trim();
        String email = edt_email.getText().toString().trim();
        String mob = edt_mob.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        boolean digitsOnly = TextUtils.isDigitsOnly(edt_mob.getText());
        if (firstname.isEmpty()) {
            edt_firstname.setError("First name is required");
            edt_firstname.requestFocus();
            return;
        }
        if (firstname.length() < 2) {
            edt_firstname.setError("First name should be atleast 2 character long");
            edt_firstname.requestFocus();
            return;
        }
        if (mob.isEmpty()) {
            edt_mob.setError("Mobile no. is required");
            edt_mob.requestFocus();
            return;
        }
        if(digitsOnly && !android.util.Patterns.PHONE.matcher(mob).matches() ||(mob.length() < 10 && mob.length() > 13)) {
            edt_mob.setError("Invalid mobile no.");
            edt_mob.requestFocus();
            return;
        }
        if(!digitsOnly) {
            edt_mob.setError("Invalid mobile no.");
            edt_mob.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edt_password.setError("Password required");
            edt_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edt_password.setError("Password should be atleast 6 character long");
            edt_password.requestFocus();
            return;
        }
        simpleProgressBar.setVisibility(View.VISIBLE);
        create_Account.setVisibility(View.GONE);
        Api apiService = RetrofitClient.getApiService();
        Call<DefaultResponse> userResponse = apiService.registration(firstname,email,mob,password);
        userResponse.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                defaultResponse = response.body();
                System.out.println("defaultResponse>>>>>" + response.body());
                if(response.isSuccessful()){
                    if(defaultResponse.getError())
                    {
                        toastMsg(defaultResponse.getMessage());
                        simpleProgressBar.setVisibility(View.GONE);
                        create_Account.setVisibility(View.VISIBLE);
                    }
                    else {
                        simpleProgressBar.setVisibility(View.GONE);
                        SharedPrefManager.getInstance(RegistrationActivity.this).saveUser(defaultResponse.getUser());
                        intent = new Intent(RegistrationActivity.this, SuccessLoginActivity.class);
                        intent.putExtra("msg", "");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
                else{
                    toastMsg("This Mobile No. is Already Exists, please try again!");
                    simpleProgressBar.setVisibility(View.GONE);
                    create_Account.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                simpleProgressBar.setVisibility(View.GONE);
                create_Account.setVisibility(View.VISIBLE);
                simpleProgressBar.setVisibility(View.GONE);
                if(!isNetworkAvailable(RegistrationActivity.this)){
                    toastMsg("There is a problem in create an account, please check your connction and try again.");
                }
            }
        });
    }
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
    }//isNetworkAvailable()
    public  void  toastMsg(String msg){
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE ); ;
        View toast = inflater.inflate(R.layout.toast_mailexixt, null);
        TextView text = (TextView) toast.findViewById(R.id.msg);
        text.setText(msg);
        Toast view  = new Toast(this);
        view.setView(toast);
        view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
        view.setDuration(Toast.LENGTH_SHORT);
        view.show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.create_Account:
                userSignUp();
                break;
            case R.id.Signin_txt:
                intent = new Intent(RegistrationActivity.this, SignInWithEmail.class);
                startActivity(intent);
                break;
        }
    }
}
