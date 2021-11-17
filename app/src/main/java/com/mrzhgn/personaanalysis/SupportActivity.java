package com.mrzhgn.personaanalysis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SupportActivity extends AppCompatActivity {

    SharedPreferences sPref;

    private EditText name;
    private EditText email;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_support);

        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);

        name.setText(getIntent().getStringExtra("userName"));
        email.setText(getIntent().getStringExtra("userEmail"));

        findViewById(R.id.tprvt_button).setOnClickListener(view -> {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });
    }
}
