package com.bbsc.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.bbsc.Model.FBLoginResponse;
import com.bbsc.Model.LoginResponse;
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
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

public class SignInWithEmail extends AppCompatActivity implements View.OnClickListener, SmartLoginCallbacks {




    static boolean isNetworkAvailable;
    Button buttonSignIn;
    private EditText editTextEmail, editTextPassword;
    String email;
    String password;
    ProgressBar simpleProgressBar;
    LinearLayout card_signin_with_facebook, txt_createaccount;
    SmartUser currentUser;

    ProfileTracker mProfileTracker;
    Profile profile;

    LoginButton loginButton;
    CallbackManager callbackManager;
    static LoginResponse loginResponse;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_email);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        card_signin_with_facebook = (LinearLayout)findViewById(R.id.card_signin_with_facebook);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        LoginManager.getInstance().logOut();
        editTextEmail.requestFocus();
        editTextPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                view.requestFocus();
                inputMethodManager.showSoftInput(view, 0);
//                .requestFocus();
            }
        });


        Window window = SignInWithEmail.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(SignInWithEmail.this,R.color.black));

        simpleProgressBar=(ProgressBar) findViewById(R.id.simpleProgressBar); // initiate the progress bar
        int maxValue=simpleProgressBar.getMax();

//        findViewById(R.id.buttonSignIn).setOnClickListener(this);
        buttonSignIn=(Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);


        //**************new code fb ************//



//        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
//        printHashKey(getApplicationContext());

        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    loginButton.performClick();
                }
                else{
//                    Toast.makeText(SignInWithEmail.this, "2222", Toast.LENGTH_SHORT).show();
                }
            }
        };
        fbTracker.startTracking();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();
        loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                    getUserProfile(AccessToken.getCurrentAccessToken());
            }


            @Override
            public void onCancel() {
                // App code
                Log.d("onCancelr", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("fb error", String.valueOf(exception));
            }
        });


        findViewById(R.id.forget_txt).setOnClickListener(this);
        txt_createaccount = (LinearLayout)findViewById(R.id.txt_createaccount);
        txt_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInWithEmail.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickFacebookButton(View view) {
        if (view == card_signin_with_facebook) {
            loginButton.performClick();
        }
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("fbTAG", object.toString());

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
//                                    Log.d("FBuser", new Gson().toJson(loginResponse.getUser()));
                                    if(response.isSuccessful()) {
                                        SharedPrefManager.getInstance(SignInWithEmail.this).saveUser(loginResponse.getUser());
//                                        Intent intent = new Intent(SignInWithEmail.this,MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                                        startActivity(intent);

                                        Intent intent = new Intent(SignInWithEmail.this, SuccessLoginActivity.class);
                                        intent.putExtra("msg", "");

                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(intent);

                                    }

                                }

                                @Override
                                public void onFailure(Call<FBLoginResponse> call, Throwable t) {


                                    Log.d("fb;log", t.toString());
//                Toast.makeText(SignInWithEmail.this, "failed fb", Toast.LENGTH_SHORT).show();
//                simpleProgressBar.setVisibility(View.GONE);
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
                                    View toast = inflater.inflate(R.layout.toast_login_fail, null);
//                Toast view  = new Toast(SignInWithEmail.this);
                                    Toast view  = Toast.makeText(SignInWithEmail.this,"Something went wrong, Please try again", Toast.LENGTH_SHORT);
                                    view.setText(t.toString());
                                    view.setView(toast);
                                    view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
                                    view.setDuration(Toast.LENGTH_LONG);
                                    view.show();
//                buttonSignIn.setVisibility(View.VISIBLE);


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
    @SuppressLint("LongLogTag")
    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.d( "printHashKey() Hash Key: ", hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.d("printHashKey()", String.valueOf(e));
        } catch (Exception e) {
            Log.e("printHashKey()", String.valueOf(e));
        }
    }

//    @Override
//    public void onLoginSuccess(SmartUser user) {
//
//        if(Profile.getCurrentProfile() == null) {
//            mProfileTracker = new ProfileTracker() {
//                @Override
//                protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//                    Log.v("facebook - profile", currentProfile.getFirstName());
//                    profile = currentProfile;
//                    mProfileTracker.stopTracking();
//                }
//            };
//            // no need to call startTracking() on mProfileTracker
//            // because it is called by its constructor, internally.
//        }
//        else {
//            profile = Profile.getCurrentProfile();
//            Log.v("facebook - profile", profile.getFirstName());
//        }
//
//
//        currentUser = getCurrentUser(SignInWithEmail.this);
//
//
//        String id = profile.getId();
//        String email =null;
//        String username = profile.getName();
//
//        Api apiService = RetrofitClient.getApiService();
//        Call<FBLoginResponse> userResponse = apiService.fbLogin(id,email,username);
//        userResponse.enqueue(new Callback<FBLoginResponse>(){
//
//            @Override
//            public void onResponse(Call<FBLoginResponse> call, Response<FBLoginResponse> response) {
//
//                FBLoginResponse loginResponse = response.body();
//                if(response.isSuccessful()) {
////                    simpleProgressBar.setVisibility(View.GONE);
//
//                    SharedPrefManager.getInstance(SignInWithEmail.this).saveUser(loginResponse.getUser());
////                    Log.d("fblog : ", new Gson().toJson(loginResponse));
////                    Intent intent = new Intent(SignInWithEmail.this,MainActivity.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////
////                    startActivity(intent);
//
//                    Intent intent = new Intent(SignInWithEmail.this, SuccessLoginActivity.class);
//                    intent.putExtra("msg", "");
//
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                    startActivity(intent);
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<FBLoginResponse> call, Throwable t) {
//
//
//                Log.d("fb;log", t.toString());
////                Toast.makeText(SignInWithEmail.this, "failed fb", Toast.LENGTH_SHORT).show();
////                simpleProgressBar.setVisibility(View.GONE);
//                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
//                View toast = inflater.inflate(R.layout.toast_login_fail, null);
////                Toast view  = new Toast(SignInWithEmail.this);
//                Toast view  = Toast.makeText(SignInWithEmail.this,t.toString(), Toast.LENGTH_SHORT);
//                view.setText(t.toString());
//                view.setView(toast);
//                view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
//                view.setDuration(Toast.LENGTH_LONG);
//                view.show();
////                buttonSignIn.setVisibility(View.VISIBLE);
//
//
//            }
//        });
//
//
//
//        /*end facebook success code*/
//
//
////        refreshLayout();
//    }

//    @Override
//    public void onLoginFailure(SmartLoginException e) {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE );
//        View toast = inflater.inflate(R.layout.toast_login_fail, null);
//        Toast view  = Toast.makeText(SignInWithEmail.this,e.toString(), Toast.LENGTH_SHORT);
//        view.setView(toast);
////        view.setText(e.toString());
//        view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
//        view.setDuration(Toast.LENGTH_LONG);
//        view.show();
//    }


        @Override
    protected void onStart() {
            super.onStart();

            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                Intent intent = new Intent(this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }

//    private boolean isValidMobile(String phone) {
//        return android.util.Patterns.PHONE.matcher(phone).matches();
//    }
    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
//        boolean digitsOnly = TextUtils.isDigitsOnly(editTextEmail.getText());
        Log.d("emlength", String.valueOf(email.length()));
        if (email.isEmpty()) {
            editTextEmail.setError("Email ID is required");
            editTextEmail.requestFocus();
            return;
        }
        else if (password.isEmpty()) {
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            return;
        }

        else if (password.length() < 6) {
            editTextPassword.setError("Password should be atleast 6 character long");
            editTextPassword.requestFocus();
            return;
        }

        else {
            simpleProgressBar.setVisibility(View.VISIBLE);
            buttonSignIn.setVisibility(View.GONE);


            /* Login code */


            Api apiService = RetrofitClient.getApiService();
            Call<LoginResponse> userResponse = apiService.userLogin(email, password);
            userResponse.enqueue(new Callback<LoginResponse>() {

                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    loginResponse = response.body();
                    simpleProgressBar.setVisibility(View.GONE);


Log.d("LoginResponse333", new Gson().toJson(loginResponse));
                    if (response.isSuccessful()) {

                        if (loginResponse.equals("null")) {
                            toastMsg("Invalid credential! Enter valid email & password.");
                            buttonSignIn.setVisibility(View.VISIBLE);
                        } else if (loginResponse.isError()) {
                            toastMsg(loginResponse.getMessage());
                            buttonSignIn.setVisibility(View.VISIBLE);
                        } else {

                            SharedPrefManager.getInstance(SignInWithEmail.this).saveUser(loginResponse.getUser());
                            System.out.println(">>>>loginResponse>>>>>" + loginResponse.getUser().getEntroll());

                            Intent intent = new Intent(SignInWithEmail.this, SuccessLoginActivity.class);
                            intent.putExtra("msg", "");

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);
                        }

                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("LoginResponse333", t.toString());

                    simpleProgressBar.setVisibility(View.GONE);
                    buttonSignIn.setVisibility(View.VISIBLE);

                    if (!isNetworkAvailable(SignInWithEmail.this)) {
                        toastMsg("There is a problem signing in, please check your connction and try again.");

                    } else {

                        toastMsg("There was a problem signing in. Check your email and password or create an account.");
                    }


                }
            });
        }

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
        View toast = inflater.inflate(R.layout.toast_login_fail, null);

        TextView text = (TextView) toast.findViewById(R.id.msg);
        text.setText(msg);

        Toast view  = new Toast(this);
        // Set layout to toast
        view.setView(toast);
        view.setGravity(Gravity.BOTTOM| Gravity.FILL_HORIZONTAL, 0, 0);
        view.setDuration(Toast.LENGTH_SHORT);
        view.show();


    }





    private void forgotPassword(){
/*

        String email = editTextEmail.getText().toString().trim();

        Intent intent = new Intent(SignInWithEmail.this, ForgotPassword.class);
        intent.putExtra("Email", email);
        startActivity(intent);
*/

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                userLogin();
                break;
//            case R.id.textViewRegister:
//                startActivity(new Intent(this, MainActivity.class));
//                break;
            case R.id.forget_txt:
                forgotPassword();
                break;
        }
    }


//    @Override
//    public SmartUser doCustomLogin() {
//        return null;
//    }
//
//    @Override
//    public SmartUser doCustomSignup() {
//        return null;
//    }
}



