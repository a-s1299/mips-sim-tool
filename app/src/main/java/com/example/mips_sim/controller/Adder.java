package com.example.mips_sim.controller;

import com.example.mips_sim.model.ProgramCounter;

public class Adder {

    private ProgramCounter PCRef;
    private final Integer FOUR = 4;
    private Adder pcPlus4Ref;
    private ShiftLeft2 SL2Ref;

    public Adder() {

    }

    public void addDependency(ProgramCounter PC){

        PCRef = PC;
    }

    public void addDependency(Adder pcPlus4, ShiftLeft2 SL2){

        pcPlus4Ref = pcPlus4;
        SL2Ref = SL2;
    }

    public Integer getResult() {

        return (PCRef != null) ? (PCRef.getIndex() + FOUR) : (pcPlus4Ref.getResult() + SL2Ref.getShiftedValue());
    }

}
