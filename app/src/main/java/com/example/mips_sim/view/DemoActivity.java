package com.example.mips_sim.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mips_sim.R;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView introTxtView;
    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_intro);

        introTxtView = findViewById(R.id.intro_txtview);
        proceedButton = findViewById(R.id.proceed_button);

        String placeHolder = "HOW TO:\n\n" +
                "\nFor all challenges you will assume the role of the Control Unit.\n" +
                "\nBase upon the instruction, you will then choose which signals" +
                " to send to each component by clicking on the toggles and then validating.\n" +
                "\nFor learning purposes the default signal, for instructions" +
                " in which the case would not be impacted, is low(zero).\n" +
                "\nTIP: Use the component buttons.  They will provide information.\n" +
                "For example, if you forget your current instruction, click the" +
                " \"instruction memory\" button to be reminded.\n" +
                "\nThis tool requires fundamental knowledge of the MIPS CPU architecture." +
                " Resources on that will not be provided.\n" +
                "\nGoodluck and have fun!";

        introTxtView.setText(placeHolder);
        proceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        finish();
    }
}
