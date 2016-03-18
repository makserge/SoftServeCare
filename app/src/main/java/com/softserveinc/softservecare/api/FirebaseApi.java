package com.softserveinc.softservecare.api;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.softserveinc.softservecare.Constants;
import com.softserveinc.softservecare.model.Survey;
import com.softserveinc.softservecare.model.SurveyAnswer;
import com.softserveinc.softservecare.model.SurveyQuestion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by smakoh on 17.03.2016.
 */
public class FirebaseApi {
    private final String SURVEYS_URL = "surveys";
    private final String SURVEY_QUESTIONS_URL = "survey_questions";
    private final String SURVEYS_ANSWERS_URL = "survey_answers";

    private static FirebaseApi mInstance;
    private Firebase mFirebase;

    private ArrayList<Survey> mSurveys = new ArrayList<Survey>();
    private Map<String, HashMap<String, SurveyAnswer>> mSurveyAnswers = new HashMap<String, HashMap<String, SurveyAnswer>>();

    private boolean mIsSurveysQuestionsLoaded = false;
    private boolean mIsSurveysAnswersLoaded = false;

    public FirebaseApi() {
        initFirebase();
    }

    public static void init() {
        mInstance = new FirebaseApi();
    }

    public static FirebaseApi getInstance() {
        if (mInstance == null) {
            mInstance = new FirebaseApi();
        }
        return mInstance;
    }

    public void initFirebase() {
        mSurveys.clear();
        mSurveyAnswers.clear();

        mFirebase = new Firebase(Constants.FIREBASE_URL);
        Firebase firebaseSurvey = mFirebase.child(SURVEYS_URL);
        firebaseSurvey.addValueEventListener(mSurveyEventListener);
    }

    ValueEventListener mSurveyEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            mSurveys.clear();
            if (snapshot.getChildren() != null) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    mSurveys.add(new Survey(String.valueOf(item.child("id").getValue()), (String) item.child("title").getValue()));
                }
            }

            mFirebase.child(SURVEY_QUESTIONS_URL).addListenerForSingleValueEvent(mSurveyQuestionsEventListener);
            mFirebase.child(SURVEYS_ANSWERS_URL).addValueEventListener(mSurveyAnswersEventListener);
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    };

    ValueEventListener mSurveyQuestionsEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            if (snapshot.getChildren() != null) {
                Map<String, ArrayList<SurveyQuestion>> questions = new HashMap<String, ArrayList<SurveyQuestion>>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String surveyId = String.valueOf(item.child("survey_id").getValue());
                    ArrayList<SurveyQuestion> surveyQuestions = questions.get(surveyId);
                    if (surveyQuestions == null) {
                        surveyQuestions = new ArrayList<SurveyQuestion>();
                    }
                    surveyQuestions.add(new SurveyQuestion(String.valueOf(item.child("id").getValue()), (String) item.child("title").getValue()));
                    questions.put(surveyId, surveyQuestions);
                }
                if (questions.size() > 0)
                    for (Map.Entry<String, ArrayList<SurveyQuestion>> entry : questions.entrySet()) {
                        Survey survey = findSurveyById(entry.getKey());
                        if (survey != null) {
                            survey.setQuestions(entry.getValue());
                        }
                    }
            }
            mIsSurveysQuestionsLoaded = true;
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    };

    ValueEventListener mSurveyAnswersEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            if (snapshot.getChildren() != null) {
                String userId = getUserId();
                if (userId != null) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        if (userId.equals((String) item.child("user_id").getValue())) {
                            String surveyId = (String) item.child("survey_id").getValue();

                            HashMap<String, SurveyAnswer> answers = mSurveyAnswers.get(surveyId);
                            if (answers == null) {
                                answers = new HashMap<String, SurveyAnswer>();
                            }
                            String id = (String) item.child("id").getValue();
                            String questionId = (String) item.child("question_id").getValue();
                            String title = (String) item.child("title").getValue();
                            String answer = (String) item.child("answer").getValue();

                            answers.put(questionId, new SurveyAnswer(id, questionId, title, answer));

                            mSurveyAnswers.put(surveyId, answers);
                        }
                    }
                }
            }
            mIsSurveysAnswersLoaded = true;
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    };

    public boolean isSurveysLoaded() {
        return mIsSurveysQuestionsLoaded && mIsSurveysAnswersLoaded;
    }

    public List<Survey> getSurveys() {
        return mSurveys;
    }

    private Survey findSurveyById(String surveyId) {
        for (Survey item : mSurveys) {
            if (surveyId.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public void doLogout() {
        mFirebase.unauth();
    }

    public boolean checkLogin() {
        return (mFirebase.getAuth() == null);
    }

    private String getUserId() {
        try {
            return mFirebase.getAuth().getUid();
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isSurveyAnswered(String surveyId) {
        return mSurveyAnswers.keySet().contains(surveyId);
    }

    public HashMap<String, SurveyAnswer> getSurveyAnswersById(String surveyId) {
        return mSurveyAnswers.get(surveyId);
    }

    public void setSurveyAnswers(String surveyId, Map<String, SurveyAnswer> answers) {
        String userId = getUserId();

        Firebase firebaseAnswers = mFirebase.child(SURVEYS_ANSWERS_URL);

        long idOffset = Calendar.getInstance().getTimeInMillis();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        df.setTimeZone(tz);
        String dateTimeISO = df.format(new Date());

        for (SurveyAnswer item: answers.values()) {
            Map<String, String> data = new HashMap<String, String>();

            data.put("id", idOffset + item.getId());
            data.put("date", dateTimeISO);
            data.put("survey_id", surveyId);
            data.put("question_id", item.getQuestionId());
            data.put("user_id", userId);
            data.put("title", item.getTitle());
            data.put("answer", item.getAnswer());

            Firebase answer = firebaseAnswers.push();
            answer.setValue(data);

            answer.child("id").setValue(answer.getKey());
        }
    }
}
