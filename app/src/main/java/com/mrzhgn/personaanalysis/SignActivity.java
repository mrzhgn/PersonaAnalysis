package com.mrzhgn.personaanalysis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignActivity extends AppCompatActivity {

    SharedPreferences sPref;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sign_activity);

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean isFirstLaunch = sPref.getBoolean("firstLaunch", true);
        if (isFirstLaunch) {
            SharedPreferences.Editor editor = sPref.edit();
            editor.putBoolean("firstLaunch", false);
            editor.commit();
        }

        findViewById(R.id.voiti_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });

        findViewById(R.id.regist_button).setOnClickListener(view -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });
    }

}
