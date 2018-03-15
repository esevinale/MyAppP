package com.esevinale.myappportfolio.ui.MovieListScreen;

import android.os.Bundle;

import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.ui.BaseActivity;

public class MovieListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(new MovieListFragment());
    }

    @Override
    protected int getMainContentLayout() {
        return R.layout.activity_base;
    }
}
