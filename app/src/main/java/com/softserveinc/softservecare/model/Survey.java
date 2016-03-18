package com.softserveinc.softservecare.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by smakoh on 16.03.2016.
 */
public class Survey implements Parcelable {

    private String mId;
    private String mTitle;

    private ArrayList<SurveyQuestion> mQuestions = new ArrayList<SurveyQuestion>();

    public Survey(String id, String title) {
        mId = id;
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setQuestions(ArrayList<SurveyQuestion> questions) {
        mQuestions = questions;
    }

    public ArrayList<SurveyQuestion> getQuestions() {
        return mQuestions;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeList(mQuestions);
    }

    protected Survey(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mQuestions = new ArrayList<SurveyQuestion>();
        in.readList(mQuestions, SurveyQuestion.class.getClassLoader());
    }

    public static final Creator<Survey> CREATOR = new Creator<Survey>() {
        @Override
        public Survey createFromParcel(Parcel source) {
            return new Survey(source);
        }

        @Override
        public Survey[] newArray(int size) {
            return new Survey[size];
        }
    };
}