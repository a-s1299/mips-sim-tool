package com.example.mips_sim.controller;

import com.example.mips_sim.model.DataMemory;
import com.example.mips_sim.model.RegisterFile;

public class MUX {

    private Integer controlSignal;
    private Integer zeroVal;
    private Integer oneVal;
    private Integer correctVal;

    private DecoderWires wiresRef = null;
    private RegisterFile regFileRef = null;
    private SignExtender signExtenderRef = null;
    private DataMemory dataMemRef = null;
    private ALU mainALURef = null;
    private Adder PCPlus4Ref = null;
    private Adder otherAdderRef = null;

    public MUX () {

    }

    public void addDependency(DecoderWires decoderWires) { // RegDst

        this.wiresRef = decoderWires;
    }

    public void addDependency(RegisterFile regFile, SignExtender signExtender) { // ALUSrc

        this.regFileRef = regFile;
        this.signExtenderRef = signExtender;
    }

    public void addDependency(DataMemory dataMemory, ALU mainALU, RegisterFile regFile) { // MemtoReg

        this.dataMemRef = dataMemory;
        this.mainALURef = mainALU;
        this.regFileRef = regFile;
    }

    public void addDependency(Adder pcPlus4, Adder otherAdder) { // PCSrc

        PCPlus4Ref = pcPlus4;
        otherAdderRef = otherAdder;
    }

    private void loadVals() {

        if (wiresRef != null) { // RegDst

            this.zeroVal = wiresRef.getRt();
            this.oneVal = wiresRef.getRd();

        } else if(signExtenderRef != null) { // ALUSrc

            this.zeroVal = (int) regFileRef.getRead2Data();
            this.oneVal = (int) signExtenderRef.getExtended();

        } else if(dataMemRef != null) { // MemtoReg

            this.zeroVal = dataMemRef.getReadData();
            this.oneVal = mainALURef.getResult();

        } else { //PCSrc

            this.zeroVal = PCPlus4Ref.getResult();
            this.oneVal = otherAdderRef.getResult();
        }
    }

    private void setCorrectValue() {

        this.correctVal = ( controlSignal == 0 ) ? zeroVal : oneVal;
    }

    public Integer getCorrectVal() {

        return this.correctVal;
    }

    public void receiveSignal(Integer newSignal) {

        this.controlSignal = newSignal;
        loadVals();
        setCorrectValue();

        if (dataMemRef != null) // MemtoReg
            regFileRef.write( Integer.toBinaryString(correctVal) );
    }

    public Integer getControlSignal() {

        return controlSignal;
    }

}