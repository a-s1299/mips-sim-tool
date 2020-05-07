package com.example.mips_sim.model;

import com.example.mips_sim.controller.MUX;

public class ProgramCounter {

    private MUX PCSrcRef;

    private Integer currentInstructionIndex;

    public ProgramCounter() {

        currentInstructionIndex = 0;
    }

    public void addDependency(MUX PCSrc){
        PCSrcRef = PCSrc;
    }

    public void updatePC() {

        currentInstructionIndex = PCSrcRef.getCorrectVal();
    }

    public Integer getIndex() {

        return currentInstructionIndex;
    }
}
