package com.mrzhgn.personaanalysis;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private States state;
    RelativeLayout dialog1, dialog2, dialog1state3, dialog2state3;
    TextView textView1, textView2, textView1state2, textView2state2, textView1state3, textView2state3, rubli1, rubli2, rubli3;
    ImageView imageView, imageViewState2, imageViewState3, graph;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);

        state = States.STATE_ONE;

        dialog1 = (RelativeLayout) findViewById(R.id.dialog1);
        dialog2 = (RelativeLayout) findViewById(R.id.dialog2);
        dialog1state3 = (RelativeLayout) findViewById(R.id.dialog1state3);
        dialog2state3 = (RelativeLayout) findViewById(R.id.dialog2state3);
        textView1 = (TextView) findViewById(R.id.next_text);
        textView2 = (TextView) findViewById(R.id.next_text_2);
        textView1state2 = (TextView) findViewById(R.id.next_text_state2);
        textView2state2 = (TextView) findViewById(R.id.next_text_2_state2);
        textView1state3 = (TextView) findViewById(R.id.next_text_state3);
        textView2state3 = (TextView) findViewById(R.id.next_text_2_state3);
        rubli1 = (TextView) findViewById(R.id.rubli_1);
        rubli2 = (TextView) findViewById(R.id.rubli_2);
        rubli3 = (TextView) findViewById(R.id.rubli_3);
        imageView = (ImageView) findViewById(R.id.photo);
        imageViewState2 = (ImageView) findViewById(R.id.photo_state2);
        imageViewState3 = (ImageView) findViewById(R.id.photo_state3);
        graph = (ImageView) findViewById(R.id.graph_photo);
        nextButton = (Button) findViewById(R.id.next_button);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        animation.setDuration(2000);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
        animation2.setDuration(2000);
        Animation animation3 = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        animation3.setDuration(2000);

        nextButton.setOnClickListener(v -> {
            switch (state) {
                case STATE_ONE: {
                    if (dialog1.getVisibility() == View.INVISIBLE) {
                        dialog1.startAnimation(animation);
                        dialog1.setVisibility(View.VISIBLE);
                    } else if (dialog2.getVisibility() == View.INVISIBLE) {
                        dialog2.startAnimation(animation);
                        dialog2.setVisibility(View.VISIBLE);
                        state = States.STATE_TWO;
                    }
                    break;
                }
                case STATE_TWO: {
                    if (imageViewState2.getVisibility() == View.INVISIBLE) {
                        textView1.startAnimation(animation2);
                        textView2.startAnimation(animation2);
                        imageView.startAnimation(animation2);
                        dialog1.startAnimation(animation2);
                        dialog2.startAnimation(animation2);
                        textView1state2.startAnimation(animation);
                        textView2state2.startAnimation(animation);
                        imageViewState2.startAnimation(animation);
                        textView1.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                        dialog1.setVisibility(View.INVISIBLE);
                        dialog2.setVisibility(View.INVISIBLE);
                        textView1state2.setVisibility(View.VISIBLE);
                        textView2state2.setVisibility(View.VISIBLE);
                        imageViewState2.setVisibility(View.VISIBLE);
                    } else if (graph.getVisibility() == View.INVISIBLE) {
                        graph.startAnimation(animation3);
                        new Handler().postDelayed(() -> {
                            rubli1.startAnimation(animation);
                            rubli2.startAnimation(animation);
                            rubli3.startAnimation(animation);
                            rubli1.setVisibility(View.VISIBLE);
                            rubli2.setVisibility(View.VISIBLE);
                            rubli3.setVisibility(View.VISIBLE);
                        }, 1000L);
                        graph.setVisibility(View.VISIBLE);
                        state = States.STATE_THREE;
                    }
                    break;
                }
                case STATE_THREE: {
                    if (imageViewState3.getVisibility() == View.INVISIBLE) {
                        textView1state2.startAnimation(animation2);
                        textView2state2.startAnimation(animation2);
                        imageViewState2.startAnimation(animation2);
                        graph.startAnimation(animation2);
                        rubli1.startAnimation(animation2);
                        rubli2.startAnimation(animation2);
                        rubli3.startAnimation(animation2);
                        textView1state2.setVisibility(View.INVISIBLE);
                        textView2state2.setVisibility(View.INVISIBLE);
                        imageViewState2.setVisibility(View.INVISIBLE);
                        graph.setVisibility(View.INVISIBLE);
                        rubli1.setVisibility(View.INVISIBLE);
                        rubli2.setVisibility(View.INVISIBLE);
                        rubli3.setVisibility(View.INVISIBLE);
                        textView1state3.startAnimation(animation);
                        textView2state3.startAnimation(animation);
                        imageViewState3.startAnimation(animation);
                        textView1state3.setVisibility(View.VISIBLE);
                        textView2state3.setVisibility(View.VISIBLE);
                        imageViewState3.setVisibility(View.VISIBLE);
                    } else if (dialog1state3.getVisibility() == View.INVISIBLE) {
                        dialog1state3.startAnimation(animation);
                        dialog1state3.setVisibility(View.VISIBLE);
                    } else if (dialog2state3.getVisibility() == View.INVISIBLE) {
                        dialog2state3.startAnimation(animation);
                        dialog2state3.setVisibility(View.VISIBLE);
                        state = States.STATE_FOUR;
                    }
                    break;
                }
                case STATE_FOUR: {
                    Intent intent = new Intent(this, AuthenticationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                }
            }
        });
    }
}