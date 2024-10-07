package com.bbsc.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bbsc.R;


public class SuccessLoginActivity extends AppCompatActivity {
    Handler handler;
    TextView img_tick, success_content;
    Animation zoom_in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);

        img_tick = (TextView)findViewById(R.id.img_tick);
        zoom_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        img_tick.startAnimation(zoom_in);
        success_content = (TextView) findViewById(R.id.success_content);
        String msg = getIntent().getExtras().getString("msg");
        if(msg !="" || !msg.isEmpty())
            success_content.setText(msg);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SuccessLoginActivity.this, QuizInfo.class);
                intent.putExtra("refresh","");
                startActivity(intent);
                finish();
            }
        },3000);


    }



}
