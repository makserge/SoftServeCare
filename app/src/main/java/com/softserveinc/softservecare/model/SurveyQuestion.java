package com.softserveinc.softservecare.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smakoh on 16.03.2016.
 */
public class SurveyQuestion implements Parcelable {
    private String mId;
    private String mTitle;
    private List<SurveyAnswer> mAnswers = new ArrayList<SurveyAnswer>();

    public SurveyQuestion(String id, String title, List<SurveyAnswer> answers) {
        mId = id;
        mTitle = title;
        mAnswers = answers;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<SurveyAnswer> getAnswers() {
        return mAnswers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mTitle);
        dest.writeList(this.mAnswers);
    }

    protected SurveyQuestion(Parcel in) {
        this.mId = in.readString();
        this.mTitle = in.readString();
        this.mAnswers = new ArrayList<SurveyAnswer>();
        in.readList(this.mAnswers, SurveyAnswer.class.getClassLoader());
    }

    public static final Creator<SurveyQuestion> CREATOR = new Creator<SurveyQuestion>() {
        @Override
        public SurveyQuestion createFromParcel(Parcel source) {
            return new SurveyQuestion(source);
        }

        @Override
        public SurveyQuestion[] newArray(int size) {
            return new SurveyQuestion[size];
        }
    };
}