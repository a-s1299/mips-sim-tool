package com.example.mips_sim.model;

import com.example.mips_sim.controller.DecoderWires;
import com.example.mips_sim.controller.MUX;

import java.util.Vector;

public class RegisterFile {

    private Integer regWrite;
    private String[] registers;
    private Integer read1Reg;
    private Integer read2Reg;
    private Integer writeReg;
    private MUX regDstRef;
    private DecoderWires wiresRef;

    public RegisterFile() {

        registers = new String[32];

        for (int i = 0; i < 32; ++i)
            registers[i] = "0";
    }

    public void addDependency(MUX RegDstMux, DecoderWires decoderWires){

        regDstRef = RegDstMux;
        wiresRef = decoderWires;
    }

    public Integer getRead1Data() {

        return Integer.parseInt( registers[read1Reg], 2);
    }

    public void setRead1Reg() {

        read1Reg = wiresRef.getRs();
    }

    public short getRead2Data() {

        short res = (short)Integer.parseInt(registers[read2Reg], 2);
        return res;
    }

    public void setRead2Reg() {

        read2Reg = wiresRef.getRt();
    }

    public void setWriteReg() {

        writeReg = regDstRef.getCorrectVal();
    }

    public void write(String newData) {

        if (regWrite == 1)
            registers[writeReg] = newData;
    }

    public void receiveSignal(Integer isWriting) {

        regWrite = isWriting;
        setRead1Reg();
        setRead2Reg();
        setWriteReg();
    }

    public Vector<String> getRegisterState() {

        Vector<String> allOccupiedRegisters = new Vector<>();
        Integer iter = 0;

        for (String eachReg : registers){
            if (eachReg != "0")
                allOccupiedRegisters.add( "$" + iter + " = " + Integer.parseInt(eachReg, 2) + "\n");

            ++iter;
        }

        if (allOccupiedRegisters.size() == 0)
            allOccupiedRegisters.add( "Currently Empty");

        return allOccupiedRegisters;
    }

    public void preloadReg(Integer regNum, String regVal) {

        registers[regNum] = regVal;
    }

}
