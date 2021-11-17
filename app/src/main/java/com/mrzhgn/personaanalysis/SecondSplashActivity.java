package com.mrzhgn.personaanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class SecondSplashActivity extends AppCompatActivity {

    ViewPager viewPager;
    DotsIndicator dotsIndicator;
    ViewPagerAdapter adapter;

    RelativeLayout skipButton;

    List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second_splash);

        fragments = new ArrayList<>();
        setFragments();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsIndicator = (DotsIndicator) findViewById(R.id.dots_indicator);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager(viewPager);

        skipButton = (RelativeLayout) findViewById(R.id.skip_button);
        skipButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        });
    }

    private void setFragments() {
        fragments.add(AboutFragment.newInstance(0));
        fragments.add(AboutFragment.newInstance(1));
        fragments.add(AboutFragment.newInstance(2));
    }
}
