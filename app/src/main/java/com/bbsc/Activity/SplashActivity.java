package com.bbsc.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bbsc.R;
import com.bbsc.SharedPrefManagerFile.SharedPrefManager;
import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    Handler handler;

    Boolean user;
    String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView ani_logo = (ImageView) findViewById(R.id.ani_logo);

        Glide.with(this).load(R.drawable.splash).into(ani_logo);

        user= SharedPrefManager.getInstance(this).isLoggedIn();
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            new GetVersionCode().execute();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        System.out.println("user1>>>>>>>>>." + user);
//
                System.out.println("user1>>pending." + SharedPrefManager.getInstance(SplashActivity.this).getUser().getpending_status());



    }

    class GetVersionCode extends AsyncTask<Void, String, String> {

        @Override

        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (org.jsoup.nodes.Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (Float.parseFloat(currentVersion) < Float.parseFloat(onlineVersion)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

                    builder.setTitle("New version available")
                            .setCancelable(false)
//                            .setIcon(R.drawable.toolbarlogo)
                            .setMessage("Please update your app to new version ")
                            .setPositiveButton(Html.fromHtml("<font color='#FF5247'>Update</font>"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity String
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                    Log.d("QuizInfo", "Sending atomic bombs to Jupiter");
//                                    rateApp();

                                }
                            })

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {

                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .show();
                } else {
//
                    handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            if(!user){

                                System.out.println("user>>>>>>>>>." + user);

                                Intent intent = new Intent(SplashActivity.this, SignInWithEmail.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);

                            }else{
                                if(SharedPrefManager.getInstance(SplashActivity.this).getUser().getpending_status() == 1)
                                {
                                    String exam_title = SharedPrefManager.getInstance(SplashActivity.this).getUser().getExam_title();
                                    Intent intent = new Intent(SplashActivity.this,SuccessActivity.class);
                                    intent.putExtra("exam_title",exam_title);
                                    intent.putExtra("submit","1");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Intent intent = new Intent(SplashActivity.this, QuizInfo.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("refresh", "refresh");
                                    finish();
                                    startActivity(intent);
                                }
                            }


                        }
                    },3600);
                    handler=new Handler();
                }

            }
            else{
                handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        if(!user){

                            System.out.println("user>>>>>>>>>." + user);

                            Intent intent = new Intent(SplashActivity.this, SignInWithEmail.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            finish();
                            startActivity(intent);

                        }else{
                            if(SharedPrefManager.getInstance(SplashActivity.this).getUser().getpending_status() == 1)
                            {
                                String exam_title = SharedPrefManager.getInstance(SplashActivity.this).getUser().getExam_title();
                                Intent intent = new Intent(SplashActivity.this,SuccessActivity.class);
                                intent.putExtra("exam_title",exam_title);
                                intent.putExtra("submit","1");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(SplashActivity.this, QuizInfo.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("refresh", "refresh");
                                finish();
                                startActivity(intent);
                            }
                        }


                    }
                },3600);
                handler=new Handler();

            }

            Log.d("update", "Current version " + currentVersion + "   playstore version " + onlineVersion);

        }

    }
}
