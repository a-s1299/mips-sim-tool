package com.example.mips_sim.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mips_sim.R;
import com.example.mips_sim.controller.UserRuntime;

public class StageSelectActivity  extends AppCompatActivity implements View.OnClickListener {

    private Button firstButton, secondButton, thirdButton;

    private Intent intent;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_select);

        init();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.challenge1_button:
                UserRuntime.setStageSelection(0);
                startProcessActivity(0);
                break;

            case R.id.challenge2_button:
                UserRuntime.setStageSelection(1);
                if (UserRuntime.getStageSelection() > UserRuntime.getStatus())
                    stockFailResponse();
                else
                    startProcessActivity(0);
                break;

            case R.id.challenge3_button:
                UserRuntime.setStageSelection(2);
                if (UserRuntime.getStageSelection() > UserRuntime.getStatus())
                    stockFailResponse();
                else
                    startProcessActivity(1);
                break;
        }
    }

    private void init() {

        firstButton = findViewById(R.id.challenge1_button);
        secondButton = findViewById(R.id.challenge2_button);
        thirdButton = findViewById(R.id.challenge3_button);

        firstButton.setOnClickListener(this);
        secondButton.setOnClickListener(this);
        thirdButton.setOnClickListener(this);
    }

    private void startProcessActivity(Integer customEqualsOne) {

        switch (customEqualsOne) {
            case 0:
                intent = new Intent(this, ProcessActivity.class);
                break;
            case 1:
                intent = new Intent(this, LoadActivity.class);
                break;
        }

        startActivity(intent);
    }

    private void stockFailResponse() {
        toast = Toast.makeText(this, "Must complete prior challenges", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}