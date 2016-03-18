package com.softserveinc.softservecare.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.softserveinc.softservecare.R;
import com.softserveinc.softservecare.model.SurveyAnswer;
import com.softserveinc.softservecare.model.SurveyQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smakoh on 16.03.2016.
 */
public class SurveyQuestionsAdapter extends RecyclerView.Adapter<SurveyQuestionsAdapter.ViewHolder> {
    private List<SurveyQuestion> mQuestions = new ArrayList<SurveyQuestion>();
    private Map<String, SurveyAnswer> mAnswers = new HashMap<String, SurveyAnswer>();
    private final Map<String, SurveyAnswer> mAnswersFilled;

    public SurveyQuestionsAdapter(Map<String, SurveyAnswer> answersFilled) {
        mAnswersFilled = answersFilled;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mQuestionTextView;
        public TextView mAnswerTextView;
        public EditText mAnswerEditText;

        public ViewHolder(View view) {
            super(view);
            mQuestionTextView = (TextView) view.findViewById(R.id.questionTextView);
            mAnswerTextView = (TextView) view.findViewById(R.id.answerTextView);
            mAnswerEditText = (EditText) view.findViewById(R.id.answerEditText);
        }
    }

    public void updateItems(List<SurveyQuestion> items, HashMap<String, SurveyAnswer> answers) {
        mQuestions = items;
        mAnswers = answers;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_question_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SurveyQuestion question = mQuestions.get(position);

        final String questionId = question.getId();
        final String questionTitle = question.getTitle();

        holder.mQuestionTextView.setText(questionTitle);

        if (mAnswers != null) {
            SurveyAnswer answer = mAnswers.get(questionId);
            if (answer != null) {
                holder.mAnswerEditText.setVisibility(View.GONE);
                holder.mAnswerTextView.setVisibility(View.VISIBLE);

                holder.mAnswerTextView.setText(answer.getAnswer());
            }
        }
        else {
            holder.mAnswerTextView.setVisibility(View.GONE);
            holder.mAnswerEditText.setVisibility(View.VISIBLE);

            holder.mAnswerEditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if(s.length() != 0) {
                        mAnswersFilled.put(question.getId(), new SurveyAnswer(String.valueOf(position), questionId, questionTitle, s.toString()));
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }
}