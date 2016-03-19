package com.softserveinc.softservecare.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.softserveinc.softservecare.R;
import com.softserveinc.softservecare.SoftServeCareApplication;
import com.softserveinc.softservecare.adapters.SurveyQuestionsAdapter;
import com.softserveinc.softservecare.api.FirebaseApi;
import com.softserveinc.softservecare.model.Survey;
import com.softserveinc.softservecare.model.SurveyAnswer;
import com.softserveinc.softservecare.model.SurveyQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity {

    private boolean mIsSaveEnabled;
    private Map<String, SurveyAnswer> mAnswers = new HashMap<String, SurveyAnswer>();
    private int mQuestionCount;
    private String mSurveyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Survey survey = getIntent().getParcelableExtra(SoftServeCareApplication.SURVEY);

        setTitle(survey.getTitle());

        HashMap<String, SurveyAnswer> answers = FirebaseApi.getInstance().getSurveyAnswersById(survey.getId());

        mIsSaveEnabled = (answers == null);

        ArrayList<SurveyQuestion> questions = survey.getQuestions();

        mQuestionCount = questions.size();
        mSurveyId = survey.getId();

        setupListView(questions, answers);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_survey, menu);

        menu.findItem(R.id.action_done).setVisible(mIsSaveEnabled);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                save();
                break;
            default:
                finish();
        }
        return true;
    }

    private void setupListView(ArrayList<SurveyQuestion> questions, HashMap<String, SurveyAnswer> answers) {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.questionsList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        SurveyQuestionsAdapter adapter = new SurveyQuestionsAdapter(this, mAnswers);
        recyclerView.setAdapter(adapter);
        adapter.updateItems(questions, answers);
    }

    private void save() {
        if (mAnswers.size() > 0 && mAnswers.size() == mQuestionCount) {

            FirebaseApi.getInstance().setSurveyAnswers(mSurveyId, mAnswers);
            finish();

        }
        else {
            Toast.makeText(
                    this, R.string.survey_not_filled,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}