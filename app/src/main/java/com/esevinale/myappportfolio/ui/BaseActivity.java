package com.esevinale.myappportfolio.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.esevinale.myappportfolio.R;
import com.esevinale.myappportfolio.application.AppController;
import com.esevinale.myappportfolio.utils.manager.MyFragmentManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseActivity extends MvpAppCompatActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    protected ProgressBar mProgressBar;
    @BindView(R.id.main_wrapper)
    FrameLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppController.getAppComponent().inject(this);
        setContentView(R.layout.activity_base);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getLayoutInflater().inflate(getMainContentLayout(), parent);
    }

    public ProgressBar getmProgressBar() {
        return mProgressBar;
    }

    @LayoutRes
    protected abstract int getMainContentLayout();

    public void fragmentOnScreen(BaseFragment fragment) {
        setToolbarTitle(fragment.createToolbarTitle(this));
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void setContent(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_wrapper, fragment);
        fragmentTransaction.commit();
        fragmentOnScreen(fragment);
    }
}
