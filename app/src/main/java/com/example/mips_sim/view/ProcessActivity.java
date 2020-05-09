package com.example.mips_sim.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mips_sim.R;
import com.example.mips_sim.controller.*;
import com.example.mips_sim.model.*;

import java.util.Timer;
import java.util.TimerTask;

public class ProcessActivity extends AppCompatActivity implements View.OnClickListener {

    // CPU dependencies
    private static ProgramCounter PC;
    private static InstructionMemory instMem;
    private static RegisterFile regFile;
    private static DataMemory dataMem;

    private static ControlUnit contUnit;
    private static ALU mainALU;
    private static MUX regDst, ALUSrc, memToReg, PCSrc;

    private static ALUControl ALUController;
    private static Adder PCPlusFour, otherAdder;

    private static DecoderWires wires;
    private static BranchGate branch;
    private static SignExtender extender;
    private static ShiftLeft2 sl2;

    // GUI dependencies
    private ImageButton backButton;
    private Button pcButton, instrMemButton, regFileButton, controlUnitButton, memoryButton, checkButton, nextButton;
    private Switch regDstSwitch, aluSrcSwitch, memToRegSwitch, pcSrcSwitch;
    private ToggleButton regWriteToggle, memWriteToggle, memReadToggle, branchToggle;

    // User dependencies
    private UserRuntime user1;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_instruction);

        initCPU();
        checkPreloadDirective();
        initComponents();

        if (!processInstruction()) {
            user1 = new UserRuntime();
            makeToast(user1.translateInstruction(extender));
        } else {

        }


    }

    @Override
    public void onClick(View v) {

        switch ( v.getId() ) {

            case R.id.back_button:
                finish();
                break;
            case R.id.pc_button:
                makeToast(PC.getIndex().toString());
                break;

            case R.id.instrmem_button:
                makeToast(user1.translateInstruction(extender));
                break;

            case R.id.regfile_button:
                makeToast(String.valueOf( regFile.getRegisterState() ));
                break;

            case R.id.mem_button:
                makeToast(String.valueOf( dataMem.getMemoryState() ));
                break;

            case R.id.check_button:
                user1.setUserGuess( (regDstSwitch.isChecked()) ? 1 : 0,
                        (regWriteToggle.isChecked()) ? 1 : 0,
                        (aluSrcSwitch.isChecked()) ? 1 : 0,
                        (memWriteToggle.isChecked()) ? 1 : 0,
                        (memReadToggle.isChecked()) ? 1 : 0,
                        (memToRegSwitch.isChecked()) ? 1 : 0,
                        (branchToggle.isChecked()) ? 1 : 0
                );
                if ( user1.validate(contUnit) ) {
                    makeToast("Correct");
                    UserRuntime.toggleCorrect();

                } else {
                    makeToast("Wrong");
                }
                break;
            case R.id.next_button:
                if (UserRuntime.getIsCorrect()) {
                    UserRuntime.toggleCorrect();
                    if ( PCSrc.getControlSignal() == 1 ) {
                        pcSrcSwitch.setChecked(true);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> pcSrcSwitch.setChecked(false), 2000);
                    }
                    try {
                        resetMovables();
                        PC.updatePC();
                    } catch (Exception err) {
                        makeToast("Error:" + err.getMessage() + "\nPlease re-evaluate your code");
                        finish();
                    }


                    if (!processInstruction()) {
                        makeToast(user1.translateInstruction(extender));
                    }
                }
                else {
                    makeToast("You must guess correctly to proceed");
                }
                break;

        }
    }

    private Boolean processInstruction() { // <<----------------------------------------------------

        boolean programEnd = false;
        try {
            instMem.loadWords();
            instMem.setNextInstruction();
            contUnit.computeSignals();

        } catch (ArrayIndexOutOfBoundsException err) {

            makeToast("Program completed successfully!");
            // countdown to previous
            programEnd = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    if (UserRuntime.getStatus() == UserRuntime.getStageSelection())
                        UserRuntime.levelUp();
                    finish();
                }
            }, 3000);
        } catch (Exception err) {

            makeToast("Logic error has occurred\n\nProgram exiting");
            finish();
        }
        return programEnd;
    }

    private void initCPU() {

        createAllObjects();
        addDependencies();
        instMem.loadWords();
    }

    private void checkPreloadDirective() {

        if (UserRuntime.getStageSelection() == 0) {


        } else if (UserRuntime.getStageSelection() == 1) {
            regFile.preloadReg(9, "101");

        } else {
            switch (UserRuntime.getObjective()) {

                case 0:
                    regFile.preloadReg(8, "10");
                    dataMem.preloadMem(4,"101");
                case 1:
                case 2:
            }

        }
    }

    private static void createAllObjects() {

        PC = new ProgramCounter();
        instMem = new InstructionMemory(UserRuntime.getInstructionSet());
        regFile = new RegisterFile();
        dataMem = new DataMemory();

        contUnit = new ControlUnit();
        mainALU = new ALU();
        regDst = new MUX();
        ALUSrc = new MUX();
        memToReg = new MUX();
        PCSrc = new MUX();

        ALUController = new ALUControl();
        PCPlusFour = new Adder();
        otherAdder = new Adder();

        wires = new DecoderWires();
        branch = new BranchGate();
        extender = new SignExtender();
        sl2 = new ShiftLeft2();
    }

    private static void addDependencies() {

        PC.addDependency(PCSrc);
        instMem.addDependency(PC);
        regFile.addDependency(regDst, wires);
        dataMem.addDependency(mainALU, regFile);

        contUnit.addDependency(wires, regDst, ALUSrc, memToReg, ALUController, regFile, dataMem, branch);
        mainALU.addDependency(regFile, ALUSrc, branch);
        regDst.addDependency(wires);
        ALUSrc.addDependency(regFile, extender);
        memToReg.addDependency(dataMem, mainALU, regFile);
        PCSrc.addDependency(PCPlusFour, otherAdder);

        ALUController.addDependency(wires, mainALU);
        PCPlusFour.addDependency(PC);
        otherAdder.addDependency(PCPlusFour, sl2);

        branch.addDependency(PCSrc);
        extender.addDependency(wires);
        sl2.addDependency(extender);
    }

    private void initComponents() {

        backButton = findViewById(R.id.back_button);
        pcButton = findViewById(R.id.pc_button);
        instrMemButton = findViewById(R.id.instrmem_button);
        regFileButton = findViewById(R.id.regfile_button);
        controlUnitButton = findViewById(R.id.controlunit_button);
        memoryButton = findViewById(R.id.mem_button);
        checkButton = findViewById(R.id.check_button);
        nextButton = findViewById(R.id.next_button);

        regDstSwitch = findViewById(R.id.regDstSwitch);
        aluSrcSwitch = findViewById(R.id.ALUSrcSwitch);
        memToRegSwitch = findViewById(R.id.memToRegSwitch);
        pcSrcSwitch = findViewById(R.id.PCSrcSwitch);

        regWriteToggle = findViewById(R.id.regWrite);
        memWriteToggle = findViewById(R.id.memWriteToggle);
        memReadToggle = findViewById(R.id.memReadToggle);
        branchToggle = findViewById(R.id.branchToggle);

        setListeners();
    }

    private void setListeners() {

        backButton.setOnClickListener(this);
        pcButton.setOnClickListener(this);
        instrMemButton.setOnClickListener(this);
        regFileButton.setOnClickListener(this);
        controlUnitButton.setOnClickListener(this);
        memoryButton.setOnClickListener(this);
        checkButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
    }

    private void resetMovables() {

        regDstSwitch.setChecked(false);
        aluSrcSwitch.setChecked(false);
        memToRegSwitch.setChecked(false);

        regWriteToggle.setChecked(false);
        memWriteToggle.setChecked(false);
        memReadToggle.setChecked(false);
        branchToggle.setChecked(false);
    }

    private void makeToast(String toDisplay) {

        toast = Toast.makeText(this, toDisplay, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}