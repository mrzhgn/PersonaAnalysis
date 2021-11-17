package com.mrzhgn.personaanalysis;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class SwipeRefreshWithScrollLayout extends SwipeRefreshLayout {

    public SwipeRefreshWithScrollLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    public boolean canChildScrollUp() {
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrl);

        if (scrollView != null)
            return scrollView.canScrollVertically(-1);

        return false;
    }

}
