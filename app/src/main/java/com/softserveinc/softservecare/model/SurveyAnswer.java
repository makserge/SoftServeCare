package com.softserveinc.softservecare.model;

import java.io.Serializable;

/**
 * Created by smakoh on 16.03.2016.
 */
public class SurveyAnswer implements Serializable {
    private String mId;
    private String mQuestionId;
    private String mTitle;
    private String mAnswer;

    public SurveyAnswer(String id, String questionId, String title, String answer) {
        mId = id;
        mQuestionId = questionId;
        mTitle = title;
        mAnswer = answer;
    }

    public String getId() {
        return mId;
    }

    public String getQuestionId() {
        return mQuestionId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAnswer() {
        return mAnswer;
    }
}