package com.esevinale.myappportfolio;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.idling.CountingIdlingResource;

public class MyIdlingResources {
    private CountingIdlingResource idlingResource;

    public MyIdlingResources() {
        idlingResource =  new CountingIdlingResource("Load data");
    }

    public void loadStart() {
        IdlingRegistry.getInstance().register(idlingResource);
        idlingResource.increment();
    }

    public void loadFinish() {
        idlingResource.decrement();
        IdlingRegistry.getInstance().unregister(idlingResource);
    }
}
