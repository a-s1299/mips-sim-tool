package com.example.mips_sim.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mips_sim.R;
import com.example.mips_sim.controller.UserRuntime;

public class StageSelectActivity  extends AppCompatActivity implements View.OnClickListener {

    private Button firstButton, secondButton, thirdButton, resetButton;

    private Intent intent;
    private Toast toast;

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String USER_STATUS = "status";
    private static final String OBJECTIVE_STATUS = "objectiveStatus";
    private static final String HAS_HAD_INTRO = "hasHadIntro";
    private static final Integer DEFAULT_USER_STATUS = 0;
    private static final Boolean DEFAULT_OBJ_STATUS = false;
    private static final Boolean DEFAULT_INTRO = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_select);

        init();
        checkUserStatus();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.challenge1_button:
                UserRuntime.setStageSelection(0);
                startActivity(0);
                break;

            case R.id.challenge2_button:
                UserRuntime.setStageSelection(1);
                if (UserRuntime.getStageSelection() > UserRuntime.getStatus())
                    makeToast("Must complete prior challenges");
                else
                    startActivity(0);
                break;

            case R.id.challenge3_button:
                UserRuntime.setStageSelection(2);
                if (UserRuntime.getStageSelection() > UserRuntime.getStatus())
                    makeToast("Must complete prior challenges");
                else
                    startActivity(1);
                break;

            case R.id.reset_button:
                makeToast("Reset");
                UserRuntime.setStatus(0);
                for (int i = 0; i < LoadActivity.TOTAL_OBJECTIVES; ++i)
                    UserRuntime.objectiveStatus[i] = false;
                UserRuntime.hasHadIntro = false;
                break;
        }
        saveData();
    }

    private void init() {

        firstButton = findViewById(R.id.challenge1_button);
        secondButton = findViewById(R.id.challenge2_button);
        thirdButton = findViewById(R.id.challenge3_button);
        resetButton = findViewById(R.id.reset_button);


        firstButton.setOnClickListener(this);
        secondButton.setOnClickListener(this);
        thirdButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    private void startActivity(Integer loadEqualsOne) {

        switch (loadEqualsOne) {
            case 0:
                intent = new Intent(this, ProcessActivity.class);
                break;
            case 1:
                intent = new Intent(this, LoadActivity.class);
                break;
        }

        startActivity(intent);
    }

    private void makeToast(String toDisplay) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    private void checkUserStatus() {

        if (UserRuntime.objectiveStatus == null) {
            UserRuntime.objectiveStatus = new Boolean[LoadActivity.TOTAL_OBJECTIVES];
            for (int i = 0; i < LoadActivity.TOTAL_OBJECTIVES; ++i)
                UserRuntime.objectiveStatus[i] = false;
        }
    }

    private void saveData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(USER_STATUS, UserRuntime.getStatus());
        for (Integer i = 0; i < UserRuntime.objectiveStatus.length; ++i)
            editor.putBoolean(OBJECTIVE_STATUS + i.toString(), UserRuntime.objectiveStatus[i]);
        editor.putBoolean(HAS_HAD_INTRO, UserRuntime.hasHadIntro);

        editor.apply();
    }

    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        UserRuntime.setStatus( sharedPreferences.getInt(USER_STATUS, DEFAULT_USER_STATUS) );
        for (Integer i = 0; i < UserRuntime.objectiveStatus.length; ++i)
            UserRuntime.objectiveStatus[i] = sharedPreferences.getBoolean(OBJECTIVE_STATUS + i.toString(), DEFAULT_OBJ_STATUS);
        UserRuntime.hasHadIntro = sharedPreferences.getBoolean(HAS_HAD_INTRO, DEFAULT_INTRO);
    }
}