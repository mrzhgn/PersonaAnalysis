package com.mrzhgn.personaanalysis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AfterSplashActivity extends AppCompatActivity {

    SharedPreferences sPref;
    ImageView imageView, imageView2;
    TextView textView, versionView;
    //RelativeLayout btn;

/*    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("firstLaunch", false);
        editor.commit();

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean("firstLaunch", false);
        editor.commit();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_after_splash);

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isFirstLaunch = sPref.getBoolean("firstLaunch", true);

        imageView = (ImageView) findViewById(R.id.image);
        imageView2 = (ImageView) findViewById(R.id.image2);
        textView = (TextView) findViewById(R.id.text);
        versionView = (TextView) findViewById(R.id.version);
        //btn = (RelativeLayout) findViewById(R.id.dalee_button);
        imageView.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        versionView.setVisibility(View.INVISIBLE);
        //btn.setVisibility(View.INVISIBLE);

//        Animation fadeIn = new AlphaAnimation(0, 1);
//        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
//        fadeIn.setDuration(5000);
//
//        Animation fadeOut = new AlphaAnimation(1, 0);
//        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
//        fadeOut.setStartOffset(5000);
//        fadeOut.setDuration(5000);
//
//        AnimationSet animation = new AnimationSet(false); //change to false
//        animation.addAnimation(fadeIn);
//        animation.addAnimation(fadeOut);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        animation.setDuration(2000);

        new Handler().postDelayed(() -> {
            imageView.startAnimation(animation);
            imageView2.startAnimation(animation);
            textView.startAnimation(animation);
            versionView.startAnimation(animation);
            //btn.startAnimation(animation);
            imageView.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            versionView.setVisibility(View.VISIBLE);
            //btn.setVisibility(View.VISIBLE);
        }, 1000L);

        new Handler().postDelayed(() -> {
            if (isFirstLaunch) {
                Intent intent = new Intent(this, SecondSplashActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            } else if (sPref.getInt("userid", -1) == -1) {
                Intent intent = new Intent(this, SignActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            } else {
                Intent intent = new Intent(AfterSplashActivity.this, MainActivity.class);
                intent.putExtra("userId", sPref.getInt("userid", -1));
                intent.putExtra("userToken", sPref.getString("usertoken", ""));
                intent.putExtra("userName", sPref.getString("username", ""));
                intent.putExtra("userEmail", sPref.getString("useremail", ""));
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }
        }, 3000L);

/*        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SecondSplashActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });*/

/*        new Handler().postDelayed(() -> {
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }, 3000L);*/
/*        else new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        }, 3000L);*/
//        imageView.setVisibility(View.INVISIBLE);
//        imageView2.setVisibility(View.INVISIBLE);
//        imageView3.setVisibility(View.INVISIBLE);
//        textView.setVisibility(View.INVISIBLE);
//        Intent intent = new Intent(this, AfterSplashActivity.class);
//        startActivity(intent);
    }
}