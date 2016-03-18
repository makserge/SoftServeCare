package com.softserveinc.softservecare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.softserveinc.softservecare.R;
import com.softserveinc.softservecare.SoftServeCareApplication;
import com.softserveinc.softservecare.adapters.SurveysAdapter;
import com.softserveinc.softservecare.api.FirebaseApi;
import com.softserveinc.softservecare.interfaces.SurveysItemClickListener;
import com.softserveinc.softservecare.model.Survey;
import com.softserveinc.softservecare.model.SurveyAnswer;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SurveysItemClickListener {

    private SurveysAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkLogin();

        setupListView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAdapter.updateItems(FirebaseApi.getInstance().getSurveys());
    }

    private void checkLogin() {
        if (FirebaseApi.getInstance().checkLogin()) {
            loadLoginView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseApi.getInstance().doLogout();
            loadLoginView();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupListView() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.surveysList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SurveysAdapter(this, recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void openItem(Survey survey) {
        Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
        intent.putExtra(SoftServeCareApplication.SURVEY, survey);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}