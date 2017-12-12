package com.esevinale.myappportfolio.ui;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;


@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    public void activityStarted() {
        getViewState().setContent();
    }
}
