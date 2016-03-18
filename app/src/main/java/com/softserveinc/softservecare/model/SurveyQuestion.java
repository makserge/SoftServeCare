package com.softserveinc.softservecare.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by smakoh on 16.03.2016.
 */
public class SurveyQuestion implements Serializable {
    private String mId;
    private String mTitle;

    public SurveyQuestion(String id, String title) {
        mId = id;
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

}