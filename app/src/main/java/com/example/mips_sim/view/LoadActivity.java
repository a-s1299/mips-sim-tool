package com.example.mips_sim.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mips_sim.R;
import com.example.mips_sim.controller.UserRuntime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static android.view.View.VISIBLE;
import static android.view.View.INVISIBLE;

public class LoadActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    // GUI dependencies
    private final String[] INSTRUCTION_LIST = {"add", "sub", "and", "or", "slt", "addi", "lw", "sw", "beq", "bne"};
    private final String[] REGISTER_LIST = {"$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9", "$a0", "$a1", "$a2", "$a3", "$v0"};

    private final Map<String, String> OPCODE_MAP = Stream.of(new Object[][]{
            {"add", "000000"}, {"sub", "000000"}, {"and", "000000"},
            {"or", "000000"}, {"slt", "000000"}, {"addi", "001000"},
            {"lw", "100011"}, {"sw", "101011"}, {"beq", "000100"},
            {"bne", "000101"}
    }).collect(Collectors.toMap(p -> (String)p[0], p -> (String)p[1]));

    private final Map<String, String> FUNCT_MAP = Stream.of(new Object[][]{
            {"add", "100000"}, {"sub", "100010"}, {"and", "100100"},
            {"or", "100101"}, {"slt", "101010"}
    }).collect(Collectors.toMap(p -> (String)p[0], p -> (String)p[1]));

    private final Map<String, String> REGISTER_MAP = Stream.of(new Object[][]{
            {"$t0", "01000"}, {"$t1", "01001"}, {"$t2", "01010"},
            {"$t3", "01011"}, {"$t4", "01100"}, {"$t5", "01101"},
            {"$t6", "01110"}, {"$t7", "01111"}, {"$t8", "11000"},
            {"$t9", "11001"}, {"$a0", "00100"}, {"$a1", "00101"},
            {"$a2", "00110"}, {"$a3", "00111"}, {"$v0", "00010"},
    }).collect(Collectors.toMap(p -> (String)p[0], p -> (String) p[1]));

    private Spinner opcodeSpinner,
            rsSpinner,
            rtSpinner,
            rdSpinner,
            shamtSpinner,
            immediateSpinner;
    //private Spinner address_spinner;

    private TextView opcodeTxtView,
            rsTxtView,
            rtTxtView,
            rdTxtView,
            shamtTxtView,
            functFirstTxtView,
            functTxtView,
            immediateTxtView;

    private Button processButton,
            addButton,
            removeButton,
            runButton;

    private EditText instructionBinaryEditTxt;
    private TextView userInstructionSetTxtView;

    // User Dependencies
    private Intent intent;
    private Toast toast;
    private String[] userInstructionSet = new String[4];
    private Integer iterator = 0;
    private String instructionBinaryString;
    private String placeHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_instruction);

        init();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.process_button:
                switch (opcodeSpinner.getSelectedItem().toString()) {
                    case "add": case "sub": case "and": case "or": case "slt": // I-instruction
                        placeHolder = OPCODE_MAP.get(opcodeSpinner.getSelectedItem().toString())
                                + REGISTER_MAP.get(rsSpinner.getSelectedItem().toString())
                                + REGISTER_MAP.get(rtSpinner.getSelectedItem().toString())
                                + REGISTER_MAP.get(rdSpinner.getSelectedItem().toString())
                                + shamtTxtView.getText()
                                + functTxtView.getText();
                        instructionBinaryString = placeHolder;
                        placeHolder = placeHolder.substring(0,6)
                                + "-" + placeHolder.substring(6,11)
                                + "-" + placeHolder.substring(11,16)
                                + "-" + placeHolder.substring(16,21)
                                + "-" + placeHolder.substring(21,26)
                                + "-" + placeHolder.substring(26,32);
                        instructionBinaryEditTxt.setText(placeHolder);
                        break;

                    case "addi": case "lw": case "sw": case "beq": case "bne": //R-instruction
                        placeHolder = OPCODE_MAP.get(opcodeSpinner.getSelectedItem().toString())
                                + REGISTER_MAP.get(rsSpinner.getSelectedItem().toString())
                                + REGISTER_MAP.get(rtSpinner.getSelectedItem().toString())
                                + immediateTxtView.getText();
                        instructionBinaryString = placeHolder;
                        placeHolder = placeHolder.substring(0,6)
                                + "-" + placeHolder.substring(6,11)
                                + "-" + placeHolder.substring(11,16)
                                + "-" + placeHolder.substring(16,32);
                        instructionBinaryEditTxt.setText(placeHolder);
                        break;
                }
                break;

            case R.id.add_instruction_button:
                if (instructionBinaryString != null) {
                    userInstructionSet[iterator++] = instructionBinaryString.substring(24,32);
                    userInstructionSet[iterator++] = instructionBinaryString.substring(16,24);
                    userInstructionSet[iterator++] = instructionBinaryString.substring(8,16);
                    userInstructionSet[iterator++] = instructionBinaryString.substring(0,8);
                    placeHolder = userInstructionSetTxtView.getText().toString() +
                            UserRuntime.translateInstruction(
                                    opcodeTxtView.getText().toString(),
                                    rsTxtView.getText().toString(),
                                    rtTxtView.getText().toString(),
                                    rdTxtView.getText().toString(),
                                    shamtTxtView.getText().toString(),
                                    functTxtView.getText().toString(),
                                    immediateTxtView.getText().toString() );
                    userInstructionSetTxtView.setText(placeHolder);
                    increaseSet();
                } else {
                    makeToast("Please create an instruction");
                }

                break;

            case R.id.remove_button:
                if ( userInstructionSetTxtView.length() > 5 ) {
                    int end = userInstructionSetTxtView.getText().toString().lastIndexOf("\n");
                    end = userInstructionSetTxtView.getText().toString().lastIndexOf("\n", end-1);
                    userInstructionSetTxtView.setText( userInstructionSetTxtView.getText().toString().substring(0,end+1) );
                    reduceSet();
                }
                break;

            case R.id.run_button:
                reduceSet();
                if (userInstructionSetTxtView.length() > 5) {
                    UserRuntime.setCustomInstruction(userInstructionSet);
                    intent = new Intent(this, ProcessActivity.class);
                    startActivity(intent);
                } else {
                    makeToast("Must add at least 1 instruction");
                }

                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        switch (parent.getId())
        {
            case R.id.opcode_spinner:
                resetVisibility();
                switch (pos) {
                    case 0: case 1: case 2: case 3: case 4:
                        setRTypeVisibility();
                        opcodeTxtView.setText( OPCODE_MAP.get( parent.getItemAtPosition(pos).toString()) );
                        functTxtView.setText( FUNCT_MAP.get(parent.getItemAtPosition(pos).toString()) );
                        break;
                    case 5: case 6: case 7: case 8: case 9:
                        setITypeVisibility();
                        opcodeTxtView.setText( OPCODE_MAP.get(parent.getItemAtPosition(pos).toString()) );
                        break;
                }
                break;

            case R.id.rs_spinner:
                rsTxtView.setText( REGISTER_MAP.get(rsSpinner.getItemAtPosition(pos).toString()) );
                break;

            case R.id.rt_spinner:
                rtTxtView.setText( REGISTER_MAP.get(rtSpinner.getItemAtPosition(pos).toString()) );
                break;

            case R.id.rd_spinner:
                rdTxtView.setText( REGISTER_MAP.get(rdSpinner.getItemAtPosition(pos).toString()) );
                break;

            case R.id.immediate_spinner:
                int val = Integer.parseInt(immediateSpinner.getItemAtPosition(pos).toString());
                if (val < 0)
                    immediateTxtView.setText( Integer.toBinaryString( Integer.parseInt(immediateSpinner.getItemAtPosition(pos).toString())).substring(16, 32) );
                else {
                    String temp = Integer.toBinaryString( Integer.parseInt(immediateSpinner.getItemAtPosition(pos).toString()));
                    String zeros = new String(new char[16-temp.length()]).replace("\0", "0");
                    temp = zeros + temp;
                    immediateTxtView.setText( temp );
                }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void init() {

        opcodeSpinner = findViewById(R.id.opcode_spinner);
        rsSpinner = findViewById(R.id.rs_spinner);
        rtSpinner = findViewById(R.id.rt_spinner);
        rdSpinner = findViewById(R.id.rd_spinner);
        shamtSpinner = findViewById(R.id.shamt_spinner);
        immediateSpinner = findViewById(R.id.immediate_spinner);
        //address_spinner = findViewById(R.id.address_spinner);

        opcodeTxtView = findViewById(R.id.opcode_txtview);
        rsTxtView = findViewById(R.id.rs_txtview);
        rtTxtView = findViewById(R.id.rt_txtview);
        rdTxtView = findViewById(R.id.rd_txtview);
        shamtTxtView = findViewById(R.id.shamt_txtview);
        functFirstTxtView = findViewById(R.id.funct_txtview2);
        functTxtView = findViewById(R.id.funct_txtview);
        immediateTxtView = findViewById(R.id.immediate_txtview);

        instructionBinaryEditTxt = findViewById(R.id.full_instruction);

        processButton = findViewById(R.id.process_button);
        addButton = findViewById(R.id.add_instruction_button);
        removeButton = findViewById(R.id.remove_button);
        runButton = findViewById(R.id.run_button);

        userInstructionSetTxtView = findViewById(R.id.user_instruction_set_txtview);

        populateSpinnersAndListeners();
    }

    private void populateSpinnersAndListeners() {

        ArrayAdapter<String> opcode_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, INSTRUCTION_LIST);
        opcode_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcodeSpinner.setAdapter(opcode_adapter);
        opcodeSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> register_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, REGISTER_LIST);
        register_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rsSpinner.setAdapter(register_adapter);
        rtSpinner.setAdapter(register_adapter);
        rdSpinner.setAdapter(register_adapter);
        rsSpinner.setOnItemSelectedListener(this);
        rtSpinner.setOnItemSelectedListener(this);
        rdSpinner.setOnItemSelectedListener(this);

        List<String> shamt_list = new ArrayList<>();
        for (Integer i = 0; i < 33; ++i)
            shamt_list.add(i.toString());
        ArrayAdapter<String> shamt_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, shamt_list);
        shamt_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shamtSpinner.setAdapter(shamt_adapter);
        shamtSpinner.setOnItemSelectedListener(this);

        List<String> immediate_list = new ArrayList<>();
        for (Integer i = 100; i > -101; --i)
            immediate_list.add(i.toString());
        ArrayAdapter<String> immediate_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, immediate_list);
        immediate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        immediateSpinner.setAdapter(immediate_adapter);
        immediateSpinner.setSelection(100);
        immediateSpinner.setOnItemSelectedListener(this);

        processButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        runButton.setOnClickListener(this);
    }

    private void resetVisibility() {
        rsSpinner.setVisibility(INVISIBLE);
        rsTxtView.setVisibility(INVISIBLE);
        rtSpinner.setVisibility(INVISIBLE);
        rtTxtView.setVisibility(INVISIBLE);
        rdSpinner.setVisibility(INVISIBLE);
        rdTxtView.setVisibility(INVISIBLE);
        shamtSpinner.setVisibility(INVISIBLE);
        shamtTxtView.setVisibility(INVISIBLE);
        functFirstTxtView.setVisibility(INVISIBLE);
        functTxtView.setVisibility(INVISIBLE);
        immediateSpinner.setVisibility(INVISIBLE);
        immediateTxtView.setVisibility(INVISIBLE);

    }

    private void setRTypeVisibility() {

        rsSpinner.setVisibility(VISIBLE);
        rsTxtView.setVisibility(VISIBLE);
        rtSpinner.setVisibility(VISIBLE);
        rtTxtView.setVisibility(VISIBLE);
        rdSpinner.setVisibility(VISIBLE);
        rdTxtView.setVisibility(VISIBLE);
        functFirstTxtView.setVisibility(VISIBLE);
        functTxtView.setVisibility(VISIBLE);
    }

    private void setITypeVisibility() {

        rsSpinner.setVisibility(VISIBLE);
        rsTxtView.setVisibility(VISIBLE);
        rtSpinner.setVisibility(VISIBLE);
        rtTxtView.setVisibility(VISIBLE);
        immediateSpinner.setVisibility(VISIBLE);
        immediateTxtView.setVisibility(VISIBLE);
    }

    private void increaseSet() {

        String[] temp = userInstructionSet;
        userInstructionSet = new String[temp.length + 4];

        for (int i = 0 ; i < temp.length; ++i)
            userInstructionSet[i] = temp[i];
    }

    private void reduceSet() {

        if (userInstructionSet.length > 4) {
            String[] temp = userInstructionSet;
            userInstructionSet = new String[temp.length - 4];

            for (int i = 0; i < temp.length-4; ++i)
                userInstructionSet[i] = temp[i];

            iterator = iterator - 4;
        }

    }

    private void makeToast(String toDisplay) {

        toast = Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

}
