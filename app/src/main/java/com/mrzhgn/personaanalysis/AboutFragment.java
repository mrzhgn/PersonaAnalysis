package com.mrzhgn.personaanalysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    private static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    private int pageNumber;

    public static AboutFragment newInstance(int page) {
        AboutFragment aboutFragment = new AboutFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        aboutFragment.setArguments(arguments);
        return aboutFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);

        ImageView image = (ImageView) view.findViewById(R.id.fragment_image);
        TextView headText = (TextView) view.findViewById(R.id.head_text);
        TextView bodyText = (TextView) view.findViewById(R.id.body_text);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.scrlvw);
        scrollView.setSmoothScrollingEnabled(true);

        switch (pageNumber) {
            case 0:
                image.setImageResource(R.drawable.photov211);
                image.setPivotX(500f);
                image.setPivotY(1500f);
                headText.setText("Весь салон\nв одном приложении");
                bodyText.setText("В вашем распоряжении будут более 15 самых важных и нужных показателей, с помощью которых Вы сможете с лёгкостью управлять салоном и добиваться своих целей");
                break;
            case 1:
                image.setImageResource(R.drawable.photov22);
                image.setPivotX(500f);
                image.setPivotY(0f);
                image.setScaleX(0.9f);
                image.setScaleY(0.9f);
                image.setAdjustViewBounds(true);
                headText.setText("Все данные в режиме\nреального времени");
                bodyText.setText("Вы будете знать актуальные данные на данную минуту, за неделю, месяц или год. Доступ к вашим данным 24/7 с мобильного приложения или компьютера.");
                break;
            case 2:
                image.setImageResource(R.drawable.photov23);
                image.setPivotX(500f);
                image.setPivotY(0f);
                image.setScaleX(0.9f);
                image.setScaleY(0.9f);
                image.setAdjustViewBounds(true);
                headText.setText("Будьте в курсе о\nваших сотрудниках");
                bodyText.setText("От эффективности мастера зависит эффективность салона. Знайте насколько хорошо работают Ваши мастера и правильно выстраивайте с ними взаимоотношения");
                break;
        }

        return view;
    }

}
