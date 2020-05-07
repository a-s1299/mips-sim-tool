package com.example.mips_sim.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mips_sim.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtPrompt;
    Button okayButton, cancelButton;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPrompt = findViewById(R.id.textView_fun);
        okayButton = findViewById(R.id.button_okay);
        cancelButton = findViewById(R.id.button_cancel);

        okayButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if ( v.getId() == R.id.button_okay ) {
            intent = new Intent(this, StageSelectActivity.class);
            startActivity(intent);

        } else {
            finish();
            System.exit(0);
        }
    }
}
