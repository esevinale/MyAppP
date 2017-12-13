package com.esevinale.myappportfolio.models;

import io.realm.RealmObject;

/**
 * Created by twili on 13.12.2017.
 */

public class RealmInteger extends RealmObject {
    private Integer integer;

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
