package com.esevinale.myappportfolio.ui;

import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.ui.MovieScreen.MovieListFragment;

public class MainActivity extends BaseActivity implements MainView {

    @InjectPresenter
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.activityStarted();
    }

    @Override
    protected int getMainContentLayout() {
        return R.layout.activity_base;
    }

    @Override
    public void setContent() {
        setContent(new MovieListFragment());
    }
}
